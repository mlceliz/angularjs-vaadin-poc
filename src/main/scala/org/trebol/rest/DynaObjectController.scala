package org.trebol.rest

import org.json4s.DefaultFormats
import org.json4s.Formats
import org.scalatra.NotFound
import org.scalatra.ScalatraServlet
import org.scalatra.json.JacksonJsonSupport
import org.scalatra.UrlGeneratorSupport

class DynaObjectController extends ScalatraServlet with JacksonJsonSupport with UrlGeneratorSupport {

  protected implicit val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  val getDyno = get("/ndyno/:id") {
    NotFound("Sorry, the DynaObject not exist!!!")
  }

  get("/:id") {
    redirect(url(getDyno, "id" -> params("id")))
  }
  

}