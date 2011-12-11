package edgematching.problem;

import java.util.*;
import edgematching.cnf.*;

public class ProblemEncodingOrder extends ProblemEncodingSimple
{
	protected int m_next_free_variable;

	public ProblemEncodingOrder (Problem problem)
	{
		super (problem);

		m_next_free_variable = m_sat_start_next_free_variable;

		m_sat_comment = "order encoding of an etch-matching puzzle\n" +
				"with size " + m_grid_width + " x " + m_grid_height + "\n" +
				(m_bounded ? "bounded" : "unbounded") + " and " + (m_signed ? "signed" : "unsigned") + ".\n";
	}

	/*
	 * override domain constraints and apply order encoding for explicit one out of n mappings
	 */

	@Override
	protected void encodeCorners (CNFFormula formula) {
		if (m_corner_pieces_count < 2) {
			super.encodeCorners (formula);

			return;
		}

		int number_of_additional_variables = m_corner_pieces_count - 1;
		int start_variable;

		for (Integer i_piece : m_corner_piece_numbers) {
			start_variable        = m_next_free_variable;
			m_next_free_variable += number_of_additional_variables;

			int i = 0;

			for (Integer i_place : m_corner_place_numbers) {
				int current_placement = convertXijToSATVariable (i_piece, i_place);

				// i: start_variable + 0   --> n = 2
				//    start_variable + 1   --> n = 3
				//    ...
				//    start_variable + N-2 --> n = N

				if (i == 0) {
					int[] tempArray1 = {- current_placement, - (start_variable + i)};
					int[] tempArray2 = {current_placement, (start_variable + i)};

					formula.addClause (tempArray1);
					formula.addClause (tempArray2);
				} else if (i == number_of_additional_variables) {
					int[] tempArray1 = {- current_placement, (start_variable + i - 1)};
					int[] tempArray2 = {current_placement, - (start_variable + i - 1)};

					formula.addClause (tempArray1);
					formula.addClause (tempArray2);
				} else {
					int[] tempArray1 = {- current_placement, (start_variable + i - 1)};
					int[] tempArray2 = {- current_placement, - (start_variable + i)};
					int[] tempArray3 = {current_placement, - (start_variable + i - 1), (start_variable + i)};
					int[] tempArrayO = {(start_variable + i - 1), - (start_variable + i)};

					formula.addClause (tempArray1);
					formula.addClause (tempArray2);
					formula.addClause (tempArray3);
					formula.addClause (tempArrayO);
				}

				i ++;
			}

			// forbid impossible combinations
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
			start_variable        = m_next_free_variable;
			m_next_free_variable += number_of_additional_variables;

			int i = 0;

			for (Integer i_piece : m_corner_piece_numbers) {
				int current_placement = convertXijToSATVariable (i_piece, i_place);

				// i: start_variable + 0   --> n = 2
				//    start_variable + 1   --> n = 3
				//    ...
				//    start_variable + N-2 --> n = N

				if (i == 0) {
					int[] tempArray1 = {- current_placement, - (start_variable + i)};
					int[] tempArray2 = {current_placement, (start_variable + i)};

					formula.addClause (tempArray1);
					formula.addClause (tempArray2);
				} else if (i == number_of_additional_variables) {
					int[] tempArray1 = {- current_placement, (start_variable + i - 1)};
					int[] tempArray2 = {current_placement, - (start_variable + i - 1)};

					formula.addClause (tempArray1);
					formula.addClause (tempArray2);
				} else {
					int[] tempArray1 = {- current_placement, (start_variable + i - 1)};
					int[] tempArray2 = {- current_placement, - (start_variable + i)};
					int[] tempArray3 = {current_placement, - (start_variable + i - 1), (start_variable + i)};
					int[] tempArrayO = {(start_variable + i - 1), - (start_variable + i)};

					formula.addClause (tempArray1);
					formula.addClause (tempArray2);
					formula.addClause (tempArray3);
					formula.addClause (tempArrayO);
				}

				i ++;
			}
		}
	}

