package me.lauriichan.school.compile.window.input.mouse;

import me.lauriichan.school.compile.window.input.Input;
import me.lauriichan.school.compile.window.input.InputProvider;
import me.lauriichan.school.compile.window.util.Point;

public class MouseDrag extends Input {

    protected final Point previous;
    protected final Point position;
    protected final Point screenPosition;
    protected final MouseButton button;

    public MouseDrag(InputProvider provider, int oldX, int oldY, int x, int y, int screenX, int screenY, int button) {
        super(provider);
        this.previous = new Point(oldX, oldY);
        this.button = MouseButton.values()[button - 1];
        this.position = new Point(x, y);
        this.screenPosition = new Point(screenX, screenY);
    }

    public MouseButton getButton() {
        return button;
    }

    public int getX() {
        return position.getX();
    }

    public int getScreenX() {
        return screenPosition.getX();
    }

    public int getY() {
        return position.getY();
    }

    public int getScreenY() {
        return screenPosition.getY();
    }

    public int getOldX() {
        return previous.getX();
    }

    public int getOldY() {
        return previous.getY();
    }

}
