import scala.reflect.runtime._
import scala.reflect.universe._
import scala.reflect.runtime.universe._
import scala.reflect._
import scala.tools.reflect.ToolBox

val code = scala.io.Source.fromFile("src/main/scala/ids.scala").mkString
val toolbox = runtimeMirror(getClass.getClassLoader).mkToolBox()
val codeTree = tb.parse(code)
val cdef = codeTree.children(2)
val ClassDef(modifier, name, typeParams, impl) = cdef
val Template(parents, self, body) = impl
val ValDef(vmod, name, vtree, rhs) = body(2)

//vmod: reflect.runtime.universe.Modifiers = Modifiers(, , Map())
//name: reflect.runtime.universe.TermName = history
//vtree: reflect.runtime.universe.Tree = Queue[Packet]
//rhs: reflect.runtime.universe.Tree = new Queue[Packet]()
//

class ModelTraverser extends Traverser {
  var classes = List[ClassDef]()
  override def traverse(tree: Tree): Unit = tree match {
    case cdef @ ClassDef (_, _, _, impl) => 
      val Template(parents, _, _) = impl
      parents.find( parent => { val Ident(tn) = parent
                                tn == newTypeName("AbstractModel")} ) match {
        case Some(_) => classes = cdef::classes
        case None => ()
      }
    case _ => super.traverse(tree)
  }
}
