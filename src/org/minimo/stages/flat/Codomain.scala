package org.minimo.stages.flat

/** I used the term Codomain because Sébastien introduced me to the term.  Hopefully
 *  I'm using it correctly. :-)
 */
sealed abstract class Codomain;

/** Type for variables that are evaluated to reals */
case object RealType extends Codomain;
/** Type for variables that are evaluated to integers */
case object IntegerType extends Codomain;
/** Type for variables that are evaluated to booleans */
case object BooleanType extends Codomain;
/** Type for variables that are evaluated to strings */
case object StringType extends Codomain;
/** Type for variables that are evaluated to enumerated values. */
case class EnumeratedType(values: Map[Int,String]) {
  require(values.values.toList.removeDuplicates.size==values.values.size);
}
