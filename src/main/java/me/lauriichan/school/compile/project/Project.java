package me.lauriichan.school.compile.project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import com.syntaxphoenix.syntaxapi.utils.java.Files;

import me.lauriichan.school.compile.data.Category;
import me.lauriichan.school.compile.data.ISetting;
import me.lauriichan.school.compile.data.Serialize;
import me.lauriichan.school.compile.data.Settings;
import me.lauriichan.school.compile.project.template.Template;
import me.lauriichan.school.compile.util.Singleton;
import me.lauriichan.school.compile.util.UserSettings;

public final class Project {

    public static final Category PROJECTS = new Category("projects");

    public static String getDefaultName() {
        return UserSettings.getString("project");
    }

    public static void setDefaultName(String name) {
        UserSettings.setString("project", name);
    }

    public static Project getDefault() {
        String name = getDefaultName();
        return name.isEmpty() ? null : get(name);
    }

    public static void setDefault(Project project) {
        setDefaultName(project.getName());
    }

    public static Project get(String name) {
        ISetting setting = Singleton.get(Settings.class).get(name, PROJECTS.getName());
        if (!setting.isValid()) {
            return null;
        }
        return setting.getAs(Project.class);
    }

    public static void create(String name, String packet, File directory, Template template) {
        Project project = new Project(name, packet, directory);
        Singleton.get(Settings.class).put(PROJECTS.of(name, Project.class, true)).set(project);
        Files.createFolder(directory);
        if (template == null) {
            return;
        }
        project.getOptions().add("-t " + template.getType().toLowerCase());
        template.setup(packet, directory);
    }

    @Serialize
    private final String name;
    @Serialize
    private final String packet;
    @Serialize
    private final File directory;
    @Serialize
    private final ArrayList<String> options = new ArrayList<>();
    @Serialize
    private long hash;

    protected Project() {
        this(null, null, null);
    }

    private Project(String name, String packet, File directory) {
        this.directory = directory;
        this.name = name;
        this.packet = packet;
    }

    public String getName() {
        return name;
    }

    public File getDirectory() {
        return directory;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void open() {
        open(Application.getDefault());
    }

    public void open(String applicationName) {
        open(Application.get(applicationName));
    }

    public void open(Application application) {
        if (application == null) {
            return;
        }
        application.run('"' + directory.getAbsolutePath() + '"');
    }

    public boolean isCompileable() {
        return options.contains("-t java");
    }

    public long hash() {
        return hash;
    }

    public void renewHash() {
        if (!isCompileable()) {
            return;
        }
        try {
            hash = FileUtils.checksumCRC32(directory);
        } catch (IOException e) {
            return;
        }
    }

    public void println(String line) {

    }

    public boolean compile() {
        return false;
    }

    public boolean execute() {
        return false;
    }

}
