package me.lauriichan.school.compile.window.ui;

import java.awt.Color;
import java.util.Iterator;

import me.lauriichan.school.compile.util.ArrayIterator;
import me.lauriichan.school.compile.window.input.InputProvider;

public abstract class Bar<E extends IComponent> implements IComponent, Iterable<E> {

    private Component parent;

    private int height = 0;
    private InputProvider input;
    
    private boolean hidden = false;

    protected final void setInput(Component component) {
        if (component == null) {
            parent = null;
            input.unregister(this);
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
    
    @Override
    public boolean isHidden() {
        return hidden;
    }

    @Override
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
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

    public int getGlobalY() {
        return hasParent() ? getParent().getGlobalY() : 0;
    }

    public int getGlobalX() {
        return hasParent() ? getParent().getGlobalX() : 0;
    }

    public void setHeight(int height) {
        this.height = height;
        if (hasParent()) {
            parent.updateChildren(0, height);
        }
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

}
