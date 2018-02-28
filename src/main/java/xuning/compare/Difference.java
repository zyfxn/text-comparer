package xuning.compare;

/**
 * The class {@code Difference} represents a result of difference by
 * {@code add}, {@code mod}, {@code del}
 * 
 * @author Xu Ning
 *
 */
public class Difference {

	private int add = 0;
	private int mod = 0;
	private int del = 0;

	public Difference(int dp, int ds) {
		mod = dp > ds ? ds : dp;
		add = dp - mod;
		del = ds - mod;
	}

	public Difference() {
	}

	public int getAdd() {
		return add;
	}

	public int getMod() {
		return mod;
	}

	public int getDel() {
		return del;
	}

	public void add(Difference d) {
		add += d.add;
		mod += d.mod;
		del += d.del;
	}

	public String toString() {
		return String.format("%d,%d,%d", add, mod, del);
	}
}
