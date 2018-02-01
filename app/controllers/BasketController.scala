package controllers

import models._
import play.api.mvc._

import scala.collection.mutable.ArrayBuffer


class BasketController extends Controller{

  def viewBasket = Action { implicit request: Request[AnyContent] =>
    val basket = ArrayBuffer[Option[Item]]()
    models.Basket.items.foreach(id => basket.append(models.Item.getItem(id)))
    Ok(views.html.basketPages.basket(basket.view.flatten.groupBy(identity).mapValues(_.size)))
  }

  def addItem(id: Int) = Action { implicit request: Request[AnyContent] =>
    Item.getItem(id) match {
      case Some(item) =>
        if(item.inStock) {
          Basket.addItem(id)
          Item.buyItem(id)
          Redirect(routes.BasketController.viewBasket())
        } else {
          Redirect(routes.ItemController.showItem(id))
        }
      case None => Redirect(routes.ItemController.showItem(id))
    }
  }

  def removeItem(id: Int) = Action { implicit request: Request[AnyContent] =>
    Basket.removeItem(id)
    Item.replaceItem(id)
    Redirect(routes.BasketController.viewBasket())
  }

}
