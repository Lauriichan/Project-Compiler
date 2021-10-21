package me.lauriichan.school.compile.window.ui.component;

import java.awt.Color;

import me.lauriichan.school.compile.window.input.Listener;
import me.lauriichan.school.compile.window.input.mouse.MouseHover;
import me.lauriichan.school.compile.window.ui.Component;
import me.lauriichan.school.compile.window.ui.animation.Animators;
import me.lauriichan.school.compile.window.ui.animation.FadeAnimation;
import me.lauriichan.school.compile.window.ui.util.Area;

public final class Button extends Component {

    private final FadeAnimation<Color> color = new FadeAnimation<>(Animators.COLOR);

    private String text = "";

    @Override
    protected void render(Area area) {
        area.drawRectangle(0, 0, getWidth(), getHeight(), color.getValue());
    }

    @Override
    protected void update(long deltaTime) {
        color.tick(deltaTime);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = (text == null ? "" : text);
    }

    @Listener
    public void onMove(MouseHover hover) {
        if (color.isTriggered()) {
            if (isInside(hover.getX(), hover.getY())) {
                return;
            }
            color.setTriggered(false);
            return;
        }
        if (!isInside(hover.getX(), hover.getY())) {
            return;
        }
        color.setTriggered(true);
    }

}
