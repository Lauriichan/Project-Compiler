package me.lauriichan.school.compile.project.template.java;

import static me.lauriichan.school.compile.util.file.java.Security.PUBLIC;

import java.io.File;

import me.lauriichan.school.compile.project.template.JavaTemplate;
import me.lauriichan.school.compile.util.file.java.JavaFileBuilder;

public final class Empty extends JavaTemplate {

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
