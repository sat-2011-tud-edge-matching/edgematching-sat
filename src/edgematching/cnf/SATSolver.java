package edgematching.cnf;

public interface SATSolver 
{
	void solveSAT (SATSolvable problem);
	boolean getSatisfiable ();
}
