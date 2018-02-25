package xuning.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TextFile {

    /**
     * Get file content.
     *
     * @param filePath
     * @return content string.
     * @throws IOException
     */
    public static String getFileContent(String filePath) throws IOException {
        if (filePath == null)
            return null;

        File file = new File(filePath);
        FileReader filereader = new FileReader(file);
        BufferedReader buffReader = new BufferedReader(filereader);

        StringBuilder builder = new StringBuilder();
        String temp;
        while ((temp = buffReader.readLine()) != null) {
            builder.append(temp).append('\n');
        }

        buffReader.close();
        filereader.close();

        return builder.toString();
    }

    /**
     * A string split into lines.
     *
     * @param content
     *            - file content
     * @return line data {@code String[]}. the value is null if content is null.
     */
    public static String[] splitFrom(String content) {
        if (content == null || content.trim().isEmpty()) {
            return null;
        }

        return content.split("\\n");
    }
}
