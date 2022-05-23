//package repositories
//
//import models.DataModel
//import org.mongodb.scala.bson.conversions.Bson
//import org.mongodb.scala.model.Filters.empty
//import org.mongodb.scala.model._
//import org.mongodb.scala.result
//import uk.gov.hmrc.mongo.MongoComponent
//import uk.gov.hmrc.mongo.play.json.PlayMongoRepository
//
//import javax.inject.{Inject, Singleton}
//import scala.concurrent.{ExecutionContext, Future}
//
//@Singleton
//class DataRepository @Inject()(
//                                mongoComponent: MongoComponent
//                              )(implicit ec: ExecutionContext) extends PlayMongoRepository[DataModel](
//  collectionName = "dataModels",
//  mongoComponent = mongoComponent,
//  domainFormat = DataModel.formats,
//  indexes = Seq(IndexModel(
//    Indexes.ascending("bookId"),
//    IndexOptions()
//      .name("booksInDataBase")
//      .unique(true)
//  )),
//  replaceIndexes = false
//) {
//
//  def create(book: DataModel): Future[DataModel] = //TODO: add a .recover() method if a book already exists
//    collection
//      .insertOne(book)
//      .toFuture()
//      .map(_ => book)
//
//  private def byID(id: String): Bson =
//    Filters.and(
//      Filters.equal("bookId", id)
//    )
//
//  def read(id: String): Future[DataModel] =
//    collection.find(byID(id)).headOption flatMap {
//      case Some(data) =>
//        Future(data)
//    } //TODO: add a case if data is not found
//
//  def update(id: String, book: DataModel): Future[result.UpdateResult] = //TODO: Can you think of a way of updating an aspect of a book rather than the whole book?
//    collection.replaceOne(
//      filter = byID(id),
//      replacement = book,
//      options = new ReplaceOptions().upsert(true) //TODO: What happens when we set this to false?
//    ).toFuture()
//
//  def delete(id: String): Future[result.DeleteResult] =
//    collection.deleteOne(
//      filter = byID(id)
//    ).toFuture()
//
//  def deleteAll(): Future[Unit] = collection.deleteMany(empty()).toFuture().map(_ => ()) //needed for tests
//
//}
