package models

import play.api.libs.json.{Json, OFormat}

trait APIError extends Throwable with Product with Serializable

final case class BookNotFound(code: String = "400", reason: String = "Book not found in database") extends APIError

object BookNotFound {
  implicit val format: OFormat[BookNotFound] = Json.format[BookNotFound]
}

final case class BookAlreadyExists(code: String = "400", reason: String = "Book already exists in database") extends APIError

object BookAlreadyExists {
  implicit val format: OFormat[BookAlreadyExists] = Json.format[BookAlreadyExists]
}

object APIError