package me.lauriichan.school.compile.exec;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.apache.commons.io.FileUtils;

import com.syntaxphoenix.syntaxapi.utils.java.Exceptions;
import com.syntaxphoenix.syntaxapi.utils.java.Files;

import me.lauriichan.school.compile.Main;
import me.lauriichan.school.compile.project.Project;
import me.lauriichan.school.compile.util.file.FileHelper;
import me.lauriichan.school.compile.window.view.ConsoleView;

public final class ProjectCompiler implements DiagnosticListener<JavaFileObject> {

    private final JavaCompiler compiler;
    private final EnumMap<Kind, ArrayList<Diagnostic<? extends JavaFileObject>>> diagnostics = new EnumMap<>(Kind.class);
    private final Function<Kind, ArrayList<Diagnostic<? extends JavaFileObject>>> function = (ignore) -> new ArrayList<>();

    private boolean compiles = false;
    private Consumer<ProjectCompiler> diagnosticUpdate;

    private CompilationTask task;

    public ProjectCompiler() {
        this.compiler = ToolProvider.getSystemJavaCompiler();
    }

    public boolean isCompiling() {
        return compiles;
    }

    public void compile(Project project) {
        if (compiles) {
            ConsoleView.APP_LOG.ifPresent(log -> {
                log.warn("Der Compiler ist derzeit beschäftigt!");
            });
            Main.SELECT.ifPresent(consumer -> consumer.accept(3));
            return;
        }
        compiles = true;
        long hash = project.hash();
        project.renewHash();
        if (hash == project.hash()) {
            compiles = false;
            ConsoleView.APP_LOG.ifPresent(log -> {
                log.warn("Das Projekt '" + project.getName() + "' ist bereits auf dem neusten Stand!");
            });
            Main.SELECT.ifPresent(consumer -> consumer.accept(3));
            return;
        }
        for (Kind kind : Kind.values()) {
            diagnostics.computeIfAbsent(kind, function).clear();
        }
        File directory = project.getDirectory();
        File source = new File(directory, "src");
        File resource = new File(directory, "resources");
        File srcManifest = new File(directory, "MANIFEST.MF");

        File bin = new File(directory, "bin");
        FileHelper.deleteIfExists(bin);
        Files.createFolder(bin);

        File manifest = new File(bin, "MANIFEST.MF");
        try {
            FileUtils.copyFile(srcManifest, manifest);
        } catch (IOException e1) {
            System.err.println("Failed to copy manifest of '" + project.getName() + "'!");
            System.err.println(Exceptions.stackTraceToString(e1));
            compiles = false;
            ConsoleView.APP_LOG.ifPresent(log -> {
                log.error("Das Projekt '" + project.getName() + "' konnte nicht kompiliert werden!");
                log.error("Für mehr Informationen schau in den 'Probleme' Tab!");
            });
            Main.SELECT.ifPresent(consumer -> consumer.accept(3));
            return;
        }

        File resources = new File(bin, "resources");
        Files.createFolder(resources);

        try {
            FileUtils.copyDirectory(resource, resources);
        } catch (IOException e) {
            System.err.println("Failed to copy resources of '" + project.getName() + "'!");
            System.err.println(Exceptions.stackTraceToString(e));
            compiles = false;
            ConsoleView.APP_LOG.ifPresent(log -> {
                log.error("Das Projekt '" + project.getName() + "' konnte nicht kompiliert werden!");
                log.error("Für mehr Informationen schau in den 'Probleme' Tab!");
            });
            Main.SELECT.ifPresent(consumer -> consumer.accept(3));
            return;
        }

        File classes = new File(bin, "classes");
        Files.createFolder(classes);

        Collection<File> javaFiles = FileUtils.listFiles(source, new String[] {
            "java"
        }, true);

        StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, StandardCharsets.UTF_8);
        Iterable<? extends JavaFileObject> iterable = manager.getJavaFileObjectsFromFiles(javaFiles);

        try {
            task = compiler.getTask(null, manager, this, Arrays.asList("-d", classes.getPath()), null, iterable);
            task.setLocale(Locale.GERMAN);
            if (!task.call()) {
                compiles = false;
                ConsoleView.APP_LOG.ifPresent(log -> {
                    log.error("Das Projekt '" + project.getName() + "' konnte nicht kompiliert werden!");
                    log.error("Für mehr Informationen schau in den 'Probleme' Tab!");
                });
                Main.SELECT.ifPresent(consumer -> consumer.accept(3));
                task = null;
                if (diagnosticUpdate != null) {
                    diagnosticUpdate.accept(this);
                }
                return;
            }
            task = null;
            if (diagnosticUpdate != null) {
                diagnosticUpdate.accept(this);
            }
        } catch (Exception exp) {
            compiles = false;
            ConsoleView.APP_LOG.ifPresent(log -> {
                log.error("Das Projekt '" + project.getName() + "' konnte nicht kompiliert werden!");
                log.error("Für mehr Informationen schau in den 'Probleme' Tab!");
            });
            Main.SELECT.ifPresent(consumer -> consumer.accept(3));
            System.err.println("Failed to compile '" + project.getName() + "'!");
            System.err.println(Exceptions.stackTraceToString(exp));
            if (diagnosticUpdate != null) {
                diagnosticUpdate.accept(this);
            }
            return;
        }
        try {
            manager.close();
        } catch (IOException exp) {
            System.err.println("Failed to close StandardJavaFileManager");
            System.err.println(Exceptions.stackTraceToString(exp));
        }

