package edgematching.problem;

import java.util.*;
import edgematching.cnf.*;

/*
 * extends ProblemEncodingSimple with redundant Clauses
 *
 * encoding from the paper "Solving edge-matching problems with satisfiability solvers"
 * by Marijn J. H. Heule
 */
public class ProblemEncodingSimpleRedundant extends ProblemEncodingSimple
{
	/*
	 * constructor cloning the original problem
	 */
	public ProblemEncodingSimpleRedundant (Problem problem)
	{
		super (problem);
	}

	/*
	 * function encoding our problem to sat
	 */
	public CNFFormula encodeToSAT ()
	{
		CNFFormula formula = super.encodeToSAT ();

		encodeForbiddenColorClauses (formula);
		encodeExplicitOneOnOneMapping (formula);

		return formula;
	}

	protected void encodeForbiddenColorClauses (CNFFormula formula)
	{

	}

	protected void encodeExplicitOneOnOneMapping (CNFFormula formula)
	{
		// center pieces

		for (ListIterator<Integer> i_first_piece = m_center_piece_numbers.listIterator (); i_first_piece.hasNext (); ) {
			int first_piece = i_first_piece.next ();

			for (ListIterator<Integer> i_second_piece = m_center_piece_numbers.listIterator (i_first_piece.nextIndex ()); i_second_piece.hasNext (); ) {
				int second_piece = i_second_piece.next ();

				for (Integer place : m_center_place_numbers) {
					int[] tempArray = { - convertXijToSATVariable (first_piece, place), -convertXijToSATVariable (second_piece, place)};

					formula.addClause (tempArray);
				}
			}
		}
	}
}
