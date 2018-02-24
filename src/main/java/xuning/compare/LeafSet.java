package xuning.compare;

import java.util.Comparator;
import java.util.LinkedList;
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

	public LinkedList<PairRange> output() {
		LinkedList<PairRange> res = new LinkedList<PairRange>();
		Node i = null;
		if (leafs.size() > 0)
			i = leafs.last();

		PairRange t = null;
		while (i != null) {
			if (t == null) {
				t = new PairRange(i.x, i.y);
			} else if (!t.merge(i.x, i.y)) {
				res.addFirst(t);
				t = new PairRange(i.x, i.y);
			}

			i = i.getFrom();
			if (i == null) {
				res.addFirst(t);
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
