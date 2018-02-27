package xuning.compare;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.fail;

public class MatchedRangeListTest {

    @Test
    public void addSingleLineData_shouldGet3PairRanges() {
        MatchedRangeList ranges = new MatchedRangeList();

        ranges.addLine(1,1);
        ranges.addLine(2,2);
        ranges.addLine(3,3);
        ranges.addLine(4,5);
        ranges.addLine(5,6);
        ranges.addLine(6,7);
        ranges.addLine(9,9);
        ranges.addLineFinished();

        List<PairRange> matchedRangeList = ranges.getMatchedRangeList();
        if(matchedRangeList.size() != 3) {
            fail("single line data test, result should have 3 paired range.");
        }

        PairRange tmp = matchedRangeList.get(0);
        if(!tmp.equals(1,3,1,3)) {
            fail("single line data test, first paired range should be (1,3)(1,3).");
        }

        tmp = matchedRangeList.get(1);
        if(!tmp.equals(4,6,5,7)) {
            fail("single line data test, first paired range should be (4,6)(5,7).");
        }

        tmp = matchedRangeList.get(2);
        if(!tmp.equals(9,9,9,9)) {
            fail("single line data test, first paired range should be (9,9)(9,9).");
        }
    }

    @Test
    public void getDifferentRangesTest() {
        MatchedRangeList ranges = new MatchedRangeList()
                .setPrimaryRangeSize(10)
                .setSecondaryRangeSize(11);

        ranges.addRange(new PairRange(1,3,1,3));
        ranges.addRange(new PairRange(4,6,5,7));
        ranges.addRange(new PairRange(9,9,9,9));

        List<PairRange> diffRangeList = ranges.getDifferentRangeList();
        if(diffRangeList.size() != 3) {
            fail("get different range list test, result should have 3 one.");
        }

        PairRange tmp = diffRangeList.get(0);
        if(!tmp.equals(4,3,4,4)) {
            fail("get different range list test, first paired range should be (4,3)(4,4).");
        }

        tmp = diffRangeList.get(1);
        if(!tmp.equals(7,8,8,8)) {
            fail("get different range list test, first paired range should be (7,8)(8,8).");
        }

        tmp = diffRangeList.get(2);
        if(!tmp.equals(10,10,10,11)) {
            fail("get different range list test, first paired range should be (10,10)(10,11).");
        }
    }
}