package me.lauriichan.school.compile.window.input.mouse;

import me.lauriichan.school.compile.window.input.Input;
import me.lauriichan.school.compile.window.input.InputProvider;
import me.lauriichan.school.compile.window.util.Point;

public class MouseHover extends Input {

    protected final Point previous;
    protected final Point position;
    protected final Point screenPosition;

    public MouseHover(InputProvider provider, int oldX, int oldY, int x, int y, int screenX, int screenY) {
        super(provider);
        this.previous = new Point(oldX, oldX);
        this.position = new Point(x, y);
        this.screenPosition = new Point(screenX, screenY);
    }

    public int getX() {
        return position.getX();
    }

    public int getY() {
        return position.getY();
    }

    public int getOldX() {
        return previous.getX();
    }

    public int getOldY() {
        return previous.getY();
    }
    
    public int getScreenX() {
        return screenPosition.getX();
    }
    
    public int getScreenY() {
        return screenPosition.getY();
    }

}