	@Override
	protected void encodeBorders (CNFFormula formula) {
		if (m_border_pieces_count < 2) {
			super.encodeBorders (formula);

			return;
		}

		int number_of_additional_variables = m_border_pieces_count - 1;
		int start_variable;

		for (Integer i_piece : m_border_piece_numbers) {
			start_variable        = m_next_free_variable;
			m_next_free_variable += number_of_additional_variables;

			int i = 0;

			for (Integer i_place : m_border_place_numbers) {
				int current_placement = convertXijToSATVariable (i_piece, i_place);

				// i: start_variable + 0   --> n = 2
				//    start_variable + 1   --> n = 3
				//    ...
				//    start_variable + N-2 --> n = N

				if (i == 0) {
					int[] tempArray1 = {- current_placement, - (start_variable + i)};
					int[] tempArray2 = {current_placement, (start_variable + i)};

					formula.addClause (tempArray1);
					formula.addClause (tempArray2);
				} else if (i == number_of_additional_variables) {
					int[] tempArray1 = {- current_placement, (start_variable + i - 1)};
					int[] tempArray2 = {current_placement, - (start_variable + i - 1)};

					formula.addClause (tempArray1);
					formula.addClause (tempArray2);
				} else {
					int[] tempArray1 = {- current_placement, (start_variable + i - 1)};
					int[] tempArray2 = {- current_placement, - (start_variable + i)};
					int[] tempArray3 = {current_placement, - (start_variable + i - 1), (start_variable + i)};
					int[] tempArrayO = {(start_variable + i - 1), - (start_variable + i)};

					formula.addClause (tempArray1);
					formula.addClause (tempArray2);
					formula.addClause (tempArray3);
					formula.addClause (tempArrayO);
				}

				i ++;
			}

			// forbid impossible combinations
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
			start_variable        = m_next_free_variable;
			m_next_free_variable += number_of_additional_variables;

			int i = 0;

			for (Integer i_piece : m_border_piece_numbers) {
				int current_placement = convertXijToSATVariable (i_piece, i_place);

				// i: start_variable + 0   --> n = 2
				//    start_variable + 1   --> n = 3
				//    ...
				//    start_variable + N-2 --> n = N

				if (i == 0) {
					int[] tempArray1 = {- current_placement, - (start_variable + i)};
					int[] tempArray2 = {current_placement, (start_variable + i)};

					formula.addClause (tempArray1);
					formula.addClause (tempArray2);
				} else if (i == number_of_additional_variables) {
					int[] tempArray1 = {- current_placement, (start_variable + i - 1)};
					int[] tempArray2 = {current_placement, - (start_variable + i - 1)};

					formula.addClause (tempArray1);
					formula.addClause (tempArray2);
				} else {
					int[] tempArray1 = {- current_placement, (start_variable + i - 1)};
					int[] tempArray2 = {- current_placement, - (start_variable + i)};
					int[] tempArray3 = {current_placement, - (start_variable + i - 1), (start_variable + i)};
					int[] tempArrayO = {(start_variable + i - 1), - (start_variable + i)};

					formula.addClause (tempArray1);
					formula.addClause (tempArray2);
					formula.addClause (tempArray3);
					formula.addClause (tempArrayO);
				}

				i ++;
			}
		}
	}

	@Override
	protected void encodeCenter (CNFFormula formula) {
		if (m_center_pieces_count < 2) {
			super.encodeCenter (formula);

			return;
		}

		int number_of_additional_variables = m_center_pieces_count - 1;
		int start_variable;

		for (Integer i_piece : m_center_piece_numbers) {
			start_variable        = m_next_free_variable;
			m_next_free_variable += number_of_additional_variables;

			int i = 0;

			for (Integer i_place : m_center_place_numbers) {
				int current_placement = convertXijToSATVariable (i_piece, i_place);

				// i: start_variable + 0   --> n = 2
				//    start_variable + 1   --> n = 3
				//    ...
				//    start_variable + N-2 --> n = N

				if (i == 0) {
					int[] tempArray1 = {- current_placement, - (start_variable + i)};
					int[] tempArray2 = {current_placement, (start_variable + i)};

					formula.addClause (tempArray1);
					formula.addClause (tempArray2);
				} else if (i == number_of_additional_variables) {
					int[] tempArray1 = {- current_placement, (start_variable + i - 1)};
					int[] tempArray2 = {current_placement, - (start_variable + i - 1)};

					formula.addClause (tempArray1);
					formula.addClause (tempArray2);
				} else {
					int[] tempArray1 = {- current_placement, (start_variable + i - 1)};
					int[] tempArray2 = {- current_placement, - (start_variable + i)};
					int[] tempArray3 = {current_placement, - (start_variable + i - 1), (start_variable + i)};
					int[] tempArrayO = {(start_variable + i - 1), - (start_variable + i)};

					formula.addClause (tempArray1);
					formula.addClause (tempArray2);
					formula.addClause (tempArray3);
					formula.addClause (tempArrayO);
				}

				i ++;
			}

			// forbid impossible combinations
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
			start_variable        = m_next_free_variable;
			m_next_free_variable += number_of_additional_variables;

			int i = 0;

			for (Integer i_piece : m_center_piece_numbers) {
				int current_placement = convertXijToSATVariable (i_piece, i_place);

				// i: start_variable + 0   --> n = 2
				//    start_variable + 1   --> n = 3
				//    ...
				//    start_variable + N-2 --> n = N

				if (i == 0) {
					int[] tempArray1 = {- current_placement, - (start_variable + i)};
					int[] tempArray2 = {current_placement, (start_variable + i)};

					formula.addClause (tempArray1);
					formula.addClause (tempArray2);
				} else if (i == number_of_additional_variables) {
					int[] tempArray1 = {- current_placement, (start_variable + i - 1)};
					int[] tempArray2 = {current_placement, - (start_variable + i - 1)};

					formula.addClause (tempArray1);
					formula.addClause (tempArray2);
				} else {
					int[] tempArray1 = {- current_placement, (start_variable + i - 1)};
					int[] tempArray2 = {- current_placement, - (start_variable + i)};
					int[] tempArray3 = {current_placement, - (start_variable + i - 1), (start_variable + i)};
					int[] tempArrayO = {(start_variable + i - 1), - (start_variable + i)};

					formula.addClause (tempArray1);
					formula.addClause (tempArray2);
					formula.addClause (tempArray3);
					formula.addClause (tempArrayO);
				}

				i ++;
			}
		}
	}

