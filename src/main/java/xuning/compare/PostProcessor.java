package xuning.compare;

import xuning.compare.range.PairRange;
import xuning.compare.range.PairRangeComparator;

import java.util.*;

class PostProcessor {

    private TreeSet<PairRange> matchedRangeSet = new TreeSet<>(new PairRangeComparator());
    private int primaryRangeSize;
    private int secondaryRangeSize;
    private boolean trimmedLengthThresholdExceeded;
    private List<PairRange> differentRangeList = new LinkedList<>();

    public PostProcessor setPrimaryRangeSize(int primaryRangeSize) {
        this.primaryRangeSize = primaryRangeSize;
        return this;
    }

    public PostProcessor setSecondaryRangeSize(int secondaryRangeSize) {
        this.secondaryRangeSize = secondaryRangeSize;
        return this;
    }

    public PostProcessor setTrimmedLengthThresholdExceeded(boolean trimmedLengthThresholdExceeded) {
        this.trimmedLengthThresholdExceeded = trimmedLengthThresholdExceeded;
        return this;
    }

    public void addRange(PairRange matchedRange) {
        matchedRangeSet.add(matchedRange);
    }

    public CompareResult getResult() {
        CompareResult result = new CompareResult();
        result.setPrimaryRangeSize(primaryRangeSize);
        result.setSecondaryRangeSize(secondaryRangeSize);

        if(matchedRangeSet.size() == 0) {
            setDifferentData(new PairRange(1, primaryRangeSize, 1, secondaryRangeSize),
                    result);
        } else {
            makeDifferenceAtLeftBoundary(result);

            if(matchedRangeSet.size() > 1) {
                makeDifferenceInTheMiddle(result);
            }

            makeDifferenceAtRightBoundary(result);
        }

        result.setDifferentRangeList(differentRangeList);
        result.setMatchedRangeSet(matchedRangeSet);
        return result;
    }

    private void makeDifferenceInTheMiddle(CompareResult result) {
        Iterator<PairRange> iterator = matchedRangeSet.iterator();
        PairRange lastRange = null;

        while(iterator.hasNext()) {
            if(lastRange == null) {
                lastRange = iterator.next();
                continue;
            }

            PairRange current = iterator.next();
            PairRange diff = lastRange.between(current);
            setDifferentData(diff, result);

            lastRange = current;
        }
    }

    private void makeDifferenceAtLeftBoundary(CompareResult result) {
        int primaryLeftBoundary = 0;
        int secondaryLeftBoundary = 0;

        PairRange firstDifferentRange = matchedRangeSet.first().between(primaryLeftBoundary, secondaryLeftBoundary);
        setDifferentData(firstDifferentRange, result);
    }

    private void makeDifferenceAtRightBoundary(CompareResult result) {
        int primaryRightBoundary = primaryRangeSize + 1;
        int secondaryRightBoundary = secondaryRangeSize + 1;

        PairRange lastDifferentRange = matchedRangeSet.last().between(primaryRightBoundary, secondaryRightBoundary);
        setDifferentData(lastDifferentRange, result);
    }

    private void setDifferentData(PairRange diffRange, CompareResult result) {
        if(diffRange == null || diffRange.isEmpty()) return;

        differentRangeList.add(diffRange);
        result.add(new Difference(
                diffRange.P().length(),
                diffRange.S().length()));
    }
}
