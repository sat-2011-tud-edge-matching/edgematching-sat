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
	@Override
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

			HashSet<Integer> forbidden_colors        = new HashSet<Integer> ();
			HashSet<Integer> forbidden_colors_mapped = new HashSet<Integer> ();

			forbidden_colors.addAll (m_center_colors_map_forward.keySet ());

			current_piece.removeAllColorsFrom (forbidden_colors);

			for (Integer i_color : forbidden_colors) {
				forbidden_colors_mapped.add (m_center_colors_map_forward.get (i_color));
			}

			forbidden_color_sets_center.put (i_piece, forbidden_colors_mapped);
		}

		for (int i_place : m_center_place_numbers) {
			int i_x = convertPlaceNumberToX (i_place);
			int i_y = convertPlaceNumberToY (i_place);

			int diamond_left   = (i_x > 0                 ? m_center_diamonds_map_forward.get (getLeftDiamondOfPlace   (i_x, i_y)) : -1);;
			int diamond_right  = (i_x < m_grid_width - 1  ? m_center_diamonds_map_forward.get (getRightDiamondOfPlace  (i_x, i_y)) : -1);;
			int diamond_top    = (i_y > 0                 ? m_center_diamonds_map_forward.get (getTopDiamondOfPlace    (i_x, i_y)) : -1);;
			int diamond_bottom = (i_y < m_grid_height - 1 ? m_center_diamonds_map_forward.get (getBottomDiamondOfPlace (i_x, i_y)) : -1);;

			if ((diamond_left < 0) || (diamond_right < 0) || (diamond_top < 0) || (diamond_bottom < 0)) continue;

			for (int i_piece : m_center_piece_numbers) {
				int xij = convertXijToSATVariable (i_piece, i_place);

				HashSet<Integer> forbidden_colors = forbidden_color_sets_center.get (i_piece);

				for (Integer i_forbidden_color : forbidden_colors) {
					int[] tempArray1 = {-xij, -convertYkcCenterToSATVariable (diamond_left,   i_forbidden_color)};
					int[] tempArray2 = {-xij, -convertYkcCenterToSATVariable (diamond_right,  i_forbidden_color)};
					int[] tempArray3 = {-xij, -convertYkcCenterToSATVariable (diamond_top,    i_forbidden_color)};
					int[] tempArray4 = {-xij, -convertYkcCenterToSATVariable (diamond_bottom, i_forbidden_color)};

					formula.addClause (tempArray1);
					formula.addClause (tempArray2);
					formula.addClause (tempArray3);
					formula.addClause (tempArray4);
				}
			}
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
