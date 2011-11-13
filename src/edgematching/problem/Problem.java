package edgematching.problem;

import java.util.*;

public class Problem
{
	protected boolean m_bounded;
	protected boolean m_signed;

	protected int m_grid_width;
	protected int m_grid_height;
	
	protected ArrayList<Piece> m_corner_pieces;
	protected ArrayList<Piece> m_border_pieces;
	protected ArrayList<Piece> m_center_pieces;

	protected int m_corner_pieces_count;
	protected int m_border_pieces_count;
	protected int m_center_pieces_count;

	public Problem (boolean bounded, boolean signedProblem, int width, int height)
	{
		m_bounded = bounded;
		m_signed = signedProblem;

		m_grid_width = (width > 0 ? width : 0);
		m_grid_height = (height > 0 ? height : 0);

		if (m_bounded) {
			m_corner_pieces_count = 4;
			m_border_pieces_count = 2 * m_grid_width + 2 * m_grid_height - 4;
			m_center_pieces_count = (m_grid_width - 2) * (m_grid_height - 2);
		} else {
			m_corner_pieces_count = 0;
			m_border_pieces_count = 0;
			m_center_pieces_count = m_grid_width * m_grid_height;
		}

		m_corner_pieces = new ArrayList<Piece> (m_corner_pieces_count);
		m_border_pieces = new ArrayList<Piece> (m_border_pieces_count);
		m_center_pieces = new ArrayList<Piece> (m_center_pieces_count);
	}

	public boolean specificationCorrect ()
	{
		if (m_corner_pieces.size() != m_corner_pieces_count) return false;
		if (m_border_pieces.size() != m_border_pieces_count) return false;
		if (m_center_pieces.size() != m_center_pieces_count) return false;

		return true;
	}

	public boolean addPiece (Piece piece)
	{
		if (piece == null) return false;

		if (m_bounded) {
			switch (piece.getAmountOfColor (0)) {
				case 0:
					return m_center_pieces.add (piece);
				case 1:
					return m_border_pieces.add (piece);
				case 2:
					return m_corner_pieces.add (piece);
				default:
					return false;
			}
		} else {
			return m_center_pieces.add (piece);
		}
	}

	public void print ()
	{
		System.out.println ("Edge-Matching Problem:");
		System.out.println ("Bounded: " + (m_bounded ? "yes" : "no"));
		System.out.println ("Signed:  " + (m_signed  ? "yes" : "no"));
		System.out.println ("Width:   " + m_grid_width);
		System.out.println ("Height:  " + m_grid_height);
		System.out.println ("Pieces: ");

		for (Iterator<Piece> i_piece = m_corner_pieces.iterator (); i_piece.hasNext (); ) {
			System.out.println (i_piece.next ());
		}

		for (Iterator<Piece> i_piece = m_border_pieces.iterator (); i_piece.hasNext (); ) {
			System.out.println (i_piece.next ());
		}

		for (Iterator<Piece> i_piece = m_center_pieces.iterator (); i_piece.hasNext (); ) {
			System.out.println (i_piece.next ());
		}
	}
}
