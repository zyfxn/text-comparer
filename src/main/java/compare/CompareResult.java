package compare;

import java.util.LinkedList;

public class CompareResult extends Difference {

	/**
	 * Primary file size
	 */
	public int size0;
	/**
	 * Secondary file size
	 */
	public int size1;
	/**
	 * A list of same content paired ranges like this: a-b,c-d;e-f,g-h
	 */
	public String sameLine;
	public LinkedList<PairRange> changedLines;

	public boolean trimedLengthExceeded = false;
}
