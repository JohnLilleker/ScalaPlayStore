package controllers

import models.Item
import play.api.mvc._

class ItemController extends Controller {

  val items = List(
    Item(1, "Potion", 100, "Heals 10HP", 20),
    Item(2, "Super Potion", 300, "Heals 50HP", 15),
    Item(3, "Hyper Potion", 1500, "Heals 100HP", 3),
    Item(4, "Revive", 1000, "Heals a fainted Pokemon", 0)
  )


  def allItems = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.itemsPages.items(items))
  }

  def showItem(id: Int) = Action {implicit request: Request[AnyContent] =>
    Ok(views.html.itemsPages.showItem(items.filter(_.id == id).head))
  }

  def buyItem(id: Int) = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.itemsPages.buyItem(items.filter(_.id == id).head))
  }
}
