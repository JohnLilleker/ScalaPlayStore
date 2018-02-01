import scala.collection.mutable.ArrayBuffer

val li = ArrayBuffer(1,2,3,4,5,6,3,5,2,3)
li.view.groupBy(identity).mapValues(count)