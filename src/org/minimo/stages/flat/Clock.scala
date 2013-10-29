package org.minimo.stages.flat

/**
 * This is a base class for all types of clocks we will consider.
 * Like "types", we have a top clock, a bottom clock and subclock
 * relationships (potentially).
 */
sealed abstract class Clock {
  /* Ultimately, we need some functionality here that allows us
   * to represent the potential overlap between different clocks.
   * By knowing which clocks can overlap synchronously, we whould
   * be able to formulate a system of equations for each case.
   * 
   * The way I envision this is we might have some number of clocks
   * e.g., A1, A2, SA1, SA2, SB1 where A1 and A2 are asyncronous
   * clocks, SA* are clocks that share a subsampling relationship
   * and SB1 is a sampled clock without a subsampling relatinship.
   * We could enumerate all the possible global states that
   * involve all combinations of activation or deactivation
   * for all clocks (i.e., 2^5) but many of these can be eliminated
   * since there is no synchronous relationship.  In fact, the only
   * cases we should technically have to consider are:
   * W: A1 = TRUE
   * W: A1 = FALSE
   * X: A2 = TRUE
   * X: A2 = FALSE
   * Y: SA1 = TRUE, SA2 = TRUE
   * Y: SA1 = FALSE, SA2 = TRUE
   * Y: SA1 = TRUE, SA2 = FALSE
   * Y: SA1 = TRUE, SA2 = FALSE
   * Z: SB1 = TRUE
   * Z: SB2 = FALSE
   * 
   * For each of these (10) cases, we should be able to formulate
   * a consistent set of variables and unknowns.  Furthermore, W, X,
   * Y and Z represent partitions and all variables should belong
   * to exactly one partition.
   * 
   * I haven't had time yet to write the code that identifies all
   * the unique partitions.
   */
}

/** A clock that is always active */
case object Always extends Clock;

/** A clock triggered by a zero crossing (i.e. a state event) */
case class ZeroCrossing(expr: Expression) extends Clock;

/** An abstract clock that is sampled in some way */
sealed abstract class SampledClock extends Clock;

/** A sampled clock that is specified by a rational sample rate and a start time */
case class Sampled(num: Int, den: Int, start: Long) extends SampledClock;

/** A sampled clock that is specified as subsampling another sampled clock */
case class Subsampled(base: Sampled, div: Int) extends SampledClock;

/** A clock that is never active */
case object Never extends Clock;
