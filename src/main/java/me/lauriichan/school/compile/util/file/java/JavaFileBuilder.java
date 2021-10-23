package me.lauriichan.school.compile.util.file.java;

import java.io.File;

import me.lauriichan.school.compile.util.file.FileBuilder;

public class JavaFileBuilder extends FileBuilder<JavaFileBuilder> {

    public static final int INDENTION = 4;

    private final String name;

    private int indent = 0;

    public JavaFileBuilder(File path, String name, String extension) {
        super(path, name + '.' + extension);
        this.name = name;
    }

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

    public JavaFileBuilder setPackage(String packet) {
        return add("package ").add(packet).add(';').next().next();
    }

    public JavaFileBuilder normalImport(String className) {
        return add("import ").add(className).add(';').next();
    }

    public JavaFileBuilder normalImport(Class<?> clazz) {
        return normalImport(clazz.getName());
    }

    public JavaFileBuilder wildNormalImport(String packageName) {
        return add("import ").add(packageName).add(".*;").next();
    }

    public JavaFileBuilder wildNormalImport(Package packageObj) {
        return wildNormalImport(packageObj.getName());
    }

    public JavaFileBuilder staticImport(String className, String name) {
        return add("import ").modStatic().add(className).add('.').add(name).add(';').next();
    }

    public JavaFileBuilder staticImport(Class<?> clazz, String name) {
        return staticImport(clazz.getName(), name);
    }

    public JavaFileBuilder wildStaticImport(String className) {
        return staticImport(className, "*");
    }

    public JavaFileBuilder wildStaticImport(Class<?> clazz) {
        return wildStaticImport(clazz.getName());
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

    public JavaFileBuilder ifStatement(String statement) {
        return add("if(").add(statement).add(')').open();
    }

    public JavaFileBuilder keyReturn() {
        return add("return;");
    }

    public JavaFileBuilder keyReturn(String variable) {
        return add("return ").add(variable).add(';');
    }

    public JavaFileBuilder keyBreak() {
        return add("break;");
    }

    public JavaFileBuilder keyContinue() {
        return add("continue;");
    }

    public JavaFileBuilder loopForMax(String name, String target) {
        return add("for(int ").add(name).add(" = 0; ").add(name).add(" < ").add(target).add("; ").add(name).add("++)").open();
    }

    public JavaFileBuilder loopForMin(String name, String start) {
        return add("for(int ").add(name).add(" = ").add(start).add("; ").add(name).add(" >= 0").add("; ").add(name).add("--)").open();
    }

    public JavaFileBuilder loopForMin(String name, String start, String min) {
        return add("for(int ").add(name).add(" = ").add(start).add("; ").add(name).add(" >= ").add(min).add("; ").add(name).add("--)")
            .open();
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

    public JavaFileBuilder variableCallField(Class<?> type, String name, Class<?> clazz, String field, String target, String... params) {
        return varType(type).space().setCallField(name, clazz, field, target, params);
    }

    public JavaFileBuilder variableCall(Class<?> type, String name, Class<?> clazz, String target, String... params) {
        return varType(type).space().setCall(name, clazz, target, params);
    }

    public JavaFileBuilder variableCall(Class<?> type, String name, String varName, String target, String... params) {
        return varType(type).space().setCall(name, varName, target, params);
    }

    public JavaFileBuilder variableSmartIf(Class<?> clazz, String name, String statement, String first, String second) {
        return varType(clazz).space().setSmartIf(name, statement, first, second);
    }

    public JavaFileBuilder variable(Class<?> clazz, String name, String value) {
        return varType(clazz).space().set(name, value);
    }

    public JavaFileBuilder setCallField(String name, Class<?> clazz, String field, String target, String... params) {
        return add(name).add(" = ").callField(clazz, field, target, params);
    }

    public JavaFileBuilder setCall(String name, Class<?> clazz, String target, String... params) {
        return add(name).add(" = ").call(clazz, target, params);
    }

    public JavaFileBuilder setCall(String name, String varName, String target, String... params) {
        return add(name).add(" = ").call(varName, target, params);
    }

    public JavaFileBuilder setSmartIf(String name, String statement, String first, String second) {
        return add(name).add(" = ").add(statement).add(" ? ").add(first).add(" : ").add(second).add(';');
    }

    public JavaFileBuilder set(String name, String value) {
        return add(name).add(" = ").add(value).add(';');
    }

    public JavaFileBuilder construct(Class<?> clazz, String... params) {
        return add("new ").varType(clazz).add('(').params(params).add(");");
    }

    public JavaFileBuilder constructVar(Class<?> clazz, String name, String... params) {
        return varType(clazz).space().add(name).add(" = new ").varType(clazz).add('(').params(params).add(");");
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
