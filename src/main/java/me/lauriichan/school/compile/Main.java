package me.lauriichan.school.compile;

import java.awt.Color;
import java.io.File;

import me.lauriichan.school.compile.data.Settings;
import me.lauriichan.school.compile.data.converter.*;
import me.lauriichan.school.compile.project.Application;
import me.lauriichan.school.compile.project.Project;
import me.lauriichan.school.compile.project.template.Template;
import me.lauriichan.school.compile.util.Singleton;
import me.lauriichan.school.compile.window.input.mouse.MouseButton;
import me.lauriichan.school.compile.window.ui.Pane;
import me.lauriichan.school.compile.window.ui.Panel;
import me.lauriichan.school.compile.window.ui.RootBar;
import me.lauriichan.school.compile.window.ui.component.TextField;
import me.lauriichan.school.compile.window.ui.component.bar.BarBox;
import me.lauriichan.school.compile.window.ui.component.bar.BoxRenderers;
import me.lauriichan.school.compile.window.ui.component.tab.SimpleTabBar;
import me.lauriichan.school.compile.window.ui.component.tab.TabButton;

public final class Main {

    private Main() {}

    public static void main(String[] args) {
        registerConverters();
        loadData();
        initSingletons();
        testUi();
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

    public static void testTemplate() {
        Project.create("test", "we.got.a.test", new File("projects/test"), Template.TEMPLATES.get(0));
        Project.get("test").open();
    }

    public static void testUi() {
        Panel panel = new Panel();
        panel.setWidth(800);
        panel.setHeight(600);
        panel.setBarHeight(40);
        panel.setBackground(Color.GRAY);

        // Bar
        RootBar bar = panel.getBar();
        BarBox close = bar.createBox(BoxRenderers.CLOSE);
        close.setIcon(Color.GRAY, Color.decode("#F26161"));
        close.setIconFade(0.3, 0.15);
        close.setBox(Color.DARK_GRAY);
        close.setAction(MouseButton.LEFT, () -> System.exit(0));
        BarBox minimize = bar.createBox(BoxRenderers.MINIMIZE);
        minimize.setIcon(Color.GRAY, Color.WHITE);
        minimize.setIconFade(0.3, 0.15);
        minimize.setBox(Color.DARK_GRAY);
        minimize.setAction(MouseButton.LEFT, () -> panel.minimize());

        // Pane
        Pane pane = panel.getPane();
        SimpleTabBar btar = new SimpleTabBar();
        btar.setHeight(20);
        btar.setSize(80);
        for(int index = 0; index < 6; index++) {
            TabButton button = new TabButton();
            button.setText("Test " + index);
            button.setPress(Color.LIGHT_GRAY);
            button.setShadow(Color.LIGHT_GRAY);
            button.setHover(Color.DARK_GRAY, Color.GRAY);
            button.setHoverFade(0.4, 0.2);
            button.setHoverShadow(Color.DARK_GRAY, Color.GRAY);
            button.setHoverShadowFade(0.4, 0.2);
            btar.add(button);
        }
        btar.setSelectEnabled(true);
        btar.setSelected(0);
        pane.setBar(btar);
        
        TextField field = new TextField();
        field.setSize(800, 400);
        field.setReturnAllowed(true);
        pane.addChild(field);
        
        panel.center();
        panel.show();
    }

}
