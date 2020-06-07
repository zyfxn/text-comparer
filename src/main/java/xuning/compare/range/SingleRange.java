package xuning.compare.range;

/**
 * The class {@code SingleRange} represents a normal range on the number axis.
 * It defines the left boundary value must less than the right one, except a
 * empty range. And a empty range, like (1,0) or (5,4), must have the
 * feature:<br>
 * <blockquote>
 * <p>
 * {@code left = right + 1} </blockquote>
 * 
 * @author Xu Ning
 * 
 */
public class SingleRange {

	private int left = 0;
	private int right = -1;

	public SingleRange() {
	}

	public SingleRange(int left, int right) {
		setLeft(left);
		setRight(right);
	}

	/**
	 * Set left value. The right value is force to {@code left - 1} if
	 * {@code left > right}
	 * 
	 * @param left
	 *            - left boundary value
	 */
	public SingleRange setLeft(int left) {
		this.left = left;
		if (this.right < left)
			this.right = left - 1;

		return this;
	}

	/**
	 * Set right value. The left value is force to {@code right + 1} if
	 * {@code left > right}
	 * 
	 * @param right
	 *            - right boundary value
	 */
	public SingleRange setRight(int right) {
		this.right = right;
		if (right < this.left)
			this.left = right + 1;

		return this;
	}

	public int getLeft() {
		return left;
	}

	public int getRight() {
		return right;
	}

	/**
	 * Range length.
	 * <p>
	 * <b>For example:</b><br>
	 * (2,2) is 1.<br>
	 * (2,1) is 0.
	 * 
	 * @return the value more than 0 or equal to 0.
	 */
	public int length() {
		return right - left + 1;
	}

	/**
	 * Distance to another range.
	 * <p>
	 * <b>For example:</b><br>
	 * (2,3) to (5,9) is 2.<br>
	 * (2,3) to (0,1) is -1.<br>
	 * (2,3) to (1,5) is 0, overlay.<br>
	 * (2,3) to (3,5) is 0, overlay.
	 * <p>
	 * 
	 * @param r
	 *            - range
	 * @return the value is less than 0 if argument is on the left. the value is
	 *         more than 0 if argument is on the right. the value is 0 if
	 *         argument has overlay.
	 */
	public int distanceTo(SingleRange r) {
		if (r.right < left)
			return r.right - left;
		else if (right < r.left)
			return r.left - right;
		return 0;
	}

	/**
	 * Distance to a boundary.
	 * <p>
	 * <b>For example:</b><br>
	 * (2,3) to 5 is 2.<br>
	 * (2,3) to 1 is -1.<br>
	 * (2,3) to 2 is 0, in range.
	 * <p>
	 * 
	 * @param b
	 *            - boundary
	 * @return the value is less than 0 if argument is on the left. the value is
	 *         more than 0 if argument is on the right. the value is 0 if
	 *         argument has overlay.
	 */
	public int distanceTo(int b) {
		if (b < left)
			return b - left;
		else if (right < b)
			return b - right;
		return 0;
	}

	/**
	 * {@code SingleRange} between this one and another.
	 * <p>
	 * <b>For example:</b><br>
	 * (2,3) between (5,9) is (4,4).<br>
	 * (2,3) between (0,1) is (2,1), empty range.<br>
	 * (2,3) between (1,5) is null.<br>
	 * 
	 * @param r
	 *            - range
	 * @return range result. the value is null if argument has overlay.
	 */
	public SingleRange between(SingleRange r) {
		if (r.right < this.left) {
			return new SingleRange(r.right + 1, this.left - 1);
		} else if (this.right < r.left) {
			return new SingleRange(this.right + 1, r.left - 1);
		}
		return null;
	}

	/**
	 * {@code SingleRange} between this one and a boundary.
	 * <p>
	 * <b>For example:</b><br>
	 * (2,3) between 5 is (4,4).<br>
	 * (2,3) between 1 is (2,1), empty range.<br>
	 * (2,3) between 2 is null.<br>
	 * 
	 * @param b
	 *            - boundary
	 * @return range result. the value is null if boundary in the range.
	 */
	public SingleRange between(int b) {
		if (b < this.left) {
			return new SingleRange(b + 1, this.left - 1);
		} else if (this.right < b) {
			return new SingleRange(this.right + 1, b - 1);
		}
		return null;
	}

	/**
	 * Extend range to a line position. Nothing will happen if the line is in
	 * the range.
	 * <p>
	 * <b>For example:</b><br>
	 * (2,3) extend to 8 is (2,8).<br>
	 * (2,3) extend to 1 is (1,3).<br>
	 * 
	 * @param l
	 *            - line number
	 * @return successful if true
	 */
	public boolean extend(int l) {
		if (l < this.left) {
			setLeft(l);
			return true;
		} else if (this.right < l) {
			setRight(l);
			return true;
		}
		return false;
	}

	public boolean equals(int left, int right) {
		return this.left == left && this.right == right;
	}

	public boolean isEmpty() {
		return left > right;
	}

	public String toString() {
		return String.format("%d-%d", left, right);
	}
}
