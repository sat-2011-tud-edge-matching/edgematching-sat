package edgematching;

import edgematching.*;
import edgematching.problem.*;

/*
 * Main Class ...
 *
 * contains main-function (invoked at program start)
 * and all "global" variables (static ...)
 *
 * ... is never instantiated (--> static variables)
 *
 */
public class Main
{
	protected static String m_filename;
	protected static Problem m_problem;

	/*
	 * main function
	 *
	 *
	 */
	public static void main (String[] args) 
	{
		parseArguments (args);
		System.out.println ("Reading input file ...");
		readProblem ();
		m_problem.print ();
	}

	protected static void parseArguments (String[] args)
	{
		if (args.length > 0) {
			m_filename = args[0];
		} else {
			System.out.println ("No input file specified");
			System.exit (1);
		}
	}

	public static void readProblem ()
	{
		edgematching.problem.Reader temp_reader = new edgematching.problem.Reader (m_filename);

		m_problem = temp_reader.getProblem();

		if (m_problem == null) {
			System.out.println ("An error occured during parsing the input file!");
			System.exit (1);
		}

		if (! m_problem.specificationCorrect ()) {
			System.out.println ("Not a valid Edge Matching specification!");
			System.exit (1);
		}
	}

}
