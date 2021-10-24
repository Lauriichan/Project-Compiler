package me.lauriichan.school.compile.exec;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.function.Function;

import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;
import javax.tools.DiagnosticListener;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import org.apache.commons.io.FileUtils;

import com.syntaxphoenix.syntaxapi.utils.java.Exceptions;
import com.syntaxphoenix.syntaxapi.utils.java.Files;

import me.lauriichan.school.compile.project.Project;
import me.lauriichan.school.compile.util.file.FileHelper;

public final class ProjectCompiler implements DiagnosticListener<JavaFileObject> {

    private final JavaCompiler compiler;
    private final EnumMap<Kind, ArrayList<Diagnostic<? extends JavaFileObject>>> diagnostics = new EnumMap<>(Kind.class);
    private final Function<Kind, ArrayList<Diagnostic<? extends JavaFileObject>>> function = (ignore) -> new ArrayList<>();

    private boolean compiles = false;

    public ProjectCompiler() {
        this.compiler = ToolProvider.getSystemJavaCompiler();
    }

    public boolean isCompiling() {
        return compiles;
    }

    public void compile(Project project) {
        if (!project.isCompileable() || compiles) {
            return;
        }
        compiles = true;
        long hash = project.hash();
        project.renewHash();
        if (hash == project.hash()) {
            compiles = false;
            return;
        }
        for (Kind kind : Kind.values()) {
            diagnostics.computeIfAbsent(kind, function).clear();
        }
        File directory = project.getDirectory();
        File source = new File(directory, "src");
        File resource = new File(directory, "resources");

        File bin = new File(directory, "bin");
        FileHelper.deleteIfExists(bin);
        Files.createFolder(bin);

        File resources = new File(bin, "resources");
        Files.createFolder(resources);

        try {
            FileUtils.copyDirectory(resource, resources);
        } catch (IOException e) {
            System.err.println("Failed to copy resources of '" + project.getName() + "'!");
            System.err.println(Exceptions.stackTraceToString(e));
        }

        File classes = new File(bin, "classes");
        Files.createFolder(classes);

        Collection<File> javaFiles = FileUtils.listFiles(source, new String[] {
            ".java"
        }, true);

        StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, StandardCharsets.UTF_8);
        Iterable<? extends JavaFileObject> iterable = manager.getJavaFileObjectsFromFiles(javaFiles);

        try {
            compiler.getTask(null, manager, null, null, null, iterable);
        } catch (Exception exp) {
            compiles = false;
            System.err.println("Failed to compile '" + project.getName() + "'!");
            System.err.println(Exceptions.stackTraceToString(exp));
            return;
        }
        try {
            manager.close();
        } catch (IOException exp) {
            System.err.println("Failed to close StandardJavaFileManager");
            System.err.println(Exceptions.stackTraceToString(exp));
        }

    }

    public void exit() {

    }

    @Override
    public void report(Diagnostic<? extends JavaFileObject> diagnostic) {
        diagnostics.get(diagnostic.getKind()).add(diagnostic);
    }

}