	@Override
	protected void encodeDiamondsBorder (CNFFormula formula)
	{
		// each border diamond has one color

		if (m_border_colors_count < 2) {
			super.encodeDiamondsBorder (formula);

			return;
		}

		int number_of_additional_variables = m_border_colors_count - 1;
		int start_variable;

		for (int i_diamond = 0; i_diamond < m_border_diamonds_count; i_diamond ++) {
			start_variable        = m_next_free_variable;
			m_next_free_variable += number_of_additional_variables;

			int i = 0;

			for (int i_color = 0; i_color < m_border_colors_count; i_color ++) {

				int current_ykc = convertYkcBorderToSATVariable (i_diamond, i_color);

				// i: start_variable + 0   --> n = 2
				//    start_variable + 1   --> n = 3
				//    ...
				//    start_variable + N-2 --> n = N

				if (i == 0) {
					int[] tempArray1 = {- current_ykc, - (start_variable + i)};
					int[] tempArray2 = {current_ykc, (start_variable + i)};

					formula.addClause (tempArray1);
					formula.addClause (tempArray2);
				} else if (i == number_of_additional_variables) {
					int[] tempArray1 = {- current_ykc, (start_variable + i - 1)};
					int[] tempArray2 = {current_ykc, - (start_variable + i - 1)};

					formula.addClause (tempArray1);
					formula.addClause (tempArray2);
				} else {
					int[] tempArray1 = {- current_ykc, (start_variable + i - 1)};
					int[] tempArray2 = {- current_ykc, - (start_variable + i)};
					int[] tempArray3 = {current_ykc, - (start_variable + i - 1), (start_variable + i)};
					int[] tempArrayO = {(start_variable + i - 1), - (start_variable + i)};

					formula.addClause (tempArray1);
					formula.addClause (tempArray2);
					formula.addClause (tempArray3);
					formula.addClause (tempArrayO);
				}

				i ++;
			}
		}
	}

	@Override
	protected void encodeDiamondsCenter (CNFFormula formula)
	{
		// each center diamond has one color

		if (m_center_colors_count < 2) {
			super.encodeDiamondsCenter (formula);

			return;
		}

		int number_of_additional_variables = m_center_colors_count - 1;
		int start_variable;

		for (int i_diamond = 0; i_diamond < m_center_diamonds_count; i_diamond ++) {
			start_variable        = m_next_free_variable;
			m_next_free_variable += number_of_additional_variables;

			int i = 0;

			for (int i_color = 0; i_color < m_center_colors_count; i_color ++) {

				int current_ykc = convertYkcCenterToSATVariable (i_diamond, i_color);

				// i: start_variable + 0   --> n = 2
				//    start_variable + 1   --> n = 3
				//    ...
				//    start_variable + N-2 --> n = N

				if (i == 0) {
					int[] tempArray1 = {- current_ykc, - (start_variable + i)};
					int[] tempArray2 = {current_ykc, (start_variable + i)};

					formula.addClause (tempArray1);
					formula.addClause (tempArray2);
				} else if (i == number_of_additional_variables) {
					int[] tempArray1 = {- current_ykc, (start_variable + i - 1)};
					int[] tempArray2 = {current_ykc, - (start_variable + i - 1)};

					formula.addClause (tempArray1);
					formula.addClause (tempArray2);
				} else {
					int[] tempArray1 = {- current_ykc, (start_variable + i - 1)};
					int[] tempArray2 = {- current_ykc, - (start_variable + i)};
					int[] tempArray3 = {current_ykc, - (start_variable + i - 1), (start_variable + i)};
					int[] tempArrayO = {(start_variable + i - 1), - (start_variable + i)};

					formula.addClause (tempArray1);
					formula.addClause (tempArray2);
					formula.addClause (tempArray3);
					formula.addClause (tempArrayO);
				}

				i ++;
			}
		}
	}
}
