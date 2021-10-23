package me.lauriichan.school.compile.window.ui.component.bar;

import java.awt.Color;
import java.util.EnumMap;
import java.util.function.BiConsumer;

import me.lauriichan.school.compile.window.ui.ITriggerComponent;
import me.lauriichan.school.compile.window.ui.animation.Animators;
import me.lauriichan.school.compile.window.input.mouse.MouseButton;
import me.lauriichan.school.compile.window.ui.animation.FadeAnimation;
import me.lauriichan.school.compile.window.ui.util.Area;

public final class BarBox implements ITriggerComponent {

    private final FadeAnimation<Color> iconColor = new FadeAnimation<>(Animators.COLOR);
    private final FadeAnimation<Color> boxColor = new FadeAnimation<>(Animators.COLOR);

    private final BiConsumer<Area, Color> iconDrawer;
    private final EnumMap<MouseButton, Runnable> actions = new EnumMap<>(MouseButton.class);
    
    private boolean hidden = false;

    public BarBox(BiConsumer<Area, Color> iconDrawer) {
        this.iconDrawer = iconDrawer;
    }

    @Override
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
    
    @Override
    public boolean isHidden() {
        return hidden;
    }

    @Override
    public void render(Area area) {
        area.fill(boxColor.getValue());
        iconDrawer.accept(area, iconColor.getValue());
    }

    @Override
    public void update(long deltaTime) {
        iconColor.tick(deltaTime);
        boxColor.tick(deltaTime);
    }

    public void click(MouseButton button) {
        Runnable action = getAction(button);
        if (action == null) {
            return;
        }
        action.run();
    }

    public Runnable getAction(MouseButton button) {
        return actions.get(button);
    }

    public void setAction(MouseButton button, Runnable action) {
        if (action == null) {
            actions.remove(button);
            return;
        }
        actions.put(button, action);
    }

    public void setIcon(Color color) {
        setIcon(color, color);
    }

    public void setIcon(Color start, Color end) {
        setIconStart(start);
        setIconEnd(end);
    }

    public void setIconStart(Color color) {
        iconColor.setStart(color);
    }

    public void setIconEnd(Color color) {
        iconColor.setEnd(color);
    }

    public void setIconFade(double fadeIn, double fadeOut) {
        iconColor.setFade(fadeIn, fadeOut);
    }

    public void setBox(Color color) {
        setBox(color, color);
    }

    public void setBox(Color start, Color end) {
        setBoxStart(start);
        setBoxEnd(end);
    }

    public void setBoxStart(Color color) {
        boxColor.setStart(color);
    }

    public void setBoxEnd(Color color) {
        boxColor.setEnd(color);
    }

    public void setBoxFade(double fadeIn, double fadeOut) {
        boxColor.setFade(fadeIn, fadeOut);
    }

    public void setTriggered(boolean triggered) {
        iconColor.setTriggered(triggered);
        boxColor.setTriggered(triggered);
    }

    public boolean isTriggered() {
        return iconColor.isTriggered();
    }

}
