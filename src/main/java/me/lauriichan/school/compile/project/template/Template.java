package me.lauriichan.school.compile.project.template;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Template {

    public static final List<Template> TEMPLATES;

    static {
        ArrayList<Template> templates = new ArrayList<>();
        templates.add(new RandomTemplate());
        templates.add(new Empty());
        templates.add(new HelloWorld());
        templates.add(new HelloName());
        templates.add(new Secret99Bottles());
        TEMPLATES = Collections.unmodifiableList(templates);
    }

    private final String name;
    private final boolean hidden;

    public Template(String name) {
        this(name, false);
    }

    public Template(String name, boolean hidden) {
        this.name = name;
        this.hidden = hidden;
    }

    public String getName() {
        return name;
    }

    public boolean isHidden() {
        return hidden;
    }

    public abstract void setup(String packet, File directory);

}
