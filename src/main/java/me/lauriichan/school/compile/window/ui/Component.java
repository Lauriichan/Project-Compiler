package me.lauriichan.school.compile.window.ui;

import me.lauriichan.school.compile.window.ui.input.InputProvider;
import me.lauriichan.school.compile.window.ui.util.Area;
import me.lauriichan.school.compile.window.ui.util.Point;

public abstract class Component {

    private final Point position = new Point(0, 0);
    private final Point size = new Point(0, 0);

    private boolean hidden = false;

    private InputProvider input;

    protected final void setInput(Component component) {
        if (isRoot()) {
            throw new IllegalStateException("Can't set InputProvider to root component!");
        }
        if (component == null) {
            input = null;
            return;
        }
        if (component.getInput() == null) {
            throw new IllegalArgumentException("Component isn't initialised (no InputProvider)");
        }
        input = component.getInput();
    }

    public InputProvider getInput() {
        return input;
    }

    public boolean isRoot() {
        return false;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

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

    public void setPosition(int x, int y) {
        setX(x);
        setY(y);
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

    public void setSize(int width, int height) {
        setWidth(width);
        setHeight(height);
    }

    protected void render(Area area) {}

    protected void update(long deltaTime) {}

}
