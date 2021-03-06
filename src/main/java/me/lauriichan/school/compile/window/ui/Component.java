package me.lauriichan.school.compile.window.ui;

import me.lauriichan.school.compile.window.input.InputProvider;
import me.lauriichan.school.compile.window.ui.util.Area;
import me.lauriichan.school.compile.window.util.Point;

public abstract class Component implements IComponent {

    private final Point position = new Point(0, 0);
    private final Point size = new Point(0, 0);

    private boolean hidden = false;
    private boolean update = true;

    private Component parent;
    private InputProvider input;

    public void setInput(Component component) {
        if (isRoot()) {
            throw new IllegalStateException("Can't set InputProvider to root component!");
        }
        if (component == null) {
            input.unregister(this);
            input = null;
            parent = null;
            return;
        }
        if (component.getInput() == null) {
            throw new IllegalArgumentException("Component isn't initialised (no InputProvider)");
        }
        parent = component;
        input = component.getInput();
        input.register(this);
    }

    public InputProvider getInput() {
        return input;
    }

    public boolean isRoot() {
        return false;
    }

    public boolean isContainer() {
        return false;
    }

    public Component getContainer() {
        if (isContainer()) {
            return this;
        }
        return hasParent() ? parent.getContainer() : null;
    }

    public boolean hasContainer() {
        return getContainer() != null;
    }

    public Component getParent() {
        return parent;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public Component getRoot() {
        if (isRoot()) {
            return this;
        }
        return hasParent() ? parent.getRoot() : null;
    }

    public boolean hasRoot() {
        return getRoot() != null;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isUpdating() {
        return update;
    }

    public void setUpdating(boolean update) {
        this.update = update;
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

    public int getGlobalX() {
        return hasParent() ? parent.getGlobalX() + position.getX() : position.getX();
    }

    public int getGlobalY() {
        return hasParent() ? parent.getGlobalY() + position.getY() : position.getY();
    }

    public boolean isInside(int x, int y) {
        int gx = getGlobalX();
        int gy = getGlobalY();
        return gx <= x && gx + size.getX() >= x && gy <= y && gy + size.getY() >= y;
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
    
    public void updateChildren() {}
    
    public void updateChildren(int width, int height) {}

    public void render(Area area) {}

    public void update(long deltaTime) {}

}
