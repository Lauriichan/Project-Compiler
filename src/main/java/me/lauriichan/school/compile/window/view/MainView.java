package me.lauriichan.school.compile.window.view;

import static me.lauriichan.school.compile.window.ui.util.ColorCache.color;

import java.awt.Color;
import java.util.ArrayList;
import java.util.function.Consumer;

import com.syntaxphoenix.syntaxapi.utils.java.Strings;

import me.lauriichan.school.compile.data.ISetting;
import me.lauriichan.school.compile.data.Settings;
import me.lauriichan.school.compile.project.Project;
import me.lauriichan.school.compile.util.Singleton;
import me.lauriichan.school.compile.window.ui.BasicPane;
import me.lauriichan.school.compile.window.ui.Component;
import me.lauriichan.school.compile.window.ui.component.Button;
import me.lauriichan.school.compile.window.ui.component.RadioButton;
import me.lauriichan.school.compile.window.ui.component.RadioList;

public final class MainView extends View<BasicPane> {

    public MainView() {
        super("Manager", new BasicPane());
    }

    @Override
    protected void onSetup(BasicPane pane, int width, int height) {

        RadioList list = new RadioList();
        list.setX(10);
        list.setY(10);
        list.setComponentOffset(10);
        list.setComponentHeight(64);
        list.setWidth(((width / 3) * 2) - list.getX() * 2);
        list.setHeight(height - (list.getY() * 2));
        list.setBackground(color("#6D6D6D"));
        list.setLine(Color.DARK_GRAY);
        pane.addChild(list);
        fillList(list);

        addButton(10, "Ordner öffnen", projectAction(Project::openDirectory));
        addButton(50, "Editor öffnen", projectAction(Project::open));
        addWarnButton(90, "Löschen", projectAction(this::deleteProject));
        addButton(170, "Kompilieren", projectAction(Project::compile));
        addButton(210, "Ausführen", projectAction(Project::execute));
        addWarnButton(250, "Stoppen", Project::abort);

        addButton(height - 80, "Export", projectAction(this::exportProject));
        addButton(height - 40, "Import", this::importProject);

    }

    private Runnable projectAction(Consumer<Project> action) {
        return () -> {
            Project project = Project.getDefault();
            if (project == null) {
                return;
            }
            action.accept(project);
        };
    }

    private void addButton(int y, String label, Runnable action) {
        Button button = new Button();
        button.setY(y);
        button.setX((pane.getWidth() / 3) * 2);
        button.setHeight(30);
        button.setWidth(pane.getWidth() / 3 - 10);
        button.setText(label);
        button.setAction(action);
        button.setPress(color("#646363"));
        button.setShadow(color("#646363"));
        button.setHover(Color.DARK_GRAY, color("#353737"));
        button.setHoverFade(0.3, 0.125);
        button.setHoverShadow(Color.DARK_GRAY, color("#353737"));
        button.setHoverShadowFade(0.3, 0.125);
        pane.addChild(button);
    }

    private void addWarnButton(int y, String label, Runnable action) {
        Button button = new Button();
        button.setY(y);
        button.setX((pane.getWidth() / 3) * 2);
        button.setHeight(30);
        button.setWidth(pane.getWidth() / 3 - 10);
        button.setText(label);
        button.setAction(action);
        button.setPress(color("#F47777"));
        button.setShadow(color("#F47777"));
        button.setHover(color("#6F3535"), color("#954040"));
        button.setHoverFade(0.3, 0.125);
        button.setHoverShadow(color("#6F3535"), color("#954040"));
        button.setHoverShadowFade(0.3, 0.125);
        pane.addChild(button);
    }

    public void addProject(Project project) {
        Component component = pane.getChild(0);
        if (component == null || !(component instanceof RadioList)) {
            return;
        }
        addButton((RadioList) component, project);
    }

    public void updateProject(Project project) {
        Component component = pane.getChild(0);
        if (component == null || !(component instanceof RadioList)) {
            return;
        }
        RadioList list = (RadioList) component;
        RadioButton[] buttons = list.getChildren();
        for (RadioButton button : buttons) {
            String name = button.getText().split("\n", 2)[0];
            if (!project.getName().equals(name)) {
                continue;
            }
            StringBuilder text = new StringBuilder();
            String type = getType(project.getOptions());
            text.append(project.getName());
            text.append('\n').append(project.getDirectory().getPath());
            if (type != null) {
                text.append("\n(").append(Strings.firstLetterToUpperCase(type)).append(')');
            }
            button.setText(text.toString());
            break;
        }
    }

    public void deleteProject(Project project) {
        removeProject(project.getName());
        Project.PROJECTS.delete(Singleton.get(Settings.class), project.getName());
    }

    public void removeProject(String name) {
        Component component = pane.getChild(0);
        if (component == null || !(component instanceof RadioList)) {
            return;
        }
        RadioList list = (RadioList) component;
        RadioButton[] buttons = list.getChildren();
        for (RadioButton button : buttons) {
            String btnName = button.getText().split("\n", 2)[0];
            if (!name.equals(btnName)) {
                continue;
            }
            list.removeChild(button);
            break;
        }
    }

    public void exportProject(Project project) {
        // TODO: Implement project zip export
    }

    public void importProject() {
        // TODO: Implement project zip import
    }

    private void fillList(RadioList list) {
        ISetting[] settings = Project.PROJECTS.get(Singleton.get(Settings.class));
        for (ISetting setting : settings) {
            if (!setting.isValid()) {
                continue;
            }
            Project project = setting.getAs(Project.class);
            if (project == null) {
                continue;
            }
            addButton(list, project);
            if (Project.getDefault() == project) {
                list.setCurrent(list.getChildrenCount() - 1);
            }
        }
    }

    private void addButton(RadioList list, Project project) {
        RadioButton button = new RadioButton();
        String type = getType(project.getOptions());
        StringBuilder text = new StringBuilder();
        button.setX(10);
        button.setWidth(list.getWidth() - button.getX() * 2);
        text.append(project.getName());
        text.append('\n').append(project.getDirectory().getPath());
        if (type != null) {
            text.append("\n(").append(Strings.firstLetterToUpperCase(type)).append(')');
        }
        button.setMultilineAllowed(true);
        button.setText(text.toString());
        button.setAction(() -> Project.setDefault(project));
        button.setPress(color("#646363"));
        button.setShadow(color("#646363"));
        button.setHover(Color.DARK_GRAY, color("#353737"));
        button.setHoverFade(0.3, 0.125);
        button.setHoverShadow(Color.DARK_GRAY, color("#353737"));
        button.setHoverShadowFade(0.3, 0.125);
        list.addChild(button);
    }

    private String getType(ArrayList<String> options) {
        for (String option : options) {
            if (!option.startsWith("-t ")) {
                continue;
            }
            return option.substring(3);
        }
        return null;
    }

}
