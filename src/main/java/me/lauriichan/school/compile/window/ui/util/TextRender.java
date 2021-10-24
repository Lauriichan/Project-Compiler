package me.lauriichan.school.compile.window.ui.util;

import java.awt.FontMetrics;

public final class TextRender {

    private final String[] lines;
    private final int[] indices;
    private final FontMetrics metrics;

    public TextRender(String[] lines, int[] indices, FontMetrics metrics) {
        this.lines = lines;
        this.metrics = metrics;
        this.indices = indices;
    }

    public FontMetrics getMetrics() {
        return metrics;
    }

    public int getHeight() {
        return metrics.getHeight();
    }

    public int getLines() {
        return lines.length;
    }

    public int getLineId(int index) {
        if (index <= 0) {
            return 0;
        }
        for (int idx = 0; idx < indices.length; idx++) {
            if (indices[idx] < index) {
                continue;
            }
            return idx;
        }
        return indices.length - 1;
    }

    public int getLineIndex(int line) {
        return (line > indices.length || line < 0) ? -1 : indices[line];
    }

    public String getLine(int line) {
        return (line > lines.length || line < 0) ? null : lines[line];
    }

}
