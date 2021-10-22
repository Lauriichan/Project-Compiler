package me.lauriichan.school.compile.window.ui.tab;

import java.awt.Color;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import me.lauriichan.school.compile.window.input.Listener;
import me.lauriichan.school.compile.window.input.mouse.MouseButton;
import me.lauriichan.school.compile.window.input.mouse.MouseClick;
import me.lauriichan.school.compile.window.input.mouse.MouseDrag;
import me.lauriichan.school.compile.window.input.mouse.MouseHover;
import me.lauriichan.school.compile.window.ui.Panel;
import me.lauriichan.school.compile.window.ui.TabBar;
import me.lauriichan.school.compile.window.ui.component.goemetry.LineSeperator;
import me.lauriichan.school.compile.window.ui.component.goemetry.Seperator;
import me.lauriichan.school.compile.window.ui.util.Area;

public class SimpleTabBar extends TabBar {

    private Seperator seperator = new LineSeperator();
    private int size = 0;

    private Color background = Color.DARK_GRAY;

    private final ArrayList<TabButton> boxes = new ArrayList<>();

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock read = lock.readLock();
    private final Lock write = lock.writeLock();

    private boolean triggered = false;

    public SimpleTabBar() {
        LineSeperator seperator = new LineSeperator();
        seperator.setColor(Color.WHITE);
        seperator.setWidth(12);
        seperator.setThickness(2);
        this.seperator = seperator;
    }

    @Override
    public boolean add(TabButton box) {
        read.lock();
        try {
            if (boxes.contains(box)) {
                return false;
            }
        } finally {
            read.unlock();
        }
        write.lock();
        try {
            return boxes.add(box);
        } finally {
            write.unlock();
        }
    }

    @Override
    public boolean remove(TabButton box) {
        read.lock();
        try {
            if (!boxes.contains(box)) {
                return false;
            }
        } finally {
            read.unlock();
        }
        write.lock();
        try {
            return boxes.remove(box);
        } finally {
            write.unlock();
        }
    }

    @Override
    public int getCount() {
        read.lock();
        try {
            return boxes.size();
        } finally {
            read.unlock();
        }
    }

    @Override
    public TabButton get(int index) {
        read.lock();
        try {
            return boxes.get(index);
        } finally {
            read.unlock();
        }
    }

    @Override
    public TabButton[] getAll() {
        read.lock();
        try {
            return boxes.toArray(new TabButton[boxes.size()]);
        } finally {
            read.unlock();
        }
    }

    @Override
    public void render(Area area) {
        area.fill(background);
        TabButton[] boxes = getAll();
        int next = seperator.getWidth() + size;
        int distance = area.getWidth() - next;
        for (int index = 0; index < boxes.length; index++) {
            boxes[index].render(area.create(distance - (next * index), 0, size, getHeight()));
            if (index + 1 != boxes.length) {
                seperator.render(area.create(distance - (next * index) + size, 0, seperator.getWidth(), getHeight()));
            }
        }
    }

    @Override
    public void update(long deltaTime) {
        TabButton[] boxes = getAll();
        for (TabButton box : boxes) {
            box.update(deltaTime);
        }
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }
    
    public Seperator getSeperator() {
        return seperator;
    }
    
    public void setSeperator(Seperator seperator) {
        this.seperator = seperator;
    }

    @Listener
    public void onClick(MouseClick click) {
        if (!( <= click.getY() && (size + offset) >= click.getY())) {
            return;
        }
        int count = getCount();
        int next = (offset * 2) + size;
        int distance = click.getProvider().getPanel().getWidth() - next;
        for (int index = 0; index < count; index++) {
            int x = distance - (next * index);
            int endX = distance - (next * index) + size;
            if (!(x <= click.getX() && endX >= click.getX())) {
                continue;
            }
            TabButton box = get(index);
            if (box == null) {
                break;
            }
            box.click(click.getButton());
            break;
        }
    }

    @Listener
    public void onHover(MouseHover hover) {
        if (!(offset <= hover.getY() && (size + offset) >= hover.getY())) {
            if (!triggered) {
                return;
            }
            triggered = false;
            TabButton[] boxes = getAll();
            for (TabButton box : boxes) {
                box.setTriggered(false);
            }
            return;
        }
        int count = getCount();
        int next = (offset * 2) + size;
        int distance = hover.getProvider().getPanel().getWidth() - next;
        for (int index = 0; index < count; index++) {
            int x = distance - (next * index);
            int endX = distance - (next * index) + size;
            if (!(x <= hover.getX() && endX >= hover.getX())) {
                setTriggered(index, false);
                continue;
            }
            setTriggered(index, true);
            triggered = true;
        }
    }

    private void setTriggered(int index, boolean triggered) {
        TabButton box = get(index);
        if (box == null) {
            return;
        }
        box.setTriggered(triggered);
    }

    @Listener
    public void onDrag(MouseDrag drag) {
        if (drag.getOldY() > getHeight() || drag.getButton() != MouseButton.LEFT) {
            return;
        }
        Panel panel = drag.getProvider().getPanel();
        panel.setPosition(panel.getX() + drag.getX() - drag.getOldX(), panel.getY() + drag.getY() - drag.getOldY());
    }

}
