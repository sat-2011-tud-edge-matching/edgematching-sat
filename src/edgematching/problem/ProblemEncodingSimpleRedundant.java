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
	public ProblemEncodingSimpleRedundant (Problem problem)
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
		encodeExplicitOneOnOneMappingPieces (formula);
		encodeExplicitOneOnOneMappingPlaces (formula);

		return formula;
	}

	protected void encodeForbiddenColorClauses (CNFFormula formula)
	{
		// center colors
		TreeMap<Integer,HashSet<Integer>> forbidden_color_sets_center = new TreeMap<Integer,HashSet<Integer>> ();

		for (int i_piece : m_center_piece_numbers) {
			Piece current_piece = m_pieces.get (i_piece);

			HashSet<Integer> forbidden_colors = new HashSet<Integer> ();

			forbidden_colors.addAll (m_center_colors_map_forward.keySet ());

			current_piece.removeAllColorsFrom (forbidden_colors);

			forbidden_color_sets_center.put (i_piece, forbidden_colors);
		}

		for (int i_place : m_center_place_numbers) {

		}
	}

	protected void encodeExplicitOneOnOneMappingPlaces (CNFFormula formula)
	{
		// corner places 
		for (ListIterator<Integer> i_first_place = m_corner_place_numbers.listIterator (); i_first_place.hasNext (); ) {
			int first_place= i_first_place.next ();

			for (ListIterator<Integer> i_second_place = m_corner_place_numbers.listIterator (i_first_place.nextIndex ()); i_second_place.hasNext (); ) {
				int second_place = i_second_place.next ();

				for (Integer piece: m_corner_piece_numbers) {
					int[] tempArray = { - convertXijToSATVariable (piece, first_place), -convertXijToSATVariable (piece, second_place)};

					formula.addClause (tempArray);
				}
			}
		}

		// border places 
		for (ListIterator<Integer> i_first_place = m_border_place_numbers.listIterator (); i_first_place.hasNext (); ) {
			int first_place= i_first_place.next ();

			for (ListIterator<Integer> i_second_place = m_border_place_numbers.listIterator (i_first_place.nextIndex ()); i_second_place.hasNext (); ) {
				int second_place = i_second_place.next ();

				for (Integer piece: m_border_piece_numbers) {
					int[] tempArray = { - convertXijToSATVariable (piece, first_place), -convertXijToSATVariable (piece, second_place)};

					formula.addClause (tempArray);
				}
			}
		}

		// center places 
		for (ListIterator<Integer> i_first_place = m_center_place_numbers.listIterator (); i_first_place.hasNext (); ) {
			int first_place= i_first_place.next ();

			for (ListIterator<Integer> i_second_place = m_center_place_numbers.listIterator (i_first_place.nextIndex ()); i_second_place.hasNext (); ) {
				int second_place = i_second_place.next ();

				for (Integer piece: m_center_piece_numbers) {
					int[] tempArray = { - convertXijToSATVariable (piece, first_place), -convertXijToSATVariable (piece, second_place)};

					formula.addClause (tempArray);
				}
			}
		}
	}

	protected void encodeExplicitOneOnOneMappingPieces (CNFFormula formula)
	{
		// corner pieces
		for (ListIterator<Integer> i_first_piece = m_corner_piece_numbers.listIterator (); i_first_piece.hasNext (); ) {
			int first_piece = i_first_piece.next ();

			for (ListIterator<Integer> i_second_piece = m_corner_piece_numbers.listIterator (i_first_piece.nextIndex ()); i_second_piece.hasNext (); ) {
				int second_piece = i_second_piece.next ();

				for (Integer place : m_corner_place_numbers) {
					int[] tempArray = { - convertXijToSATVariable (first_piece, place), -convertXijToSATVariable (second_piece, place)};

					formula.addClause (tempArray);
				}
			}
		}

		// border pieces
		for (ListIterator<Integer> i_first_piece = m_border_piece_numbers.listIterator (); i_first_piece.hasNext (); ) {
			int first_piece = i_first_piece.next ();

			for (ListIterator<Integer> i_second_piece = m_border_piece_numbers.listIterator (i_first_piece.nextIndex ()); i_second_piece.hasNext (); ) {
				int second_piece = i_second_piece.next ();

				for (Integer place : m_border_place_numbers) {
					int[] tempArray = { - convertXijToSATVariable (first_piece, place), -convertXijToSATVariable (second_piece, place)};

					formula.addClause (tempArray);
				}
			}
		}

		// center pieces
		for (ListIterator<Integer> i_first_piece = m_center_piece_numbers.listIterator (); i_first_piece.hasNext (); ) {
			int first_piece = i_first_piece.next ();

			for (ListIterator<Integer> i_second_piece = m_center_piece_numbers.listIterator (i_first_piece.nextIndex ()); i_second_piece.hasNext (); ) {
				int second_piece = i_second_piece.next ();

				for (Integer place : m_center_place_numbers) {
					int[] tempArray = { - convertXijToSATVariable (first_piece, place), -convertXijToSATVariable (second_piece, place)};

					formula.addClause (tempArray);
				}
			}
		}
	}
}
