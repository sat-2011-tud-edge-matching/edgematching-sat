package edgematching.cnf;

import java.util.*;
import java.lang.Math;

public class Clause
{
	protected ArrayList<Integer> m_literals;
	protected int m_hash_code;

	public Clause ()
	{
		m_literals = new ArrayList<Integer> ();
		m_hash_code = 0;
	}
	
	public Clause (int[] literals)
	{
		m_literals = new ArrayList<Integer> (literals.length);
		m_hash_code = 0;

		for (int i = 0; i < literals.length; i++) {
			m_literals.add (literals[i]);
			m_hash_code ^= literals[i];
		}
	}

	public boolean add (int literal)
	{
		if (literal != 0) {
			if (m_literals.add (literal)) {
				m_hash_code ^= literal;
				return true;
			}
		}

		return false;
	}

	public Set<Integer> getVariables ()
	{
		HashSet<Integer> result = new HashSet<Integer> ();

		for (Iterator<Integer> i_literal = m_literals.iterator (); i_literal.hasNext (); ) {
			result.add (Math.abs (i_literal.next ()));
		}

		return result;
	}

	public String toString ()
	{
		String result = new String ();

		for (Iterator<Integer> i_literal = m_literals.iterator (); i_literal.hasNext (); ) {
			result += i_literal.next ();
			if (i_literal.hasNext ()) result += " ";
		}
		
		return result;
	}

	public String toDIMACS ()
	{
		return toString () + " 0\n";
	}

	public int hashCode ()
	{
		return m_hash_code;
	}

	public boolean equals (Object o)
	{
		if (o == null) {
			if (this == null) return true;
			return false;
		}

		if (o instanceof Clause) return false;

		Clause other_clause = (Clause) o;

		if (other_clause.m_hash_code != this.m_hash_code) return false;
		if (other_clause.m_literals.size () != this.m_literals.size ()) return false;

		for (Iterator<Integer> i_literal = m_literals.iterator (); i_literal.hasNext (); ) {
			if (! other_clause.m_literals.contains (i_literal.next ())) return false;
		}

		return true;
	}
}
