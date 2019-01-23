package com.example.practice.modules

import cats.syntax.option._
import com.example.practice.ServerMain._
import com.example.practice.annotations.{ZoonCollection, PersonCollection}
import com.example.practice.util.AppConfigLib._
import com.example.practice.util.PipeOperator._
import com.github.mehmetakiftutuncu.errors.{CommonError, Errors, Maybe}
import com.google.inject.Provides
import com.twitter.inject.TwitterModule
import com.twitter.util.{Future, Time}
import io.github.hamsters.Validation._
import io.github.hamsters.twitter.Implicits._
import javax.inject.Singleton
import mouse.`try`._
import mouse.option._
import perfolation._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.{MongoConnection, MongoDriver}

// TODO
// READ THE MANUAL! http://twitter.github.io/finatra/user-guide/getting-started/modules.html
object MongoDBModule extends TwitterModule {
  val mongoUri                   = getConfig[String]("MONGODB_URI")
  private[this] val databaseName = p"test-svc-db-${Time.now.inMilliseconds}"

  private[this] val dbFuture: Future[Option[(MongoConnection, String)]] =
    Future(
      mongoUri
        .flatMap { uri =>
          MongoConnection
            .parseURI(uri)
            .cata(
              u => ((new MongoDriver).connection(u) -> u.db.getOrElse(databaseName)).some,
              t => {
                // DEBUG
                debug(p"MongoDB connection failed: ${t.getMessage} - [${t.getStackTrace.mkString("\n")}]")

                None
              }
            )
            .$$(z => info(p"Initiating MongoDBModule => URI:[$uri], [$z]"))
        }
    )

  @Singleton
  @Provides
  @PersonCollection
  def providesPersonCollection(dbF: Future[Option[(MongoConnection, String)]]): Future[Maybe[BSONCollection]] =
    dbF.flatMap {
      _.cata(
        x => {
          val (conn, dbName) = x

          conn
            .database(dbName)
            .map(d => OK(d.collection("person")))
            .handle {
              case e: Throwable =>
                KO(
                  Errors(
                    CommonError.database
                      .reason("MONGODB-EXCEPTION")
                      .data(p"Provided URI:[$mongoUri]\n${e.getMessage} - ${e.getStackTrace.mkString("\n")}")
                  )
                )
            }
        },
        Future.value(
          KO(
            Errors(
              CommonError.database.reason(
                "Failed to acquire a collection: please check if system environment variable `MONGO_DB_URI` was set"
              )
            )
          )
        )
      )
    }

  @Singleton
  @Provides
  @ZoonCollection
  def providesManCollection(dbF: Future[Option[(MongoConnection, String)]]): Future[Maybe[BSONCollection]] =
    dbF.flatMap {
      _.cata(
        x => {
          val (conn, dbName) = x

          conn
            .database(dbName)
            .map(d => OK(d.collection("man")))
            .handle {
              case e: Throwable =>
                KO(
                  Errors(
                    CommonError.database
                      .reason("MONGODB-EXCEPTION")
                      .data(p"Provided URI:[$mongoUri]\n${e.getMessage} - ${e.getStackTrace.mkString("\n")}")
                  )
                )
            }
        },
        Future.value(
          KO(
            Errors(
              CommonError.database.reason(
                "Failed to acquire a collection: please check if system environment variable `MONGO_DB_URI` was set"
              )
            )
          )
        )
      )
    }

  @Singleton
  @Provides
  def providesMongoDB: Future[Option[(MongoConnection, String)]] = dbFuture
}
