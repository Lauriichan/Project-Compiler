package me.lauriichan.school.compile.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import com.syntaxphoenix.syntaxapi.utils.java.Exceptions;
import com.syntaxphoenix.syntaxapi.utils.java.Files;

public final class ZipUtil {

    private ZipUtil() {}

    public static void zip(File output, File directory, String... exclusions) {
        if (!directory.exists() || !directory.isDirectory()) {
            return;
        }
        Files.createFile(output);
        List<String> list = Arrays.asList(exclusions);
        int length = directory.getPath().length() + 1;
        try (ZipOutputStream stream = new ZipOutputStream(new FileOutputStream(output))) {
            for (File file : directory.listFiles()) {
                if (list.contains(file.getName())) {
                    continue;
                }
                addEntry(length, file, stream);
            }
        } catch (IOException e) {
            System.err.println("Failed to zip '" + directory.getPath() + "' to '" + output.getPath() + "'!");
            System.err.println(Exceptions.stackTraceToString(e));
        }

    }

    private static void addEntry(int length, File target, ZipOutputStream stream) throws IOException {
        if (target.isDirectory()) {
            String name = target.getPath().replace("\\", "/").substring(length);
            if (name.isEmpty()) {
                return;
            }
            if (!name.endsWith("/")) {
                name += "/";
            }
            ZipEntry entry = new ZipEntry(name);
            entry.setTime(target.lastModified());
            stream.putNextEntry(entry);
            stream.closeEntry();
            for (File file : target.listFiles()) {
                addEntry(length, file, stream);
            }
            return;
        }
        ZipEntry entry = new ZipEntry(target.getPath().replace("\\", "/").substring(length));
        entry.setTime(target.lastModified());
        stream.putNextEntry(entry);
        try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(target))) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = input.read(buffer)) != -1) {
                stream.write(buffer, 0, read);
            }
        }
        stream.closeEntry();
    }

    public static void unzip(File input, File directory) {
        if (!input.exists() || !input.isFile()) {
            return;
        }
        try (ZipInputStream stream = new ZipInputStream(new FileInputStream(input))) {
            ZipEntry entry;
            while ((entry = stream.getNextEntry()) != null) {
                File file = new File(directory, entry.getName());
                if (entry.isDirectory()) {
                    Files.createFolder(file);
                    stream.closeEntry();
                    continue;
                }
                Files.createFile(file);
                try (FileOutputStream output = new FileOutputStream(file)) {
                    byte[] buffer = new byte[1024];
                    int read;
                    while ((read = stream.read(buffer)) != -1) {
                        output.write(buffer, 0, read);
                    }
                }
                stream.closeEntry();
            }
        } catch (IOException e) {
            System.err.println("Failed to unzip to '" + directory.getPath() + "' from '" + input.getPath() + "'!");
            System.err.println(Exceptions.stackTraceToString(e));
        }
    }

}
