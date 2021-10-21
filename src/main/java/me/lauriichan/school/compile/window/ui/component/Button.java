package me.lauriichan.school.compile.window.ui.component;

import java.awt.Color;

import me.lauriichan.school.compile.window.input.Listener;
import me.lauriichan.school.compile.window.input.mouse.MouseButton;
import me.lauriichan.school.compile.window.input.mouse.MouseHover;
import me.lauriichan.school.compile.window.input.mouse.MousePress;
import me.lauriichan.school.compile.window.input.mouse.MouseRelease;
import me.lauriichan.school.compile.window.ui.Component;
import me.lauriichan.school.compile.window.ui.animation.Animators;
import me.lauriichan.school.compile.window.ui.animation.FadeAnimation;
import me.lauriichan.school.compile.window.ui.util.Area;
import me.lauriichan.school.compile.window.ui.util.InputHelper;

public final class Button extends Component {

    private final FadeAnimation<Color> hover = new FadeAnimation<>(Animators.COLOR);
    private final FadeAnimation<Color> hoverShadow = new FadeAnimation<>(Animators.COLOR);

    private String text = "";

    private Color press = Color.WHITE;
    private Color shadow = Color.BLACK;
    private int shadowThickness = 2;

    private boolean pressed = false;

    @Override
    protected void render(Area area) {
        renderBackground(area);
    }

    private void renderBackground(Area area) {
        if (pressed) {
            area.fillShadow(press, shadowThickness, shadow);
            return;
        }
        area.fillShadow(hover.getValue(), shadowThickness, hoverShadow.getValue());
    }

    @Override
    protected void update(long deltaTime) {
        hover.tick(deltaTime);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = (text == null ? "" : text);
    }

    @Listener
    public void onMove(MouseHover hover) {
        InputHelper.hover(hover, this, hoverShadow);
        this.hover.setTriggered(hoverShadow.isTriggered());
    }

    @Listener
    public void onPress(MousePress press) {
        if (!isInside(press.getX(), press.getY()) || press.getButton() != MouseButton.LEFT) {
            return;
        }
        press.consume();
        pressed = true;
    }

    @Listener
    public void onRelease(MouseRelease release) {
        if (!pressed || release.getButton() != MouseButton.LEFT) {
            return;
        }
        release.consume();
        pressed = false;
    }

}
