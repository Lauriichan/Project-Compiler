package me.lauriichan.school.compile.window.ui;

import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import me.lauriichan.school.compile.window.ui.util.Area;

public class BasicPane extends Pane {

    protected final ArrayList<Component> components = new ArrayList<>();

    protected final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    protected final Lock read = lock.readLock();
    protected final Lock write = lock.writeLock();

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
        component.setY(component.getY() + previous);
        component.setHeight(component.getHeight() - previous);
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
        component.setY(component.getY() - previous);
        component.setHeight(component.getHeight() + previous);
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
        if (width != 0) {
            return;
        }
        int diff = height - previous;
        previous = height;
        Component[] children = getChildren();
        for (Component child : children) {
            child.setY(child.getY() - diff);
            child.setHeight(child.getHeight() + diff);
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
            component.render(area.create(component.getX(), component.getY(), component.getWidth(), component.getHeight()));
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
            if (!component.isUpdating()) {
                continue;
            }
            component.update(deltaTime);
        }
    }
    
    @Override
    public void exit() {
        Component[] children = getChildren();
        for (Component child : children) {
            child.exit();
        }
        components.clear();
    }

}
