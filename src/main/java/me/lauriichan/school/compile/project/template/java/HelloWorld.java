package me.lauriichan.school.compile.project.template.java;

import static me.lauriichan.school.compile.util.file.java.Security.*;

import java.io.File;

import me.lauriichan.school.compile.project.template.JavaTemplate;
import me.lauriichan.school.compile.util.file.java.JavaFileBuilder;

public final class HelloWorld extends JavaTemplate {

    public HelloWorld() {
        super("Hallo Welt");
    }

    @Override
    public void setup(String packet, File directory) {
        File source = buildJava(packet, directory);
        
        JavaFileBuilder builder = new JavaFileBuilder(source, "Main");
        builder.setPackage(packet);
        builder.security(PUBLIC).add("class ").name().open();
        builder.next().security(PUBLIC).modStatic().type(Void.class).add("main(").param(String[].class, "args").add(") throws Exception").open();
        builder.callField(System.class, "out", "println", "\"Hallo Welt\"").close();
        builder.next().close().exit();
    }

}
