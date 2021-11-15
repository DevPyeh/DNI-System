package me.pyeh.dni.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileUtils {

    public static String readWholeFile(File file) {
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {

            StringBuilder builder = new StringBuilder();

            String line;
            while((line = reader.readLine()) != null) {
                builder.append(line);
            }

            return builder.length() != 0 ? builder.toString() : null;

        } catch(IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static File getOrCreateFile(File parent, String name) {
        File file = new File(parent, name);

        if(!parent.exists()) parent.mkdir();

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    public static void writeString(File file, String content) {
        try(BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {

            writer.write(content);

        } catch(IOException e) {
            e.printStackTrace();
        }
    }

}
