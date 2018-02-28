package xuning.compare;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

class LeafSet {

	private final TreeSet<Node> leafs;
	private final LinkedList<Node> branchs;
	private Node h;
	private Node l;
	private int same;
	private boolean trim;

	public LeafSet(int same) {
		leafs = new TreeSet<Node>(new LeafComparator());
		branchs = new LinkedList<Node>();
		this.same = same;
	}

	public boolean add(int x, int y) {
		if (l != null && l.x == x && h != null && h.y >= y) {
			return false;
		}

		Node n = new Node(x, y);

		h = leafs.ceiling(n);
		if (h != null && h.y == y)
			return false;
		l = leafs.lower(n);

		boolean bottom = false;
		if (l == null)
			bottom = true;

		if (!(trim && bottom)) {
			n.setFrom(l);
			leafs.add(n);
		}
		l = n;

		if (trim && bottom)
			return false;

		if (h == null) {
			if (trim)
				leafs.remove(leafs.first());
			return true;
		} else
			branchs.add(h);

		return false;
	}

	public void nextLine() {
		for (Node n : branchs) {
			leafs.remove(n);
		}
		branchs.clear();

		if (trim && leafs.size() > 1)
			leafs.remove(leafs.first());

		same--;
		if (!trim && leafs.size() >= same)
			trim = true;
	}

	public List<PairRange> output() {
		List<PairRange> res = new LinkedList<>();
		Node node = null;
		if (leafs.size() > 0)
			node = leafs.last();

		PairRange cacheRange = null;
		while (node != null) {
			if (cacheRange == null) {
				cacheRange = new PairRange(node.x, node.y);
			} else {
				boolean success = cacheRange.merge(node.x, node.y);
				if (!success) {
					res.add(cacheRange);
					cacheRange = new PairRange(node.x, node.y);
				}
			}
			node = node.getFrom();
			if (node == null) {
				res.add(cacheRange);
			}
		}
		return res;
	}

	private class LeafComparator implements Comparator<Node> {

		@Override
		public int compare(Node o1, Node o2) {
			// TODO Auto-generated method stub
			return o1.y - o2.y;
		}

	}

	/**
	 * The class {@code Node} represents a pair of line get same content from
	 * two files. The class has a link to the parent {@code Node}. All nodes
	 * construct a data tree.
	 * 
	 * @author Xu Ning
	 *
	 */
	private class Node {

		public final int x;
		public final int y;
		private Node from;

		Node(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public void setFrom(Node from) {
			this.from = from;
		}

		public Node getFrom() {
			return from;
		}
	}
}
