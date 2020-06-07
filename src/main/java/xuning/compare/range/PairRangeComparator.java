package xuning.compare.range;

import java.util.Comparator;

public class PairRangeComparator implements Comparator<PairRange> {
    @Override
    public int compare(PairRange o1, PairRange o2) {
        return o2.P().distanceTo(o1.P());
    }
}
