package services

import connectors.LibraryConnector
import models.Book

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class LibraryService @Inject()(connector: LibraryConnector) {

  def getBookFromGoogle(urlOverrride: Option[String] = None, search: String, term: String)(implicit ec: ExecutionContext): Future[Book] =
    connector.get[Book](urlOverrride.getOrElse(s"https://www.googleapis.com/books/v1/volumes?q=$search%$term"))


  //https://www.googleapis.com/books/v1/volumes?q=search+terms
  //https://developers.google.com/books/docs/v1/using?hl=en
}
