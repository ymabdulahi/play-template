package models

import play.api.libs.json.{Json, OFormat}

case class DataModel(_id: String,
                     name: String,
                     description: String,
                     numSales: Int)


object DataModel {
  implicit val formats: OFormat[DataModel] = Json.format[DataModel]
}

// val bookOne = DataModel("id1", "Book name", "Author name", 10)

// val bookOne = DataModel(_id = "id1", name = "Book name", description = "Author name", numSales = 10)