package xuning.compare;

import java.util.*;

class MatchedRangeWorker extends CompareResult {

    private class MatchedRangeComparator implements Comparator<PairRange> {

        @Override
        public int compare(PairRange o1, PairRange o2) {
            return o2.P().distanceTo(o1.P());
        }
    }

    MatchedRangeWorker() {
        matchedRangeSet = new TreeSet<>(new MatchedRangeComparator());
    }

    public MatchedRangeWorker setPrimaryRangeSize(int primaryRangeSize) {
        this.primaryRangeSize = primaryRangeSize;
        return this;
    }

    public MatchedRangeWorker setSecondaryRangeSize(int secondaryRangeSize) {
        this.secondaryRangeSize = secondaryRangeSize;
        return this;
    }

    public void addRange(PairRange matchedRange) {
        matchedRangeSet.add(matchedRange);
    }

    public CompareResult getDifferenceResult() {
        differentRangeList = new LinkedList<>();

        if(matchedRangeSet.size() == 0) {
            setDifferentData(new PairRange(1, primaryRangeSize, 1, secondaryRangeSize));
        } else {
            makeDifferenceAtLeftBoundary();

            if(matchedRangeSet.size() > 1) {
                makeDifferenceInTheMiddle();
            }

            makeDifferenceAtRightBoundary();
        }
        return this;
    }

    private void makeDifferenceInTheMiddle() {
        Iterator<PairRange> iterator = matchedRangeSet.iterator();
        PairRange lastRange = null;

        while(iterator.hasNext()) {
            if(lastRange == null) {
                lastRange = iterator.next();
                continue;
            }

            PairRange current = iterator.next();
            PairRange diff = lastRange.between(current);
            setDifferentData(diff);

            lastRange = current;
        }
    }

    private void makeDifferenceAtLeftBoundary() {
        int primaryLeftBoundary = 0;
        int secondaryLeftBoundary = 0;

        PairRange firstDifferentRange = matchedRangeSet.first().between(primaryLeftBoundary, secondaryLeftBoundary);
        setDifferentData(firstDifferentRange);
    }

    private void makeDifferenceAtRightBoundary() {
        int primaryRightBoundary = primaryRangeSize + 1;
        int secondaryRightBoundary = secondaryRangeSize + 1;

        PairRange lastDifferentRange = matchedRangeSet.last().between(primaryRightBoundary, secondaryRightBoundary);
        setDifferentData(lastDifferentRange);
    }

    private void setDifferentData(PairRange diffRange) {
        if(diffRange == null || diffRange.isEmpty()) return;

        differentRangeList.add(diffRange);
        add(new Difference(
                diffRange.P().length(),
                diffRange.S().length()));
    }
}
