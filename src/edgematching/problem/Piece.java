package edgematching.problem;

import java.util.*;

public class Piece
{
	private ArrayList<Integer> m_colors;

	public Piece () 
	{
		m_colors = new ArrayList<Integer> (4);
	}

	public Piece (int color1, int color2, int color3, int color4)
	{
		m_colors = new ArrayList<Integer> (4);
		m_colors.add (0, color1);
		m_colors.add (1, color2);
		m_colors.add (2, color3);
		m_colors.add (3, color4);
	}

	public List<Integer> getColors ()
	{
		return m_colors;
	}

	public int getAmountOfColor (int color)
	{
		int result = 0;

		for (Iterator<Integer> i_color = m_colors.iterator() ; i_color.hasNext() ; ) {
			if (i_color.next() == color) result++;
		}

		return result;
	}
}
