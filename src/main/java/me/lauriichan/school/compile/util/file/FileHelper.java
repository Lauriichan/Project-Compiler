package me.lauriichan.school.compile.util.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;

import org.apache.commons.io.FileUtils;

import com.syntaxphoenix.syntaxapi.utils.java.Exceptions;

import me.lauriichan.school.compile.Main;

public final class FileHelper {

    private FileHelper() {}

    public static void deleteIfExists(File file) {
        if (!file.exists()) {
            return;
        }
        try {
            if (file.isDirectory()) {
                FileUtils.deleteDirectory(file);
                return;
            }
            FileUtils.forceDelete(file);
        } catch (IOException e) {
        }
    }

    public static File asRelative(File input) {
        try {
            File jarFile = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            String jarPath = asDrivePath(URLDecoder.decode(jarFile.getAbsolutePath(), "UTF-8")).replace('\\', '/');
            String inputPath = asDrivePath(input.getAbsolutePath()).replace('\\', '/');

            String[] directories = jarPath.split("/");
            String[] inputDirectories = inputPath.split("/");

            int length = directories.length > inputDirectories.length ? inputDirectories.length : directories.length;

            int lastCommonRoot = -1;
            for (int index = 0; index < length; index++) {
                if (!directories[index].equals(inputDirectories[index])) {
                    break;
                }
                lastCommonRoot = index;
            }

            if (lastCommonRoot == -1) {
                return input;
            }

            StringBuilder path = new StringBuilder();
            for (int index = lastCommonRoot + 1; index < directories.length - 1; index++) {
                if (directories[index].length() == 0) {
                    continue;
                }
                path.append("../");
            }

            for (int index = lastCommonRoot + 1; index < inputDirectories.length - 1; index++) {
                path.append(inputDirectories[index] + '/');
            }
            path.append(inputDirectories[inputDirectories.length - 1]);

            return new File(path.toString());
        } catch (Exception exp) {
            System.err.println("Failed to relavize path");
            System.err.println(Exceptions.stackTraceToString(exp));
            return input;
        }
    }

    public static String asDrivePath(String path) throws IOException {
        Runtime runTime = Runtime.getRuntime();
        Process process = runTime.exec("net use");
        InputStream inStream = process.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = null;
        String[] components = null;
        while (null != (line = bufferedReader.readLine())) {
            components = line.split("\\s+");
            if ((components.length > 2) && (components[1].equals(path.substring(0, 2)))) {
                path = path.replace(components[1], components[2]);
            }
        }
        return path;
    }

}
