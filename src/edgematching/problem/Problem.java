package edgematching.problem;

import java.util.*;

/*
 * class for the edgematching-problem
 */
public class Problem
{
	/*
	 * bounded and signed?
	 */
	protected boolean m_bounded;
	protected boolean m_signed;

	/*
	 * width and height of problem
	 */
	protected int m_grid_width;
	protected int m_grid_height;
	
	/*
	 * pieces grouped by corner/border/center
	 */
	protected ArrayList<Piece> m_corner_pieces;
	protected ArrayList<Piece> m_border_pieces;
	protected ArrayList<Piece> m_center_pieces;

	/*
	 * grid containing all pieces after solving the puzzle
	 */
	protected ArrayList<Piece> m_solution_grid;

	/*
	 * amount of pieces in each group
	 */
	protected int m_corner_pieces_count;
	protected int m_border_pieces_count;
	protected int m_center_pieces_count;

	/*
	 * colors in border-/center-diamonds
	 */
	protected SortedSet<Integer> m_border_colors;
	protected SortedSet<Integer> m_center_colors;

	/*
	 * constructor with main properties --> calculation of other properties possible
	 */
	public Problem (boolean bounded, boolean signedProblem, int width, int height)
	{
		m_bounded = bounded;
		m_signed = signedProblem;

		m_grid_width = (width > 0 ? width : 0);
		m_grid_height = (height > 0 ? height : 0);

		if (m_bounded) {
			m_corner_pieces_count = 4;
			m_border_pieces_count = 2 * m_grid_width + 2 * m_grid_height - 8;
			m_center_pieces_count = (m_grid_width - 2) * (m_grid_height - 2);
		} else {
			m_corner_pieces_count = 0;
			m_border_pieces_count = 0;
			m_center_pieces_count = m_grid_width * m_grid_height;
		}

		m_corner_pieces = new ArrayList<Piece> (m_corner_pieces_count);
		m_border_pieces = new ArrayList<Piece> (m_border_pieces_count);
		m_center_pieces = new ArrayList<Piece> (m_center_pieces_count);

		m_solution_grid = null;

		m_border_colors = new TreeSet<Integer> ();
		m_center_colors = new TreeSet<Integer> ();
	}

	protected Problem (Problem problem)
	{
		m_bounded = problem.m_bounded;
		m_signed  = problem.m_signed;

		m_grid_width = problem.m_grid_width;
		m_grid_height = problem.m_grid_height;
	
		m_corner_pieces = problem.m_corner_pieces;
		m_border_pieces = problem.m_border_pieces;
		m_center_pieces = problem.m_center_pieces;

		m_corner_pieces_count = problem.m_corner_pieces_count;
		m_border_pieces_count = problem.m_border_pieces_count;
		m_center_pieces_count = problem.m_center_pieces_count;

		m_border_colors = problem.m_border_colors;
		m_center_colors = problem.m_center_colors;
	}

	/*
	 * checks for correct amount of pieces in each set
	 */
	public boolean specificationCorrect ()
	{
		if (m_signed) return false;

		if (m_corner_pieces.size() != m_corner_pieces_count) return false;
		if (m_border_pieces.size() != m_border_pieces_count) return false;
		if (m_center_pieces.size() != m_center_pieces_count) return false;

		return true;
	}

	/*
	 * add a piece to the problem --> put it to the correct set
	 */
	public boolean addPiece (Piece piece)
	{
		if (piece == null) return false;

		if (m_bounded) {
			switch (piece.getAmountOfColor (0)) {
				case 0:
					piece.insertAllColors (m_center_colors);
					return m_center_pieces.add (piece);
				case 1:
					piece.insertBorderColors (m_border_colors);
					return m_border_pieces.add (piece);
				case 2:
					piece.insertBorderColors (m_border_colors);
					return m_corner_pieces.add (piece);
				default:
					return false;
			}
		} else {
			return m_center_pieces.add (piece);
		}
	}

	/*
	 * print problem (header and pieces)
	 */
	public void printProblem ()
	{
		System.out.println ("Edge-Matching Problem:");
		System.out.println ("Bounded: " + (m_bounded ? "yes" : "no"));
		System.out.println ("Signed:  " + (m_signed  ? "yes" : "no"));
		System.out.println ("Width:   " + m_grid_width);
		System.out.println ("Height:  " + m_grid_height);
		System.out.println ("Pieces: ");

		for (Piece i_piece : m_corner_pieces) {
			System.out.println (i_piece);
		}

		for (Piece i_piece : m_border_pieces) {
			System.out.println (i_piece);
		}

		for (Piece i_piece : m_center_pieces) {
			System.out.println (i_piece);
		}
	}

	/*
	 * print solution of the problem
	 */
	public void printSolution ()
	{
		if (m_solution_grid == null) {
			System.out.println ("Problem is not yet solved...");
			return;
		}

		for (int i_grid_line = 0; i_grid_line < m_grid_height; i_grid_line ++) {
			for (int i_piece_line = 0; i_piece_line < Piece.getStringLineCount; i_piece_line ++) {
				String currentOutputLine = new String ();

				for (int i_column = 0; i_column < m_grid_width; i_column ++) {
					currentOutputLine += m_solution_grid.get (i_grid_line * m_grid_width + i_column).getStringLine (i_piece_line);
				}

				System.out.println (currentOutputLine);
			}
		}
	}
}
