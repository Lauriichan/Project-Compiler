package me.lauriichan.school.compile.util.file;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

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

}
