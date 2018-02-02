package controllers

import javax.inject.Inject

import models._
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

import scala.collection.mutable.ArrayBuffer

class BasketController @Inject() (val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def viewBasket = Action { implicit request: Request[AnyContent] =>
    val basket = ArrayBuffer[Option[Item]]()
    models.Basket.items.foreach(id => basket.append(models.Item.getItem(id)))
    Ok(views.html.basketPages.basket(basket.view.flatten.groupBy(identity).mapValues(_.size)))
  }

  def addItem() = Action { implicit request: Request[AnyContent] =>
    Basket.quantityForm.bindFromRequest.fold({ errorForm =>
      val item = Item.getItem(errorForm.data("id").toInt).get
      BadRequest(views.html.itemsPages.showItem(item, errorForm))
    }, {wantIt =>
      val id = wantIt.id
      Item.getItem(id) match {
        case Some(item) =>
          if(item.inStock) {
            if (wantIt.number <= item.stock) {
              for (_ <- 1 to wantIt.number) Basket.addItem(id)
              Item.buyItem(id, wantIt.number)
              Redirect(routes.BasketController.viewBasket())
            }
            else {
              BadRequest(views.html.itemsPages.showItem(item, Basket.quantityForm.withError("number", s"Not enough Stock, we only have ${item.stock}")))
            }
          } else {
            Redirect(routes.ItemController.showItem(id))
          }
        case None => Redirect(routes.ItemController.showItem(id))
      }
    })
  }

  def removeItem(id: Int) = Action { implicit request: Request[AnyContent] =>
    Basket.removeItem(id)
    Item.replaceItem(id)
    Redirect(routes.BasketController.viewBasket())
  }

  def clearBasket = Action { implicit request: Request[AnyContent] =>
    for (i <- Basket.items)
      Item.replaceItem(i)
    Basket.items.clear()
    Redirect(routes.BasketController.viewBasket())
  }
}
