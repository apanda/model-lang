import Modeling._
import scala.collection.mutable.Queue

abstract class IDS extends AbstractModel {
  def suspicious (p: Packet, history: Seq[Packet]) : Boolean
  val history: Queue[Packet] = new Queue[Packet]
  def model = {
    case p if suspicious (p, history) => 
              history += p
              Seq.empty[Packet]
    case p => history += p
              List(p)
  }
}
