package xuning.compare.range;

import org.junit.Test;

import static org.junit.Assert.*;

public class SingleRangeTest {

    @Test
    public void setLeft6_setRight3_LeftShouldBe4() {
        SingleRange range = new SingleRange()
                .setLeft(6)
                .setRight(3);

        if(range.getLeft() != 4) {
            fail("left should be set to right+1 if the right is smaller than the left.");
        }
    }

    @Test
    public void setRight3_setLeft6_RightShouldBe5() {
        SingleRange range = new SingleRange()
                .setRight(3)
                .setLeft(6);

        if(range.getRight() != 5) {
            fail("right should be set to left-1 if the left is bigger than the right.");
        }
    }

    @Test
    public void distanceFrom_range2to3_to_range5to9_is_2() {
        SingleRange range0 = new SingleRange(2,3);
        SingleRange range1 = new SingleRange(5,9);

        if(range0.distanceTo(range1) != 2) {
            fail("distance from range(2,3) to range(5,9) should be 2.");
        }
    }

    @Test
    public void distanceFrom_range2to3_to_range0to1_is_minus1() {
        SingleRange range0 = new SingleRange(2,3);
        SingleRange range1 = new SingleRange(0,1);

        if(range0.distanceTo(range1) != -1) {
            fail("distance from range(2,3) to range(0,1) should be -1.");
        }
    }

    @Test
    public void distanceFrom_range2to3_and_range1to5_is_0() {
        SingleRange range0 = new SingleRange(2,3);
        SingleRange range1 = new SingleRange(1,5);

        if(range0.distanceTo(range1) != 0) {
            fail("range(2,3) should overlay range(1,5).");
        }

        if(range1.distanceTo(range0) != 0) {
            fail("range(1,5) should overlay range(2,3).");
        }
    }

    @Test
    public void distanceFrom_range2to3_and_range3to5_is_0() {
        SingleRange range0 = new SingleRange(2,3);
        SingleRange range1 = new SingleRange(3,5);

        if(range0.distanceTo(range1) != 0) {
            fail("range(2,3) should overlay range(1,5).");
        }

        if(range1.distanceTo(range0) != 0) {
            fail("range(3,5) should overlay range(2,3).");
        }
    }

    @Test
    public void distanceFrom_range2to3_to_5_is_2() {
        SingleRange range0 = new SingleRange(2,3);

        if(range0.distanceTo(5) != 2) {
            fail("distance from range(2,3) to 5 should be 2.");
        }
    }

    @Test
    public void distanceFrom_range2to3_to_1_is_minus1() {
        SingleRange range0 = new SingleRange(2,3);

        if(range0.distanceTo(1) != -1) {
            fail("distance from range(2,3) to 1 should be -1.");
        }
    }

    @Test
    public void distanceFrom_range2to3_to_2_is_0() {
        SingleRange range0 = new SingleRange(2,3);

        if(range0.distanceTo(1) != -1) {
            fail("2 should be in range(2,3).");
        }
    }

    @Test
    public void rangeBetween_range2to3_and_range5to9_is_range4to4() {
        SingleRange range = new SingleRange(2,3)
                .between(new SingleRange(5,9));

        if(!range.equals(4,4)) {
            fail("range between (2,3) and (5,9) should be (4,4).");
        }

        range = new SingleRange(5,9)
                .between(new SingleRange(2,3));
        if(!range.equals(4,4)) {
            fail("range between (5,9) and (2,3) should be (4,4).");
        }
    }

    @Test
    public void rangeBetween_range2to3_and_range0to1_is_range2to1() {
        SingleRange range = new SingleRange(2,3)
                .between(new SingleRange(0,1));

        if(!range.equals(2,1)) {
            fail("range between (2,3) and (0,1) should be (2,1).");
        }
        if(!range.isEmpty()) {
            fail("range between (2,3) and (0,1) should be empty.");
        }

        range = new SingleRange(0,1)
                .between(new SingleRange(2,3));
        if(!range.equals(2,1)) {
            fail("range between (0,1) and (2,3) is (4,4).");
        }
        if(!range.isEmpty()) {
            fail("range between (0,1) and (2,3) should be empty.");
        }
    }

    @Test
    public void rangeBetween_range2to3_and_range1to5_is_null() {
        SingleRange range = new SingleRange(2,3)
                .between(new SingleRange(1,5));

        if(range != null) {
            fail("range between (2,3) and (1,5) should be null.");
        }

        range = new SingleRange(1,5)
                .between(new SingleRange(2,3));
        if(range != null) {
            fail("range between (1,5) and (2,3) should be null.");
        }
    }

    @Test
    public void rangeBetween_range2to3_and_range3to5_is_null() {
        SingleRange range = new SingleRange(2,3)
                .between(new SingleRange(3,5));

        if(range != null) {
            fail("range between (2,3) and (3,5) should be null.");
        }

        range = new SingleRange(3,5)
                .between(new SingleRange(2,3));
        if(range != null) {
            fail("range between (3,5) and (2,3) should be null.");
        }
    }

    @Test
    public void rangeBetween_range2to3_and_5_is_range4to4() {
        SingleRange range = new SingleRange(2,3)
                .between(5);

        if(!range.equals(4,4)) {
            fail("range between (2,3) and 5 should be (4,4).");
        }
    }

    @Test
    public void rangeBetween_range2to3_and_1_is_range2to1() {
        SingleRange range = new SingleRange(2,3)
                .between(1);

        if(!range.equals(2,1)) {
            fail("range between (2,3) and 1 should be (2,1).");
        }
        if(!range.isEmpty()) {
            fail("range between (2,3) and 1 should be empty.");
        }
    }

    @Test
    public void rangeBetween_range2to3_and_2_is_null() {
        SingleRange range = new SingleRange(2,3)
                .between(2);

        if(range != null) {
            fail("range between (2,3) and 2 should be null.");
        }
    }

    @Test
    public void extendRange2to3_to_8_is_range2to8() {
        SingleRange range0 = new SingleRange(2,3);

        range0.extend(8);
        if(!range0.equals(2,8)) {
            fail("extend range (2,3) to 8 should be (2,8).");
        }
    }

    @Test
    public void extendRange2to3_to_1_is_range1to3() {
        SingleRange range = new SingleRange(2,3);

        range.extend(1);
        if(!range.equals(1,3)) {
            fail("extend range (2,3) to 1 should be (1,3).");
        }
    }

    @Test
    public void extendRange2to3_to_2_nothingHappen() {
        SingleRange range = new SingleRange(2,3);

        boolean success = range.extend(2);
        if(success)
        {
            fail("extend range (2,3) to 2 should change nothing.");
        }
        if(!range.equals(2,3)) {
            fail("extend range (2,3) to 2 should change nothing.");
        }
    }
}
