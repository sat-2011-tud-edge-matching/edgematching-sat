import java.util.*;

public class Problem
{
	private Boolean m_bounded;
	private Boolean m_signed;

	private int m_grid_width;
	private int m_grid_height;
	
	private ArrayList<Piece> m_corner_pieces;
	private ArrayList<Piece> m_border_pieces;
	private ArrayList<Piece> m_center_pieces;

	public Problem (Boolean bounded, Boolean signedProblem, int width, int height)
	{
		m_bounded = bounded;
		m_signed = signedProblem;

		m_grid_width = (width > 0 ? width : 0);
		m_grid_height = (height > 0 ? height : 0);

		if (m_bounded) {
			m_corner_pieces = new ArrayList<Piece> (4);
			m_border_pieces = new ArrayList<Piece> (2 * m_grid_width + 2 * m_grid_height - 4);
			m_center_pieces = new ArrayList<Piece> ((m_grid_width - 2) * (m_grid_height - 2));
		} else {
			m_corner_pieces = new ArrayList<Piece> (0);
			m_border_pieces = new ArrayList<Piece> (0);
			m_center_pieces = new ArrayList<Piece> (m_grid_width * m_grid_height);
		}
	}
}
