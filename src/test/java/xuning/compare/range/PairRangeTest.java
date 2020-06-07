package xuning.compare.range;

import org.junit.Test;
import xuning.compare.Difference;

import static org.junit.Assert.*;

public class PairRangeTest {

    @Test
    public void mergePairRange2to2and2to3_with_line3and4_shouldBe2to3and2to4() {
        PairRange range0 = new PairRange(2,2,2,3);

        boolean success = range0.merge(3,4);
        if(!success) {
            fail("pair range(2,2)(2,3) could merge single line3,4 to (2,3)(2,4)");
        }
        if(!range0.equals(2,3,2,4)) {
            fail("pair range(2,2)(2,3) could merge single line3,4 to (2,3)(2,4)");
        }
    }

    @Test
    public void mergePairRange2to2and2to3_with_line4and4_shouldChangeNothing() {
        PairRange range0 = new PairRange(2,2,2,3);

        boolean success = range0.merge(4,4);
        if(success) {
            fail("pair range(2,2)(2,3) could not merge single line4,4");
        }
        if(!range0.equals(2,2,2,3)) {
            fail("pair range(2,2)(2,3) could not merge single line4,4");
        }
    }

    @Test
    public void mergePairRange2to2and2to3_with_line3and3_shouldChangeNothing() {
        PairRange range0 = new PairRange(2,2,2,3);

        boolean success = range0.merge(3,3);
        if(success) {
            fail("pair range(2,2)(2,3) could not merge single line3,3");
        }
        if(!range0.equals(2,2,2,3)) {
            fail("pair range(2,2)(2,3) could not merge single line3,3");
        }
    }

    @Test
    public void mergePairRange2to2and2to3_with_line1and4_shouldChangeNothing() {
        PairRange range = new PairRange(2,2,2,3);

        boolean success = range.merge(1,4);
        if(success) {
            fail("pair range(2,2)(2,3) could not merge single line1,4");
        }
        if(!range.equals(2,2,2,3)) {
            fail("pair range(2,2)(2,3) could not merge single line1,4");
        }
    }

    @Test
    public void pairRangeBetween1to2and2to3_and_4to5and4to5_shouldBe3to3and4to3() {
        PairRange range = new PairRange(1,2,2,3)
                .between(new PairRange(4,5,4,5));

        if(!range.equals(3,3,4,3)) {
            fail("pair range between (1,2)(2,3) and (4,5)(4,5) should be (3,3)(4,3)");
        }
    }

    @Test
    public void pairRangeBetween1to2and2to3_and_0to0and4to4_shouldBeNull() {
        PairRange range = new PairRange(1,2,2,3)
                .between(new PairRange(0,0,4,4));

        if(range != null) {
            fail("pair range between (1,2)(2,3) and (0,0)(4,4) should be null");
        }
    }

    @Test
    public void pairRangeBetween1to2and2to3_and_3to3and3to3_shouldBeNull() {
        PairRange range = new PairRange(1,2,2,3)
                .between(new PairRange(3,3,3,3));

        if(range != null) {
            fail("pair range between (1,2)(2,3) and (3,3)(3,3) should be null");
        }
    }

    @Test
    public void pairRangeBetween1to2and2to3_and_singleline4and4_shouldBe3to3and4to3() {
        PairRange range = new PairRange(1,2,2,3)
                .between(4,4);

        if(!range.equals(3,3,4,3)) {
            fail("pair range between (1,2)(2,3) and 4,4 should be (3,3)(4,3)");
        }
    }

    @Test
    public void pairRangeBetween1to2and2to3_and_singleline3and3_shouldBeNull() {
        PairRange range = new PairRange(1,2,2,3)
                .between(3,3);

        if(range != null) {
            fail("pair range between (1,2)(2,3) and 3,3 should be null");
        }
    }

    @Test
    public void pairRangeBetween1to2and2to3_and_singleline0and4_shouldBeNull() {
        PairRange range = new PairRange(1,2,2,3)
                .between(0,4);

        if(range != null) {
            fail("pair range between (1,2)(2,3) and 0,4 should be null");
        }
    }

    @Test
    public void differentOfPairRange2to2and2to3_shouldBeMod1Del1() {
        PairRange range0 = new PairRange(2,2,2,3);

        Difference diff = range0.difference();
        if(!(diff.getAdd() == 0 && diff.getMod() == 1 && diff.getDel() == 1)) {
            fail("different of PairRange(2,2)(2,3) should be modify 1 line and delete 1 line.");
        }
    }

    @Test
    public void differentOfPairRange2to2and2to1_shouldBeAdd1() {
        PairRange range0 = new PairRange(2,2,2,1);

        Difference diff = range0.difference();
        if(!(diff.getAdd() == 1 && diff.getMod() == 0 && diff.getDel() == 0)) {
            fail("different of PairRange(2,2)(2,1) should be add 1 line.");
        }
    }
}
