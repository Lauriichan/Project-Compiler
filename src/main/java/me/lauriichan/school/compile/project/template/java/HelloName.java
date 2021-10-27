package me.lauriichan.school.compile.project.template.java;

import static me.lauriichan.school.compile.util.file.java.Security.*;

import java.io.File;
import java.util.Scanner;

import me.lauriichan.school.compile.project.template.JavaTemplate;
import me.lauriichan.school.compile.util.file.java.JavaFileBuilder;

public final class HelloName extends JavaTemplate {

    @Override
    public void setup(String packet, File directory) {
        File source = buildJava(packet, directory);

        JavaFileBuilder builder = new JavaFileBuilder(source, "Main");
        builder.setPackage(packet).normalImport(Scanner.class);
        builder.next().security(PUBLIC).add("class ").name().open();
        builder.next().security(PUBLIC).modStatic().type(Void.class).add("main(").param(String[].class, "args").add(") throws Exception")
            .open();
        builder.callField(System.class, "out", "println", "\"Bitte gebe einen Namen an:\"").next();
        builder.constructVar(Scanner.class, "input", "System.in").next();
        builder.variable(String.class, "name", null).next();
        builder.add("while(true)").open();
        builder.add("name = ").call("input", "nextLine").next();
        builder.ifStatement("name == null || name.trim().isEmpty()");
        builder.callField(System.class, "out", "println", "\"Bitte gebe einen validen Namen an!\"");
        builder.next().keyContinue().close().next().keyBreak().close().next();
        builder.callField(System.class, "out", "printf", "\"Hallo %s!\"", "name").next().callField(System.class, "out", "println");
        builder.close().next().close().exit();
    }

}