        Manifest manifestObj = new Manifest();
        try (FileInputStream stream = new FileInputStream(manifest)) {
            manifestObj.read(stream);
        } catch (Exception exp) {
            System.err.println("Failed to read MANIFEST of '" + project.getName() + "'!");
            System.err.println(Exceptions.stackTraceToString(exp));
            compiles = false;
            ConsoleView.APP_LOG.ifPresent(log -> {
                log.error("Das Projekt '" + project.getName() + "' konnte zu einem Jar Archiv gebaut werden!");
                log.error("Für mehr Informationen schau in die 'Debug Konsole'!");
            });
            Main.SELECT.ifPresent(consumer -> consumer.accept(3));
            return;
        }

        File jarFile = new File(bin, "main.jar");
        Files.createFile(jarFile);

        try (JarOutputStream stream = new JarOutputStream(new FileOutputStream(jarFile), manifestObj)) {
            for (File file : classes.listFiles()) {
                addRootEntry(classes, file, stream);
            }
            for (File file : resources.listFiles()) {
                addRootEntry(resources, file, stream);
            }
        } catch (Exception exp) {
            System.err.println("Failed to create Jar Archive of '" + project.getName() + "'!");
            System.err.println(Exceptions.stackTraceToString(exp));
            compiles = false;
            ConsoleView.APP_LOG.ifPresent(log -> {
                log.error("Das Projekt '" + project.getName() + "' konnte zu einem Jar Archiv gebaut werden!");
                log.error("Für mehr Informationen schau in die 'Debug Konsole'!");
            });
            Main.SELECT.ifPresent(consumer -> consumer.accept(3));
            return;
        }
        compiles = false;
        ConsoleView.APP_LOG.ifPresent(log -> {
            log.info("Das Projekt '" + project.getName() + "' wurde erfolgreich kompiliert!");
        });
        Main.SELECT.ifPresent(consumer -> consumer.accept(3));
    }

    private void addRootEntry(File workFolder, File target, JarOutputStream stream) throws IOException {
        int length = workFolder.getPath().replace("\\", "/").length() + 1;
        if (target.isDirectory()) {
            String name = target.getPath().replace("\\", "/").substring(length);
            if (name.isEmpty()) {
                return;
            }
            if (!name.endsWith("/")) {
                name += "/";
            }
            JarEntry entry = new JarEntry(name);
            entry.setTime(target.lastModified());
            stream.putNextEntry(entry);
            stream.closeEntry();
            for (File file : target.listFiles()) {
                addEntry(file, length, stream);
            }
            return;
        }
        JarEntry entry = new JarEntry(target.getPath().replace("\\", "/").substring(length));
        entry.setTime(target.lastModified());
        stream.putNextEntry(entry);
        try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(target))) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = input.read(buffer)) != -1) {
                stream.write(buffer, 0, read);
            }
        }
        stream.closeEntry();
    }

    private void addEntry(File target, int length, JarOutputStream stream) throws IOException {
        if (target.isDirectory()) {
            String name = target.getPath().replace("\\", "/").substring(length);
            if (name.isEmpty()) {
                return;
            }
            if (!name.endsWith("/")) {
                name += "/";
            }
            JarEntry entry = new JarEntry(name);
            entry.setTime(target.lastModified());
            stream.putNextEntry(entry);
            stream.closeEntry();
            for (File file : target.listFiles()) {
                addEntry(file, length, stream);
            }
            return;
        }
        JarEntry entry = new JarEntry(target.getPath().replace("\\", "/").substring(length));
        entry.setTime(target.lastModified());
        stream.putNextEntry(entry);
        try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(target))) {
            byte[] buffer = new byte[1024];
            int read;
            while ((read = input.read(buffer)) != -1) {
                stream.write(buffer, 0, read);
            }
        }
        stream.closeEntry();
    }

    public void setDiagnosticUpdate(Consumer<ProjectCompiler> diagnosticUpdate) {
        this.diagnosticUpdate = diagnosticUpdate;
    }

    public Consumer<ProjectCompiler> getDiagnosticUpdate() {
        return diagnosticUpdate;
    }

    public EnumMap<Kind, ArrayList<Diagnostic<? extends JavaFileObject>>> getDiagnostics() {
        return diagnostics;
    }

    @SuppressWarnings("unchecked")
    public Diagnostic<? extends JavaFileObject>[] getDiagnosticsOfKind(Kind kind) {
        ArrayList<Diagnostic<? extends JavaFileObject>> list = diagnostics.get(kind);
        return list.toArray(new Diagnostic[list.size()]);
    }

    @Override
    public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
        diagnostics.get(diagnostic.getKind()).add(diagnostic);
    }

    public void exit() {

    }

}
