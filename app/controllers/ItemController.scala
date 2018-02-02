package controllers

import javax.inject.Inject

import models.Basket.BuyItem
import models.Item
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

class ItemController @Inject() (val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def allItems(search_term: Option[String]) = Action { implicit request: Request[AnyContent] =>
    val list = search_term match {
      case Some(term) => Item.findByName(term)
      case None => Item.items
    }
    Ok(views.html.itemsPages.items(list))
  }

  def showItem(id: Int) = Action { implicit request: Request[AnyContent] =>
    Item.getItem(id) match {
      case Some(item) => Ok (views.html.itemsPages.showItem (item, models.Basket.quantityForm.fill(BuyItem(id, 1))))
      case None => NotFound ("No such item")
    }
  }

  def newItem = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.itemsPages.newItem(Item.createItemForm))
  }

  def createItem = Action { implicit request: Request[AnyContent] =>
    Item.createItemForm.bindFromRequest.fold({errorForm =>
      BadRequest(views.html.itemsPages.newItem(errorForm))
    }, {item =>
      Item.items.append(item)
      Redirect(routes.ItemController.showItem(item.id))
    })
  }

  def editItem(id: Int) = Action { implicit request: Request[AnyContent] =>
    Item.getItem(id) match {
      case Some(item) => Ok(views.html.itemsPages.editItem(item, Item.createItemForm.fill(item)))
      case None => NotFound("No such item")
    }
  }

  def updateItem = Action { implicit request: Request[AnyContent] =>
    Item.createItemForm.bindFromRequest.fold({errorForm =>
      val item = Item.getItem(errorForm.data("id").toInt).get
      BadRequest(views.html.itemsPages.editItem(item, errorForm))
    }, {item =>
      Item.updateItem(item)
      Redirect(routes.ItemController.showItem(item.id))
    })
  }

  def search = Action { implicit request: Request[AnyContent] =>
    models.Item.searchBar.bindFromRequest.fold({_ =>
      Redirect(routes.ItemController.allItems())
    }, {term =>
      Redirect(routes.ItemController.allItems(Some(term.term)))
    })
  }
}

