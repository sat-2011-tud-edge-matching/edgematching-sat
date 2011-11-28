package edgematching.cnf;

import java.io.*;
import java.util.*;
import java.lang.Math;

public class MiniSAT
	implements SATSolver
{
	protected String m_mini_sat_path;
	protected boolean m_satisfiable;

	protected String m_cnf_file;
	protected String m_sol_file;

	public MiniSAT (String pathToBinary, String pathForFiles)
	{
		m_mini_sat_path = pathToBinary;
		m_satisfiable   = false;

		Integer file_prefix = (int) (Math.random () * 100000);

		m_cnf_file = pathForFiles + "/mini_sat_" + file_prefix + ".cnf";
		m_sol_file = pathForFiles + "/mini_sat_" + file_prefix + ".sol";
	}

	public void solveSAT (SATSolvable problem)
	{
		m_satisfiable = false;

		CNFFormula formula = problem.encodeToSAT ();

		System.err.println ("writing cnf file...");
		if (! writeInputFile (formula, m_cnf_file)) return;

		System.err.println ("solving...");
		if (! solveFormula ()) return;

		System.err.println ("reading solution file...");
		if (! readOutputFile (formula, m_sol_file)) return;

		System.err.println ("checking solution file...");
		if (! m_satisfiable) return;

		System.err.println ("decoding solution...");
		problem.decodeSolution (formula);
	}

	public boolean getSatisfiable ()
	{
		return m_satisfiable;
	}

	protected boolean solveFormula ()
	{
		boolean result = false;

		String[] command_array = {m_mini_sat_path, m_cnf_file, m_sol_file};

		try {
			Process mini_sat_process = Runtime.getRuntime ().exec (command_array);

			BufferedReader error_stream_reader  = new BufferedReader (new InputStreamReader(mini_sat_process.getErrorStream()));
			BufferedReader output_stream_reader = new BufferedReader (new InputStreamReader(mini_sat_process.getInputStream()));
			
			String current_output_line = output_stream_reader.readLine ();

			while (current_output_line != null) {
				System.err.println (current_output_line);
				current_output_line = output_stream_reader.readLine ();
			}

			String current_error_line  = error_stream_reader.readLine ();

			while (current_error_line != null) {
				System.err.println (current_error_line);
				current_error_line = error_stream_reader.readLine ();
			}

			error_stream_reader.close ();
			output_stream_reader.close ();

			mini_sat_process.waitFor ();

			result = true;
		} catch (IOException exception) {
		} catch (InterruptedException exception) {
		}


		return result;
	}

	protected boolean writeInputFile (CNFFormula formula, String filename)
	{
		boolean result = false;

		String toWrite = formula.toDIMACS ();

		try {
			BufferedWriter writer = new BufferedWriter (new FileWriter (filename));

			writer.write (toWrite, 0, toWrite.length ());

			writer.flush ();

			writer.close ();

			result = true;
		} catch (IOException exception) {
			System.err.println ("Error writing " + filename + "...");
			return false;
		}

		return result;
	}

	protected boolean readOutputFile (CNFFormula formula, String filename)
	{
		boolean result = false;

		try {
			BufferedReader reader = new BufferedReader (new FileReader (filename));

			String firstLine = reader.readLine ();

			if (!firstLine.equals ("SAT")) return true;

			String solution_line = reader.readLine ();
			ArrayList<Integer> solution_literals = new ArrayList<Integer> (formula.getAmountOfVariables ());
			Scanner literal_scanner = new Scanner (solution_line);

			while (literal_scanner.hasNextInt ()) {
				int current_literal = literal_scanner.nextInt ();
				if (current_literal != 0)
					solution_literals.add (current_literal);
				else
					break;
			}

			formula.setSolution (solution_literals);

			m_satisfiable = true;

			result = true;

			reader.close ();
		} catch (IOException exception) {
			System.err.println ("Error reading " + filename + "...");
			return false;
		}

		return result;
	}
}
