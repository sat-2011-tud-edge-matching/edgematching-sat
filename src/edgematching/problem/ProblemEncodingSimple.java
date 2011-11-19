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

	protected ArrayList<Piece> m_pieces;
	protected int m_pieces_current_position;

	/*
	 * simple constructor cloning the original problem
	 */
	public ProblemEncodingSimple (Problem problem)
	{
		super (problem);

		m_amount_of_border_colors = m_border_colors.size ();
		m_amount_of_center_colors = m_center_colors.size ();

		m_pieces = new ArrayList<Piece> (m_grid_width * m_grid_height);
		m_pieces_current_position = 0;
	}

	/*
	 * function encoding our problem to sat
	 */
	public CNFFormula encodeToSAT ()
	{
		CNFFormula formula = new CNFFormula ("simple encoding of an etch-matching puzzle\n" +
				"with size " + m_grid_width + " x " + m_grid_height + "\n" +
				(m_bounded ? "bounded" : "unbounded") + " and " + (m_signed ? "signed" : "unsigned") + ".\n");

		if (m_bounded) {
			encodeCornerPieces (formula);
		}

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
	 * encoding functions ...
	 * ===============================================================================
	 */

	protected void encodeCornerPieces (CNFFormula formula)
	{
		int indexFirstPiece = m_pieces_current_position;

		for (Piece i_corner_piece : m_corner_pieces) {
			m_pieces.add (m_pieces_current_position, i_corner_piece);

			int[] tempClause = {
				convertXijToSATVariable (m_pieces_current_position, convertXYToSubsequentNumber (0, 0)),
				convertXijToSATVariable (m_pieces_current_position, convertXYToSubsequentNumber (m_grid_width - 1, 0)),
				convertXijToSATVariable (m_pieces_current_position, convertXYToSubsequentNumber (0, m_grid_height - 1)),
				convertXijToSATVariable (m_pieces_current_position, convertXYToSubsequentNumber (m_grid_width - 1, m_grid_height - 1))
				};

			formula.addClause (tempClause);

			m_pieces_current_position ++;
		}

		int indexLastPiece = m_pieces_current_position - 1;
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
