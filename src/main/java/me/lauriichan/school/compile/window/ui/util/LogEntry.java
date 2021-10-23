package me.lauriichan.school.compile.window.ui.util;

import java.awt.Color;

import com.syntaxphoenix.syntaxapi.utils.java.Times;

public final class LogEntry {

    private final Color color;
    private final String text;

    private final String time;

    public LogEntry(Color color, String text, boolean time) {
        this.color = color;
        this.text = text;
        this.time = time ? "[" + Times.getTime(":") + "] " : "";
    }

    public Color getColor() {
        return color;
    }

    public String getText() {
        return text;
    }

    public String toString() {
        return time + text;
    }

}
