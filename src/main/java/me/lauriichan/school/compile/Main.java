package me.lauriichan.school.compile;

import java.io.File;

import me.lauriichan.school.compile.data.Settings;
import me.lauriichan.school.compile.data.converter.*;
import me.lauriichan.school.compile.project.Application;
import me.lauriichan.school.compile.project.Project;
import me.lauriichan.school.compile.project.template.Template;
import me.lauriichan.school.compile.util.Singleton;

public final class Main {

    private Main() {}

    public static void main(String[] args) {
        registerConverters();
        loadData();
        initSingletons();
        test();
        
    }

    private static void loadData() {
        Settings settings = Singleton.get(Settings.class);
        settings.load();
        Project.PROJECTS.load(settings, Project.class);
        Application.APPLICATIONS.load(settings, Application.class);
        settings.save();
        
    }

    private static void registerConverters() {
        new FileConverter();
        new ListConverter();
    }

    private static void initSingletons() {

    }
    
    private static void test() {
        Project.create("test", "the.test", new File("projects/test"), Template.TEMPLATES.get(1));
        Project project = Project.get("test");
        if(project == null) {
            return;
        }
        project.open();
    }

}
