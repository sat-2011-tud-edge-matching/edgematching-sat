package edgematching.cnf;

import java.util.*;
import java.lang.Math;

/*
 * Class containing a clause ...
 * used for CNFFormulas
 */
public class Clause
{
	/*
	 * m_literals contains the Literals of the Clause
	 * m_hash_code for getting a useful hash for comparison of 2 clauses
	 *  for equality
	 */
	protected ArrayList<Integer> m_literals;
	protected int m_hash_code;

	/*
	 * Empty constructor, initializes variables
	 */
	public Clause ()
	{
		m_literals = new ArrayList<Integer> ();
		m_hash_code = 0;
	}

	/*
	 * Constructor accepting an array of literals
	 * which are inserted;
	 * m_hash_code is modified appropriately
	 *
	 * preferred method for creating a clause with known literals
	 * --> more efficient
	 */
	public Clause (int[] literals)
	{
		m_literals = new ArrayList<Integer> (literals.length);
		m_hash_code = 0;

		for (int i = 0; i < literals.length; i++) {
			m_literals.add (literals[i]);
			m_hash_code ^= literals[i];
		}
	}

	/*
	 * Add one literal to the clause 
	 * and modify m_hash_code
	 */
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

	/*
	 * get all variables of the clause --> absolute value of literals
	 */
	public Set<Integer> getVariables ()
	{
		HashSet<Integer> result = new HashSet<Integer> ();

		for (Integer i_literal : m_literals) {
			result.add (Math.abs (i_literal));
		}

		return result;
	}

	/*
	 * convert clause to a String --> Literals separated by space
	 */
	public String toString ()
	{
		String result = new String ();

		for (Iterator<Integer> i_literal = m_literals.iterator (); i_literal.hasNext (); ) {
			result += i_literal.next ();
			if (i_literal.hasNext ()) result += " ";
		}
		
		return result;
	}

	/*
	 * convert clause as String according to DIMACS format
	 * --> literals separated by space, followed by '0' and newline
	 */
	public String toDIMACS ()
	{
		return toString () + " 0\n";
	}

	/*
	 * return our m_hash_code for comparison of 2 clauses
	 * --> has to be the same for 2 equal clauses,
	 *    has not to be different for different clauses (just a hash)
	 */
	public int hashCode ()
	{
		return m_hash_code;
	}

	/*
	 * check for equality with another Clause
	 */
	public boolean equals (Object o)
	{
		// if null passed, check for null
		if (o == null) {
			if (this == null) return true;
			return false;
		}

		// else check for type of other object
		if (o instanceof Clause) return false;

		Clause other_clause = (Clause) o;

		// first check hashcode and length
		if (other_clause.m_hash_code != this.m_hash_code) return false;
		if (other_clause.m_literals.size () != this.m_literals.size ()) return false;

		// if all the same until now, check for different literals
		for (Integer i_literal : m_literals) {
			if (! other_clause.m_literals.contains (i_literal)) return false;
		}

		// if this part is reached, they are equal (semantically)
		return true;
	}
}
