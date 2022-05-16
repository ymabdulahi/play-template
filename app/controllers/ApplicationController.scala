/*
 * Copyright 2018 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers

import models.{Book, BookNotFound, UpdateKeyPair}
import org.mongodb.scala.Document
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import repositories.BookRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ApplicationController @Inject()(
                                       val controllerComponents: ControllerComponents,
                                       dataRepository: BookRepository)(
                                       implicit val ec: ExecutionContext) extends BaseController {

  def index(): Action[AnyContent] = Action.async { implicit request =>
      Future(Ok)
    }

  def create: Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[Book] match {
      case JsSuccess(dataModel, _) =>
        dataRepository.create(dataModel).map { _ => Created } recover {
          case _: BookNotFound => InternalServerError(Json.obj(
            "message" -> "Error adding item to Mongo"
          ))
          case _ => InternalServerError(Json.obj(
            "message" -> "Error adding item to Mongo, unique error"
          ))
        }
      case JsError(_) =>
        Future(BadRequest)
    }
  }

  def update(id: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[UpdateKeyPair] match {
      case JsSuccess(value, _) =>
        dataRepository.update(id, Document("$set" -> Document(s"{${value.key}: ${value.newValue}}"))).map { result =>
          Accepted(s"Model updated to: $result")
        }
      case JsError(_) =>
        Future(BadRequest)
    }
  }

  def upsert(id: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[Book] match {
      case JsSuccess(dataModel, _) =>
        dataRepository.upsert(id, dataModel).map(_ => Accepted(s"Model updated to: ${Json.toJson(dataModel)}"))
      case JsError(_) => Future(BadRequest)
    }
  }

  def delete(id: String): Action[AnyContent] = Action.async { implicit request =>
    dataRepository.delete(id).map(_ => Accepted)
  }

  def deleteAll(): Action[AnyContent] = Action.async { implicit request =>
    dataRepository.deleteAll().map(_ => Accepted)
  }

  def find(id: String): Action[AnyContent] = Action.async { implicit request =>
    dataRepository.find(id).map {
      case Left(_) => BadRequest
      case Right(value) => Ok(Json.toJson(value))
    }
  }

  def findByBook(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[Book] match {
      case JsSuccess(book, _) =>
        dataRepository.find(book).map {
          case Right(value) =>
            Ok(s"Model Found: ${Json.toJson(value)}")
          case Left(value) =>
            BadRequest(value.getMessage)
        }
          .recover {
            case _: BookNotFound => InternalServerError(Json.obj(
              "message" -> "Error finding item in Mongo"
            ))
            case _ => InternalServerError(Json.obj(
              "message" -> "Error finding item in Mongo, unique error"
            ))
        }
      case JsError(_) => Future(BadRequest)
    }
  }
}
