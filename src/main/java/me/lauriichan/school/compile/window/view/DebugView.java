package me.lauriichan.school.compile.window.view;

import java.awt.Color;

import me.lauriichan.school.compile.Main;
import me.lauriichan.school.compile.window.ui.BasicPane;
import me.lauriichan.school.compile.window.ui.component.LogDisplay;
import me.lauriichan.school.compile.window.ui.component.TextField;
import me.lauriichan.school.compile.window.ui.util.ColorCache;

public final class DebugView extends View {

    public DebugView() {
        super("Debug Konsole");
    }

    @Override
    protected void onSetup(BasicPane pane, int width, int height) {
        LogDisplay display = new LogDisplay();
        display.setY(10);
        display.setX(10);
        display.setWidth(width - 20);
        display.setHeight(height - 80);
        display.setHistorySize(500);
        display.setBarFill(Color.GREEN);
        pane.addChild(display);

        Main.SYSTEM_OUT.ifPresent(listener -> listener.setDelegate(display::info));
        Main.SYSTEM_ERR.ifPresent(listener -> listener.setDelegate(display::error));
        Main.LOG_FILE.ifPresent(stream -> display.setListener(entry -> stream.println(entry.toString())));

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
        });
        pane.addChild(field);
    }

}
