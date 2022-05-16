package models

import play.api.libs.json.{Json, OFormat}

case class UpdateKeyPair (key: String, newValue: String)

object UpdateKeyPair {
  implicit val formats: OFormat[UpdateKeyPair] = Json.format[UpdateKeyPair]
}
