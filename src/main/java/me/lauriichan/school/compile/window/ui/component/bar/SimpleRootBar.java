package me.lauriichan.school.compile.window.ui.component.bar;

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
import me.lauriichan.school.compile.window.ui.util.IBoxRenderer;

public final class SimpleRootBar extends RootBar {

    private int lineThickness = 2;
    private int boxOffset = 0;
    private int offset = 0;
    private int size = 0;

    private Color background = Color.DARK_GRAY;

    private final ArrayList<BarBox> boxes = new ArrayList<>();

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock read = lock.readLock();
    private final Lock write = lock.writeLock();

    private boolean triggered = false;

    @Override
    public boolean add(BarBox box) {
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
    public boolean remove(BarBox box) {
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
    public BarBox get(int index) {
        read.lock();
        try {
            return boxes.get(index);
        } finally {
            read.unlock();
        }
    }

    @Override
    public BarBox[] getAll() {
        read.lock();
        try {
            return boxes.toArray(new BarBox[boxes.size()]);
        } finally {
            read.unlock();
        }
    }

    @Override
    public void render(Area area) {
        area.fill(background);
        BarBox[] boxes = getAll();
        int next = (boxOffset * 2) + size;
        int distance = area.getWidth() - next;
        for (int index = 0; index < boxes.length; index++) {
            boxes[index].render(area.create(distance - (next * index), boxOffset, size, size));
        }
    }

    @Override
    public void update(long deltaTime) {
        BarBox[] boxes = getAll();
        for (BarBox box : boxes) {
            box.update(deltaTime);
        }
    }

    @Override
    public BarBox createBox(IBoxRenderer renderer) {
        BarBox box = new BarBox((area, color) -> renderer.render(area, color, (size / 6) + offset, (size / 3) * 2, lineThickness));
        add(box);
        return box;
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        int tmp = height / 3;
        this.boxOffset = tmp / 2;
        this.size = height - tmp;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public void setLineThickness(int lineThickness) {
        this.lineThickness = lineThickness;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

    @Listener
    public void onClick(MouseClick click) {
        if (!isInside(click.getX(), click.getY())) {
            return;
        }
        click.consume();
        if (!(boxOffset <= click.getY() && (size + boxOffset) >= click.getY())) {
            return;
        }
        int count = getCount();
        int next = (boxOffset * 2) + size;
        int distance = click.getProvider().getPanel().getWidth() - next;
        for (int index = 0; index < count; index++) {
            int x = distance - (next * index);
            int endX = distance - (next * index) + size;
            if (!(x <= click.getX() && endX >= click.getX())) {
                continue;
            }
            BarBox box = get(index);
            if (box == null) {
                break;
            }
            box.click(click.getButton());
            break;
        }
    }

    @Listener
    public void onHover(MouseHover hover) {
        boolean consumed = hover.isConsumed();
        if (isInside(hover.getX(), hover.getY())) {
            hover.consume();
        }
        if (!(boxOffset <= hover.getY() && (size + boxOffset) >= hover.getY())) {
            if (!triggered) {
                return;
            }
            triggered = false;
            BarBox[] boxes = getAll();
            for (BarBox box : boxes) {
                box.setTriggered(false);
            }
            return;
        }
        if (consumed) {
            return;
        }
        int count = getCount();
        int next = (boxOffset * 2) + size;
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
        BarBox box = get(index);
        if (box == null) {
            return;
        }
        box.setTriggered(triggered);
    }

    @Listener
    public void onDrag(MouseDrag drag) {
        if (drag.isConsumed() || drag.getOldY() > getHeight() || drag.getButton() != MouseButton.LEFT) {
            return;
        }
        drag.consume();
        Panel panel = drag.getProvider().getPanel();
        panel.setPosition(panel.getX() + drag.getX() - drag.getOldX(), panel.getY() + drag.getY() - drag.getOldY());
    }

}
