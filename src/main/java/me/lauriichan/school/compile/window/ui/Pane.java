package me.lauriichan.school.compile.window.ui;

import java.util.Iterator;

import me.lauriichan.school.compile.util.ArrayIterator;

public abstract class Pane extends Component implements Iterable<Component> {

    public abstract boolean addChild(Component component);

    public abstract boolean removeChild(Component component);

    public abstract int getChildrenCount();

    public abstract Component getChild(int index);

    public abstract Component[] getChildren();

    public abstract boolean hasBar();

    public abstract void setBar(Bar<?> bar);

    public abstract Bar<?> getBar();

    public int getAddition() {
        return 0;
    }

    @Override
    public Iterator<Component> iterator() {
        return new ArrayIterator<>(getChildren());
    }

}
