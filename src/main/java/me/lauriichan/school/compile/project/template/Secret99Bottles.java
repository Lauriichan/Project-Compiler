package me.lauriichan.school.compile.project.template;

import static me.lauriichan.school.compile.util.file.java.Security.PUBLIC;

import java.io.File;

import me.lauriichan.school.compile.util.file.java.JavaFileBuilder;

final class Secret99Bottles extends JavaTemplate {

    public Secret99Bottles() {
        super("99 Flaschen", true);
    }

    @Override
    public void setup(String packet, File directory) {
        buildJava(packet, directory);

        File source = new File(directory, "src");

        JavaFileBuilder builder = new JavaFileBuilder(source, "Main");
        builder.setPackage(packet);
        builder.security(PUBLIC).add("class ").name().open();
        builder.next().security(PUBLIC).modStatic().type(Void.class).add("main(").param(String[].class, "args").add(") throws Exception")
            .open();
        builder.loopForMin("bottle", "99", "2");
        builder.callField(System.class, "out", "printf", "\"%s bottles of beer on the wall, %s bottles of beer.\"", "bottle", "bottle")
            .next();
        builder.callField(System.class, "out", "println").next();
        builder.variableSmartIf(String.class, "bottleName", "(bottle - 1) == 1", "\"bottle\"", "\"bottles\"").next();
        builder.callField(System.class, "out", "printf", "\"Take one down and pass it around, %s %s of beer on the wall.\"", "bottle - 1",
            "bottleName").next();
        builder.callField(System.class, "out", "println").next();
        builder.callField(System.class, "out", "println").close().next();
        builder.callField(System.class, "out", "println", "\"1 bottle of beet on the wall, 1 bottle of beer.\"").next();
        builder.callField(System.class, "out", "println", "\"Take one down and pass it around, no more bottles of beer on the wall.\"")
            .next();
        builder.callField(System.class, "out", "println").next();
        builder.callField(System.class, "out", "println", "\"No more bottles of beer on the wall, no more bottles of beer.\"").next();
        builder.callField(System.class, "out", "println", "\"Go to the store and buy some more, 99 bottles of beer on the wall.\"");
        builder.close().next().close().exit();
    }

}
