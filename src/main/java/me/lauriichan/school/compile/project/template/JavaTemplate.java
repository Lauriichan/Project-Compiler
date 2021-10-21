package me.lauriichan.school.compile.project.template;

import java.io.File;

import com.syntaxphoenix.syntaxapi.utils.java.Files;

import me.lauriichan.school.compile.util.file.DefaultFileBuilder;

public abstract class JavaTemplate extends Template {

    public JavaTemplate(String name) {
        super(name);
    }

    public JavaTemplate(String name, boolean hidden) {
        super(name, hidden);
    }

    protected void buildJava(String packet, File directory) {
        File source = new File(directory, "src");
        Files.createFolder(source);
        File resource = new File(directory, "resources");
        Files.createFolder(resource);

        DefaultFileBuilder manifest = new DefaultFileBuilder(directory, "MANIFEST.MF");
        manifest.add("Manifest-Version: 1.0").next();
        manifest.add("Main-Class: ").add(packet).add(".Main");
    }

}
