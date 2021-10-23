package me.lauriichan.school.compile;

import static me.lauriichan.school.compile.window.ui.util.ColorCache.color;

import java.awt.Color;
import java.io.File;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import jnafilechooser.api.JnaFileChooser;
import jnafilechooser.api.JnaFileChooser.Mode;
import me.lauriichan.school.compile.data.Settings;
import me.lauriichan.school.compile.data.converter.*;
import me.lauriichan.school.compile.project.Application;
import me.lauriichan.school.compile.project.Project;
import me.lauriichan.school.compile.project.ProjectInfo;
import me.lauriichan.school.compile.project.template.Template;
import me.lauriichan.school.compile.util.Singleton;
import me.lauriichan.school.compile.window.input.mouse.MouseButton;
import me.lauriichan.school.compile.window.ui.Dialog;
import me.lauriichan.school.compile.window.ui.Pane;
import me.lauriichan.school.compile.window.ui.Panel;
import me.lauriichan.school.compile.window.ui.RootBar;
import me.lauriichan.school.compile.window.ui.component.Button;
import me.lauriichan.school.compile.window.ui.component.Label;
import me.lauriichan.school.compile.window.ui.component.TextField;
import me.lauriichan.school.compile.window.ui.component.bar.BarBox;
import me.lauriichan.school.compile.window.ui.component.bar.BoxRenderers;
import me.lauriichan.school.compile.window.ui.component.tab.SimpleTabBar;
import me.lauriichan.school.compile.window.ui.component.tab.TabButton;
import me.lauriichan.school.compile.window.view.CompileView;
import me.lauriichan.school.compile.window.view.MainView;
import me.lauriichan.school.compile.window.view.SettingView;
import me.lauriichan.school.compile.window.view.TemplateView;
import me.lauriichan.school.compile.window.view.View;
import me.lauriichan.school.compile.window.view.ViewManager;

public final class Main {

    private Main() {}

