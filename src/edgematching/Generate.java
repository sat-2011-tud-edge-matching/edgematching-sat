import edgematching.problem.*;

import java.util.*;

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
			System.out.println ("Incorrect specification of problem to generate!");
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

				System.out.println (currentOutputLine);
			}
		}
	}
}
