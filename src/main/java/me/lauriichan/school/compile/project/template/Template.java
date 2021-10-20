package me.lauriichan.school.compile.project.template;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Template {

    public static final List<Template> TEMPLATES;

    static {
        ArrayList<Template> templates = new ArrayList<>();
        templates.add(new Empty());
        templates.add(new HelloWorld());
        TEMPLATES = Collections.unmodifiableList(templates);
    }

    private final String name;

    public Template(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void setup(String packet, File directory);

}
