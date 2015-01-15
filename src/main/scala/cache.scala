import Modeling._
import scala.collection.mutable.Map
abstract class CacheModel(acls: Set[(Address, Address)], addr: Address) extends AbstractModel {
  val cached: Map[(Address, Body), Body]
  val origin: Map[(Address, Body), Node]
  val request: Map[Flow, Body]
  def flow (p: Packet) : Flow
  def is_response(p: Packet): Boolean
  def model = {
    case p if acls.contains((p.src, p.dest)) && cached.contains((p.dest, p.body)) => {
      val p2 = Packet(addr, p.src, cached((p.dest, p.body)), origin((p.dest, p.body)))
      List(p2)
    }
    case p if is_response(p) && request.contains(flow(p)) => 
                                 cached((p.src, request(flow(p)))) = p.body
                                 origin((p.src, request(flow(p)))) = p.origin
                                 List(p)
    case p if !acls.contains((p.src, p.dest)) => Seq.empty[Packet]
    case p => {
      request(flow(p)) = p.body
      List(p)
    }
  }
}
