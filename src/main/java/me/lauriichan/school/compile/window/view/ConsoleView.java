package me.lauriichan.school.compile.window.view;

import java.awt.Color;

import me.lauriichan.school.compile.window.ui.BasicPane;
import me.lauriichan.school.compile.window.ui.component.LogDisplay;
import me.lauriichan.school.compile.window.ui.component.TextField;
import me.lauriichan.school.compile.window.ui.util.ColorCache;

public final class ConsoleView extends View {

    public ConsoleView() {
        super("Konsole");
    }

    @Override
    protected void onSetup(BasicPane pane, int width, int height) {
        LogDisplay display = new LogDisplay();
        display.setY(10);
        display.setX(10);
        display.setWidth(width - 20);
        display.setHeight(height - 80);
        display.setBarFill(Color.GREEN);
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
            display.command(text.getContent().trim());
            text.setContent("");
        });
        pane.addChild(field);
    }

}