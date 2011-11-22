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
	 * amounts of colors and mapping
	 */
	protected int m_border_colors_count;
	protected int m_center_colors_count;

	protected Map<Integer,Integer> m_border_colors_map_forward;
	protected Map<Integer,Integer> m_border_colors_map_backward;
	protected Map<Integer,Integer> m_center_colors_map_forward;
	protected Map<Integer,Integer> m_center_colors_map_backward;

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
	 * diamonds and mapping of diamonds ...
	 */
	protected ArrayList<Integer> m_border_diamonds;
	protected ArrayList<Integer> m_center_diamonds;

	protected int m_border_diamonds_count;
	protected int m_center_diamonds_count;

	protected Map<Integer,Integer> m_border_diamonds_map_forward;
	protected Map<Integer,Integer> m_border_diamonds_map_backward;
	protected Map<Integer,Integer> m_center_diamonds_map_forward;
	protected Map<Integer,Integer> m_center_diamonds_map_backward;

	/*
	 * SAT-Start-values
	 */
	protected int m_sat_start_border_diamonds;
	protected int m_sat_start_center_diamonds;

	/*
	 * simple constructor cloning the original problem
	 */
	public ProblemEncodingSimple (Problem problem)
	{
		super (problem);

		initPiecesAndPlaces ();
		initColors ();
		initDiamonds ();
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
		encodeDiamonds (formula);

		if (m_bounded) {
			encodeCornerDiamondCorrelation (formula);
			encodeBorderDiamondCorrelation (formula);
		}

		encodeCenterDiamondCorrelation (formula);

		return formula;
	}

	/*
	 * function decoding our sat-solution
	 */
	public void decodeSolution (CNFFormula formula)
	{
		if (! formula.isSolved ()) return;

		TreeSet<Integer> solution = new TreeSet<Integer> ();

		for (Integer i_literal : formula.getSolution ()) {
			if (i_literal > 0) {
				solution.add (i_literal);
			}
		}

		/*
		 * here the solution of formula has to be decoded back ...
		 */

		System.out.println ("Solution: ");
		System.out.println (solution);
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
		// each corner piece has to be on at least one corner place
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

			// two equal pieces cannot be on the same place
			for (Integer i_other_piece : m_corner_piece_numbers) {
				if (i_piece == i_other_piece) continue;
				if (! (m_pieces.get (i_piece).equals (m_pieces.get (i_other_piece)))) continue;

				for (Integer i_place : m_corner_place_numbers) {
					int[] tempArray = {- convertXijToSATVariable (i_piece, i_place),
							   - convertXijToSATVariable (i_other_piece, i_place)};
					formula.addClause (tempArray);
				}
			}
		}

		// each corner place has to contain at least one corner piece
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

			// two equal pieces cannot be on the same place
			for (Integer i_other_piece : m_border_piece_numbers) {
				if (i_piece == i_other_piece) continue;
				if (! (m_pieces.get (i_piece).equals (m_pieces.get (i_other_piece)))) continue;

				for (Integer i_place : m_border_place_numbers) {
					int[] tempArray = {- convertXijToSATVariable (i_piece, i_place),
							   - convertXijToSATVariable (i_other_piece, i_place)};
					formula.addClause (tempArray);
				}
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

			// two equal pieces cannot be on the same place
			for (Integer i_other_piece : m_center_piece_numbers) {
				if (i_piece == i_other_piece) continue;
				if (! (m_pieces.get (i_piece).equals (m_pieces.get (i_other_piece)))) continue;

				for (Integer i_place : m_center_place_numbers) {
					int[] tempArray = {- convertXijToSATVariable (i_piece, i_place),
							   - convertXijToSATVariable (i_other_piece, i_place)};
					formula.addClause (tempArray);
				}
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

	protected void encodeDiamonds (CNFFormula formula)
	{
		// each border diamond has a color
		for (int i_diamond = 0; i_diamond < m_border_diamonds_count; i_diamond ++) {
			Clause tempClause = new Clause (m_border_colors_count);

			for (int i_color = 0; i_color < m_border_colors_count; i_color ++) {
				tempClause.add (convertYkcBorderToSATVariable (i_diamond, i_color));

				// each border diamond has just one color
				for (int j_color = i_color + 1; j_color < m_border_colors_count; j_color ++) {
					int[] tempArray = {- convertYkcBorderToSATVariable (i_diamond, i_color),
							   - convertYkcBorderToSATVariable (i_diamond, j_color)};

					formula.addClause (tempArray);
				}
			}

			formula.addClause (tempClause);
		}

		// each center diamond has a color
		for (int i_diamond = 0; i_diamond < m_center_diamonds_count; i_diamond ++) {
			Clause tempClause = new Clause (m_center_colors_count);

			for (int i_color = 0; i_color < m_center_colors_count; i_color ++) {
				tempClause.add (convertYkcCenterToSATVariable (i_diamond, i_color));

				// each center diamond has just one color
				for (int j_color = i_color + 1; j_color < m_center_colors_count; j_color ++) {
					int[] tempArray = {- convertYkcCenterToSATVariable (i_diamond, i_color),
							   - convertYkcCenterToSATVariable (i_diamond, j_color)};

					formula.addClause (tempArray);
				}
			}

			formula.addClause (tempClause);
		}
	}

	/*
	 * encode correlation of placing a corner piece on a place and coloring connected diamonds
	 */
	protected void encodeCornerDiamondCorrelation (CNFFormula formula)
	{
		// directions are named watching the piece/place with zeroes at top
		int topleft_corner     = convertXYToPlaceNumber (0, 0);
		int topright_corner    = convertXYToPlaceNumber (m_grid_width - 1, 0);
		int bottomleft_corner  = convertXYToPlaceNumber (0, m_grid_height - 1);
		int bottomright_corner = convertXYToPlaceNumber (m_grid_width - 1, m_grid_height - 1);

		int topleft_diamond_left      = m_border_diamonds_map_forward.get (getBottomDiamondOfPlace (0, 0));
		int topleft_diamond_right     = m_border_diamonds_map_forward.get (getRightDiamondOfPlace (0, 0));
		int topright_diamond_left     = m_border_diamonds_map_forward.get (getLeftDiamondOfPlace (m_grid_width - 1, 0));
		int topright_diamond_right    = m_border_diamonds_map_forward.get (getBottomDiamondOfPlace (m_grid_width - 1, 0));
		int bottomleft_diamond_left   = m_border_diamonds_map_forward.get (getRightDiamondOfPlace (0, m_grid_height - 1));
		int bottomleft_diamond_right  = m_border_diamonds_map_forward.get (getTopDiamondOfPlace (0, m_grid_height - 1));
		int bottomright_diamond_left  = m_border_diamonds_map_forward.get (getTopDiamondOfPlace (m_grid_width - 1, m_grid_height - 1));
		int bottomright_diamond_right = m_border_diamonds_map_forward.get (getLeftDiamondOfPlace (m_grid_width - 1, m_grid_height - 1));

		for (int i_piece : m_corner_piece_numbers) {
			int color_left  = m_border_colors_map_forward.get (m_pieces.get (i_piece).getBorderColorLeft ());
			int color_right = m_border_colors_map_forward.get (m_pieces.get (i_piece).getBorderColorRight ());

			int[] tempArray00 = {- convertXijToSATVariable (i_piece, topleft_corner),
				       convertYkcBorderToSATVariable (topleft_diamond_left, color_left)};
			formula.addClause (tempArray00);
			int[] tempArray01 = {- convertXijToSATVariable (i_piece, topleft_corner),
				       convertYkcBorderToSATVariable (topleft_diamond_right, color_right)};
			formula.addClause (tempArray01);

			int[] tempArray02 = {- convertXijToSATVariable (i_piece, topright_corner),
				       convertYkcBorderToSATVariable (topright_diamond_left, color_left)};
			formula.addClause (tempArray02);
			int[] tempArray03 = {- convertXijToSATVariable (i_piece, topright_corner),
				       convertYkcBorderToSATVariable (topright_diamond_right, color_right)};
			formula.addClause (tempArray03);

			int[] tempArray04 = {- convertXijToSATVariable (i_piece, bottomleft_corner),
				       convertYkcBorderToSATVariable (bottomleft_diamond_left, color_left)};
			formula.addClause (tempArray04);
			int[] tempArray05 = {- convertXijToSATVariable (i_piece, bottomleft_corner),
				       convertYkcBorderToSATVariable (bottomleft_diamond_right, color_right)};
			formula.addClause (tempArray05);

			int[] tempArray06 = {- convertXijToSATVariable (i_piece, bottomright_corner),
				       convertYkcBorderToSATVariable (bottomright_diamond_left, color_left)};
			formula.addClause (tempArray06);
			int[] tempArray07 = {- convertXijToSATVariable (i_piece, bottomright_corner),
				       convertYkcBorderToSATVariable (bottomright_diamond_right, color_right)};
			formula.addClause (tempArray07);
		}
	}

	/*
	 * encode correlation of placing a border piece on a place and coloring connected diamonds
	 */
	protected void encodeBorderDiamondCorrelation (CNFFormula formula)
	{
		for (int i_piece : m_border_piece_numbers) {
			Piece currentPiece = m_pieces.get (i_piece);

			int color_left   = m_border_colors_map_forward.get (currentPiece.getBorderColorLeft ());
			int color_right  = m_border_colors_map_forward.get (currentPiece.getBorderColorRight ());
			int color_bottom = m_center_colors_map_forward.get (currentPiece.getBorderColorBottem ());

			// top and bottom row
			for (int i_x = 1; i_x < m_grid_width - 1; i_x ++) {
				int top_place    = convertXYToPlaceNumber (i_x, 0);
				int bottom_place = convertXYToPlaceNumber (i_x, m_grid_height - 1);

				int top_diamond_left      = m_border_diamonds_map_forward.get (getLeftDiamondOfPlace (i_x, 0));
				int top_diamond_right     = m_border_diamonds_map_forward.get (getRightDiamondOfPlace (i_x, 0));
				int top_diamond_bottom    = m_center_diamonds_map_forward.get (getBottomDiamondOfPlace (i_x, 0));
				int bottom_diamond_left      = m_border_diamonds_map_forward.get (getRightDiamondOfPlace (i_x, m_grid_height - 1));
				int bottom_diamond_right     = m_border_diamonds_map_forward.get (getLeftDiamondOfPlace (i_x, m_grid_height - 1));
				int bottom_diamond_bottom    = m_center_diamonds_map_forward.get (getTopDiamondOfPlace (i_x, m_grid_height - 1));

				int[] tempArray00 = {- convertXijToSATVariable (i_piece, top_place),
						       convertYkcBorderToSATVariable (top_diamond_left, color_left)};
				formula.addClause (tempArray00);
				int[] tempArray01 = {- convertXijToSATVariable (i_piece, top_place),
						       convertYkcBorderToSATVariable (top_diamond_right, color_right)};
				formula.addClause (tempArray01);
				int[] tempArray02 = {- convertXijToSATVariable (i_piece, top_place),
						       convertYkcCenterToSATVariable (top_diamond_bottom, color_bottom)};
				formula.addClause (tempArray02);

				int[] tempArray10 = {- convertXijToSATVariable (i_piece, bottom_place),
						       convertYkcBorderToSATVariable (bottom_diamond_left, color_left)};
				formula.addClause (tempArray10);
				int[] tempArray11 = {- convertXijToSATVariable (i_piece, bottom_place),
						       convertYkcBorderToSATVariable (bottom_diamond_right, color_right)};
				formula.addClause (tempArray11);
				int[] tempArray12 = {- convertXijToSATVariable (i_piece, bottom_place),
						       convertYkcCenterToSATVariable (bottom_diamond_bottom, color_bottom)};
				formula.addClause (tempArray12);
			}

			// left and right column
			for (int i_y = 1; i_y < m_grid_height - 1; i_y ++) {
				int left_place    = convertXYToPlaceNumber (0, i_y);
				int right_place = convertXYToPlaceNumber (m_grid_width - 1, i_y);

				int left_diamond_left      = m_border_diamonds_map_forward.get (getBottomDiamondOfPlace (0, i_y));
				int left_diamond_right     = m_border_diamonds_map_forward.get (getTopDiamondOfPlace (0, i_y));
				int left_diamond_bottom    = m_center_diamonds_map_forward.get (getRightDiamondOfPlace (0, i_y));
				int right_diamond_left      = m_border_diamonds_map_forward.get (getTopDiamondOfPlace (m_grid_width - 1, i_y));
				int right_diamond_right     = m_border_diamonds_map_forward.get (getBottomDiamondOfPlace (m_grid_width - 1, i_y));
				int right_diamond_bottom    = m_center_diamonds_map_forward.get (getLeftDiamondOfPlace (m_grid_width - 1, i_y));

				int[] tempArray00 = {- convertXijToSATVariable (i_piece, left_place),
						       convertYkcBorderToSATVariable (left_diamond_left, color_left)};
				formula.addClause (tempArray00);
				int[] tempArray01 = {- convertXijToSATVariable (i_piece, left_place),
						       convertYkcBorderToSATVariable (left_diamond_right, color_right)};
				formula.addClause (tempArray01);
				int[] tempArray02 = {- convertXijToSATVariable (i_piece, left_place),
						       convertYkcCenterToSATVariable (left_diamond_bottom, color_bottom)};
				formula.addClause (tempArray02);

				int[] tempArray10 = {- convertXijToSATVariable (i_piece, right_place),
						       convertYkcBorderToSATVariable (right_diamond_left, color_left)};
				formula.addClause (tempArray10);
				int[] tempArray11 = {- convertXijToSATVariable (i_piece, right_place),
						       convertYkcBorderToSATVariable (right_diamond_right, color_right)};
				formula.addClause (tempArray11);
				int[] tempArray12 = {- convertXijToSATVariable (i_piece, right_place),
						       convertYkcCenterToSATVariable (right_diamond_bottom, color_bottom)};
				formula.addClause (tempArray12);
			}
		}
	}

	protected void encodeCenterDiamondCorrelation (CNFFormula formula)
	{
		/*
		 * encode correlation of center piece placing and diamond colors...
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
	protected final int convertXYToPlaceNumber (int x, int y)
	{
		return (m_grid_width * y) + x;
	}

	protected final int convertPlaceNumberToX (int place)
	{
		return (place % m_grid_width);
	}

	protected final int convertPlaceNumberToY (int place)
	{
		return (place / m_grid_width);
	}

	protected final int convertXijToSATVariable (int piece, int place)
	{
		return (m_grid_width * m_grid_height * place + piece) + 1;
	}

	protected final int convertSATVariableToPiece (int variable)
	{
		return (variable - 1) % (m_grid_width * m_grid_height);
	}

	protected final int convertSATVariableToPlace (int variable)
	{
		return (variable - 1) / (m_grid_width * m_grid_height);
	}

	protected final int getLeftDiamondOfPlace (int x, int y)
	{
		return ((2 * m_grid_width - 1) * y + x - 1);
	}

	protected final int getRightDiamondOfPlace (int x, int y)
	{
		return ((2 * m_grid_width - 1) * y + x);
	}

	protected final int getTopDiamondOfPlace (int x, int y)
	{
		return ((2 * m_grid_width - 1) * y + x - m_grid_width);
	}

	protected final int getBottomDiamondOfPlace (int x, int y)
	{
		return ((2 * m_grid_width - 1) * y + x + m_grid_width - 1);
	}

	protected final int convertYkcBorderToSATVariable (int diamond, int color)
	{
		return (m_sat_start_border_diamonds + diamond * m_border_colors_count + color);
	}

	protected final int convertYkcCenterToSATVariable (int diamond, int color)
	{
		return (m_sat_start_center_diamonds + diamond * m_center_colors_count + color);
	}

	/*
	 * initialize arrays containing piece and place numbers...
	 */
	protected void initPiecesAndPlaces ()
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

		if (m_bounded) {
			m_corner_place_numbers.add (convertXYToPlaceNumber (0, 0));
			m_corner_place_numbers.add (convertXYToPlaceNumber (0, m_grid_height - 1));
			m_corner_place_numbers.add (convertXYToPlaceNumber (m_grid_width - 1, 0));
			m_corner_place_numbers.add (convertXYToPlaceNumber (m_grid_width - 1, m_grid_height - 1));

			for (int i_x = 1; i_x < m_grid_width - 1; i_x ++) {
				m_border_place_numbers.add (convertXYToPlaceNumber (i_x, 0));
				m_border_place_numbers.add (convertXYToPlaceNumber (i_x, m_grid_height - 1));
			}

			for (int i_y = 1; i_y < m_grid_height - 1; i_y ++) {
				m_border_place_numbers.add (convertXYToPlaceNumber (0, i_y));
				m_border_place_numbers.add (convertXYToPlaceNumber (m_grid_height - 1, i_y));
			}

			for (int i_x = 1; i_x < m_grid_width - 1; i_x ++) {
				for (int i_y = 1; i_y < m_grid_height - 1; i_y ++) {
					m_center_place_numbers.add (convertXYToPlaceNumber (i_x, i_y));
				}
			}
		} else {
			for (int i_x = 0; i_x < m_grid_width; i_x ++) {
				for (int i_y = 0; i_y < m_grid_height; i_y ++) {
					m_center_place_numbers.add (convertXYToPlaceNumber (i_x, i_y));
				}
			}
		}
	}

	/*
	 * initialize colors and mapping
	 */
	protected void initColors ()
	{
		m_border_colors_count = m_border_colors.size ();
		m_center_colors_count = m_center_colors.size ();

		m_border_colors_map_forward  = new TreeMap<Integer,Integer> ();
		m_border_colors_map_backward = new TreeMap<Integer,Integer> ();
		m_center_colors_map_forward  = new TreeMap<Integer,Integer> ();
		m_center_colors_map_backward = new TreeMap<Integer,Integer> ();

		int i_color = 0;
		for (Integer color : m_border_colors) {
			m_border_colors_map_backward.put (i_color, color);
			m_border_colors_map_forward.put (color, i_color);
			i_color ++;
		}

		i_color = 0;
		for (Integer color : m_center_colors) {
			m_center_colors_map_backward.put (i_color, color);
			m_center_colors_map_forward.put (color, i_color);
			i_color ++;
		}
	}

	/*
	 * initialize arrays and maps containing diamond information
	 */
	protected void initDiamonds ()
	{
		if (m_bounded) {
			m_border_diamonds_count = 2 * (m_grid_width + m_grid_height - 2);
			m_center_diamonds_count = 2 * m_grid_width * m_grid_height - 3 * (m_grid_width + m_grid_height) + 4;
		} else {
			m_border_diamonds_count = 0;
			m_center_diamonds_count = 2 * m_grid_width * m_grid_height - m_grid_width - m_grid_height;
		}

		m_border_diamonds = new ArrayList<Integer> (m_border_diamonds_count);
		m_center_diamonds = new ArrayList<Integer> (m_center_diamonds_count);

		m_border_diamonds_map_forward  = new TreeMap<Integer,Integer> ();
		m_border_diamonds_map_backward = new TreeMap<Integer,Integer> ();
		m_center_diamonds_map_forward  = new TreeMap<Integer,Integer> ();
		m_center_diamonds_map_backward = new TreeMap<Integer,Integer> ();

		// fill border diamonds
		if (m_bounded) {
			for (int i_x = 1; i_x < m_grid_width; i_x ++) {
				m_border_diamonds.add (getLeftDiamondOfPlace (i_x, 0));
				m_border_diamonds.add (getLeftDiamondOfPlace (i_x, m_grid_height - 1));
			}

			for (int i_y = 1; i_y < m_grid_height; i_y ++) {
				m_border_diamonds.add (getTopDiamondOfPlace (0, i_y));
				m_border_diamonds.add (getTopDiamondOfPlace (m_grid_width - 1, i_y));
			}
		}

		// fill center diamonds with remaining diamonds
		for (int i_diamond = 0; i_diamond < m_center_diamonds_count + m_border_diamonds_count; i_diamond ++) {
			if (! m_border_diamonds.contains (i_diamond)) {
				m_center_diamonds.add (i_diamond);
			}
		}

		// map diamonds
		for (int i_diamond = 0; i_diamond < m_border_diamonds_count; i_diamond ++) {
			m_border_diamonds_map_backward.put (i_diamond, m_border_diamonds.get (i_diamond));
			m_border_diamonds_map_forward.put (m_border_diamonds.get (i_diamond), i_diamond);
		}

		for (int i_diamond = 0; i_diamond < m_center_diamonds_count; i_diamond ++) {
			m_center_diamonds_map_backward.put (i_diamond, m_center_diamonds.get (i_diamond));
			m_center_diamonds_map_forward.put (m_center_diamonds.get (i_diamond), i_diamond);
		}

		m_sat_start_border_diamonds = m_grid_width * m_grid_width * m_grid_height * m_grid_height + 1;
		m_sat_start_center_diamonds = m_sat_start_border_diamonds + m_border_diamonds_count * m_border_colors_count;
	}
}
