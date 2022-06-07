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

    "find a book in the database by id" in "ApplicationController .read()" should {
      beforeEach()
      "find a book in the database by id" in {

        val request: FakeRequest[JsValue] = buildPost("/api/create").withBody[JsValue](Json.toJson(dataModel))
        //val readRequest: FakeRequest[AnyContentAsEmpty.type ] = buildGet("/api/:id")
        val createdResult: Future[Result] = TestApplicationController.create()(request)
        //val readResult: Future[Result] = TestApplicationController.read("abcd")(readRequest) //need to uncomment readRequest
        val readResult: Future[Result] = TestApplicationController.read("abcd")(FakeRequest()) // works without having readRequest

        status(createdResult) shouldBe Status.CREATED
        status(readResult) shouldBe Status.OK
        contentAsJson(readResult).as[DataModel] shouldBe DataModel(dataModel)

      }
      afterEach()
    }

  "ApplicationController .update(id: String, newData: DataModel)" should {

    "find a book in the database by id and replace it" in {

      val request: FakeRequest[JsValue] = buildPost("/api").withBody[JsValue](Json.toJson(dataModel))
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      status(createdResult)(defaultAwaitTimeout) shouldBe Status.CREATED


    }
  }

  "ApplicationController .delete(id: String)" should {

    "delete a book in the database by id" in {

      val request: FakeRequest[JsValue] = buildPost("/api").withBody[JsValue](Json.toJson(dataModel))
      val deleteRequest: FakeRequest[AnyContentAsEmpty.type ] = buildDelete("/api/:id")
      val createdResult: Future[Result] = TestApplicationController.create()(request)
      val deleteResult: Future[Result] = TestApplicationController.delete("abcd")(deleteRequest)

      status(createdResult) shouldBe Status.CREATED
      status(deleteResult) shouldBe Status.ACCEPTED
    }
    afterEach()
  }


    // They will delete the contents of the repository before and after each test.
  override def beforeEach(): Unit = repository.deleteAll()
  override def afterEach(): Unit = repository.deleteAll()
}
