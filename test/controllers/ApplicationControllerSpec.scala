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

import baseSpec.BaseSpecWithApplication
import models.{APIError, Author, Book}
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import play.api.http.Status
import play.api.libs.json._
import play.api.mvc.Result
import play.api.test.FakeRequest
import play.api.test.Helpers.{await, defaultAwaitTimeout, status}

import scala.concurrent.{ExecutionContext, Future}

class ApplicationControllerSpec extends BaseSpecWithApplication with ScalaFutures with MockFactory {


  implicit val executionContext: ExecutionContext = app.injector.instanceOf[ExecutionContext]

  val TestApplicationController = new ApplicationController(
    component,
    repository
  )

  val gameOfThrones: JsValue = Json.obj(
    "bookId" -> "someId",
    "name" -> "A Game of Thrones",
    "author" -> Json.obj(
      "firstNames" -> "George R. R.",
      "lastName" -> "Martin"
    ),
    "description" -> "The best book!!!",
    "numInStock" -> 100
  )

  "ApplicationController .index" should {

    "return OK" in {

      val result = TestApplicationController.index()(FakeRequest())
       status(result) shouldBe Status.OK
    }

    "load the main page" in {}
  }

  "ApplicationController .create" should {

    "create a book in the database" in {

      val request: FakeRequest[JsValue] = buildPost("/library").withBody[JsValue](gameOfThrones)
      val createdResult: Future[Result] = TestApplicationController.create(request)

      status(createdResult) shouldBe Status.CREATED
    }

    "return an error when a book already exists" in {}

    "validate a json body" in {
      gameOfThrones.validate[Book].shouldBe(
        JsSuccess(
          Book("someId", "A Game of Thrones", Author("George R. R.","Martin"), "The best book!!!", 100),
          JsPath(Nil)
        )
      )
    }

    "return an error for invalid json" in {
      val jsonBody: JsObject = Json.obj(
        "_id" -> "abcd",
        "blah" -> "test name"
      )

      val result = TestApplicationController.create()(FakeRequest().withBody(jsonBody))

      status(result) shouldBe Status.BAD_REQUEST
    }
  }

  "ApplicationController .find" should { //(Hint: You're probably going to need to create in the database before you can find)

    "find a book in the database by id" in {

      val request: FakeRequest[JsValue] = buildPost("/library").withBody[JsValue](gameOfThrones)
      val createdResult: Future[Result] = TestApplicationController.create(request)

      status(createdResult) shouldBe Status.CREATED

      val foundResult: Future[Either[APIError, Book]] = repository.find("someId")

      await(foundResult) shouldBe Right(gameOfThrones.as[Book])
    }

    "find a book in the database by book" in {}

    "find all books by author's lastname" in {}

    "return an error when a book doesn't exist for a given id" in {}
  }

  "ApplicationController .update" should {

    "update a book's name by their id" in {}

    "return an error when an id doesn't exist" in {}

    "return an error when a key doesn't exist" in {}

    "update the number of copies available" in {//checking out a book???
    }

  }

  "ApplicationController .upsert" should {

    "update an existing book" in {}

    "create a book when one doesn't exist in" in {// how would you ensure this in a test?
    }

    "return an error for incorrect json" in {}
  }

  "ApplicationController .delete" should {

    "delete a book by id" in {}

    "give an error when no id exists" in {}
  }

  "ApplicationController .deleteAll" should {

    "return an empty database" in {}

    "Optional: return a message when the database was already empty?" in {}
  }

  override def beforeEach(): Unit = repository.deleteAll()
  override def afterEach(): Unit = repository.deleteAll()
}
