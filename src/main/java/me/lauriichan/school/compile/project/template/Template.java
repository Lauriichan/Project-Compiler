package me.lauriichan.school.compile.project.template;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.lauriichan.school.compile.project.template.hamster.*;
import me.lauriichan.school.compile.project.template.java.*;

public abstract class Template {

    public static final List<Template> TEMPLATES;

    static {
        ArrayList<Template> templates = new ArrayList<>();
        // Java
        templates.add(new Empty());
        templates.add(new HelloWorld());
        templates.add(new HelloName());
        templates.add(new Secret99Bottles());
        // Hamster
        templates.add(new HamsterEmpty());
        TEMPLATES = Collections.unmodifiableList(templates);
    }

    private final String name;
    private final String type;
    private final boolean hidden;

    public Template(String type, String name) {
        this(type, name, false);
    }

    public Template(String type, String name, boolean hidden) {
        this.name = name;
        this.type = type;
        this.hidden = hidden;
    }

    public final String getName() {
        return name;
    }

    public final String getType() {
        return type;
    }

    public final boolean isHidden() {
        return hidden;
    }

    public abstract void setup(String packet, File directory);

}
