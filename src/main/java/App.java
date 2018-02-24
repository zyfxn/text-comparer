import xuning.compare.Comparer;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException {
        PropertyConfigurator.configure("log4j.properties");

        Comparer c = new Comparer();
        c.sameRangesOutput = true;
        c.DEBUG = true;

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
