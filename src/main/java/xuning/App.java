package xuning;

import xuning.compare.Comparer;
import xuning.util.TextFile;

import java.io.IOException;
import java.util.List;

public class App {

    public static void main(String[] args) throws IOException {

        if(args.length < 2) {
            System.out.println("usage: compare.jar <primary file path> <secondary file path>");
            return;
        }

        List<String> p = null;
        List<String> s = null;

        if (!args[0].isEmpty()) {
            p = TextFile.getFileContent(args[0]);
        }
        if (!args[1].isEmpty()) {
            s = TextFile.getFileContent(args[1]);
        }

        new Comparer()
                .setDebug(true)
                .compare(p, s);
    }
}
