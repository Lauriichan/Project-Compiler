package me.lauriichan.school.compile.window.ui;

import java.util.Iterator;

import me.lauriichan.school.compile.util.ArrayIterator;

public abstract class Pane extends Component implements Iterable<Component> {

    public abstract boolean addChild(Component component);

    public abstract boolean removeChild(Component component);

    public abstract int getChildrenCount();

    public abstract Component getChild(int index);

    public abstract Component[] getChildren();

    public void applyChildren() {
        Component[] components = getChildren();
        for (Component component : components) {
            component.setHidden(isHidden());
            component.setUpdating(isUpdating());
            if (!(component instanceof Pane)) {
                continue;
            }
            ((Pane) component).applyChildren();
        }
    }

    public void clear() {
        Component[] components = getChildren();
        for (Component component : components) {
            removeChild(component);
        }
    }

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
