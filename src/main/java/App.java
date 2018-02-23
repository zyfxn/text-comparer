import compare.Comparer;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException {
        PropertyConfigurator.configure("log4j.properties");

        args = new String[2];
        args[0] = "E:\\workspace\\res\\CodeCompare.java";
        args[1] = "E:\\workspace\\res\\CodeCompare2.java";

        Comparer c = new Comparer();
        c.sameRangesOutput = true;
        c.DEBUG = true;
        // c.lengthThresholdAfterTrim = 1000;

        String p = null;
        String s = null;

        if (!args[0].isEmpty()) {
            p = c.getFileContent(args[0]);
        }
        if (!args[1].isEmpty()) {
            s = c.getFileContent(args[1]);
        }

        c.compare(p, s);
    }
}
