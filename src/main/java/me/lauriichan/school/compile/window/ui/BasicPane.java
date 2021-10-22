package me.lauriichan.school.compile.window.ui;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import me.lauriichan.school.compile.window.ui.util.Area;

public final class BasicPane extends Pane {

    private final ArrayList<Component> components = new ArrayList<>();

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock read = lock.readLock();
    private final Lock write = lock.writeLock();

    private Bar<?> bar;
    private int previous = 0;

    public boolean addChild(Component component) {
        read.lock();
        try {
            if (component.isRoot() || components.contains(component)) {
                return false;
            }
        } finally {
            read.unlock();
        }
        component.setInput(this);
        write.lock();
        try {
            component.setY(component.getY() + previous);
            return components.add(component);
        } finally {
            write.unlock();
        }
    }

    public boolean removeChild(Component component) {
        read.lock();
        try {
            if (component.isRoot() || !components.contains(component)) {
                return false;
            }
        } finally {
            read.unlock();
        }
        component.setInput(null);
        write.lock();
        try {
            component.setY(component.getY() - previous);
            return components.remove(component);
        } finally {
            write.unlock();
        }
    }

    public int getChildrenCount() {
        read.lock();
        try {
            return components.size();
        } finally {
            read.unlock();
        }
    }

    public Component getChild(int index) {
        read.lock();
        try {
            return components.get(index);
        } finally {
            read.unlock();
        }
    }

    @Override
    public Component[] getChildren() {
        read.lock();
        try {
            return components.toArray(new Component[components.size()]);
        } finally {
            read.unlock();
        }
    }

    @Override
    public boolean hasBar() {
        return bar != null;
    }

    @Override
    public void setBar(Bar<?> bar) {
        if (this.bar != null) {
            this.bar.setInput(null);
            updateChildren(0, 0);
        }
        this.bar = bar;
        if (bar != null) {
            bar.setInput(this);
            updateChildren(0, bar.getHeight());
        }
    }

    @Override
    public Bar<?> getBar() {
        return bar;
    }

    @Override
    public int getAddition() {
        return previous;
    }

    @Override
    public void updateChildren(int width, int height) {
        if (width != 0 || height == previous) {
            return;
        }
        int diff = height - previous;
        previous = height;
        Component[] children = getChildren();
        for (Component child : children) {
            child.setY(child.getY() + diff);
        }
    }

    @Override
    public void render(Area area) {
        if (bar != null) {
            bar.render(area.create(0, 0, area.getWidth(), bar.getHeight()));
        }
        if (getChildrenCount() == 0) {
            return;
        }
        Component[] children = getChildren();
        for (Component component : children) {
            if (component.isHidden()) {
                continue;
            }
            Area current = area.create(component.getX(), component.getY(), component.getWidth(), component.getHeight());
            current.clear();
            component.render(current);
        }
    }

    @Override
    public void update(long deltaTime) {
        if (bar != null) {
            bar.update(deltaTime);
        }
        if (getChildrenCount() == 0) {
            return;
        }
        Component[] children = getChildren();
        for (Component component : children) {
            if (component.isHidden()) {
                continue;
            }
            component.update(deltaTime);
        }
    }

}
