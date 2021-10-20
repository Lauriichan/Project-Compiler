package me.lauriichan.school.compile.util.file.java;

import java.io.File;

import me.lauriichan.school.compile.util.file.FileBuilder;

public class JavaFileBuilder extends FileBuilder<JavaFileBuilder> {

    public static final int INDENTION = 4;

    private final String name;

    private int indent = 0;

    public JavaFileBuilder(File path, String name) {
        super(path, name + ".java");
        this.name = name;
    }

    public JavaFileBuilder(File file) {
        super(file);
        this.name = file.getName().split("\\.", 2)[0];
    }

    @Override
    protected JavaFileBuilder instance() {
        return this;
    }

    public JavaFileBuilder indent() {
        if (indent == 0) {
            return this;
        }
        StringBuilder builder = new StringBuilder();
        for (int x = 0; x < indent; x++) {
            builder.append(' ');
        }
        return add(builder.toString());
    }

    public JavaFileBuilder name() {
        return add(name);
    }

    @Override
    public JavaFileBuilder next() {
        return super.next().indent();
    }

    public JavaFileBuilder modFinal() {
        return add("final ");
    }

    public JavaFileBuilder modStatic() {
        return add("static ");
    }

    public JavaFileBuilder normalImport(Class<?> clazz) {
        return add("import ").add(clazz.getName()).add(';').next();
    }

    public JavaFileBuilder setPackage(String packet) {
        return add("package ").add(packet).add(';').next();
    }

    public JavaFileBuilder wildImport(Class<?> clazz) {
        return add("import ").modStatic().add(clazz.getName()).add(".*;").next();
    }

    public JavaFileBuilder wildImport(Package packet) {
        return add("import ").add(packet.getName()).add(".*;").next();
    }

    public JavaFileBuilder security(Security security) {
        return add(security.asName()).add(' ');
    }

    private JavaFileBuilder typeImpl(Class<?> clazz) {
        if (Void.class == clazz) {
            return add("void");
        }
        String type = clazz.getTypeName();
        if (type.contains(".")) {
            String[] tmp = type.split("\\.");
            type = tmp[tmp.length - 1];
        }
        return add(type);
    }

    public JavaFileBuilder type(Class<?> clazz) {
        return typeImpl(clazz).add(' ');
    }

    public JavaFileBuilder varType(Class<?> clazz) {
        if (Void.class == clazz) {
            throw new IllegalArgumentException("Void can't be a var type");
        }
        return typeImpl(clazz);
    }

    public JavaFileBuilder param(Class<?> clazz, String name) {
        return varType(clazz).space().add(name);
    }

    public JavaFileBuilder paramNx(Class<?> clazz, String name) {
        return param(clazz, name).add(", ");
    }

    public JavaFileBuilder params(String... params) {
        if (params.length == 0) {
            return this;
        }
        StringBuilder builder = new StringBuilder();
        for (String param : params) {
            builder.append(param).append(", ");
        }
        return add(builder.substring(0, builder.length() - 2));
    }

    public JavaFileBuilder loopForMax(String name, String target) {
        return add("for(int ").add(name).add(" = 0; ").add(name).add(" < ").add(target).add("; ").add(name).add("(++) ").open();
    }

    public JavaFileBuilder loopForEach(Class<?> clazz, String name, String target) {
        return add("for(").param(clazz, name).add(" : ").add(target).add(") ").open();
    }

    public JavaFileBuilder call(String name, String target, String... params) {
        return add(name).add('.').add(target).add('(').params(params).add(");");
    }

    public JavaFileBuilder call(Class<?> clazz, String target, String... params) {
        return varType(clazz).add('.').add(target).add('(').params(params).add(");");
    }

    public JavaFileBuilder callField(Class<?> clazz, String name, String target, String... params) {
        return field(clazz, name).add('.').add(target).add('(').params(params).add(");");
    }

    public JavaFileBuilder field(Class<?> clazz, String name) {
        return varType(clazz).add('.').add(name);
    }

    public JavaFileBuilder open() {
        indent += INDENTION;
        return add(" {").next();
    }

    public JavaFileBuilder close() {
        indent -= INDENTION;
        return next().add('}');
    }

}
