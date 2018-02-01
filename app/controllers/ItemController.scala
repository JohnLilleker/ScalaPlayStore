package controllers

import javax.inject.Inject

import models.Item
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._

class ItemController @Inject() (val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def allItems = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.itemsPages.items(Item.items))
  }

  def showItem(id: Int) = Action {implicit request: Request[AnyContent] =>
    val item = Item.getItem(id)
    if (item.isDefined)
      Ok(views.html.itemsPages.showItem(item.get))
    else
      NotFound("No such item")
  }

  def newItem = Action { implicit request: Request[AnyContent] =>
    Ok(views.html.itemsPages.newItem(Item.createItemForm))
  }

  def createItem = Action { implicit request: Request[AnyContent] =>
    Item.createItemForm.bindFromRequest.fold({errorForm =>
      BadRequest(views.html.itemsPages.newItem(errorForm))
    }, {item =>
      Item.items.append(item)
      Redirect(routes.ItemController.allItems())
    })
  }
}

