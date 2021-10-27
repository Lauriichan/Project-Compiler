package me.lauriichan.school.compile;

import static me.lauriichan.school.compile.window.ui.util.ColorCache.color;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.NoSuchElementException;

import com.syntaxphoenix.syntaxapi.utils.java.Exceptions;
import com.syntaxphoenix.syntaxapi.utils.java.Files;
import com.syntaxphoenix.syntaxapi.utils.java.tools.Container;

import it.unimi.dsi.fastutil.ints.IntConsumer;
import me.lauriichan.school.compile.data.Settings;
import me.lauriichan.school.compile.data.converter.*;
import me.lauriichan.school.compile.data.translation.Translation;
import me.lauriichan.school.compile.exec.*;
import me.lauriichan.school.compile.project.Application;
import me.lauriichan.school.compile.project.Project;
import me.lauriichan.school.compile.util.Singleton;
import me.lauriichan.school.compile.util.UserSettings;
import me.lauriichan.school.compile.util.io.StreamListener;
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

    public static final Container<StreamListener> SYSTEM_OUT = Container.of();
    public static final Container<StreamListener> SYSTEM_ERR = Container.of();
    public static final Container<PrintStream> LOG_FILE = Container.of();
    public static final Container<Panel> UI = Container.of();

    public static final Container<PrintStream> OLD_OUT = Container.of();
    public static final Container<PrintStream> OLD_ERR = Container.of();

    public static final Container<IntConsumer> SELECT = Container.of();

    private Main() {}

    /*
     * Stop
     */

    private static void shutdown() {
        Singleton.get(Runner.class).exit();
        Singleton.get(ProjectCompiler.class).exit();
        Singleton.get(Settings.class).save();
        UI.ifPresent(panel -> {
            panel.exit();
        });
        LOG_FILE.ifPresent(stream -> {
            stream.flush();
            stream.close();
        });
        SYSTEM_OUT.ifPresent(stream -> {
            try {
                stream.close();
            } catch (NoSuchElementException | IOException e) {
                return; // Ignore, we're exiting
            }
        });
        SYSTEM_ERR.ifPresent(stream -> {
            try {
                stream.close();
            } catch (NoSuchElementException | IOException e) {
                return; // Ignore, we're exiting
            }
        });
    }

    /*
     * Start
     */

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(Main::shutdown));
        registerConverters();
        loadData();
        initSingletons();
        initLogger();
        try {
            buildUi();
        } catch (Exception exp) {
            OLD_ERR.get().println(Exceptions.stackTraceToString(exp));
        }
    }

    private static void loadData() {
        Settings settings = Singleton.get(Settings.class);
        settings.load();
        UserSettings.load();
        Project.PROJECTS.load(settings, Project.class);
        Application.APPLICATIONS.load(settings, Application.class);
        settings.save();
        Translation.load();
    }

    private static void registerConverters() {
        new FileConverter();
        new ListConverter();
        new MapConverter();
    }

    private static void initSingletons() {
        Singleton.get(Compiler.class);
        Singleton.get(Runner.class);
    }

    private static void initLogger() {
        try {
            OLD_OUT.replace(System.out).lock();
            OLD_ERR.replace(System.err).lock();
            LOG_FILE.replace(new PrintStream(Files.createFile(new File("debug.log")))).lock();
            System.setOut(SYSTEM_OUT.replace(new StreamListener("SystemOut")).lock().get().getStream());
            System.setErr(SYSTEM_ERR.replace(new StreamListener("SystemErr")).lock().get().getStream());
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    public static void buildUi() {
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
        MainView main = new MainView();
        viewManager.add(main);
        viewManager.add(new TemplateView(main));
        viewManager.add(new CompileView());
        viewManager.add(new ConsoleView());
        viewManager.add(new SettingView());
        pane.addChild(viewManager);
        btar.setSelectEnabled(true);
        viewManager.select(0);
        btar.setSelected(0);

        SELECT.replace((index) -> {
            viewManager.select(index);
            btar.setSelected(index);
        }).lock();

        panel.center();
        panel.show();

        UI.replace(panel).lock();
    }

}
