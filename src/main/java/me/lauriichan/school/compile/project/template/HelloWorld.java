package me.lauriichan.school.compile.project.template;

import static me.lauriichan.school.compile.util.file.java.Security.*;

import java.io.File;

import me.lauriichan.school.compile.util.file.java.JavaFileBuilder;

final class HelloWorld extends JavaTemplate {

    public HelloWorld() {
        super("Hello World");
    }

    @Override
    public void setup(String packet, File directory) {
        buildJava(packet, directory);
        
        File source = new File(directory, "src");
        
        JavaFileBuilder builder = new JavaFileBuilder(source, "Main");
        builder.setPackage(packet).next().security(PUBLIC).add("class ").name().open();
        builder.next().security(PUBLIC).modStatic().type(Void.class).add("main(").param(String[].class, "args").add(')').open();
        builder.callField(System.class, "out", "println", "\"Hello World\"").close();
        builder.next().close().exit();
    }

}
