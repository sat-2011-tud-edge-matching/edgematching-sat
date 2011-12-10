package edgematching;

import edgematching.*;
import edgematching.problem.*;
import edgematching.cnf.*;

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
	 * reads in the file containing pieces,
	 * transforms them to sat (not yet implemented)
	 * ...
	 *
	 */
	public static void main (String[] args) 
	{
		parseArguments (args);
		System.err.println ("Reading input file ...");
		readProblem ();
		m_problem.printProblem ();

		//ProblemEncodingSimple problemEncoding = new ProblemEncodingSimple (m_problem);
		//ProblemEncodingSimpleRedundant problemEncoding = new ProblemEncodingSimpleRedundant (m_problem);
		//ProblemEncodingOrder problemEncoding = new ProblemEncodingOrder (m_problem);
		ProblemEncodingOrderRedundant problemEncoding = new ProblemEncodingOrderRedundant (m_problem);

		SATSolver solver = new MiniSAT ("minisat", "../files");
		//SATSolver solver = new PicoSAT ("../solvers/picosat");

		solver.solveSAT (problemEncoding);

		problemEncoding.printSolution ();
	}

	/*
	 * parse arguments of the program...
	 * until now just read filename of input-file
	 * print an error otherwise
	 */
	protected static void parseArguments (String[] args)
	{
		if (args.length > 0) {
			m_filename = args[0];
		} else {
			System.err.println ("No input file specified");
			System.exit (1);
		}
	}

	/*
	 * read problem out of file to m_problem
	 * and check for valid problem
	 */
	public static void readProblem ()
	{
		edgematching.problem.Reader temp_reader = new edgematching.problem.Reader (m_filename);

		m_problem = temp_reader.getProblem();

		if (m_problem == null) {
			System.err.println ("An error occured during parsing the input file!");
			System.exit (1);
		}

		if (! m_problem.specificationCorrect ()) {
			System.err.println ("Not a valid Edge Matching specification!");
			System.exit (1);
		}
	}

}
