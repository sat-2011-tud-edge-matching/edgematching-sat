package edgematching.cnf;

public class MiniSAT
	implements SATSolver
{
	protected String m_mini_sat_path;
	protected boolean m_satisfiable;

	public MiniSAT (String pathToBinary)
	{
		m_mini_sat_path = pathToBinary;
		m_satisfiable   = false;
	}

	public void solveSAT (SATSolvable problem)
	{
	}

	public boolean getSatisfiable ()
	{
		return m_satisfiable;
	}
}
