package edgematching.problem;

import java.util.*;
import edgematching.cnf.*;

/*
 * extends Problem, so that it can be encoded by the simple encoding ...
 */
public class ProblemEncodingSimple extends Problem
	implements SATSolvable
{
	protected int m_amount_of_border_colors;
	protected int m_amount_of_center_colors;

	/*
	 * simple constructor cloning the original problem
	 */
	public ProblemEncodingSimple (Problem problem)
	{
		super (problem);

		m_amount_of_border_colors = m_border_colors.size ();
		m_amount_of_center_colors = m_center_colors.size ();
	}

	/*
	 * function encoding our problem to sat
	 */
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

	/*
	 * function decoding our sat-solution
	 */
	public void decodeSolution (CNFFormula formula)
	{
		/*
		 * here the solution of formula has to be decoded back ...
		 */
	}


	/*
	 * ===============================================================================
	 * helper functions ...
	 * ===============================================================================
	 */

	/*
	 * convert x-y-coordinates of a place in {0, ..., n-1} to a number in {0, ..., n^2-1}
	 */
	protected int convertXYToSubsequentNumber (int x, int y)
	{
		return (m_grid_width * x) + y;
	}

	protected int convertXijToSATVariable (int piece, int place)
	{
		return (m_grid_width * m_grid_height * place + piece) + 1;
	}

	protected int convertSATVariableToPiece (int variable)
	{
		return (variable - 1) % (m_grid_width * m_grid_height);
	}

	protected int convertSATVariableToPlace (int variable)
	{
		return (variable - 1) / (m_grid_width * m_grid_height);
	}
}
