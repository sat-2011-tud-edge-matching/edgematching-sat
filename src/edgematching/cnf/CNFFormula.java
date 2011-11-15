package edgematching.cnf;

import java.util.*;
import java.lang.Math;

public class CNFFormula
{
	protected HashSet<Clause> m_clauses;
	protected HashSet<Integer> m_variables;

	protected String m_comment;

	public CNFFormula ()
	{
		m_clauses   = new HashSet<Clause> ();
		m_variables = new HashSet<Integer> ();
	}

	public CNFFormula (String comment)
	{
		m_clauses   = new HashSet<Clause> ();
		m_variables = new HashSet<Integer> ();
		m_comment   = comment;
	}

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

	public boolean addClause (Clause clause)
	{
		if (clause == null) return false;

		m_clauses.add (clause);
		m_variables.addAll (clause.getVariables ());

		return true;
	}

	public int hashCode ()
	{
		return m_clauses.hashCode ();
	}

	private String commentToDIMACS ()
	{
		String result = new String ();

		Scanner m_line_scanner = new Scanner (m_comment);

		while (m_line_scanner.hasNextLine ()) {
			result += "c " + m_line_scanner.nextLine () + "\n";
		}

		return result;
	}

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
