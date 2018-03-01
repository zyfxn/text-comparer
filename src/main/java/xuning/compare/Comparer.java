package xuning.compare;

import java.util.*;

import org.apache.log4j.Logger;

/**
 * 
 * @author Xu Ning
 *
 */
public class Comparer {

	private static Logger LOG = Logger.getLogger(Comparer.class);

	private boolean DEBUG = false;
	private int trimmedLengthThreshold = 0;

	private MatchedRangeWorker matchedRangeWorker;

	public Comparer setDebug(boolean DEBUG) {
		this.DEBUG = DEBUG;
		return this;
	}

	public Comparer setTrimmedLengthThreshold(int trimmedLengthThreshold) {
		this.trimmedLengthThreshold = trimmedLengthThreshold;
		return this;
	}

	/**
	 * Compare two strings, which contain entire file content.
	 * 
	 * @param primaryFileContent
	 *            - primary file content
	 * @param secondaryFileContent
	 *            - secondary file content
	 * @return {@code CompareResult}
	 */
	public CompareResult compare(String[] primaryFileContent, String[] secondaryFileContent) {
		LinkedList<Line> pl = buildList(primaryFileContent);
		LinkedList<Line> sl = buildList(secondaryFileContent);

		matchedRangeWorker = new MatchedRangeWorker();

		if(secondaryFileContent != null) {
			matchedRangeWorker.setSecondaryRangeSize(secondaryFileContent.length);
		}
		if(primaryFileContent != null) {
			matchedRangeWorker.setPrimaryRangeSize(primaryFileContent.length);
		}

		// one of a file is empty
		if (pl == null || pl.size() == 0 || sl == null || sl.size() == 0) {
			CompareResult result = matchedRangeWorker.getDifferenceResult();
			if (DEBUG) {
				LOG.debug(String.format("diff: %s", result.toString()));
			}
			return result;
		}

		Iterator<Line> pi = pl.iterator();
		Iterator<Line> si = sl.iterator();
		// trim from head
		PairRange head = trim(pi, si);
		if(head != null) matchedRangeWorker.addRange(head);

		pi = pl.descendingIterator();
		si = sl.descendingIterator();
		// trim from tail
		PairRange tail = trim(pi, si);
		if(tail != null) matchedRangeWorker.addRange(tail);

		if (DEBUG) {
			LOG.debug("trim: " + (head != null ? head.toString() : "none") + ";"
					+ (tail != null ? tail.toString() : "none"));
		}

		if (isTrimmedLengthThresholdAvailableAndExceeded(pl, sl)) {

			if (DEBUG) {
				LOG.debug(String.format("length %d,%d exceed %d", pl.size(), sl.size(), trimmedLengthThreshold));
			}

			CompareResult res = matchedRangeWorker.getDifferenceResult()
					.setTrimmedLengthThresholdExceeded(true);
			return res;
		}

		List<PairRange> solvedResult = solv(pl, sl);

		for(PairRange matchedRange : solvedResult) {
			matchedRangeWorker.addRange(matchedRange);
		}

		CompareResult result = matchedRangeWorker.getDifferenceResult();

		if (DEBUG) {
			printDetails(result, primaryFileContent, secondaryFileContent);
		}

		return result;
	}

	private boolean isTrimmedLengthThresholdAvailableAndExceeded(LinkedList<Line> pl, LinkedList<Line> sl) {
		return trimmedLengthThreshold > 0
				&& pl.size() > trimmedLengthThreshold
				&& sl.size() > trimmedLengthThreshold;
	}

	private void printDetails(CompareResult result, String[] primaryFileContent, String[] secondaryFileContent) {
		if(result == null) return;

		LOG.debug(String.format("diff: %s", result.toString()));
		LOG.debug("details:");

		for (PairRange r : result.getDifferentRangeList()) {
			LOG.debug("");
			if(primaryFileContent != null) {
				for (int i = r.P().getLeft(); i <= r.P().getRight(); i++) {
					LOG.debug(String.format("+%d>%s", i, primaryFileContent[i - 1]
							.replaceAll("\t", "--->")));
				}
			}

			if(secondaryFileContent != null) {
				for (int i = r.S().getLeft(); i <= r.S().getRight(); i++) {
					LOG.debug(String.format("-%d>%s", i, secondaryFileContent[i - 1]
							.replaceAll("\t", "--->")));
				}
			}
        }
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
	private List<PairRange> solv(LinkedList<Line> p, LinkedList<Line> s) {
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
