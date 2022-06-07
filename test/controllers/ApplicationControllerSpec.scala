package controllers

import baseSpec.BaseSpecWithApplication
import models.DataModel
import play.api.test.FakeRequest
import play.api.http.Status
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AnyContentAsEmpty, Result}
import play.api.test.Helpers.{contentAsJson, defaultAwaitTimeout, status}

import scala.concurrent.Future

class ApplicationControllerSpec extends BaseSpecWithApplication {

  val TestApplicationController = new ApplicationController(
    component,
    repository
  )

  private val dataModel: DataModel = DataModel(
    "abcd",
    "test name",
    "test description",
    100
  )

  "ApplicationController .create" should {

    "create a book in the database" in {

      val request: FakeRequest[JsValue] = buildPost("/api").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)

      status(createdResult) shouldBe Status.CREATED
    }
  }

  "ApplicationController .index" should {
    beforeEach()
    val result = TestApplicationController.index()(FakeRequest())
    "return TODO" in {
      status(result) shouldBe Status.OK
    }
    afterEach()
  }

  // We have created a happy path case for two of the controller methods > .create and .read
  "ApplicationController .read" should {

    "find a book in the database by id" in {

      val request: FakeRequest[JsValue] = buildPost("/api").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      //Hint: You could use status(createdResult) shouldBe Status.CREATED to check this has worked again
      status(createdResult)(defaultAwaitTimeout) shouldBe Status.CREATED
      val readRequest: FakeRequest[AnyContentAsEmpty.type] = buildGet("/api/:id")
      val readResult: Future[Result] = TestApplicationController.read("abcd")(readRequest)
      status(readResult)(defaultAwaitTimeout) shouldBe Status.OK
      contentAsJson(readResult)(defaultAwaitTimeout).as[JsValue] shouldBe Json.toJson(dataModel)
    }
  }

  "ApplicationController .update(id: String, newData: DataModel)" should {

    "find a book in the database by id and replace it" in {

      val request: FakeRequest[JsValue] = buildPost("/api").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      status(createdResult)(defaultAwaitTimeout) shouldBe Status.CREATED
      val updateRequest: FakeRequest[JsValue] = buildPut("/api/:id").withBody[JsValue](Json.toJson(updatedDataModel))
      val updateResult: Future[Result] = TestApplicationController.update("abcd")(updateRequest)
      status(updateResult)(defaultAwaitTimeout) shouldBe Status.ACCEPTED
      contentAsJson(updateResult)(defaultAwaitTimeout).as[JsValue] shouldBe Json.toJson(updatedDataModel)
    }
  }

  "ApplicationController .delete(id: String)" should {

    "delete a book in the database by id" in {

      val request: FakeRequest[JsValue] = buildPost("/api").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      status(createdResult)(defaultAwaitTimeout) shouldBe Status.CREATED
      val deleteRequest: FakeRequest[AnyContentAsEmpty.type] = buildDelete("/api/:id")
      val deleteResult: Future[Result] = TestApplicationController.delete("abcd")(deleteRequest)
      status(deleteResult)(defaultAwaitTimeout) shouldBe Status.ACCEPTED
    }
  }

  // They will delete the contents of the repository before and after each test.
  override def beforeEach(): Unit = repository.deleteAll()
  override def afterEach(): Unit = repository.deleteAll()
}
