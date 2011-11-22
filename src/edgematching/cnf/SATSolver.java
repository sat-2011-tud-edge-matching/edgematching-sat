package edgematching.cnf;

/*
 * interface for a sat-solver represented by a class
 */
public interface SATSolver 
{
	void solveSAT (SATSolvable problem);
	boolean getSatisfiable ();
}
