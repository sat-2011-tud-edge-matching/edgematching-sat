package edgematching.cnf;

/*
 * interface which has to be implemented to encode a Problem to SAT
 * and read back the solution
 */
public interface SATSolvable
{
	CNFFormula encodeToSAT ();
	void decodeSolution (CNFFormula cnfformula);
}
