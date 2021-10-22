package me.lauriichan.school.compile.window.ui.component.goemetry;

import java.awt.Color;

import me.lauriichan.school.compile.window.ui.util.Area;

public final class Rectangle extends Geometry {

    private Color color;

    public Rectangle(Color color) {
        setColor(color);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = (color == null) ? Color.BLACK : color;
    }

    @Override
    public void render(Area area) {
        area.fill(color);
    }

}
