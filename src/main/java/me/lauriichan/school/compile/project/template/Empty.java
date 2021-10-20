package me.lauriichan.school.compile.project.template;

import static me.lauriichan.school.compile.util.file.java.Security.PUBLIC;

import java.io.File;

import me.lauriichan.school.compile.util.file.java.JavaFileBuilder;

final class Empty extends JavaTemplate {

    public Empty() {
        super("Empty");
    }

    @Override
    public void setup(String packet, File directory) {
        buildJava(packet, directory);
        
        File source = new File(directory, "src");
        
        JavaFileBuilder builder = new JavaFileBuilder(source, "Main");
        builder.setPackage(packet).next().security(PUBLIC).add("class ").name().open();
        builder.next().security(PUBLIC).modStatic().type(Void.class).add("main(").param(String[].class, "args").add(')').open();
        builder.close().next().close().exit();
    }

}
