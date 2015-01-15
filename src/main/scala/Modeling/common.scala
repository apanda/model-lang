package Modeling
case class Address (id: Int)
case class Body (id: Byte)
case class Node (name: String)
case class Packet (src: Address, dest: Address, body: Body, origin: Node)
case class Flow (id: Int)


object AbstractModel {
  type ModelDescription = PartialFunction[Packet, Seq[Packet]]
}

trait AbstractModel {
  import AbstractModel._
  def model : ModelDescription
}
