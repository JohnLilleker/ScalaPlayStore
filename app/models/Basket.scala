package models

import play.api.data.Form
import play.api.data.Forms._

import scala.collection.mutable.ArrayBuffer

object Basket {

  val items: ArrayBuffer[Int] = ArrayBuffer[Int]()

  def addItem(id: Int): Unit = items.append(id)

  def removeItem(id: Int): Unit = items -= id

  def numberOfItems: Int = items.size

  case class BuyItem(id: Int, number: Int)
  val quantityForm = Form(
    mapping(
      "id" -> number(min = 0),
      "number" -> number(min = 1)
    )(BuyItem.apply)(BuyItem.unapply)
  )
}
