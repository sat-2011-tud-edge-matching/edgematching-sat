package edgematching.cnf;

import java.io.*;
import java.util.*;
import java.lang.Math;

public class PicoSAT
	implements SATSolver
{
	protected String m_pico_sat_path;
	protected boolean m_satisfiable;

	public PicoSAT (String pathToBinary)
	{
		m_pico_sat_path = pathToBinary;
		m_satisfiable   = false;
	}

	public void solveSAT (SATSolvable problem)
	{
		m_satisfiable = false;

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

			pico_sat_process.waitFor ();

			result = true;
		} catch (IOException exception) {
		} catch (InterruptedException exception) {
		}


		return result;
	}




}
