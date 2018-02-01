package models

import scala.collection.mutable.ArrayBuffer

object Basket {

  val items: ArrayBuffer[Int] = ArrayBuffer[Int]()

  def addItem(id: Int): Unit = items.append(id)

  def removeItem(id: Int): Unit = items -= id

}
