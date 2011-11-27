package edgematching;

import edgematching.problem.*;

import java.util.*;
import java.lang.*;

public class Generate {
	protected static boolean m_bounded;
	protected static boolean m_signed;

	protected static int m_width;
	protected static int m_height;

	protected static int m_colors;

	protected static ArrayList<Piece> m_grid;

	public static void main (String[] args) 
	{
		parseArguments (args);
		initGrid ();
		fillGrid ();

		System.err.println ("Solution:");
		printProblem ();

		randomizeGrid ();

		System.err.println ("Puzzle:");
		printProblem ();

		System.err.println ("Specification:");
		System.out.println (printOutputProblem ());
	}

	protected static String printOutputProblem ()
	{
		String result = (m_bounded ? "1 " : "0 ") + (m_signed ? "1 " : "0 ") + m_width + " " + m_height + "\n";

		for (Piece piece : m_grid) {
			result += piece.toProblemString () + '\n';
		}

		return result;
	}

	protected static void randomizeGrid ()
	{
		// rotate
		for (Piece piece : m_grid) {
			piece.setRotation (getRandomRotation ());
		}

		// randomize
		ArrayList<Piece> new_grid = new ArrayList<Piece> (m_width * m_height);

		while (m_grid.size () > 0) {
			int grid_size = m_grid.size ();
			int random_index = ((int) (Math.floor (Math.random () * grid_size)) % grid_size);

			Piece temp_piece = m_grid.remove (random_index);

			if (temp_piece != null) {
				new_grid.add (temp_piece);
			}
		}

		m_grid = new_grid;
	}

	protected static int getRandomRotation ()
	{
		int result = ((int) (Math.floor (Math.random () * 4)) % 4);
		return result;
	}

	protected static void fillGrid ()
	{
		for (int i_x = 0; i_x < m_width; i_x ++) {
			for (int i_y = 0; i_y < m_height; i_y ++) {
				int i_piece = i_x + i_y * m_width;
				Piece piece = m_grid.get (i_piece);

				// left color
				int leftColor;
				if (i_x == 0) {
					leftColor = (m_bounded ? 0 : getRandomColor ());
				} else {
					Piece leftPiece = m_grid.get (i_piece - 1);
					leftColor = leftPiece.getColor (1);
					leftColor = (m_signed ? -leftColor : leftColor);
				}

				// top color
				int topColor;
				if (i_y == 0) {
					topColor = (m_bounded ? 0 : getRandomColor ());
				} else {
					Piece topPiece = m_grid.get (i_piece - m_width);
					topColor = topPiece.getColor (2);
					topColor = (m_signed ? -topColor : topColor);
				}

				// right color
				int rightColor;
				if (i_x == m_width - 1) {
					rightColor = (m_bounded ? 0 : getRandomColor ());
				} else {
					rightColor = getRandomColor ();
				}

				// bottom color
				int bottomColor;
				if (i_y == m_height - 1) {
					bottomColor = (m_bounded ? 0 : getRandomColor ());
				} else {
					bottomColor = getRandomColor ();
				}

				piece.setColor (0, topColor);
				piece.setColor (1, rightColor);
				piece.setColor (2, bottomColor);
				piece.setColor (3, leftColor);
			}
		}
	}

	protected static int getRandomColor ()
	{
		int result = ((int) (Math.floor (Math.random () * m_colors)) % m_colors) + 1;

		if (m_signed) {
			if (Math.random () > 0.5) {
				result = -result;
			}
		}

		return result;
	}

	protected static void initGrid ()
	{
		m_grid = new ArrayList<Piece> (m_width * m_height);

		for (int i = 0; i < m_width * m_height; i++) {
			m_grid.add (new Piece (-1, -1, -1, -1));
		}
	}

	protected static void parseArguments (String[] args)
	{
		int width  = -1;
		int height = -1;
		boolean bounded   = false;
		boolean is_signed = false;

		for (String arg : args) {
			if (arg.equals ("-b")) {
				bounded   = true;
				continue;
			}

			if (arg.equals ("-s")) {
				is_signed = true;
				continue;
			}

			try {
				int current_int = Integer.parseInt (arg);

				if (width == -1) {
					width = current_int;
				} else {
					height = current_int;
				}
			} catch (NumberFormatException ex) {
			}
		}

		if ((width <= 0) || (height <= 0)) {
			System.err.println ("Incorrect specification of problem to generate!");
			System.exit (1);
		}

		m_bounded = bounded;
		m_signed  = is_signed;
		m_width   = width;
		m_height  = height;
		m_colors  = 9;
	}

	protected static void printProblem ()
	{
		if (m_grid == null) {
			return;
		}

		for (int i_grid_line = 0; i_grid_line < m_height; i_grid_line ++) {
			for (int i_piece_line = 0; i_piece_line < Piece.getStringLineCount; i_piece_line ++) {
				String currentOutputLine = new String ();

				for (int i_column = 0; i_column < m_width; i_column ++) {
					currentOutputLine += m_grid.get (i_grid_line * m_width + i_column).getStringLine (i_piece_line);
				}

				System.err.println (currentOutputLine);
			}
		}
	}
}
