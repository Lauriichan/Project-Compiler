package me.lauriichan.school.compile.project.template.hamster;

import java.io.File;

import me.lauriichan.school.compile.project.template.HamsterTemplate;
import me.lauriichan.school.compile.util.file.java.JavaFileBuilder;

public final class HamsterEmpty extends HamsterTemplate {

    public HamsterEmpty() {
        super("Leer");
    }

    @Override
    public void setup(String packet, File directory) {
        JavaFileBuilder builder = new JavaFileBuilder(directory, "hamster", "ham");
        builder.wildNormalImport("java.lang").wildNormalImport("java.util").wildNormalImport("java.util.function")
            .wildNormalImport("de.hamster.model").wildNormalImport("de.hamster.debugger.model");
        builder.add("@SuppressWarnings(\"unchecked\")").next().next();
        builder.type(Void.class).add("main()").open().close().exit();
    }

}
