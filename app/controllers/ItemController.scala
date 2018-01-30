package controllers

import models.Item
import play.api.mvc._

class ItemController extends Controller {

  val items = List(
    Item("Potion", 100, "Heals 10HP", 20),
    Item("Super Potion", 300, "Heals 50HP", 15),
    Item("Hyper Potion", 1500, "Heals 100HP", 3),
    Item("Revive", 1000, "Heals a fainted Pokemon", 0)
  )


  def allItems = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.itemsPages.items(items))
  }

  def showItem(id: Int) = Action {implicit request: Request[AnyContent] =>
    if (id < 0 || id >= items.size)
      NotFound("No such item")
    else
      Ok(views.html.itemsPages.showItem(items(id)))
  }
}
