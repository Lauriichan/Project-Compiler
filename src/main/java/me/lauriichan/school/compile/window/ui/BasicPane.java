package me.lauriichan.school.compile.window.ui;

import java.util.ArrayList;
import java.util.Iterator;

import me.lauriichan.school.compile.window.ui.util.Area;

public final class BasicPane extends Pane {

    private final ArrayList<Component> components = new ArrayList<>();

    public boolean addChild(Component component) {
        if (component.isRoot() || components.contains(component)) {
            return false;
        }
        component.setInput(this);
        return components.add(component);
    }

    public boolean removeChild(Component component) {
        if (component.isRoot() || !components.contains(component)) {
            return false;
        }
        component.setInput(null);
        return components.remove(component);
    }

    public int getChildrenCount() {
        return components.size();
    }

    public Component getChild(int index) {
        return components.get(index);
    }

    @Override
    public Iterator<Component> iterator() {
        return components.iterator();
    }

    @Override
    protected void render(Area area) {
        for (Component component : components) {
            if (component.isHidden()) {
                continue;
            }
            component.render(area.create(component.getX(), component.getY(), component.getWidth(), component.getHeight()));
        }
    }

    @Override
    protected void update(long deltaTime) {
        for (Component component : components) {
            if (component.isHidden()) {
                continue;
            }
            component.update(deltaTime);
        }
    }

}
