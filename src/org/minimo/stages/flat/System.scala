package org.minimo.stages.flat

/** For the moment, this is just a placeholder to represent expressions.
 *  This class assumes that expressions represent variables as strings.
 *  I'm not sure if that is a good approach.
 */
sealed abstract class Expression {
  def referencedVariables: Set[String];
}

/** All variables have both a domain and a codomain.  I used
 *  the terms domain and codomain because if you think of the 
 *  *solution* as a function that translates time as the input
 *  into a value as an output then the clock represents the
 *  domain (the set of possible times passed as inputs) and
 *  the output represents the codomain.
 */
class Variable(val domain: Clock, val codomain: Codomain);

/** Equations are just equality relationships between a lhs and a rhs.  You could
 *  further generalize them into simple residual functions, but I chose not to
 *  go that far.
 */
class Equation(val lhs: Expression, val rhs: Expression) {
	def referencedVariables: Set[String] = lhs.referencedVariables ++ rhs.referencedVariables
}

/**
 * This class represents a system to be solved.  Each variable is associated with a name.  I suppose
 * the "variables" are an "environment" of some kind (since the represent the binding of names to
 * variables?).
 */
class System(variables: Map[String,Variable], equations: Set[Equation]) {
	require(referencedVariables==variables.keySet); // Make sure equations don't reference variables we don't know about
	/** Used only to check the 'require' assertion which essentially asserts that the variables
	 *  used in the equations are exactly equal to the variables in the problem.
	 */
	def referencedVariables: Set[String] = equations.flatMap { eq => eq.lhs.referencedVariables ++ eq.rhs.referencedVariables }
	
	/** Gets the set of all clocks in this system (by mapping over all variables) */
	def clocks: Set[Clock] = variables.values.toSet map { v: Variable => v.domain }
	
	/** This produces a set of System objects where all variables and equations have been partitioned
	 *  according to their clock.
	 */
	def partition: Map[Clock,System] = Map() ++ (clocks.toList map { c => (c -> system(c)) })
	
	/** This extracts the clock associated with a given equation.  Note, this method can't really
	 *  be implemented in the Equation class because it requires knowledge of the variables of
	 *  the system.
	 */
	def clock(eq: Equation): Clock = {
	    val clocks = eq.referencedVariables flatMap { v => variables.get(v) } map { _.domain }
	    require(clocks.size==1); // Should we ensure it is correct by construction instead?
	    clocks.head
	}
	
	/** This method takes a given clock and then extracts all variables and equations for that
	 *  clock and returns them as a System object.
	 */
	def system(c: Clock): System = {
		val vars = variables.filter(p => p._2.domain==c)
		val eqs = equations.filter( eq => clock(eq)==c)
		new System(vars, eqs);
	}
}
