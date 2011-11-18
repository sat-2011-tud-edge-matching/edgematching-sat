package edgematching.problem;

import java.util.*;
import java.io.*;

/*
 * Class reading in a edgematching-file into an edgematching.problem.Problem object...
 */
public class Reader
{
	/*
	 * Filename of file and object containing the Problem
	 */
	protected String m_filename;
	protected Problem m_problem;

	/* 
	 * constructor setting the filename
	 */
	public Reader (String filename)
	{
		m_filename = filename;
	}

	/*
	 * return (and if necessary read) the problem
	 */
	public Problem getProblem ()
	{
		if (m_problem == null) {
			try {
				if (! parseFile()) m_problem = null;
			} catch (FileNotFoundException exception) {
				m_problem = null;
			}
		}

		return m_problem;
	}

	/*
	 * parses the input file line by line
	 * --> hands lines over to specialised functions
	 */
	protected boolean parseFile () throws FileNotFoundException
	{
		Scanner m_line_scanner = new Scanner (new File (m_filename));

		if (! m_line_scanner.hasNextLine ()) return false;
		
		String current_line = m_line_scanner.nextLine ();
		if (! parseFirstLine (current_line)) return false;

		while (m_line_scanner.hasNextLine ()) {
			parsePieceLine (m_line_scanner.nextLine ());
		}

		return true;
	}

	/*
	 * parses the headline containing problem properties
	 * sets up m_problem apropriately
	 */
	protected boolean parseFirstLine (String line) throws FileNotFoundException
	{
		Scanner temp_scanner = new Scanner (line);

		int width     = 0;
		int height    = 0;
		int bounded   = 0;
		int is_signed = 0;

		if (! temp_scanner.hasNextInt ()) return false;
		bounded = temp_scanner.nextInt();
		if (! temp_scanner.hasNextInt ()) return false;
		is_signed = temp_scanner.nextInt();
		if (! temp_scanner.hasNextInt ()) return false;
		width = temp_scanner.nextInt();
		if (! temp_scanner.hasNextInt ()) return false;
		height = temp_scanner.nextInt();

		m_problem = new Problem ((bounded != 0), (is_signed != 0), width, height);
		return true;
	}

	/*
	 * parses lines containing a piece
	 * adds specified piece to m_problem
	 */
	protected boolean parsePieceLine (String line) throws FileNotFoundException
	{
		Scanner temp_scanner = new Scanner (line);

		int c1, c2, c3, c4;

		if (! temp_scanner.hasNextInt ()) return false;
		c1 = temp_scanner.nextInt();
		if (! temp_scanner.hasNextInt ()) return false;
		c2 = temp_scanner.nextInt();
		if (! temp_scanner.hasNextInt ()) return false;
		c3 = temp_scanner.nextInt();
		if (! temp_scanner.hasNextInt ()) return false;
		c4 = temp_scanner.nextInt();
		
		Piece temp_piece = new Piece (c1, c2, c3, c4);
		return m_problem.addPiece (temp_piece);
	}
}
