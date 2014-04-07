package org.trebol

import vaadin.scala._
import com.vaadin.terminal.gwt.server.HttpServletRequestListener
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class VaadinScalaApplication extends Application("Vaadin Scala project") with HttpServletRequestListener {

  var formulario, version, operacion: Label = Label("")

  override val main = new VerticalLayout {
    margin = true
    components += formulario
    components += version
    components += operacion
  }

  def onRequestStart(request: HttpServletRequest, response: HttpServletResponse) {
    formulario.caption = request.getParameter("formulario")
    version.caption = request.getParameter("version")
    operacion.caption = request.getParameter("operacion")
  }

  def onRequestEnd(request: HttpServletRequest, response: HttpServletResponse) {}
}