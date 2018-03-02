package xuning.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class TextFile {

    /**
     * Get file content.
     *
     * @param filePath
     * @return content string.
     * @throws IOException
     */
    public static List<String> getFileContent(String filePath) throws IOException {
        if (filePath == null)
            return null;

        List<String> output = new LinkedList<>();

        File file = new File(filePath);
        FileReader filereader = new FileReader(file);
        BufferedReader buffReader = new BufferedReader(filereader);

        String temp;
        while ((temp = buffReader.readLine()) != null) {
            output.add(temp);
        }

        buffReader.close();
        filereader.close();

        return output;
    }
}
