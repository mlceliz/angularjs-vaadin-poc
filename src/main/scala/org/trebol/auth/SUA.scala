package org.trebol.auth

import scala.collection.JavaConversions.asScalaSet
import scala.util.Failure
import scala.util.Try
import org.scalatra._
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.scalatra.json.JsonSupport
import org.scalatra.json.JacksonJsonSupport


case  class User(id:Long, descripcion:String)

case class UserLogin(val token: User, var relacion: Option[User] = None, val relaciones: Set[User] = Set.empty) {

  def changeRelation(id: Long) { 
    println(s"relacion ${id}") 
    relacion = Some(User(id, s"Relacion $id"))
  }

}

object SUA {

  def userLogin = Option(userHolder.get).getOrElse(throw new IllegalStateException("El objeto 'UserLogin' debe existir en el ThreadContext."))

  private[this] val userHolder = new ThreadLocal[UserLogin]()

  private[auth] def user(userLogin: UserLogin) { userHolder.set(userLogin) }
  private[auth] def clearUser { userHolder.remove }
}

class SUAFilter extends ScalatraFilter {

  val TokenInSession = "auth_user"

  val UserLoginInSession = "fenix.user.login"

  override def doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {
    implicit val request = req.asInstanceOf[HttpServletRequest]
    implicit val response = res.asInstanceOf[HttpServletResponse]

    val token: Option[User] = session.getAs[User](TokenInSession)
    val userLoginSession: Option[UserLogin] = session.getAs[UserLogin](UserLoginInSession)
    try {
      (token, userLoginSession) match {
        case (Some(userToken), Some(userLogin)) => {
          if (userToken != userLogin.token) {
            session.invalidate()
            val newUserLogin = buildUserLogin(userToken)
            SUA.user(newUserLogin)
            session.setAttribute(UserLoginInSession, newUserLogin)
            session.setAttribute(TokenInSession, userToken)
          } else {
            SUA.user(userLogin)
          }
          chain.doFilter(request, response)
        }
        case (Some(user), None) => {
          val newUserLogin = buildUserLogin(user)
          SUA.user(newUserLogin)
          session.setAttribute(UserLoginInSession, newUserLogin)
          chain.doFilter(request, response)
        }
        case (None, None) =>
          response.sendError(HttpServletResponse.SC_UNAUTHORIZED)
      }
    } finally {
      SUA.clearUser
    }
  }

  def buildUserLogin(user: User): UserLogin = {
    val relaciones = Set(User(1, "Relacion 1"), User(2, "Relacion 2"), User(3, "Relacion 3"))
    new UserLogin(user, None, relaciones)
  }
}

class SUAController extends ScalatraServlet with JacksonJsonSupport {

  import org.json4s._
  import org.json4s.JsonDSL.WithBigDecimal._

  protected implicit val jsonFormats: Formats = DefaultFormats

  before() {
    contentType = formats("json")
  }

  get("/user") {
    val u = SUA.userLogin
    ("token" ->
      ("cuit" -> u.token.id)) ~
      ("persona" ->
        ("cuit" -> u.token.id) ~
        ("descripcion" -> u.token.descripcion) ~
        ("relacion" -> u.relacion.map(r => ("cuit" -> r.id) ~ ("descripcion" -> r.descripcion))) ~
        ("relaciones" -> u.relaciones.map(r => ("cuit" -> r.id) ~ ("descripcion" -> r.descripcion))))

  }

  get("/relacion/:id") {
    SUA.userLogin.changeRelation(params.getAsOrElse("id", 0))
  }
}