package me.lauriichan.school.compile;

import static me.lauriichan.school.compile.window.ui.util.ColorCache.color;

import java.awt.Color;

import me.lauriichan.school.compile.data.Settings;
import me.lauriichan.school.compile.data.converter.*;
import me.lauriichan.school.compile.exec.Runner;
import me.lauriichan.school.compile.project.Application;
import me.lauriichan.school.compile.project.Project;
import me.lauriichan.school.compile.util.Singleton;
import me.lauriichan.school.compile.util.UserSettings;
import me.lauriichan.school.compile.window.input.mouse.MouseButton;
import me.lauriichan.school.compile.window.ui.Pane;
import me.lauriichan.school.compile.window.ui.Panel;
import me.lauriichan.school.compile.window.ui.RootBar;
import me.lauriichan.school.compile.window.ui.component.bar.BarBox;
import me.lauriichan.school.compile.window.ui.component.tab.SimpleTabBar;
import me.lauriichan.school.compile.window.ui.component.tab.TabButton;
import me.lauriichan.school.compile.window.ui.util.BoxRenderers;
import me.lauriichan.school.compile.window.view.*;

public final class Main {

    private Main() {}

    public static void main(String[] args) {
        registerConverters();
        loadData();
        initSingletons();
        buildUi();
        Runtime.getRuntime().addShutdownHook(new Thread(Main::shutdown));
    }

    private static void shutdown() {
        Singleton.get(Settings.class).save();
    }

    private static void loadData() {
        Settings settings = Singleton.get(Settings.class);
        settings.load();
        UserSettings.load();
        Project.PROJECTS.load(settings, Project.class);
        Application.APPLICATIONS.load(settings, Application.class);
        settings.save();
    }

    private static void registerConverters() {
        new FileConverter();
        new ListConverter();
    }

    private static void initSingletons() {
        Singleton.get(Compiler.class);
        Singleton.get(Runner.class);
    }

    public static Panel buildUi() {
        Panel panel = new Panel();
        Pane pane = panel.getPane();
        panel.setWidth(800);
        panel.setHeight(600);
        panel.setBarHeight(40);
        panel.setBackground(Color.GRAY);

        // Root bar
        RootBar bar = panel.getBar();
        BarBox close = bar.createBox(BoxRenderers.CROSS);
        close.setIcon(Color.GRAY, color("#F26161"));
        close.setIconFade(0.3, 0.15);
        close.setBox(Color.DARK_GRAY);
        close.setAction(MouseButton.LEFT, () -> System.exit(0));
        BarBox minimize = bar.createBox(BoxRenderers.UNDERSCORE);
        minimize.setIcon(Color.GRAY, Color.WHITE);
        minimize.setIconFade(0.3, 0.15);
        minimize.setBox(Color.DARK_GRAY);
        minimize.setAction(MouseButton.LEFT, () -> panel.minimize());

        // Bar
        SimpleTabBar btar = new SimpleTabBar();
        btar.setHeight(20);
        btar.setSize(120);
        pane.setBar(btar);

        // Views
        ViewManager viewManager = new ViewManager();
        viewManager.setSize(pane.getWidth(), pane.getHeight());
        viewManager.setDelegate((state, index, title) -> {
            if (state) {
                TabButton button = new TabButton();
                button.setText(title);
                button.setPress(Color.LIGHT_GRAY);
                button.setShadow(Color.LIGHT_GRAY);
                button.setHover(Color.DARK_GRAY, Color.GRAY);
                button.setHoverFade(0.4, 0.2);
                button.setHoverShadow(Color.DARK_GRAY, Color.GRAY);
                button.setHoverShadowFade(0.4, 0.2);
                button.setAction(() -> viewManager.select(index));
                btar.add(button);
                return;
            }
            btar.remove(btar.get(index));
        });
        viewManager.setUpdater((index, title) -> btar.get(index).setText(title));
        viewManager.add(new MainView());
        viewManager.add(new TemplateView());
        viewManager.add(new CompileView());
        viewManager.add(new ConsoleView());
        viewManager.add(new SettingView());
        viewManager.select(0);
        btar.setSelectEnabled(true);
        btar.setSelected(0);
        pane.addChild(viewManager);

        panel.center();
        panel.show();

        return panel;
    }

}
