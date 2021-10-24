package me.lauriichan.school.compile.window.view;

import java.awt.Color;

import com.syntaxphoenix.syntaxapi.utils.java.tools.Container;

import me.lauriichan.school.compile.project.Project;
import me.lauriichan.school.compile.window.ui.BasicPane;
import me.lauriichan.school.compile.window.ui.component.LogDisplay;
import me.lauriichan.school.compile.window.ui.component.TextField;
import me.lauriichan.school.compile.window.ui.util.ColorCache;

public final class ConsoleView extends View<BasicPane> {
    
    public static final Container<LogDisplay> APP_LOG = Container.of();

    public ConsoleView() {
        super("Konsole", new BasicPane());
    }

    @Override
    protected void onSetup(BasicPane pane, int width, int height) {
        LogDisplay display = new LogDisplay();
        display.setY(10);
        display.setX(10);
        display.setWidth(width - 20);
        display.setHeight(height - 60);
        display.setBarFill(Color.GREEN);
        APP_LOG.replace(display);
        pane.addChild(display);

        TextField field = new TextField();
        field.setX(display.getX());
        field.setY(display.getY() + display.getHeight() + 10);
        field.setWidth(display.getWidth());
        field.setHeight(28);
        field.setLimit(107);
        field.setFontName(display.getFontName());
        field.setFontSize(12);
        field.setFontColor(Color.GREEN);
        field.setBlink(Color.GREEN, ColorCache.color("#194719"));
        field.setAction(text -> {
            if (text.getContent().trim().isEmpty()) {
                text.setContent(text.getContent().trim());
                return;
            }
            String command = text.getContent().trim();
            display.command(command);
            text.setContent("");
            Project project = Project.getDefault();
            if(project == null) {
                return;
            }
            project.println(command);
        });
        pane.addChild(field);
    }
    
    @Override
    protected void exit() {
        APP_LOG.replace(null);
    }

}
