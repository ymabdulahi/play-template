package controllers

import baseSpec.BaseSpec
import models.Book
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatestplus.play.guice.{GuiceOneAppPerSuite, GuiceOneServerPerSuite}
import play.api.http.Status
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{AnyContentAsEmpty, MessagesControllerComponents, Result}
import play.api.test.CSRFTokenHelper.CSRFFRequestHeader
import play.api.test.{DefaultAwaitTimeout, FakeRequest, FutureAwaits}
import play.api.test.Helpers.baseApplicationBuilder.injector
import play.api.test.Helpers.{POST, status}
import repositories.BookRepository

import scala.concurrent.{ExecutionContext, Future}

class ApplicationControllerMockitoSpec extends BaseSpec with MockFactory with ScalaFutures with GuiceOneAppPerSuite with FutureAwaits
  with DefaultAwaitTimeout with IntegrationPatience {


//  private lazy val component = injector.instanceOf[MessagesControllerComponents]
//  implicit val executionContext: ExecutionContext = app.injector.instanceOf[ExecutionContext]
//  private lazy val mockDataRepository: BookRepository = mock[BookRepository]
//
//  val TestApplicationController = new ApplicationController(
//    component,
//    mockDataRepository
//  )
//
//  val gameOfThrones: JsValue = Json.obj(
//    "bookId" -> "someId",
//    "name" -> "A Game of Thrones",
//    "author" -> Json.obj(
//      "firstNames" -> "George R. R.",
//      "lastName" -> "Martin"
//    ),
//    "description" -> "The best book!!!",
//    "numInStock" -> 100
//  )
//
//  def buildPost(url: String): FakeRequest[AnyContentAsEmpty.type] =
//    FakeRequest(POST, url).withCSRFToken.asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]
//
//  "ApplicationController .create" should {
//
//    "create a book in the database" in {
//
//      (mockDataRepository
//        .create(_: Book))
//        .expects(*)
//        .returning(Future(Right(gameOfThrones.as[Book])))
//        .once()
//
//      val request: FakeRequest[JsValue] = buildPost("/library").withBody[JsValue](gameOfThrones)
//
//      val createdResult: Future[Result] = TestApplicationController.create(request)
//
//      status(createdResult) shouldBe Status.CREATED
//
//    }
//  }
}
