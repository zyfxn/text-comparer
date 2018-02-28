package xuning.compare;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class CompareResult extends Difference {

	protected int primaryRangeSize;
	protected int secondaryRangeSize;

	protected TreeSet<PairRange> matchedRangeSet;
	protected List<PairRange> differentRangeList;

	private boolean trimmedLengthThresholdExceeded;

	public Set<PairRange> getMatchedRangeSet() {
		return matchedRangeSet;
	}

	public boolean isTrimmedLengthThresholdExceeded() {
		return trimmedLengthThresholdExceeded;
	}

	public CompareResult setTrimmedLengthThresholdExceeded(boolean trimmedLengthThresholdExceeded) {
		this.trimmedLengthThresholdExceeded = trimmedLengthThresholdExceeded;
		return this;
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
}
