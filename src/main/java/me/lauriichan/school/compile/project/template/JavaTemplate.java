package me.lauriichan.school.compile.project.template;

import static me.lauriichan.school.compile.util.file.FileHelper.deleteIfExists;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import com.syntaxphoenix.syntaxapi.utils.java.Exceptions;
import com.syntaxphoenix.syntaxapi.utils.java.Files;

public abstract class JavaTemplate extends Template {

    public JavaTemplate(String name) {
        super("Java", name);
    }

    public JavaTemplate(String name, boolean hidden) {
        super("Java", name, hidden);
    }

    protected File buildJava(String packetName, File directory) {
        File source = new File(directory, "src");
        deleteIfExists(source);
        Files.createFolder(source);
        File resource = new File(directory, "resources");
        deleteIfExists(resource);
        Files.createFolder(resource);
        File bin = new File(directory, "bin");
        deleteIfExists(bin);
        Files.createFolder(bin);
        File packet = new File(source, packetName.replace('.', '/'));
        Files.createFolder(packet);

        File manifestFile = new File(directory, "MANIFEST.MF");
        Files.createFile(manifestFile);
        try (FileOutputStream stream = new FileOutputStream(manifestFile)) {
            Manifest manifest = new Manifest();
            Attributes attributes = manifest.getMainAttributes();
            attributes.put(Attributes.Name.MAIN_CLASS, packetName + ".Main");
            attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
            manifest.write(stream);
        } catch (IOException exp) {
            System.err.println("Something went wrong while creating Manifest for package '" + packetName + "'!");
            System.err.println(Exceptions.stackTraceToString(exp));
        }
        return packet;
    }

}
