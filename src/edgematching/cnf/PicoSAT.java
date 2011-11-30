package edgematching.cnf;

import java.io.*;
import java.util.*;
import java.lang.Math;

public class PicoSAT
	implements SATSolver
{
	protected String m_pico_sat_path;
	protected boolean m_satisfiable;

	protected TreeSet<Integer> m_solution;

	public PicoSAT (String pathToBinary)
	{
		m_pico_sat_path = pathToBinary;
		m_satisfiable   = false;

		m_solution = new TreeSet<Integer> ();
	}

	public void solveSAT (SATSolvable problem)
	{
		m_satisfiable = false;
		m_solution.clear ();

		System.err.println ("encoding problem...");
		CNFFormula formula = problem.encodeToSAT ();

		System.err.println ("solving...");
		if (! solveFormula (formula)) return;

		System.err.println ("checking solution...");
		if (! m_satisfiable) return;

		System.err.println ("decoding solution...");
		problem.decodeSolution (formula);
	}

	public boolean getSatisfiable ()
	{
		return m_satisfiable;
	}

	protected boolean solveFormula (CNFFormula formula)
	{
		boolean result = false;

		String[] command_array = {m_pico_sat_path};

		try {
			Process pico_sat_process = Runtime.getRuntime ().exec (command_array);

			BufferedReader error_stream_reader  = new BufferedReader (new InputStreamReader (pico_sat_process.getErrorStream()));
			BufferedReader output_stream_reader = new BufferedReader (new InputStreamReader (pico_sat_process.getInputStream()));
			BufferedWriter input_stream_writer = new BufferedWriter (new OutputStreamWriter (pico_sat_process.getOutputStream()));
			
			input_stream_writer.write (formula.toDIMACS ());
			input_stream_writer.flush ();
			input_stream_writer.close ();

			String current_output_line = output_stream_reader.readLine ();

			while (current_output_line != null) {
				parseOutputLine (current_output_line);
				current_output_line = output_stream_reader.readLine ();
			}

			String current_error_line  = error_stream_reader.readLine ();

			while (current_error_line != null) {
				System.err.println (current_error_line);
				current_error_line = error_stream_reader.readLine ();
			}

			error_stream_reader.close ();
			output_stream_reader.close ();

			pico_sat_process.waitFor ();

			if (m_satisfiable) {
				formula.setSolution (m_solution);
			}

			result = true;
		} catch (IOException exception) {
		} catch (InterruptedException exception) {
		}


		return result;
	}

	protected boolean parseOutputLine (String line)
	{
		if (line.length () < 3) return false;

		switch (line.charAt (0)) {
			case 'c':
				return true;
			case 's':
				if (line.substring (2).equals ("SATISFIABLE")) {
					m_satisfiable = true;
				}
				return true;
			case 'v':
				line = line.substring (2);
				parseVariables (line);
				return true;
		}

		return false;
	}

	protected void parseVariables (String line)
	{
		Scanner literal_scanner = new Scanner (line);

		while (literal_scanner.hasNextInt ()) {
			int current_literal = literal_scanner.nextInt ();
			if (current_literal != 0) {
				m_solution.add (current_literal);
			} else {
				break;
			}
		}
	}
}
