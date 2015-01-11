import Modeling._
abstract class FirewallModel extends AbstractModel {
  def AclAccept (src: Address, dest: Address) : Boolean
  def model = {
    case p if AclAccept (p.src, p.dest) => List(p)
    case _ => Seq.empty[Packet]
  }
}
