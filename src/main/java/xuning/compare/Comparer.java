package xuning.compare;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 
 * @author Xu Ning
 *
 */
public class Comparer {

	public boolean DEBUG = false;

	public boolean sameRangesOutput = false;
	public int lengthThresholdAfterTrim = 0;

	private static Logger LOG = Logger.getLogger(Comparer.class);

	/**
	 * Compare two strings, which contain entire file content.
	 * 
	 * @param p
	 *            - primary file content
	 * @param s
	 *            - secondary file content
	 * @return {@code CompareResult}
	 */
	public CompareResult compare(String[] p, String[] s) {
		LinkedList<Line> pl = buildList(p);
		LinkedList<Line> sl = buildList(s);

		CompareResult res = new CompareResult();
		int pLeft = 1;
		int pRight = 0;
		int sLeft = 1;
		int sRight = 0;

		// get file size range
		if (pl != null) {
			pLeft = pl.getFirst().num;
			pRight = pl.getLast().num;
		}
		if (sl != null) {
			sLeft = sl.getFirst().num;
			sRight = sl.getLast().num;
		}

		res.size0 = pRight;
		res.size1 = sRight;

		if (DEBUG) {
			LOG.debug(String.format("size: %d-%d,%d-%d", pLeft, pRight, sLeft, sRight));
		}

		if (pl == null && sl != null) {
			res.del = sRight;
		} else if (pl != null && sl == null) {
			res.add = pRight;
		}

		// one of a file is empty
		if (pl == null || sl == null) {
			if (DEBUG) {
				LOG.debug(String.format("diff: %d,%d,%d", res.add, res.mod, res.del));
			}
			return res;
		}

		Iterator<Line> pi = pl.iterator();
		Iterator<Line> si = sl.iterator();
		// trim from head
		PairRange head = trim(pi, si);

		pi = pl.descendingIterator();
		si = sl.descendingIterator();
		// trim from tail
		PairRange tail = trim(pi, si);

		if (DEBUG) {
			LOG.debug("trim: " + (head != null ? head.toString() : "null") + ";"
					+ (tail != null ? tail.toString() : "null"));
		}

		// check the file length after trim whether exceeded the threshold
		if (lengthThresholdAfterTrim > 0
				&& (pl.size() > lengthThresholdAfterTrim || sl.size() > lengthThresholdAfterTrim)) {
			if (DEBUG) {
				LOG.debug(String.format("length %d,%d exceed %d", pl.size(), sl.size(), lengthThresholdAfterTrim));
			}

			res.trimedLengthExceeded = true;
			return res;
		}

		// begin the solution
		LinkedList<PairRange> same = solv(pl, sl);

		// trim result add to this result
		if (head != null)
			same.addFirst(head);
		if (tail != null)
			same.add(tail);

		String out = null;
		if (sameRangesOutput) {
			out = new String();
			boolean first = true;
			for (PairRange pair : same) {
				if (first) {
					first = false;
				} else {
					out += ";";
				}
				out += pair.toString();
			}

			if (DEBUG) {
				LOG.debug("same paired ranges: " + out);
			}
		}

		// build res.changedLines and res.sameLine
		res.sameLine = out;
		res.add(same.getFirst().different(pLeft - 1, sLeft - 1));
		res.add(same.getLast().different(pRight + 1, sRight + 1));

		res.changedLines = new LinkedList<PairRange>();

		PairRange tmp = same.getFirst().between(pLeft - 1, sLeft - 1);
		if (!tmp.isEmpty())
			res.changedLines.add(tmp);

		PairRange last = null;
		for (PairRange r : same) {
			if (last != null) {
				res.add(last.different(r));
				res.changedLines.add(last.between(r));
			}
			last = r;
		}

		tmp = same.getLast().between(pRight + 1, sRight + 1);
		if (!tmp.isEmpty())
			res.changedLines.add(tmp);

		if (DEBUG) {
			LOG.debug(String.format("diff: %d,%d,%d", res.add, res.mod, res.del));
			LOG.debug("");

			for (PairRange r : res.changedLines) {
				for (int i = r.P().getLeft(); i <= r.P().getRight(); i++) {
					LOG.debug(String.format("+%d>%s", i, p[i - 1].replaceAll("\t", "--->")));
				}
				for (int i = r.S().getLeft(); i <= r.S().getRight(); i++) {
					LOG.debug(String.format("-%d>%s", i, s[i - 1].replaceAll("\t", "--->")));
				}
				LOG.debug("");
			}
		}
		return res;
	}

	/**
	 * A string split into lines.
	 * 
	 * @param content
	 *            - file content
	 * @return line data {@code LinkedList}. the value is null if content is
	 *         null.
	 */
	private LinkedList<Line> buildList(String[] content) {
		if (content == null) {
			return null;
		}

		LinkedList<Line> data = new LinkedList<Line>();
		for (int i = 0; i < content.length; i++) {
			data.add(new Line(content[i], i + 1));
		}

		return data;
	}

	/**
	 * A line content data construct to a map for searching line numbers by
	 * content.
	 * 
	 * @param data
	 *            - line data list
	 * @return map. the value is null if data is null.
	 */
	private Map<String, LinkedList<Integer>> buildMap(LinkedList<Line> data) {
		if (data == null || data.size() == 0) {
			return null;
		}

		Map<String, LinkedList<Integer>> res = new HashMap<String, LinkedList<Integer>>();

		for (Line line : data) {
			LinkedList<Integer> list = res.get(line.content);
			if (list == null) {
				list = new LinkedList<Integer>();
				res.put(line.content, list);
			}

			list.add(line.num);
		}

		return res;
	}

	/**
	 * Trim same content of the head or tail in two files.
	 * 
	 * @param pi
	 *            - iterator of data list of primary file
	 * @param si
	 *            - iterator of data list of secondary file
	 * @return same ranges. the value is null if the head or tail is different
	 *         on start.
	 */
	private PairRange trim(Iterator<Line> pi, Iterator<Line> si) {
		PairRange res = null;
		while (pi.hasNext() && si.hasNext()) {
			Line pl = pi.next();
			Line sl = si.next();

			if (pl.content.equals(sl.content)) {
				if (res == null) {
					res = new PairRange(pl.num, sl.num);
				} else {
					res.merge(pl.num, sl.num);
				}

				pi.remove();
				si.remove();
			} else {
				break;
			}
		}
		return res;
	}

	/**
	 * Begin the solution.
	 * 
	 * @param p
	 *            - primary file data
	 * @param s
	 *            - secondary file data
	 * @return the longest same paired ranges.
	 */
	private LinkedList<PairRange> solv(LinkedList<Line> p, LinkedList<Line> s) {
		if (p == null || s == null)
			return null;

		Map<String, LinkedList<Integer>> smap = buildMap(s);
		int same = 0;
		for (Line x : p) {
			if (smap.get(x.content) != null)
				same++;
		}

		LeafSet set = new LeafSet(same);

		for (Line x : p) {
			LinkedList<Integer> yl = smap.get(x.content);
			if (yl == null)
				continue;

			for (int y : yl) {
				if (set.add(x.num, y)) {
					break;
				}
			}
			set.nextLine();
		}

		return set.output();
	}

	/**
	 * The class {@code Line} represents a line in a text file.
	 * 
	 * @author Xu Ning
	 *
	 */
	private class Line {

		public final String content;
		public final int num;

		public Line(String content, int num) {
			this.content = content;
			this.num = num;
		}
	}
}
