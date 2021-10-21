package me.lauriichan.school.compile.window.ui;

import java.awt.Color;
import java.util.Iterator;

import me.lauriichan.school.compile.util.ArrayIterator;
import me.lauriichan.school.compile.window.input.InputProvider;
import me.lauriichan.school.compile.window.ui.bar.BarBox;
import me.lauriichan.school.compile.window.ui.bar.IBoxRenderer;
import me.lauriichan.school.compile.window.ui.util.Area;

public abstract class RootBar implements Iterable<BarBox> {

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
    }

    public InputProvider getInput() {
        return input;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public abstract boolean addBox(BarBox component);

    public abstract boolean removeBox(BarBox component);

    public abstract int getBoxCount();

    public abstract BarBox getBox(int index);

    public abstract BarBox[] getBoxes();
    
    public abstract BarBox createBox(IBoxRenderer renderer);
    
    public abstract void setBackground(Color color);

    @Override
    public Iterator<BarBox> iterator() {
        return new ArrayIterator<>(getBoxes());
    }

    protected void render(Area area) {}

    protected void update(long deltaTime) {}

}
