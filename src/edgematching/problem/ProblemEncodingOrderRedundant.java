package edgematching.problem;

import java.util.*;
import edgematching.cnf.*;

public class ProblemEncodingOrderRedundant extends ProblemEncodingOrder
{
	public ProblemEncodingOrderRedundant (Problem problem)
	{
		super (problem);

		m_sat_comment = "redundant order encoding of an etch-matching puzzle\n" +
				"with size " + m_grid_width + " x " + m_grid_height + "\n" +
				(m_bounded ? "bounded" : "unbounded") + " and " + (m_signed ? "signed" : "unsigned") + ".\n";
	}

	@Override
	public CNFFormula encodeToSAT ()
	{
		CNFFormula formula = super.encodeToSAT ();

		encodeForbiddenColorClauses (formula);

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
}
