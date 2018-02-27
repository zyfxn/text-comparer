package xuning.compare;

import java.util.*;

class MatchedRangeList {

    private int primaryRangeSize;
    private int secondaryRangeSize;

    private final TreeSet<PairRange> ranges;
    private PairRange cachedPairRange;

    private class MatchedRangeComparator implements Comparator<PairRange> {

        @Override
        public int compare(PairRange o1, PairRange o2) {
            return o2.P().distanceTo(o1.P());
        }
    }

    MatchedRangeList() {
        ranges = new TreeSet<PairRange>(new MatchedRangeComparator());
    }

    public MatchedRangeList setPrimaryRangeSize(int primaryRangeSize) {
        this.primaryRangeSize = primaryRangeSize;
        return this;
    }

    public MatchedRangeList setSecondaryRangeSize(int secondaryRangeSize) {
        this.secondaryRangeSize = secondaryRangeSize;
        return this;
    }

    public void addRange(PairRange matchedRange) {
        ranges.add(matchedRange);
    }

    public void addLine(int primaryLine, int secondaryLine) {
        if(cachedPairRange == null) {
            cachedPairRange = new PairRange(primaryLine, secondaryLine);
        } else {
            boolean success = cachedPairRange.merge(primaryLine, secondaryLine);
            if(!success) {
                addRange(cachedPairRange);
                cachedPairRange = new PairRange(primaryLine, secondaryLine);
            }
        }
    }

    public void addLineFinished() {
        if(cachedPairRange == null) return;
        addRange(cachedPairRange);
        cachedPairRange = null;
    }

    public List<PairRange> getMatchedRangeList() {
        List<PairRange> res = new LinkedList();
        Iterator<PairRange> iterator = ranges.iterator();

        while(iterator.hasNext()) {
            res.add(iterator.next());
        }
        return res;
    }

    public List<PairRange> getDifferentRangeList() {
        List<PairRange> res = new LinkedList();
        if(ranges.size() == 0) {
            res.add(new PairRange(1, primaryRangeSize, 1, secondaryRangeSize));
        } else {
            int primaryLeftBoundary = 0;
            int secondaryLeftBoundary = 0;

            PairRange firstDifferentRange = ranges.first().between(primaryLeftBoundary, secondaryLeftBoundary);
            if(!firstDifferentRange.isEmpty()) res.add(firstDifferentRange);

            if(ranges.size() > 1) {
                Iterator<PairRange> iterator = ranges.iterator();
                PairRange lastRange = null;

                while(iterator.hasNext()) {
                    if(lastRange == null) {
                        lastRange = iterator.next();
                        continue;
                    }

                    PairRange current = iterator.next();
                    PairRange diff = lastRange.between(current);
                    if(!diff.isEmpty()) res.add(diff);

                    lastRange = current;
                }
            }

            int primaryRightBoundary = primaryRangeSize + 1;
            int secondaryRightBoundary = secondaryRangeSize + 1;

            PairRange lastDifferentRange = ranges.last().between(primaryRightBoundary, secondaryRightBoundary);
            if(!lastDifferentRange.isEmpty()) res.add(lastDifferentRange);
        }
        return res;
    }
}
