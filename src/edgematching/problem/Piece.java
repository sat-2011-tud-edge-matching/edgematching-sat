package edgematching.problem;

import java.util.*;

/*
 * Class for a piece of the edgematching-puzzle
 */
public class Piece
{
	/*
	 * number of lines needed for printing the piece to the screen
	 */
	public static final int getStringLineCount = 7;

	/* 
	 * colors of the piece clockwise stored in this list
	 */
	protected ArrayList<Integer> m_colors;

	/* 
	 * rotation of piece (in {0,1,2,3}) in 90 degree steps clockwise
	 * used for the solution of our problem
	 */
	protected int m_rotation;

	protected int m_hash_code;

	/*
	 * constructor with all 4 colors given
	 * --> are inserted directly
	 */
	public Piece (int color1, int color2, int color3, int color4)
	{
		m_colors = new ArrayList<Integer> (4);
		m_rotation = 0;

		m_colors.add (0, color1);
		m_colors.add (1, color2);
		m_colors.add (2, color3);
		m_colors.add (3, color4);

		m_hash_code = color1 + color2 + color3 + color4;
	}

	/*
	 * getter and setter of rotation
	 */
	public void setRotation (int rotation)
	{
		m_rotation = rotation % 4;
	}

	public int getRotation ()
	{
		return m_rotation;
	}

	/*
	 * get color at position wrt current rotation
	 */
	public int getColor (int index)
	{
		return m_colors.get((index - m_rotation) % 4);
	}

	/*
	 * inserts colors of this piece into a given set of colors
	 */
	public void insertAllColors (Set<Integer> colorSet)
	{
		colorSet.addAll (m_colors);
	}

	/*
	 * inserts border-colors of this piece into a given set of colors
	 */
	public void insertBorderColors (Set<Integer> borderColorSet)
	{
		for (int i = 0; i < 4; i++) {
			if (m_colors.get (i) == 0) {
				int leftColor  = m_colors.get ((i+3)%4);
				int rightColor = m_colors.get ((i+1)%4);

				if (leftColor  != 0) borderColorSet.add (leftColor);
				if (rightColor != 0) borderColorSet.add (rightColor);
			}
		}
	}

	/*
	 * get amount of the given color ...
	 * needed for finding corner/border-pieces --> amount of 0-color
	 */
	public int getAmountOfColor (int color)
	{
		int result = 0;

		for (Integer i_color : m_colors) {
			if (i_color == color) result++;
		}

		return result;
	}

	/*
	 * converts piece to an output-String (line-by-line)
	 */
	public String toString ()
	{
		String result = new String ();

		for (int i = 0; i < getStringLineCount; i++) {
			result += getStringLine (i) + "\n";
		}

		return result;
	}

	/*
	 * delivers one line of output-String
	 * --> needed if more than 2 or more pieces are printed besides each other
	 *  --> first lines of each pieces are concatenated to one line, ...
	 */
	public String getStringLine (int index)
	{
		switch (index) {
			case 0: return "+-------+";
			case 1: return "|\\  " + getColor(0) + "  /|";
			case 2: return "|  \\ /  |";
			case 3: return "|" + getColor(3) + "  X  " + getColor(1) + "|";
			case 4: return "|  / \\  |";
			case 5: return "|/  " + getColor(2) + "  \\|";
			case 6: return "+-------+";
			default: return null;
		}
	}

	public int hashCode ()
	{
		return m_hash_code;
	}

	public boolean equals (Object o)
	{
		// if null passed, check for null
		if (o == null) {
			if (this == null) return true;
			return false;
		}

		// else check for type of other object
		if (!(o instanceof Piece)) return false;

		Piece other_piece = (Piece) o;

		for (int start_color = 0; start_color < 4; start_color ++) {
			for (int count = 0; count < 4; count ++) {
				if (m_colors.get (count) != other_piece.m_colors.get (count)) break;

				if (count == 3) return true;
			}
		}

		return false;
	}
}
