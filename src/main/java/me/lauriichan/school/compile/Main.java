package me.lauriichan.school.compile;

import me.lauriichan.school.compile.data.Settings;
import me.lauriichan.school.compile.data.converter.*;
import me.lauriichan.school.compile.project.Application;
import me.lauriichan.school.compile.project.Project;
import me.lauriichan.school.compile.util.Singleton;
import me.lauriichan.school.compile.window.ui.Button;
import me.lauriichan.school.compile.window.ui.Pane;
import me.lauriichan.school.compile.window.ui.RootPanel;

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
        RootPanel panel = new RootPanel();
        Pane pane = panel.getPane();
        Button button = new Button();
        button.setWidth(200);
        button.setHeight(100);
        pane.addChild(button);
        panel.setWidth(800);
        panel.setHeight(600);
        panel.show();
        while (true) {
            panel.render();
            try {
                Thread.sleep(100);
            } catch (InterruptedException exp) {
                break;
            }
        }
    }

}
