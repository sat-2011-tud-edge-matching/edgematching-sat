package edgematching.problem;

import java.util.*;
import edgematching.cnf.*;

/*
 * extends Problem, so that it can be encoded by the simple encoding ...
 */
public class ProblemEncodingSimple extends Problem
	implements SATSolvable
{
	/*
	 * amounts of colors
	 */
	protected int m_amount_of_border_colors;
	protected int m_amount_of_center_colors;

	/*
	 * list of all pieces
	 */
	protected ArrayList<Piece> m_pieces;

	/*
	 * numbers of pieces sorted by kind of piece
	 */
	protected ArrayList<Integer> m_corner_piece_numbers;
	protected ArrayList<Integer> m_border_piece_numbers;
	protected ArrayList<Integer> m_center_piece_numbers;

	/*
	 * numbers of places sorted by kind of place
	 */
	protected ArrayList<Integer> m_corner_place_numbers;
	protected ArrayList<Integer> m_border_place_numbers;
	protected ArrayList<Integer> m_center_place_numbers;

	/*
	 * simple constructor cloning the original problem
	 */
	public ProblemEncodingSimple (Problem problem)
	{
		super (problem);

		m_amount_of_border_colors = m_border_colors.size ();
		m_amount_of_center_colors = m_center_colors.size ();

		initNumberArrays ();
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
			encodeCorners (formula);
			encodeBorders (formula);
		}
		
		encodeCenter (formula);

		/*
		 * here the encoding steps will be inserted ...
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

	/*
	 * create clauses concerning corner pieces and places
	 */
	protected void encodeCorners (CNFFormula formula)
	{
		for (Integer i_piece : m_corner_piece_numbers) {
			Clause tempClause = new Clause (m_corner_pieces_count);

			for (Integer i_place : m_corner_place_numbers) {
				tempClause.add (convertXijToSATVariable (i_piece, i_place));
			}

			formula.addClause (tempClause);

			for (Integer i_place : m_border_place_numbers) {
				int[] tempArray = {- convertXijToSATVariable (i_piece, i_place)};
				formula.addClause (tempArray);
			}

			for (Integer i_place : m_center_place_numbers) {
				int[] tempArray = {- convertXijToSATVariable (i_piece, i_place)};
				formula.addClause (tempArray);
			}
		}

		for (Integer i_place : m_corner_place_numbers) {
			Clause tempClause = new Clause (m_corner_pieces_count);

			for (Integer i_piece : m_corner_piece_numbers) {
				tempClause.add (convertXijToSATVariable (i_piece, i_place));
			}

			formula.addClause (tempClause);
		}

	}

	/*
	 * create clauses concerning border pieces and places
	 */
	protected void encodeBorders (CNFFormula formula)
	{
		for (Integer i_piece : m_border_piece_numbers) {
			Clause tempClause = new Clause (m_border_pieces_count);

			for (Integer i_place : m_border_place_numbers) {
				tempClause.add (convertXijToSATVariable (i_piece, i_place));
			}

			formula.addClause (tempClause);

			for (Integer i_place : m_corner_place_numbers) {
				int[] tempArray = {- convertXijToSATVariable (i_piece, i_place)};
				formula.addClause (tempArray);
			}

			for (Integer i_place : m_center_place_numbers) {
				int[] tempArray = {- convertXijToSATVariable (i_piece, i_place)};
				formula.addClause (tempArray);
			}
		}

		for (Integer i_place : m_border_place_numbers) {
			Clause tempClause = new Clause (m_border_pieces_count);

			for (Integer i_piece : m_border_piece_numbers) {
				tempClause.add (convertXijToSATVariable (i_piece, i_place));
			}

			formula.addClause (tempClause);
		}

	}

	/*
	 * create clauses concerning center pieces and places
	 */
	protected void encodeCenter (CNFFormula formula)
	{
		for (Integer i_piece : m_center_piece_numbers) {
			Clause tempClause = new Clause (m_center_pieces_count);

			for (Integer i_place : m_center_place_numbers) {
				tempClause.add (convertXijToSATVariable (i_piece, i_place));
			}

			formula.addClause (tempClause);

			for (Integer i_place : m_corner_place_numbers) {
				int[] tempArray = {- convertXijToSATVariable (i_piece, i_place)};
				formula.addClause (tempArray);
			}

			for (Integer i_place : m_border_place_numbers) {
				int[] tempArray = {- convertXijToSATVariable (i_piece, i_place)};
				formula.addClause (tempArray);
			}
		}

		for (Integer i_place : m_center_place_numbers) {
			Clause tempClause = new Clause (m_center_pieces_count);

			for (Integer i_piece : m_center_piece_numbers) {
				tempClause.add (convertXijToSATVariable (i_piece, i_place));
			}

			formula.addClause (tempClause);
		}

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

	/*
	 * initialize arrays containing piece and place numbers...
	 */
	protected void initNumberArrays ()
	{
		m_pieces = new ArrayList<Piece> (m_grid_width * m_grid_height);

		m_corner_piece_numbers = new ArrayList<Integer> (m_corner_pieces_count);
		m_border_piece_numbers = new ArrayList<Integer> (m_border_pieces_count);
		m_center_piece_numbers = new ArrayList<Integer> (m_center_pieces_count);

		m_corner_place_numbers = new ArrayList<Integer> (m_corner_pieces_count);
		m_border_place_numbers = new ArrayList<Integer> (m_border_pieces_count);
		m_center_place_numbers = new ArrayList<Integer> (m_center_pieces_count);

		int current_position = 0;

		for (Piece i_corner_piece : m_corner_pieces) {
			m_pieces.add (current_position, i_corner_piece);
			m_corner_piece_numbers.add (current_position);
			current_position ++;
		}

		for (Piece i_border_piece : m_border_pieces) {
			m_pieces.add (current_position, i_border_piece);
			m_border_piece_numbers.add (current_position);
			current_position ++;
		}

		for (Piece i_center_piece : m_center_pieces) {
			m_pieces.add (current_position, i_center_piece);
			m_center_piece_numbers.add (current_position);
			current_position ++;
		}

		m_corner_place_numbers.add (convertXYToSubsequentNumber (0, 0));
		m_corner_place_numbers.add (convertXYToSubsequentNumber (0, m_grid_height - 1));
		m_corner_place_numbers.add (convertXYToSubsequentNumber (m_grid_width - 1, 0));
		m_corner_place_numbers.add (convertXYToSubsequentNumber (m_grid_width - 1, m_grid_height - 1));

		for (int i_x = 1; i_x < m_grid_width - 1; i_x ++) {
			m_border_place_numbers.add (convertXYToSubsequentNumber (i_x, 0));
			m_border_place_numbers.add (convertXYToSubsequentNumber (i_x, m_grid_height - 1));
		}

		for (int i_y = 1; i_y < m_grid_height - 1; i_y ++) {
			m_border_place_numbers.add (convertXYToSubsequentNumber (0, i_y));
			m_border_place_numbers.add (convertXYToSubsequentNumber (m_grid_height - 1, i_y));
		}

		for (int i_x = 1; i_x < m_grid_width - 1; i_x ++) {
			for (int i_y = 1; i_y < m_grid_height - 1; i_y ++) {
				m_center_place_numbers.add (convertXYToSubsequentNumber (i_x, i_y));
			}
		}
	}
}
