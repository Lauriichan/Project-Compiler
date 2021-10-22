package me.lauriichan.school.compile.window.ui;

import java.awt.Color;
import java.util.Iterator;

import me.lauriichan.school.compile.util.ArrayIterator;
import me.lauriichan.school.compile.window.input.InputProvider;
import me.lauriichan.school.compile.window.ui.util.Area;

public abstract class Bar<E extends IComponent> implements Iterable<E> {

    private Component parent;

    private int height = 0;
    private InputProvider input;

    final void setInput(Component component) {
        if (component == null) {
            return;
        }
        if (component.getInput() == null) {
            throw new IllegalArgumentException("Component isn't initialised (no InputProvider)");
        }
        input = component.getInput();
        input.register(this);
        parent = component;
    }

    public InputProvider getInput() {
        return input;
    }

    public boolean hasParent() {
        return parent != null;
    }

    public Component getParent() {
        return parent;
    }

    public boolean hasContainer() {
        return hasParent() ? parent.hasContainer() : false;
    }

    public Component getContainer() {
        return hasParent() ? parent.getContainer() : null;
    }

    public boolean hasRoot() {
        return hasParent() ? parent.hasRoot() : false;
    }

    public Component getRoot() {
        return hasParent() ? parent.getRoot() : null;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public abstract void setBackground(Color color);

    public abstract boolean add(E component);

    public abstract boolean remove(E component);

    public abstract int getCount();

    public abstract E get(int index);

    public abstract E[] getAll();

    @Override
    public Iterator<E> iterator() {
        return new ArrayIterator<>(getAll());
    }

    public void render(Area area) {}

    public void update(long deltaTime) {}

}
