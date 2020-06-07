package xuning.compare;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xuning.compare.nakatsu.Solver;
import xuning.compare.range.PairRange;

/**
 * 
 * @author Xu Ning
 *
 */
public class Comparer {

	private static Logger LOG = LogManager.getLogger();

	private boolean DEBUG = false;
	private int trimmedLengthThreshold = 0;

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
	public CompareResult compare(List<String> primaryFileContent, List<String> secondaryFileContent) {
		ArrayList<String> arrayList;
		if (primaryFileContent instanceof ArrayList) {
			arrayList = (ArrayList)primaryFileContent;
		} else {
			arrayList = new ArrayList<>();
			if (primaryFileContent != null) {
				for (String content : primaryFileContent) {
					arrayList.add(content);
				}
			}
		}

		List<PairRange> res = new Solver().run(arrayList, secondaryFileContent);

		PostProcessor postProcessor = new PostProcessor();
		postProcessor.setPrimaryRangeSize(primaryFileContent != null ? primaryFileContent.size() : 0);
		postProcessor.setSecondaryRangeSize(secondaryFileContent != null ? secondaryFileContent.size() : 0);

		for(PairRange matchedRange : res) {
			postProcessor.addRange(matchedRange);
		}

		CompareResult result = postProcessor.getResult();

		if (DEBUG) {
			printDetails(result, primaryFileContent, secondaryFileContent);
		}

		return result;
	}

	public CompareResult compareOld(List<String> primaryFileContent, List<String> secondaryFileContent) {
		LinkedList<Line> pl = buildList(primaryFileContent);
		LinkedList<Line> sl = buildList(secondaryFileContent);

		PostProcessor postProcessor = new PostProcessor();
		postProcessor.setPrimaryRangeSize(primaryFileContent != null ? primaryFileContent.size() : 0);
		postProcessor.setSecondaryRangeSize(secondaryFileContent != null ? secondaryFileContent.size() : 0);

		// one of a file is empty
		if (pl == null || pl.size() == 0 || sl == null || sl.size() == 0) {
			CompareResult result = postProcessor.getResult();
			if (DEBUG) {
				LOG.debug(String.format("diff: %s", result.toString()));
			}
			return result;
		}

		Iterator<Line> pi = pl.iterator();
		Iterator<Line> si = sl.iterator();
		// trim from head
		PairRange head = trim(pi, si);
		if(head != null) postProcessor.addRange(head);

		pi = pl.descendingIterator();
		si = sl.descendingIterator();
		// trim from tail
		PairRange tail = trim(pi, si);
		if(tail != null) postProcessor.addRange(tail);

		if (DEBUG) {
			LOG.debug("trim: " + (head != null ? head.toString() : "none") + ";"
					+ (tail != null ? tail.toString() : "none"));
		}

		if (isTrimmedLengthThresholdAvailableAndExceeded(pl, sl)) {

			if (DEBUG) {
				LOG.debug(String.format("length %d,%d exceed %d", pl.size(), sl.size(), trimmedLengthThreshold));
			}

			return postProcessor.setTrimmedLengthThresholdExceeded(true)
					.getResult();
		}

		List<PairRange> solvedResult = solve(pl, sl);

		for(PairRange matchedRange : solvedResult) {
			postProcessor.addRange(matchedRange);
		}

		CompareResult result = postProcessor.getResult();

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

	private void printDetails(CompareResult result, List<String> primaryFileContent, List<String> secondaryFileContent) {
		if(result == null) return;

		LOG.debug(String.format("diff: %s", result.toString()));
		LOG.debug("details:");

		for (PairRange r : result.getDifferentRangeList()) {
			LOG.debug("");
			if(primaryFileContent != null) {
				for (int i = r.P().getLeft(); i <= r.P().getRight(); i++) {
					LOG.debug(String.format("+%d>%s", i, primaryFileContent.get(i - 1)
							.replaceAll("\t", "--->")));
				}
			}

			if(secondaryFileContent != null) {
				for (int i = r.S().getLeft(); i <= r.S().getRight(); i++) {
					LOG.debug(String.format("-%d>%s", i, secondaryFileContent.get(i - 1)
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
	private LinkedList<Line> buildList(List<String> content) {
		if (content == null) {
			return null;
		}

		LinkedList<Line> data = new LinkedList<Line>();
		int i = 1;
		for (String line : content) {
			data.add(new Line(line, i++));
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
	private List<PairRange> solve(LinkedList<Line> p, LinkedList<Line> s) {
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
