package xuning.compare;

import xuning.compare.range.PairRange;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CompareResult extends Difference {

	private int primaryRangeSize = 0;
	private int secondaryRangeSize = 0;

	private Set<PairRange> matchedRangeSet;
	private List<PairRange> differentRangeList;

	private boolean trimmedLengthThresholdExceeded;

	public Set<PairRange> getMatchedRangeSet() {
		return matchedRangeSet;
	}

	public boolean isTrimmedLengthThresholdExceeded() {
		return trimmedLengthThresholdExceeded;
	}

	public List<PairRange> getDifferentRangeList() {
		return differentRangeList;
	}

	public int getPrimaryRangeSize() {
		return primaryRangeSize;
	}

	public int getSecondaryRangeSize() {
		return secondaryRangeSize;
	}

	public void setPrimaryRangeSize(int primaryRangeSize) {
		this.primaryRangeSize = primaryRangeSize;
	}

	public void setSecondaryRangeSize(int secondaryRangeSize) {
		this.secondaryRangeSize = secondaryRangeSize;
	}

	public void setMatchedRangeSet(TreeSet<PairRange> matchedRangeSet) {
		this.matchedRangeSet = matchedRangeSet;
	}

	public void setDifferentRangeList(List<PairRange> differentRangeList) {
		this.differentRangeList = differentRangeList;
	}

	public void setTrimmedLengthThresholdExceeded(boolean trimmedLengthThresholdExceeded) {
		this.trimmedLengthThresholdExceeded = trimmedLengthThresholdExceeded;
	}
}
