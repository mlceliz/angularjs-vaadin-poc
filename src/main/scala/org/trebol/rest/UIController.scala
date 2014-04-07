package org.trebol.rest

import org.scalatra.scalate.ScalateSupport
import org.scalatra.ScalatraServlet

class UIController extends ScalatraServlet with ScalateSupport {

  get("/") {
    contentType = "text/html"
    jade("/default.jade", "pageTitle" -> "Este es mi titulo")
    // render your views in the action (see below)
  }
}