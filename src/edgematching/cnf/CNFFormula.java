package edgematching.cnf;

import java.util.*;
import java.lang.Math;

/*
 * Class for a CNF-formula
 * contains clauses as Clause-class
 */
public class CNFFormula
{
	/*
	 * m_clauses: clauses
	 * m_variables: set of variables in all clauses
	 * m_comment: comment for output as DIMACS-file
	 */
	protected HashSet<Clause> m_clauses;
	protected HashSet<Integer> m_variables;

	protected String m_comment;

	/*
	 * simple costructor
	 */
	public CNFFormula ()
	{
		m_clauses   = new HashSet<Clause> ();
		m_variables = new HashSet<Integer> ();
	}

	/*
	 * constructor with comment
	 */
	public CNFFormula (String comment)
	{
		m_clauses   = new HashSet<Clause> ();
		m_variables = new HashSet<Integer> ();
		m_comment   = comment;
	}

	/*
	 * add a Clause directly as array of literals
	 * --> more efficient, because m_variables is updated directly
	 */
	public boolean addClause (int[] literals)
	{
		if (literals == null) return false;

		m_clauses.add (new Clause (literals));

		for (int i = 0; i < literals.length; i++) {
			int literal = literals[i];

			if (literal != 0) {
				m_variables.add (Math.abs (literal));
			}
		}

		return true;
	}

	/*
	 * add a Clause as a Clause-object
	 * --> less efficient than above, because getVariables has to be calculated separately
	 */
	public boolean addClause (Clause clause)
	{
		if (clause == null) return false;

		m_clauses.add (clause);
		m_variables.addAll (clause.getVariables ());

		return true;
	}

	/*
	 * hashCode is same, if the clauses-list has the same hash
	 */
	public int hashCode ()
	{
		return m_clauses.hashCode ();
	}

	/*
	 * converts our m_comment String to DIMACS formaet
	 * --> begin each line with "c "
	 */
	private String commentToDIMACS ()
	{
		String result = new String ();

		Scanner m_line_scanner = new Scanner (m_comment);

		while (m_line_scanner.hasNextLine ()) {
			result += "c " + m_line_scanner.nextLine () + "\n";
		}

		return result;
	}

	/*
	 * converts this formula into DIMACS-format-String
	 * --> begin with comment and header and add each Clause in DIMACS-format
	 */
	public String toDIMACS ()
	{
		String result = new String ();

		if (m_comment != null) {
			result += commentToDIMACS ();
		} else {
			result += "c cnf-formula to be satisfied\n";
		}

		result += "p cnf " + m_variables.size () + " " + m_clauses.size () + "\n";

		for (Clause i_clause : m_clauses) {
			result += i_clause.toDIMACS ();
		}

		return result;
	}
}
