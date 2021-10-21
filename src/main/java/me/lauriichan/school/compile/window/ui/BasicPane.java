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
    protected void render(Area area) {
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
    protected void update(long deltaTime) {
        Component[] children = getChildren();
        for (Component component : children) {
            if (component.isHidden()) {
                continue;
            }
            component.update(deltaTime);
        }
    }

}
