package edgematching.problem;

import java.util.*;
import edgematching.cnf.*;

/*
 * extends Problem, so that it can be encoded by the simple encoding ...
 */
public class ProblemEncodingSimple extends Problem
	implements SATSolvable
{
	public ProblemEncodingSimple (Problem problem)
	{
		super (problem);
	}

	public CNFFormula encodeToSAT ()
	{
		CNFFormula formula = new CNFFormula ("simple encoding of an etch-matching puzzle\n" +
				"with size " + m_grid_width + " x " + m_grid_height + "\n" +
				(m_bounded ? "bounded" : "unbounded") + " and " + (m_signed ? "signed" : "unsigned") + ".\n");

		/*
		 * here the encoding steps will be inserted ...
		 * something like:
		 *
		 * formula.addClause ({-1, 2, 5});
		 */

		return formula;
	}

	public void decodeSolution (CNFFormula formula)
	{
		/*
		 * here the solution of formula has to be decoded back ...
		 */
	}
}
