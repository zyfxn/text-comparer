import org.apache.log4j.PropertyConfigurator;
import org.junit.Test;
import xuning.compare.CompareResult;
import xuning.compare.Comparer;

import static org.junit.Assert.fail;

public class AppTest {

    @Test
    public void mainTest() {
        String[] primary = {
                "a","b","c","d","e","f","g"
        };
        String[] secondary = {
                "a","c","d","e","b","h","f","g"
        };

        CompareResult result = new Comparer().compare(primary, secondary);

        if(!(result.getAdd() == 1 && result.getMod() == 0 && result.getDel() == 2)) {
            fail("main test result should be add 1, mod 0, del 1.");
        }
    }

    @Test
    public void singleFileTest() {
        String[] primary = {
                "a","b","c","d"
        };

        CompareResult result = new Comparer().compare(primary, null);

        if(!(result.getAdd() == 4 && result.getMod() == 0 && result.getDel() == 0)) {
            fail("single file test result should be add 4, mod 0, del 0.");
        }

        result = new Comparer().compare(primary, new String[]{});

        if(!(result.getAdd() == 4 && result.getMod() == 0 && result.getDel() == 0)) {
            fail("single file test result should be add 4, mod 0, del 0.");
        }
    }

    @Test
    public void completelyDifferentFileTest() {
        String[] primary = {
                "a","b","c","d","e"
        };
        String[] secondary = {
                "1","2","3"
        };

        CompareResult result = new Comparer().compare(primary, secondary);

        if(!(result.getAdd() == 2 && result.getMod() == 3 && result.getDel() == 0)) {
            fail("completely different file test result should be add 2, mod 3, del 0.");
        }
    }

    @Test
    public void trimmedLengthThresholdExceededTest() {
        String[] primary = {
                "a","b","c","d","e"
        };
        String[] secondary = {
                "a","2","3", "e"
        };

        CompareResult result = new Comparer()
                .setTrimmedLengthThreshold(1)
                .compare(primary, secondary);

        if(!(result.getAdd() == 1 && result.getMod() == 2 && result.getDel() == 0)) {
            fail("completely different file test result should be add 2, mod 3, del 0.");
        }
    }
}