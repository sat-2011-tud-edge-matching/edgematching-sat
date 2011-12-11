package edgematching;

import edgematching.*;
import edgematching.problem.*;
import edgematching.cnf.*;

import java.util.*;

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

	protected enum m_enum_solvers {
		minisat, picosat, minisat_contrasat, cryptominisat
	};

	protected static m_enum_solvers m_solver;
	
	protected enum m_enum_encodings {
		simple, redundant, order, order_redundant
	};

	protected static m_enum_encodings m_encoding;

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

		SATSolvable sat_problem;
		Problem     problem_encoding;

		switch (m_encoding) {
			case simple:
				ProblemEncodingSimple temp_problem_simple = new ProblemEncodingSimple (m_problem);
				sat_problem      = temp_problem_simple;
				problem_encoding = temp_problem_simple;
				break;
			case redundant:
				ProblemEncodingSimpleRedundant temp_problem_simple_redundant = new ProblemEncodingSimpleRedundant (m_problem);
				sat_problem      = temp_problem_simple_redundant;
				problem_encoding = temp_problem_simple_redundant;
				break;
			case order:
				ProblemEncodingOrder temp_problem_order = new ProblemEncodingOrder (m_problem);
				sat_problem      = temp_problem_order;
				problem_encoding = temp_problem_order;
				break;
			case order_redundant:
			default:
				ProblemEncodingOrderRedundant temp_problem_order_redundant = new ProblemEncodingOrderRedundant (m_problem);
				sat_problem      = temp_problem_order_redundant;
				problem_encoding = temp_problem_order_redundant;
				break;
		};

		SATSolver solver;

		switch (m_solver) {
			case picosat:
				solver = new PicoSAT ("../solvers/picosat");
				break;
			case cryptominisat:
				solver = new PicoSAT ("../solvers/cryptominisat");
				break;
			case minisat_contrasat:
				solver = new MiniSAT ("../solvers/minisat-hack-contrasat", "../files");
				break;
			case minisat:
			default:
				solver = new MiniSAT ("minisat", "../files");
				break;
		};

		solver.solveSAT (sat_problem);

		problem_encoding.printSolution ();
	}

	/*
	 * help message
	 */
	protected static void printHelp ()
	{
		System.out.println ("Arguments:");
		System.out.println ("[-s solver] [-c encoding] input-file");
		System.out.println ("for help: -h");
		System.out.println ("");
		System.out.println ("with solver being one of \"minisat\", \"minisat-contrasat\", \"cryptominisat\" and \"picosat\",");
		System.out.println (" encoding being one of \"simple\", \"redundant\", \"order\" and \"order-redundant\".");
	}

	/*
	 * parse arguments of the program...
	 * until now just read filename of input-file
	 * print an error otherwise
	 */
	protected static void parseArguments (String[] args)
	{
		ArrayList <String> arguments = new ArrayList <String> ();

		for (String i_string : args) {
			arguments.add (i_string);
		}

		// defaultts
		m_solver   = m_enum_solvers.minisat;
		m_encoding = m_enum_encodings.order_redundant;
		m_filename = null;

		for (Iterator<String> i_string = arguments.iterator (); i_string.hasNext (); ) {
			String current_string = i_string.next ();

			if (current_string.equals ("-s")) {
				// solver
				if (i_string.hasNext ()) {
					String solver_string = i_string.next ();
					if (solver_string.equals ("minisat")) {
						m_solver = m_enum_solvers.minisat;
					} else if (solver_string.equals ("minisat-contrasat")) {
						m_solver = m_enum_solvers.minisat_contrasat;
					} else if (solver_string.equals ("cryptominisat")) {
						m_solver = m_enum_solvers.cryptominisat;
					} else if (solver_string.equals ("picosat")) {
						m_solver = m_enum_solvers.picosat;
					} else {
						printHelp ();
						System.exit (1);
					}
				} else {
					printHelp ();
					System.exit (1);
				}
			} else if (current_string.equals ("-c")) {
				// encoding
				if (i_string.hasNext ()) {
					String encoding_string = i_string.next ();
					if (encoding_string.equals ("simple")) {
						m_encoding = m_enum_encodings.simple;
					} else if (encoding_string.equals ("redundant")) {
						m_encoding = m_enum_encodings.redundant;
					} else if (encoding_string.equals ("order")) {
						m_encoding = m_enum_encodings.order;
					} else if (encoding_string.equals ("order-redundant")) {
						m_encoding = m_enum_encodings.order_redundant;
					} else {
						printHelp ();
						System.exit (1);
					}
				} else {
					printHelp ();
					System.exit (1);
				}
			} else if (current_string.equals ("-h")) {
				printHelp ();
				System.exit (0);
			} else {
				m_filename = current_string;
			}
		}

		if (m_filename == null) {
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
