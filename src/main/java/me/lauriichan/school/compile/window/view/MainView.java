package me.lauriichan.school.compile.window.view;

import static me.lauriichan.school.compile.window.ui.util.ColorCache.color;

import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

import org.apache.commons.io.FileUtils;

import com.syntaxphoenix.syntaxapi.json.JsonValue;
import com.syntaxphoenix.syntaxapi.random.Keys;

import jnafilechooser.api.JnaFileChooser;
import jnafilechooser.api.JnaFileChooser.Mode;
import me.lauriichan.school.compile.data.ISetting;
import me.lauriichan.school.compile.data.Settings;
import me.lauriichan.school.compile.data.json.JsonIO;
import me.lauriichan.school.compile.data.translation.Translation;
import me.lauriichan.school.compile.project.Project;
import me.lauriichan.school.compile.util.Executor;
import me.lauriichan.school.compile.util.Singleton;
import me.lauriichan.school.compile.util.ZipUtil;
import me.lauriichan.school.compile.util.file.FileHelper;
import me.lauriichan.school.compile.window.ui.BasicPane;
import me.lauriichan.school.compile.window.ui.Component;
import me.lauriichan.school.compile.window.ui.component.Button;
import me.lauriichan.school.compile.window.ui.component.RadioButton;
import me.lauriichan.school.compile.window.ui.component.RadioList;

public final class MainView extends View<BasicPane> {

    public MainView() {
        super("ui.view.main", new BasicPane());
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

        addButton(10, "ui.main.open.folder", projectAction(Project::openDirectory));
        addButton(50, "ui.main.open.editor", projectAction(Project::open));
        addWarnButton(90, "ui.main.delete", projectAction(this::deleteProject));
        addButton(170, "ui.main.compile", projectAction(Project::compile));
        addButton(210, "ui.main.execute", projectAction(Project::execute));
        addWarnButton(250, "ui.main.stop", Project::abort);

        addButton(height - 80, "ui.main.export", projectAction(this::exportProject));
        addButton(height - 40, "ui.main.import", this::importProject);

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
        button.setText(Translation.getDefault().translate(label));
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
        button.setText(Translation.getDefault().translate(label));
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
                text.append("\n(").append(Translation.getDefault().translate(type.toLowerCase() + ".name")).append(')');
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

    public void selectProject(String name) {
        Component component = pane.getChild(0);
        if (component == null || !(component instanceof RadioList)) {
            return;
        }
        RadioList list = (RadioList) component;
        RadioButton[] buttons = list.getChildren();
        for (int index = 0; index < buttons.length; index++) {
            String btnName = buttons[index].getText().split("\n", 2)[0];
            if (!name.equals(btnName)) {
                continue;
            }
            list.setCurrent(-1);
            list.setCurrent(index);
            break;
        }
    }

    public void exportProject(Project project) {
        JnaFileChooser chooser = new JnaFileChooser();
        chooser.setMultiSelectionEnabled(false);
        chooser.addFilter("Zip Archive (*.zip)", "zip");
        chooser.showSaveDialog(pane.getInput().getPanel().getFrame());
        File file = chooser.getSelectedFile();
        if (file == null) {
            return;
        }
        Executor.execute(() -> {
            try {
                File folder = java.nio.file.Files.createTempDirectory(Keys.generateKey(24) + "-" + Keys.generateKey(16)).toFile();
                FileUtils.copyDirectory(project.getDirectory(), folder);
                File settings = new File(folder, "settings.json");
                JsonValue<?> value = JsonIO.fromObject(project);
                if (value == null) {
                    FileUtils.deleteDirectory(folder);
                    throw new NullPointerException("JsonValue is null");
                }
                JsonIO.WRITER.toFile(value, settings);
                ZipUtil.zip(file, folder, "bin");
                Desktop.getDesktop().open(file.getParentFile());
                FileUtils.deleteDirectory(folder);
            } catch (IOException exp) {
                System.err.println("Failed to export Project '" + project.getName() + "' to '" + file.getPath() + "'!");
                System.err.println(exp);
            }
        });
    }

    public void importProject() {
        JnaFileChooser chooser = new JnaFileChooser();
        chooser.setMultiSelectionEnabled(false);
        chooser.addFilter("Zip Archive (*.zip)", "zip");
        chooser.showOpenDialog(pane.getInput().getPanel().getFrame());
        File file = chooser.getSelectedFile();
        if (file == null) {
            return;
        }
        Executor.execute(() -> {
            Project project = null;
            File folder = null;
            File settings = null;
            try {
                folder = java.nio.file.Files.createTempDirectory(Keys.generateKey(24) + "-" + Keys.generateKey(16)).toFile();
                ZipUtil.unzip(file, folder);
                settings = new File(folder, "settings.json");
                if (!settings.exists()) {
                    FileUtils.deleteDirectory(folder);
                    throw new IllegalStateException("Settings file doesn't exist!");
                }
                JsonValue<?> value = JsonIO.PARSER.fromFile(settings);
                if (value == null) {
                    FileUtils.deleteDirectory(folder);
                    throw new NullPointerException("JsonValue is null");
                }
                Object object = JsonIO.toObject(value, Project.class);
                if (object == null || !(object instanceof Project)) {
                    FileUtils.deleteDirectory(folder);
                    throw new IllegalStateException("Failed to read settings");
                }
                project = (Project) object;
            } catch (IOException exp) {
                if (folder != null) {
                    FileUtils.deleteDirectory(folder);
                }
                System.err.println("Failed to import '" + file.getPath() + "'!");
                System.err.println(exp);
            }
            if (settings == null || project == null) {
                return;
            }
            JnaFileChooser target = new JnaFileChooser();
            target.setMultiSelectionEnabled(false);
            target.setMode(Mode.Directories);
            target.showOpenDialog(pane.getInput().getPanel().getFrame());
            File output = target.getSelectedFile();
            if (output == null || !output.exists() || !output.isDirectory()) {
                return;
            }
            settings.delete();
            FileUtils.copyDirectory(folder, output);
            FileUtils.deleteDirectory(folder);
            Project.create(project.getName(), project.getPacket(), FileHelper.asRelative(output), null);
            Project tmpProject = Project.get(project.getName());
            if (tmpProject == null) {
                return;
            }
            tmpProject.getOptions().addAll(project.getOptions());
            addProject(tmpProject);
            selectProject(tmpProject.getName());
            Project.setDefault(tmpProject);
            Singleton.get(Settings.class).save();
            tmpProject.open();
        });
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
            if (!project.getDirectory().exists()) {
                Project.PROJECTS.delete(Singleton.get(Settings.class), project.getName());
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
            text.append("\n(").append(Translation.getDefault().translate(type.toLowerCase() + ".name")).append(')');
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
