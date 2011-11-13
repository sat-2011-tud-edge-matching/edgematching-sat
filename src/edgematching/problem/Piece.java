package edgematching.problem;

import java.util.*;

public class Piece
{
	private ArrayList<Integer> m_colors;

	private int m_rotation;

	public Piece () 
	{
		m_colors = new ArrayList<Integer> (4);
		m_rotation = 0;
	}

	public Piece (int color1, int color2, int color3, int color4)
	{
		m_colors = new ArrayList<Integer> (4);
		m_rotation = 0;

		m_colors.add (0, color1);
		m_colors.add (1, color2);
		m_colors.add (2, color3);
		m_colors.add (3, color4);
	}

	public void setRotation (int rotation)
	{
		m_rotation = rotation % 4;
	}

	public int getRotation ()
	{
		return m_rotation;
	}

	public int getColor (int index)
	{
		return m_colors.get((index + m_rotation) % 4);
	}

	public List<Integer> getColors ()
	{
		ArrayList<Integer> result = new ArrayList<Integer> (4);

		for (int i_index = 0; i_index < 4; i_index ++) {
			result.add(i_index, getColor(i_index));
		}

		return result;
	}

	public int getAmountOfColor (int color)
	{
		int result = 0;

		for (Iterator<Integer> i_color = m_colors.iterator() ; i_color.hasNext() ; ) {
			if (i_color.next() == color) result++;
		}

		return result;
	}

	public String toString ()
	{
		String result = new String();

		for (int i = 0; i < getStringLineCount; i++) {
			result += getStringLine (i);
		}

		return result;
	}

	public static final int getStringLineCount = 7;
	public String getStringLine (int index)
	{
		switch (index) {
			case 0: return "+-------+\n";
			case 1: return "|\\  " + getColor(0) + "  /|\n";
			case 2: return "|  \\ /  |\n";
			case 3: return "|" + getColor(3) + "  X  " + getColor(1) + "|\n";
			case 4: return "|  / \\  |\n";
			case 5: return "|/  " + getColor(2) + "  \\|\n";
			case 6: return "+-------+\n";
			default: return null;
		}
	}
}
