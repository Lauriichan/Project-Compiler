package me.lauriichan.school.compile.project;

import java.io.File;
import java.util.ArrayList;

import com.syntaxphoenix.syntaxapi.utils.java.Files;

import me.lauriichan.school.compile.data.Category;
import me.lauriichan.school.compile.data.ISetting;
import me.lauriichan.school.compile.data.Serialize;
import me.lauriichan.school.compile.data.Settings;
import me.lauriichan.school.compile.project.template.Template;
import me.lauriichan.school.compile.util.Singleton;

public final class Project {

    public static final Category PROJECTS = new Category("projects");

    public static String getDefaultName() {
        ISetting setting = Singleton.get(Settings.class).get("project", Settings.USER_SETTINGS);
        if (!setting.isValid()) {
            return null;
        }
        return setting.getAs(String.class);
    }

    public static void setDefaultName(String name) {
        Singleton.get(Settings.class).put(Settings.USER_SETTINGS.of("project", String.class, true)).set(name);
    }

    public static Project getDefault() {
        String name = getDefaultName();
        return name == null ? null : get(name);
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
        Singleton.get(Settings.class).put(PROJECTS.of(name, Project.class, true)).set(new Project(name, packet, directory));
        Files.createFolder(directory);
        if (template == null) {
            return;
        }
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
        Application application = Application.get(applicationName);
        if (application == null) {
            return;
        }
        application.run('"' + directory.getAbsolutePath() + '"');
    }

    public boolean compile() {
        return false;
    }

    public boolean execute() {
        return false;
    }

}
