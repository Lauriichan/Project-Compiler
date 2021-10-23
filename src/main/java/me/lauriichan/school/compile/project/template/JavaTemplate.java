package me.lauriichan.school.compile.project.template;

import static me.lauriichan.school.compile.util.file.FileHelper.deleteIfExists;

import java.io.File;

import com.syntaxphoenix.syntaxapi.utils.java.Files;

import me.lauriichan.school.compile.util.file.DefaultFileBuilder;

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
        File packet = new File(source, packetName.replace('.', '/'));
        Files.createFolder(packet);

        DefaultFileBuilder manifest = new DefaultFileBuilder(directory, "MANIFEST.MF");
        manifest.add("Manifest-Version: 1.0").next();
        manifest.add("Main-Class: ").add(packetName).add(".Main").exit();
        return packet;
    }

}
