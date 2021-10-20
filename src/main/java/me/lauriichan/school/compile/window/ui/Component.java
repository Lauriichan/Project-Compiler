package me.lauriichan.school.compile.window.ui;

import java.awt.Graphics2D;

import me.lauriichan.school.compile.window.ui.util.Area;
import me.lauriichan.school.compile.window.ui.util.Point;

public abstract class Component {

    private final Point position = new Point(0, 0);
    private final Point size = new Point(0, 0);

    public int getX() {
        return position.getX();
    }

    public void setX(int x) {
        position.setX(x);
    }

    public int getY() {
        return position.getY();
    }

    public void setY(int y) {
        position.setY(y);
    }

    public int getWidth() {
        return size.getX();
    }

    public void setWidth(int width) {
        size.setX(width);
    }

    public int getHeight() {
        return size.getY();
    }

    public void setHeight(int height) {
        size.setY(height);
    }

    protected final void draw(Graphics2D graphics) {
        draw(new Area(graphics, position.getX(), position.getY(), size.getX(), size.getY()));
    }

    protected abstract void draw(Area area);

    protected abstract boolean mouseClick(int x, int y, int button);

}
