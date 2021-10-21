package me.lauriichan.school.compile.window.input.mouse;

import me.lauriichan.school.compile.window.input.Input;
import me.lauriichan.school.compile.window.input.InputProvider;
import me.lauriichan.school.compile.window.util.Point;

public class MouseRelease extends Input {

    protected final Point position;
    protected final Point screenPosition;
    protected final MouseButton button;

    public MouseRelease(InputProvider provider, int x, int y, int screenX, int screenY, int button) {
        super(provider);
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

}
