package me.lauriichan.school.compile.window.ui.component.goemetry;

import java.awt.Color;

import me.lauriichan.school.compile.window.ui.util.Area;

public final class LineSeperator extends Seperator {

    private Color color;
    private int thickness = 2;

    public LineSeperator() {
        super();
    }

    public LineSeperator(boolean vertical) {
        super(vertical);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = (color == null) ? Color.BLACK : color;
    }

    public int getThickness() {
        return thickness;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    @Override
    public void render(Area area) {
        if (isVertical()) {
            area.drawLine(0, area.getHeight() / 2, area.getWidth(), area.getHeight() / 2, thickness);
            return;
        }
        area.drawLine(area.getWidth() / 2, 0, area.getWidth() / 2, area.getHeight(), thickness);
    }

}