    public static void main(String[] args) {
        registerConverters();
        loadData();
        initSingletons();
        new Dialog(Main::buildProjectDialog).show((test0, test1) -> {
            System.out.println(test0);
            System.out.println(test1);
            System.exit(0);
        });
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

    public static Panel buildUi() {
        Panel panel = new Panel();
        Pane pane = panel.getPane();
        panel.setWidth(800);
        panel.setHeight(600);
        panel.setBarHeight(40);
        panel.setBackground(Color.GRAY);

        // Root bar
        RootBar bar = panel.getBar();
        BarBox close = bar.createBox(BoxRenderers.CLOSE);
        close.setIcon(Color.GRAY, color("#F26161"));
        close.setIconFade(0.3, 0.15);
        close.setBox(Color.DARK_GRAY);
        close.setAction(MouseButton.LEFT, () -> System.exit(0));
        BarBox minimize = bar.createBox(BoxRenderers.MINIMIZE);
        minimize.setIcon(Color.GRAY, Color.WHITE);
        minimize.setIconFade(0.3, 0.15);
        minimize.setBox(Color.DARK_GRAY);
        minimize.setAction(MouseButton.LEFT, () -> panel.minimize());

        // Views
        ViewManager viewManager = new ViewManager();
        viewManager.setSize(pane.getWidth(), pane.getHeight());
        viewManager.add(new MainView());
        viewManager.add(new TemplateView());
        viewManager.add(new CompileView());
        viewManager.add(new SettingView());
        viewManager.select(0);
        pane.addChild(viewManager);

        // Bar
        SimpleTabBar btar = new SimpleTabBar();
        btar.setHeight(20);
        btar.setSize(120);
        for (int viewId = 0; viewId < viewManager.getViewCount(); viewId++) {
            View view = viewManager.get(viewId);
            TabButton button = new TabButton();
            button.setText(view.getTitle());
            button.setPress(Color.LIGHT_GRAY);
            button.setShadow(Color.LIGHT_GRAY);
            button.setHover(Color.DARK_GRAY, Color.GRAY);
            button.setHoverFade(0.4, 0.2);
            button.setHoverShadow(Color.DARK_GRAY, Color.GRAY);
            button.setHoverShadowFade(0.4, 0.2);
            final int id = viewId;
            button.setAction(() -> viewManager.select(id));
            btar.add(button);
        }
        btar.setSelectEnabled(true);
        btar.setSelected(0);
        pane.setBar(btar);

        panel.center();
        panel.show();

        return panel;
    }

    public static void buildProjectDialog(Dialog dialog, Panel panel) {
        Pane pane = panel.getPane();
        panel.setWidth(720);
        panel.setHeight(340);
        panel.setBarHeight(40);
        panel.setBackground(Color.GRAY);

        // Root bar
        RootBar bar = panel.getBar();
        BarBox close = bar.createBox(BoxRenderers.CLOSE);
        close.setIcon(Color.GRAY, color("#F26161"));
        close.setIconFade(0.3, 0.15);
        close.setBox(Color.DARK_GRAY);
        close.setAction(MouseButton.LEFT, () -> dialog.fail(null));
        
        Label nameLabel = new Label();
        nameLabel.setText("Name");
        nameLabel.setY(16);
        nameLabel.setX(10);
        nameLabel.setWidth(120);
        nameLabel.setHeight(32);
        nameLabel.setTextCentered(false);
        pane.addChild(nameLabel);
        
        Label packageLabel = new Label();
        packageLabel.setText("Package Name");
        packageLabel.setY(86);
        packageLabel.setX(10);
        packageLabel.setWidth(120);
        packageLabel.setHeight(32);
        packageLabel.setTextCentered(false);
        pane.addChild(packageLabel);
        
        Label directoryLabel = new Label();
        directoryLabel.setText("Projekt Ordner");
        directoryLabel.setY(156);
        directoryLabel.setX(10);
        directoryLabel.setWidth(120);
        directoryLabel.setHeight(32);
        directoryLabel.setTextCentered(false);
        pane.addChild(directoryLabel);
        
        TextField name = new TextField();
        name.setX(10);
        name.setY(40);
        name.setWidth(700);
        name.setHeight(32);
        name.setFontSize(14);
        name.setBackground(color("#6D6D6D"));
        name.setLine(color("#353737"));
        name.setInvalidBackground(color("#6F3535"));
        name.setInvalidLine(color("#353737"));
        name.setInvalidFontColor(color("#F47777"));
        name.setOutline(true);
        name.setLineSize(1);
        name.setFilter(
            character -> !(Character.isAlphabetic(character) || Character.isDigit(character) || character == ' ' || character == '_'));
        name.setValidator(string -> !(string.startsWith(" ") || string.length() < 4));
        pane.addChild(name);

        TextField packageName = new TextField();
        packageName.setX(10);
        packageName.setY(110);
        packageName.setWidth(700);
        packageName.setHeight(32);
        packageName.setFontSize(14);
        packageName.setBackground(color("#6D6D6D"));
        packageName.setLine(color("#353737"));
        packageName.setInvalidBackground(color("#6F3535"));
        packageName.setInvalidLine(color("#353737"));
        packageName.setInvalidFontColor(color("#F47777"));
        packageName.setOutline(true);
        packageName.setLineSize(1);
        Predicate<String> predicate = Pattern.compile("^[^\\d_.][^.]*(\\.[^._][^.]*)*$").asPredicate();
        packageName.setValidator(string -> predicate.test(string));
        packageName.setSpaceAllowed(false);
        packageName.setFilter(character -> {
            if (!Character.isAlphabetic(character) && !(Character.isDigit(character) || character == '.' || character == '_')) {
                return true;
            }
            character = Character.toLowerCase(character);
            return character == 'ö' || character == 'ü' || character == 'ä';
        });
        packageName.setMapper(Character::toLowerCase);
        packageName.setLimit(35);
        pane.addChild(packageName);

        TextField directory = new TextField();
        directory.setX(10);
        directory.setY(180);
        directory.setWidth(610);
        directory.setHeight(32);
        directory.setFontSize(14);
        directory.setFontColor(Color.LIGHT_GRAY);
        directory.setBackground(color("#6D6D6D"));
        directory.setLine(color("#353737"));
        directory.setInvalidBackground(color("#6F3535"));
        directory.setInvalidLine(color("#353737"));
        directory.setInvalidFontColor(color("#F47777"));
        directory.setOutline(true);
        directory.setLineSize(1);
        directory.setLocked(true);
        directory.setValidator(string -> {
            File file = new File(string);
            return file.isDirectory() && file.exists();
        });
        pane.addChild(directory);

        Button openButton = new Button();
        openButton.setText("Öffnen");
        openButton.setTextCentered(true);
        openButton.setPress(color("#646363"));
        openButton.setShadow(color("#646363"));
        openButton.setHover(Color.DARK_GRAY, color("#353737"));
        openButton.setHoverFade(0.3, 0.125);
        openButton.setHoverShadow(Color.DARK_GRAY, color("#353737"));
        openButton.setHoverShadowFade(0.3, 0.125);
        openButton.setWidth(80);
        openButton.setHeight(directory.getHeight());
        openButton.setX(630);
        openButton.setY(180);
        openButton.setAction(() -> {
            JnaFileChooser file = new JnaFileChooser();
            file.setMultiSelectionEnabled(false);
            file.setMode(Mode.Directories);
            file.showOpenDialog(panel.getFrame());
            File selected = file.getSelectedFile();
            if (selected != null && selected.isDirectory()) {
                directory.setContent(selected.getPath());
            }
        });
        pane.addChild(openButton);

        int btnHeight = pane.getHeight() - directory.getHeight() - 20;
        Button createButton = new Button();
        createButton.setText("Erstellen");
        createButton.setTextCentered(true);
        createButton.setPress(color("#646363"));
        createButton.setShadow(color("#646363"));
        createButton.setHover(Color.DARK_GRAY, color("#353737"));
        createButton.setHoverFade(0.3, 0.125);
        createButton.setHoverShadow(Color.DARK_GRAY, color("#353737"));
        createButton.setHoverShadowFade(0.3, 0.125);
        createButton.setWidth(200);
        createButton.setHeight(directory.getHeight());
        createButton.setX(80);
        createButton.setY(btnHeight);
        createButton.setAction(() -> {
            if (!name.isValid() || !packageName.isValid() || !directory.isValid()) {
                return;
            }
            dialog.success(new ProjectInfo(name.getContent(), packageName.getContent(), directory.getContent()));
        });
        pane.addChild(createButton);

        Button abortButton = new Button();
        abortButton.setText("Abbrechen");
        abortButton.setTextCentered(true);
        abortButton.setPress(color("#646363"));
        abortButton.setShadow(color("#646363"));
        abortButton.setHover(Color.DARK_GRAY, color("#353737"));
        abortButton.setHoverFade(0.3, 0.125);
        abortButton.setHoverShadow(Color.DARK_GRAY, color("#353737"));
        abortButton.setHoverShadowFade(0.3, 0.125);
        abortButton.setWidth(200);
        abortButton.setHeight(directory.getHeight());
        abortButton.setX(pane.getWidth() - abortButton.getWidth() - createButton.getX());
        abortButton.setY(btnHeight);
        abortButton.setAction(() -> dialog.fail(null));
        pane.addChild(abortButton);
    }

}
