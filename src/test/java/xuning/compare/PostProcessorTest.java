package xuning.compare;

import org.junit.Test;
import xuning.compare.range.PairRange;

import java.util.List;

import static org.junit.Assert.fail;

public class PostProcessorTest {

    @Test
    public void getDifferentRangesTest() {
        PostProcessor ranges = new PostProcessor()
                .setPrimaryRangeSize(10)
                .setSecondaryRangeSize(11);

        ranges.addRange(new PairRange(1,3,1,3));
        ranges.addRange(new PairRange(4,6,5,7));
        ranges.addRange(new PairRange(9,9,9,9));

        List<PairRange> diffRangeList = ranges.getResult()
                .getDifferentRangeList();
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