package me.lauriichan.school.compile.window.ui.util;

import me.lauriichan.school.compile.window.input.mouse.MouseHover;
import me.lauriichan.school.compile.window.ui.Component;
import me.lauriichan.school.compile.window.ui.animation.FadeAnimation;

public final class InputHelper {

    private InputHelper() {}

    public static void hover(MouseHover hover, Component component, FadeAnimation<?> animation) {
        animation.setTriggered(component.isInside(hover.getX(), hover.getY()));
    }

    public static void hover(MouseHover hover, int x, int y, int width, int height, FadeAnimation<?> animation) {
        animation.setTriggered(x <= hover.getX() && x + width >= hover.getX() && y <= hover.getY() && y + height >= hover.getY());
    }

}
