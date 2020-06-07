package xuning;

import org.junit.Test;
import xuning.compare.CompareResult;
import xuning.compare.Comparer;

import java.util.LinkedList;
import java.util.List;

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

        CompareResult result = new Comparer()
                .compare(getContentList(primary), getContentList(secondary));

        if(!(result.getAdd() == 1 && result.getMod() == 0 && result.getDel() == 2)) {
            fail("main test result should be add 1, mod 0, del 2.");
        }
    }

    @Test
    public void singleFileTest() {
        String[] primary = {
                "a","b","c","d"
        };

        CompareResult result = new Comparer()
                .compare(getContentList(primary), null);

        if(!(result.getAdd() == 4 && result.getMod() == 0 && result.getDel() == 0)) {
            fail("single file test result should be add 4, mod 0, del 0.");
        }

        result = new Comparer().compare(getContentList(primary), getContentList(null));

        if(!(result.getAdd() == 4 && result.getMod() == 0 && result.getDel() == 0)) {
            fail("single file test result should be add 4, mod 0, del 0.");
        }

        result = new Comparer().compare(null, getContentList(primary));

        if(!(result.getAdd() == 0 && result.getMod() == 0 && result.getDel() == 4)) {
            fail("single file test result should be add 0, mod 0, del 4.");
        }

        result = new Comparer().compare(getContentList(null), getContentList(primary));

        if(!(result.getAdd() == 0 && result.getMod() == 0 && result.getDel() == 4)) {
            fail("single file test result should be add 0, mod 0, del 4.");
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

        CompareResult result = new Comparer()
                .compare(getContentList(primary), getContentList(secondary));

        if(!(result.getAdd() == 2 && result.getMod() == 3 && result.getDel() == 0)) {
            fail("completely different file test result should be add 2, mod 3, del 0.");
        }
    }

    @Test
    public void mainTest2() {
        String[] primary = {
                "a","b","c","d","f","e"
        };
        String[] secondary = {
                "a","2","c","3","e"
        };

        CompareResult result = new Comparer().compare(getContentList(primary), getContentList(secondary));

        if(!(result.getAdd() == 1 && result.getMod() == 2 && result.getDel() == 0)) {
            fail("main test 2 result should be add 1, mod 2, del 0.");
        }
    }

    @Test
    public void mainTest3() {
        String[] primary = {
                "a","","c","","f","e"
        };
        String[] secondary = {
                "a","","c","d","f","e"
        };

        CompareResult result = new Comparer().compare(getContentList(primary), getContentList(secondary));

        if(!(result.getAdd() == 0 && result.getMod() == 1 && result.getDel() == 0)) {
            fail("main test 3 result should be add 0, mod 1, del 0.");
        }
    }

    @Test
    public void mainTest4() {
        String[] primary = {
                "a","","","f","e","h"
        };
        String[] secondary = {
                "a","b","d","f","e",""
        };

        CompareResult result = new Comparer().compare(getContentList(primary), getContentList(secondary));

        if(!(result.getAdd() == 0 && result.getMod() == 3 && result.getDel() == 0)) {
            fail("main test 3 result should be add 0, mod 3, del 0.");
        }
    }

    private List<String> getContentList(String[] str) {
        List<String> output = new LinkedList<>();
        if(str != null) {
            for (int i = 0; i < str.length; i++) {
                output.add(str[i]);
            }
        }
        return output;
    }
}