package me.lauriichan.school.compile.project.template;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.lauriichan.school.compile.data.translation.Translation;
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

    private final String type;
    private final String typeName;
    private final boolean hidden;

    private final String id;
    private final String name;

    public Template(String type) {
        this(type, false);
    }

    public Template(String type, boolean hidden) {
        this.type = type;
        this.hidden = hidden;
        Translation translation = Translation.getDefault();
        this.id = getClass().getSimpleName().split("\\.", 2)[0].toLowerCase().replace(type.toLowerCase(), "");
        this.name = translation.translate(type.toLowerCase() + ".template." + id);
        this.typeName = translation.translate(type.toLowerCase() + ".name");
    }

    public final String getName() {
        return name;
    }

    public final String getType() {
        return type;
    }

    public final String getTypeName() {
        return typeName;
    }

    public final boolean isHidden() {
        return hidden;
    }

    public abstract void setup(String packet, File directory);

}
