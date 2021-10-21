package me.lauriichan.school.compile.window.ui.bar;

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
import me.lauriichan.school.compile.window.ui.RootBar;
import me.lauriichan.school.compile.window.ui.util.Area;

public final class SimpleBar extends RootBar {

    private int lineThickness = 2;
    private int offset = 0;
    private int size = 0;

    private Color background = Color.DARK_GRAY;

    private final ArrayList<BarBox> boxes = new ArrayList<>();

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock read = lock.readLock();
    private final Lock write = lock.writeLock();

    private boolean triggered = false;

    @Override
    public boolean addBox(BarBox box) {
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
    public boolean removeBox(BarBox box) {
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
    public int getBoxCount() {
        read.lock();
        try {
            return boxes.size();
        } finally {
            read.unlock();
        }
    }

    @Override
    public BarBox getBox(int index) {
        read.lock();
        try {
            return boxes.get(index);
        } finally {
            read.unlock();
        }
    }

    @Override
    public BarBox[] getBoxes() {
        read.lock();
        try {
            return boxes.toArray(new BarBox[boxes.size()]);
        } finally {
            read.unlock();
        }
    }

    @Override
    protected void render(Area area) {
        area.fill(background);
        BarBox[] boxes = getBoxes();
        int next = (offset * 2) + size;
        int distance = area.getWidth() - next;
        for (int index = 0; index < boxes.length; index++) {
            boxes[index].render(area.create(distance - (next * index), offset, size, size));
        }
    }

    @Override
    protected void update(long deltaTime) {
        BarBox[] boxes = getBoxes();
        for (BarBox box : boxes) {
            box.update(deltaTime);
        }
    }

    @Override
    public BarBox createBox(IBoxRenderer renderer) {
        BarBox box = new BarBox((area, color) -> renderer.render(area, color, offset, size - offset, lineThickness));
        addBox(box);
        return box;
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        int tmp = height / 3;
        this.offset = tmp / 2;
        this.size = height - tmp;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public void setLineThickness(int lineThickness) {
        this.lineThickness = lineThickness;
    }

    @Listener
    public void onClick(MouseClick click) {
        if (!(offset <= click.getY() && (size + offset) >= click.getY())) {
            return;
        }
        int count = getBoxCount();
        int next = (offset * 2) + size;
        int distance = click.getProvider().getPanel().getWidth() - next;
        for (int index = 0; index < count; index++) {
            int x = distance - (next * index);
            int endX = distance - (next * index) + size;
            if (!(x <= click.getX() && endX >= click.getX())) {
                continue;
            }
            BarBox box = getBox(index);
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
            BarBox[] boxes = getBoxes();
            for (BarBox box : boxes) {
                box.setTriggered(false);
            }
            return;
        }
        int count = getBoxCount();
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
        BarBox box = getBox(index);
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
