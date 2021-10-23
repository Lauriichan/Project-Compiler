package me.lauriichan.school.compile.project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import me.lauriichan.school.compile.data.Category;
import me.lauriichan.school.compile.data.ISetting;
import me.lauriichan.school.compile.data.Serialize;
import me.lauriichan.school.compile.data.Settings;
import me.lauriichan.school.compile.util.Singleton;

public final class Application {

    public static final Category APPLICATIONS = new Category("applications");

    public static String getDefault() {
        ISetting setting = Singleton.get(Settings.class).put(Settings.USER_SETTINGS.of("application", String.class, true));
        if (!setting.isValid()) {
            return "Atom";
        }
        String value = setting.getAs(String.class);
        return value == null ? "Atom" : value;
    }

    public static Application get(String name) {
        ISetting setting = Singleton.get(Settings.class).get(name, APPLICATIONS);
        if (!setting.isValid()) {
            return null;
        }
        return setting.getAs(Application.class);
    }

    public static void put(String name, File executable) {
        Singleton.get(Settings.class).put(APPLICATIONS.of(name, Application.class, true)).set(new Application(executable));
    }

    @Serialize
    private final File executable;
    @Serialize
    private final ArrayList<String> options = new ArrayList<>();

    private Application() {
        this(null);
    }

    private Application(File executable) {
        this.executable = executable;
    }

    public File getExecutable() {
        return executable;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public boolean run(String... arguments) {
        StringBuilder builder = new StringBuilder();
        builder.append('"').append(executable.getPath()).append('"').append(' ');
        if (!options.isEmpty()) {
            for (String option : options) {
                builder.append(option).append(' ');
            }
        }
        for (String argument : arguments) {
            builder.append(argument).append(' ');
        }
        String command = builder.substring(0, builder.length() - 1);
        try {
            Process process = Runtime.getRuntime().exec(command);
            Thread.sleep(1500);
            return process.isAlive();
        } catch (IOException | InterruptedException e) {
            return false;
        }
    }

}
