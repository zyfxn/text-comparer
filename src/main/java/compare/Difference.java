package compare;

/**
 * The class {@code Difference} represents a result of difference by
 * {@code add}, {@code mod}, {@code del}
 * 
 * @author Xu Ning
 *
 */
public class Difference {

	public int add = 0;
	public int mod = 0;
	public int del = 0;

	public Difference() {
	}

	public Difference(int dp, int ds) {
		mod = dp > ds ? ds : dp;
		add = dp - mod;
		del = ds - mod;
	}

	public void add(Difference d) {
		add += d.add;
		mod += d.mod;
		del += d.del;
	}

	public void reset() {
		mod = 0;
		add = 0;
		del = 0;
	}
}
