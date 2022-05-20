package services

import connectors.LibraryConnector
import models.Book
import org.scalamock.scalatest.MockFactory
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.matchers.should.Matchers
import play.api.libs.json.{JsValue, Json, OFormat}
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

class LibraryServiceSpec extends AnyWordSpec with Matchers with MockFactory with ScalaFutures {

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

  "getBookFromGoogle" should {

    val mockConnector = mock[LibraryConnector]
    val testService = new LibraryService(mockConnector)

    "return a book" in {
      (mockConnector.get[Book](_: String)(_: OFormat[Book], _: ExecutionContext))
        .expects(*, *, *)
        .returning(Future(gameOfThrones.as[Book]))
        .once()

      whenReady(testService.getBookFromGoogle(search = "", term = "")) { result =>
        result shouldBe gameOfThrones.as[Book]
      }
    }
  }

}
