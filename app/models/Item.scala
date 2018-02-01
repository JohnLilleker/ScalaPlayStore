package models

import play.api.data.Form
import play.api.data.Forms._
import scala.collection.mutable.ArrayBuffer

case class Item(id: Int, name: String, price: Int, description: String, stock: Int) {
  val inStock: Boolean = stock > 0
}

object Item {

  val createItemForm = Form(
    mapping(
      "id" -> number,
      "name" -> nonEmptyText,
      "price" -> number(min=1),
      "description" -> nonEmptyText,
      "stock" -> number(min=0)
    )(Item.apply)(Item.unapply)
  )

  val items = ArrayBuffer(
    Item(0, "Potion", 100, "Heals 10HP", 20),
    Item(1, "Super Potion", 300, "Heals 50HP", 15),
    Item(2, "Hyper Potion", 1500, "Heals 100HP", 3),
    Item(3, "Revive", 1000, "Heals a fainted Pokemon", 0)
  )

  def getItem(id: Int): Option[Item] = {
    items.find(_.id == id)
  }

  def filter(predicate: (Item => Boolean)): ArrayBuffer[Item] = items filter predicate

  def buyItem(id: Int): Boolean = {
    changeStock(id, -1)
  }

  def replaceItem(id: Int): Boolean = {
    changeStock(id, +1)
  }

  def changeStock(id: Int, change: Int): Boolean = {
    val current = getItem(id)
    current match {
      case Some(item) =>
        val index = items.indexOf(item)
        items(index) = item.copy(stock = item.stock+change)
        true
      case None => false
    }
  }
}