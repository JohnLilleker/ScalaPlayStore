package controllers

import play.api.mvc._

class Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def info = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.info())
  }

}