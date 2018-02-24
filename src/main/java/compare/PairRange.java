package compare;

/**
 * The class {@code PairRange} represents a pair of ranges, {@link SingleRange}
 * actually, with relationship. It has a primary range and a secondary range,
 * can use to represent relationship like this:
 * <p>
 * A line range (23,36) in file P is same with (19,32) in file S.
 * 
 * @author Xu Ning
 *
 */
public class PairRange {

	private final SingleRange p;
	private final SingleRange s;

	/**
	 * 
	 * @param p
	 *            - primary range
	 * @param s
	 *            - secondary range
	 */
	PairRange(SingleRange p, SingleRange s) {
		if (p == null)
			p = new SingleRange();
		if (s == null)
			s = new SingleRange();

		this.p = p;
		this.s = s;
	}

	/**
	 * Construct with a pair of one line range.
	 * 
	 * @param p
	 *            - primary line
	 * @param s
	 *            - secondary line
	 */
	public PairRange(int p, int s) {
		this.p = new SingleRange(p, p);
		this.s = new SingleRange(s, s);
	}

	/**
	 * Merge from another pair of ranges. Successful if only two ranges in
	 * argument is close to this corresponding ranges at same side.
	 * <p>
	 * <b>For example:</b><br>
	 * (2,2)(2,3) can merge (3,4)(4,5) to (2,4)(2,5). (3,4)(4,5) are on the
	 * right and close.<br>
	 * range overlay at one of it is also cause failed.
	 * 
	 * @param r
	 *            - a pair of range
	 * @return successful if true
	 */
	public boolean merge(PairRange r) {
		int dm = p.distanceTo(r.p);
		int dn = s.distanceTo(r.s);

		if ((dm == 1 && dn == 1) || (dm == -1 && dn == -1)) {
			p.merge(r.p);
			s.merge(r.s);
			return true;
		}
		return false;
	}

	/**
	 * Merge from another pair of one line ranges. Successful if only two ranges
	 * in argument is close to this corresponding ranges at same side.
	 * <p>
	 * <b>For example:</b><br>
	 * (2,2)(2,3) can merge 3,4 to (2,3)(2,4). 3,4 are on the right and
	 * close.<br>
	 * 
	 * @param pl
	 *            - line number of primary one line range
	 * @param sl
	 *            - line number of secondary one line range
	 * @return successful if true
	 */
	public boolean merge(int pl, int sl) {
		int dm = p.distanceTo(pl);
		int dn = s.distanceTo(sl);

		if ((dm == 1 && dn == 1) || (dm == -1 && dn == -1)) {
			p.extend(pl);
			s.extend(sl);
			return true;
		}
		return false;
	}

	/**
	 * {@code PairRange} between this and the argument.
	 * <p>
	 * <b>For example:</b><br>
	 * (2,2)(2,3) between (4,5)(4,5) is (3,3)(4,3). (4,3) is a empty range.<br>
	 * (2,2)(2,3) between (1,1)(4,5) is null.
	 * 
	 * @param r
	 *            - a pair of range
	 * @return the value is null if one of range in argument has overlay or two
	 *         ranges get different side to this corresponding ranges.
	 */
	public PairRange between(PairRange r) {
		int dp = p.distanceTo(r.p);
		int ds = s.distanceTo(r.s);

		if ((dp > 0 && ds > 0) || (dp < 0 && ds < 0)) {
			return new PairRange(p.between(r.p), s.between(r.s));
		}
		return null;
	}

	/**
	 * {@code PairRange} between this and a pair of boundary.
	 * <p>
	 * <b>For example:</b><br>
	 * (2,2)(2,3) between 4,4 is (3,3)(4,3). (4,3) is a empty range.<br>
	 * (2,2)(2,3) between 1,4 is null.
	 * 
	 * @param bp
	 *            - primary boundary
	 * @param bs
	 *            - secondary boundary
	 * @return the value is null if one of boundary in corresponding range or
	 *         two boundaries get different side to this corresponding ranges.
	 */
	public PairRange between(int bp, int bs) {
		int dp = p.distanceTo(bp);
		int ds = s.distanceTo(bs);

		if ((dp > 0 && ds > 0) || (dp < 0 && ds < 0)) {
			return new PairRange(p.between(bp), s.between(bs));
		}
		return null;
	}

	/**
	 * {@link Difference} in a {@code PairRange} between this and the argument.
	 * 
	 * @param r
	 *            - a pair of range
	 * @return the value is null if two range in the argument at different side
	 *         of this.
	 */
	public Difference different(PairRange r) {
		int dp = p.distanceTo(r.p);
		int ds = s.distanceTo(r.s);

		if ((dp > 0 && ds > 0) || (dp < 0 && ds < 0)) {
			return new Difference(Math.abs(dp) - 1, Math.abs(ds) - 1);
		}
		return null;
	}

	/**
	 * {@link Difference} in a {@code PairRange} between this and a pair of
	 * boundary.
	 * 
	 * @param bp
	 *            - primary boundary
	 * @param bs
	 *            - secondary boundary
	 * @return the value is null if two arguments at different side of this.
	 */
	public Difference different(int bp, int bs) {
		int dp = p.distanceTo(bp);
		int ds = s.distanceTo(bs);

		if ((dp > 0 && ds > 0) || (dp < 0 && ds < 0)) {
			return new Difference(Math.abs(dp) - 1, Math.abs(ds) - 1);
		}
		return null;
	}

	public boolean isEmpty() {
		if (p.isEmpty() && s.isEmpty())
			return true;
		return false;
	}

	/**
	 * Get primary range.
	 * 
	 * @return {@code SingleRange}
	 */
	public SingleRange P() {
		return p;
	}

	/**
	 * Get secondary range.
	 * 
	 * @return {@code SingleRange}
	 */
	public SingleRange S() {
		return s;
	}

	public String toString() {
		return String.format("%s,%s", p.toString(), s.toString());
	}
}
