package repositories

import models.{APIError, Book, BookAlreadyExists, BookNotFound}
import org.mongodb.scala.bson.conversions.Bson
import org.mongodb.scala.model.Filters.empty
import org.mongodb.scala.model._
import org.mongodb.scala.{Document, result}
import uk.gov.hmrc.mongo.MongoComponent
import uk.gov.hmrc.mongo.play.json.PlayMongoRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

@Singleton
class BookRepository @Inject()(
  mongoComponent: MongoComponent
)(implicit ec: ExecutionContext) extends PlayMongoRepository[Book](
    collectionName = "dataModels",
    mongoComponent = mongoComponent,
    domainFormat = Book.formats,
    indexes = Seq(IndexModel(
      Indexes.ascending("bookId"),
      IndexOptions()
        .name("booksInDataBase")
        .unique(true)
    )),
    replaceIndexes = false
  ) {

  private def byID(id: String): Bson =
    Filters.and(
      Filters.equal("bookId", id)
    )

  private def byBook(book: Book): Bson =
    Filters.and(
      Filters.equal("book", book)
    )

  def find(book: Book): Future[Either[APIError, Book]] =
    collection.find(byBook(book)).headOption flatMap {
      case Some(data) =>
        Future(Right(data))
      case _ =>
        Future(Left(BookNotFound()))
    }

  def find(id: String): Future[Either[APIError, Book]] =
    collection.find(byID(id)).headOption flatMap {
      case Some(data) =>
        Future(Right(data))
      case _ =>
        Future(Left(BookNotFound()))
    }

  def create(book: Book): Future[Either[APIError, Book]] =
    collection
      .insertOne(book)
      .toFuture()
      .map(_ => Right(book))
      .recover {
        case NonFatal(ex) if ex.getMessage.contains("E11000") && ex.getMessage.contains(book.bookId) =>
          Left(BookAlreadyExists())
      }

  def update(id: String, inputDocument: Document): Future[result.UpdateResult] =
    collection
      .updateOne(
        filter = byID(id),
        update = Document("set" -> Document(inputDocument))
      )
      .toFuture()

  def upsert(id: String, book: Book): Future[Either[APIError, Unit]] =
    collection.replaceOne(
      filter = byID(id),
      replacement = book,
      options = new ReplaceOptions().upsert(true)
    ).toFuture().map(_ => Right(()))

  def delete(id: String): Future[Either[APIError, Unit]] =
    collection.deleteOne(
      filter = byID(id)
    ).toFuture().map(_ => Right(()))

  def deleteAll(): Future[Unit] = collection.deleteMany(empty()).toFuture().map(_ => ())

}
