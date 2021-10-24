package me.lauriichan.school.compile.window.ui.util;

import me.lauriichan.school.compile.window.input.mouse.MouseHover;
import me.lauriichan.school.compile.window.ui.Component;
import me.lauriichan.school.compile.window.ui.animation.FadeAnimation;

public final class InputHelper {

    private InputHelper() {}

    public static void hover(MouseHover hover, Component component, FadeAnimation<?> animation) {
        if(!component.isInside(hover.getX(), hover.getY()) || hover.isConsumed()) {
            animation.setTriggered(false);
            return;
        }
        hover.consume();
        animation.setTriggered(true);
    }

    public static void hover(MouseHover hover, int x, int y, int width, int height, FadeAnimation<?> animation) {
        if(!(x <= hover.getX() && x + width >= hover.getX() && y <= hover.getY() && y + height >= hover.getY()) || hover.isConsumed()) {
            animation.setTriggered(false);
            return;
        }
        hover.consume();
        animation.setTriggered(true);
    }

}
