package me.lauriichan.school.compile.window.view;

import static me.lauriichan.school.compile.window.ui.util.ColorCache.color;

import java.awt.Color;
import java.awt.Font;
import java.util.EnumMap;
import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.Diagnostic.Kind;

import javax.tools.JavaFileObject;

import me.lauriichan.school.compile.exec.ProjectCompiler;
import me.lauriichan.school.compile.util.Singleton;
import me.lauriichan.school.compile.window.ui.BasicPane;
import me.lauriichan.school.compile.window.ui.ListPane;
import me.lauriichan.school.compile.window.ui.component.Label;
import me.lauriichan.school.compile.window.ui.component.goemetry.Rectangle;

public final class CompileView extends View<ListPane> {

    private final EnumMap<Kind, Color> colorMap = new EnumMap<>(Kind.class);

    public CompileView() {
        super("Probleme (0)", new ListPane());
        Singleton.get(ProjectCompiler.class).setDiagnosticUpdate(this::update);
    }

    @Override
    protected void onSetup(ListPane pane, int width, int height) {
        pane.setComponentOffset(8);
        pane.setComponentHeight(120);
        pane.setScrollMaxSpeed(1200);
        pane.setScrollSpeed(11);
        colorMap.put(Kind.ERROR, color("#A11E0D"));
        colorMap.put(Kind.WARNING, color("#F19E2E"));
        colorMap.put(Kind.MANDATORY_WARNING, color("#E2CE29"));
        colorMap.put(Kind.NOTE, color("#BBE7AA"));
        colorMap.put(Kind.OTHER, color("#C6C7C6"));
    }

    private void update(ProjectCompiler compiler) {
        if (!isSetup()) {
            return;
        }
        pane.clear();
        int problems = 0;
        for (Kind kind : Kind.values()) {
            Diagnostic<? extends JavaFileObject>[] diagnostics = compiler.getDiagnosticsOfKind(kind);
            for (int index = 0; index < diagnostics.length; index++) {
                build(diagnostics[index]);
            }
            problems += diagnostics.length;
        }
        setTitle("Probleme (" + problems + ")");
    }

    private void build(Diagnostic<? extends JavaFileObject> diagnostic) {
        BasicPane base = new BasicPane();
        base.setX(10);
        base.setWidth(pane.getWidth() - 20);
        pane.addChild(base);

        Rectangle background = new Rectangle(color("#6D6D6D"));
        background.setWidth(base.getWidth());
        background.setHeight(base.getHeight());
        base.addChild(background);

        Label type = new Label();
        type.setX(10);
        type.setText(diagnostic.getKind().name().replace('_', ' '));
        type.setFontSize(16);
        type.setFontColor(colorMap.get(diagnostic.getKind()));
        type.setFontStyle(Font.BOLD);
        type.setSize(base.getWidth() / 3, type.getFontSize() * 2);
        base.addChild(type);

        Label file = new Label();
        file.setX(10);
        file.setY(24);
        file.setText(diagnostic.getSource().getName() + ".java in Zeile " + diagnostic.getLineNumber() + " Position "
            + diagnostic.getColumnNumber());
        file.setSize(base.getWidth(), file.getFontSize() * 2);
        base.addChild(file);

        Label description = new Label();
        description.setX(10);
        description.setY(48);
        description.setTextOffset(-2);
        description.setMultilineAllowed(true);
        description.setText(diagnostic.getMessage(Locale.GERMAN));
        description.setSize(base.getWidth() - description.getX() * 2, base.getHeight() - file.getY() - 10);
        base.addChild(description);
    }

}
