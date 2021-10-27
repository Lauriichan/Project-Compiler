package me.lauriichan.school.compile.project;

import java.awt.Desktop;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.io.FileUtils;

import com.syntaxphoenix.syntaxapi.utils.java.Exceptions;
import com.syntaxphoenix.syntaxapi.utils.java.Files;

import me.lauriichan.school.compile.Main;
import me.lauriichan.school.compile.data.Category;
import me.lauriichan.school.compile.data.ISetting;
import me.lauriichan.school.compile.data.Serialize;
import me.lauriichan.school.compile.data.Settings;
import me.lauriichan.school.compile.data.translation.Translation;
import me.lauriichan.school.compile.exec.ProjectCompiler;
import me.lauriichan.school.compile.project.template.Template;
import me.lauriichan.school.compile.util.Executor;
import me.lauriichan.school.compile.util.Singleton;
import me.lauriichan.school.compile.util.UserSettings;
import me.lauriichan.school.compile.util.io.InputStreamListener;
import me.lauriichan.school.compile.window.view.ConsoleView;

public final class Project {

    public static final Category PROJECTS = new Category("projects");

    private static boolean RUNNING = false;
    private static boolean ABORT = false;

    public static boolean isRunning() {
        return RUNNING;
    }

    public static void abort() {
        ABORT = true;
    }

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

    private long hash;

    private PrintStream stream;
    private InputStreamListener normalListener;
    private InputStreamListener errorListener;

    protected Project() {
        this(null, null, null);
    }

    private Project(String name, String packet, File directory) {
        this.name = name;
        this.directory = directory;
        this.packet = packet;
    }

    public String getName() {
        return name;
    }

    public String getPacket() {
        return packet;
    }

    public File getDirectory() {
        return directory;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void openDirectory() {
        try {
            Desktop.getDesktop().open(directory);
        } catch (IOException e) {
            System.err.println("Failed to open project folder '" + name + "'!");
            System.err.println(Exceptions.stackTraceToString(e));
        }
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
        File source = new File(directory, "src");
        File resource = new File(directory, "resources");
        Collection<File> collection = FileUtils.listFiles(source, new String[] {
            "java"
        }, true);
        collection.addAll(Files.listFiles(resource));
        long hash = 0;
        for (File file : collection) {
            try {
                hash += FileUtils.checksumCRC32(file);
            } catch (Exception e) {
                System.err.println("Failed to get checksum of '" + file.getPath() + "' while getting hash for '" + name + "'!");
                System.err.println(e);
            }
        }
        this.hash = hash;
    }

    public void println(String line) {
        if (stream == null) {
            return;
        }
        stream.println(line);
        stream.flush();
    }

    public boolean compile() {
        if (!isCompileable()) {
            ConsoleView.APP_LOG.ifPresent(log -> {
                Translation translation = Translation.getDefault();
                log.warn(translation.translate("message.header.project.compile", new String[][] {
                    {
                        "name",
                        name
                    }
                }));
                log.warn(translation.translate("message.project.compile.incompatible"));
            });
            Main.SELECT.ifPresent(consumer -> consumer.accept(3));
            return false;
        }
        ProjectCompiler compiler = Singleton.get(ProjectCompiler.class);
        if (compiler.isCompiling()) {
            ConsoleView.APP_LOG.ifPresent(log -> {
                Translation translation = Translation.getDefault();
                log.warn(translation.translate("message.header.project.compile", new String[][] {
                    {
                        "name",
                        name
                    }
                }));
                log.warn(translation.translate("message.project.compile.running"));
            });
            Main.SELECT.ifPresent(consumer -> consumer.accept(3));
            return false;
        }
        Executor.execute(() -> compiler.compile(this));
        return true;
    }

    public boolean execute() {
        if (RUNNING) {
            ConsoleView.APP_LOG.ifPresent(log -> {
                Translation translation = Translation.getDefault();
                log.warn(translation.translate("message.header.project.execute", new String[][] {
                    {
                        "name",
                        name
                    }
                }));
                log.warn(translation.translate("message.project.execute.running"));
            });
            Main.SELECT.ifPresent(consumer -> consumer.accept(3));
            return false;
        }
        if (UserSettings.getString("java").isEmpty()) {
            ConsoleView.APP_LOG.ifPresent(log -> log.error(Translation.getDefault().translate("message.project.execute.java_required")));
            Main.SELECT.ifPresent(consumer -> consumer.accept(3));
            return false;
        }
        File file = new File(directory, "bin/main.jar");
        if (!file.exists()) {
            ConsoleView.APP_LOG.ifPresent(log -> {
                Translation translation = Translation.getDefault();
                log.warn(translation.translate("message.header.project.execute", new String[][] {
                    {
                        "name",
                        name
                    }
                }));
                log.warn(translation.translate("message.project.execute.not_compiled"));
            });
            Main.SELECT.ifPresent(consumer -> consumer.accept(3));
            return false;
        }
        ABORT = false;
        RUNNING = true;
        Executor.execute(() -> {
            Main.SELECT.ifPresent(consumer -> consumer.accept(3));
            ConsoleView.APP_LOG.ifPresent(log -> log.warn(Translation.getDefault().translate("message.project.execute.started",
                new String[][] {
                    {
                        "name",
                        name
                }
            })));
            Process process = Runtime.getRuntime().exec('"' + UserSettings.getString("java") + "\" -jar \"" + file.getPath() + '"');
            stream = new PrintStream(new BufferedOutputStream(process.getOutputStream()));
            normalListener = new InputStreamListener(process.getInputStream(), name + "-Out");
            normalListener.setDelegate(ConsoleView.APP_LOG.get()::info);
            errorListener = new InputStreamListener(process.getErrorStream(), name + "-Err");
            errorListener.setDelegate(ConsoleView.APP_LOG.get()::error);
            while (process.isAlive()) {
                if (ABORT) {
                    break;
                }
                Thread.sleep(50);
            }
            if (process.isAlive()) {
                ConsoleView.APP_LOG.ifPresent(log -> log.warn(Translation.getDefault().translate("message.project.execute.aborting",
                    new String[][] {
                        {
                            "name",
                            name
                    }
                })));
                Main.SELECT.ifPresent(consumer -> consumer.accept(3));
                process.destroy();
                Thread.sleep(300);
                if (process.isAlive()) {
                    process.destroyForcibly();
                }
                ConsoleView.APP_LOG.ifPresent(log -> log.warn(Translation.getDefault().translate("message.project.execute.aborted",
                    new String[][] {
                        {
                            "name",
                            name
                    }
                })));
            } else {
                ConsoleView.APP_LOG.ifPresent(log -> log.warn(Translation.getDefault().translate("message.project.execute.stopped",
                    new String[][] {
                        {
                            "name",
                            name
                    }
                })));
                Main.SELECT.ifPresent(consumer -> consumer.accept(3));
            }
            stream.close();
            stream = null;
            normalListener.close();
            normalListener = null;
            errorListener.close();
            errorListener = null;
            ABORT = false;
            RUNNING = false;
        });
        return true;
    }

}
