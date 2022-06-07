package controllers

import models.DataModel
import play.api.mvc.{Action, AnyContent, BaseController, ControllerComponents}
import play.api.libs.json.{JsError, JsSuccess, JsValue, Json}
import repositories.DataRepository
import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ApplicationController @Inject()(val controllerComponents: ControllerComponents, val dataRepository: DataRepository) (implicit val ec: ExecutionContext) extends BaseController {

  def index(): Action[AnyContent] = Action.async { implicit request =>
    val books: Future[Seq[DataModel]] = dataRepository.collection.find().toFuture()
    books.map(items => Json.toJson(items)).map(result => Ok(result))
  }
  // .find() is a built-in method in the library we're using, and will return all items in the data repository.
  // The result returned by this method is a Future - essentially a placeholder for the result of performing the lookup operation in the database.
  // We use .map(items => Json.toJson(items)).map(result => Ok(result)) to write what we want to do with the result.
  // In this case, we take the resulting object (of type Seq[DataModel]), transform it into JSON, and return it in the body of an Ok / 200 response

  def read(id: String): Action[AnyContent] = Action.async { implicit request =>
    val book = dataRepository.read(id)
    book.map(items => Json.toJson(items)).map(result => Ok(result))
  }

  def delete(id: String): Action[AnyContent] = Action.async { implicit request =>
    dataRepository.delete(id)
    Future(Accepted)
  }

  def create(): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) =>
        dataRepository.create(dataModel).map(_ => Created)
      case JsError(_) => Future(BadRequest) // BadRequest wrapped in a future > The result of dataRepository.create() is a Future[Result], so even though we're not doing any lookup here, the type must be the same
    }
  }
  // JsSuccess > case classJsSuccess[T](value: T, path: JsPath = JsPath()) extends JsResult[T] with Product with Serializable
  // JsError > case classJsError(errors: Seq[(JsPath, Seq[JsonValidationError])]) extends JsResult[Nothing] with Product with Serializable

  def update(id: String): Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[DataModel] match {
      case JsSuccess(dataModel, _) =>
        dataRepository.update(id, dataModel).map(result => Accepted(Json.toJson(dataModel)))
      case JsError(_) => Future(BadRequest)
    }
  }
}

