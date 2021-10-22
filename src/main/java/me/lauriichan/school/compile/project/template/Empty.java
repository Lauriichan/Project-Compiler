package me.lauriichan.school.compile.project.template;

import static me.lauriichan.school.compile.util.file.java.Security.PUBLIC;

import java.io.File;

import me.lauriichan.school.compile.util.file.java.JavaFileBuilder;

final class Empty extends JavaTemplate {

    public Empty() {
        super("Leer");
    }

    @Override
    public void setup(String packet, File directory) {
        File source = buildJava(packet, directory);
        
        JavaFileBuilder builder = new JavaFileBuilder(source, "Main");
        builder.setPackage(packet);
        builder.security(PUBLIC).add("class ").name().open();
        builder.next().security(PUBLIC).modStatic().type(Void.class).add("main(").param(String[].class, "args").add(") throws Exception").open();
        builder.close().next().close().exit();
    }

}
