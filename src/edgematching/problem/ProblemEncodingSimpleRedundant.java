package edgematching.problem;

import java.util.*;
import edgematching.cnf.*;

/*
 * extends ProblemEncodingSimple with redundant Clauses
 *
 * encoding from the paper "Solving edge-matching problems with satisfiability solvers"
 * by Marijn J. H. Heule
 */
public class ProblemEncodingSimpleRedundant extends ProblemEncodingSimple
{
	/*
	 * constructor cloning the original problem
	 */
	ProblemEncodingSimpleRedundant (Problem problem)
	{
		super (problem);
	}

	/*
	 * function encoding our problem to sat
	 */
	public CNFFormula encodeToSAT ()
	{
		CNFFormula formula = super.encodeToSAT ();

		encodeForbiddenColorClauses (formula);
		encodeExplicitOneOnOneMapping (formula);

		return formula;
	}

	protected void encodeForbiddenColorClauses (CNFFormula formula)
	{

	}

	protected void encodeExplicitOneOnOneMapping (CNFFormula formula)
	{

	}
}
