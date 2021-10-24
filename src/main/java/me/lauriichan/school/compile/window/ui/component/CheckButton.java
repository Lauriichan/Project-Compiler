package me.lauriichan.school.compile.window.ui.component;

import java.awt.Color;
import java.util.function.Consumer;

import me.lauriichan.school.compile.window.input.Listener;
import me.lauriichan.school.compile.window.input.mouse.MouseHover;
import me.lauriichan.school.compile.window.input.mouse.MousePress;
import me.lauriichan.school.compile.window.ui.Component;
import me.lauriichan.school.compile.window.ui.animation.Animators;
import me.lauriichan.school.compile.window.ui.animation.FadeAnimation;
import me.lauriichan.school.compile.window.ui.util.Area;
import me.lauriichan.school.compile.window.ui.util.IBoxRenderer;
import me.lauriichan.school.compile.window.ui.util.InputHelper;

public class CheckButton extends Component {

    private final FadeAnimation<Color> iconColor = new FadeAnimation<>(Animators.COLOR);
    private final FadeAnimation<Color> boxColor = new FadeAnimation<>(Animators.COLOR);
    private final FadeAnimation<Color> lineColor = new FadeAnimation<>(Animators.COLOR);

    private IBoxRenderer offRender = null;
    private IBoxRenderer onRender = null;

    private int lineThickness = 2;

    private int iconThickness = 2;
    private int offset = 0;
    private int size = 0;

    private boolean state = false;
    private Consumer<Boolean> listener = null;

    @Override
    public void render(Area area) {
        area.fillOutline(boxColor.getValue(), lineThickness, lineColor.getValue());
        if (state) {
            if (onRender == null) {
                return;
            }
            onRender.render(area.create(size / 4, size / 4, size, size), iconColor.getValue(), offset, size, iconThickness);
            return;
        }
        if (offRender == null) {
            return;
        }
        offRender.render(area.create(size / 4, size / 4, size, size), iconColor.getValue(), offset, size, iconThickness);
    }

    @Override
    public void update(long deltaTime) {
        iconColor.tick(deltaTime);
        boxColor.tick(deltaTime);
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        if (height > getWidth()) {
            return;
        }
        this.size = height - (height / 3);
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        if (width > getHeight()) {
            return;
        }
        this.size = width - (width / 3);
    }

    @Listener
    public void onPress(MousePress press) {
        int x = press.getX();
        int y = press.getY();
        int gx = getGlobalX();
        int gy = getGlobalY();
        if (!(gx <= x && gx + size >= x && gy <= y && gy + size >= y)) {
            return;
        }
        state = !state;
        if (listener == null) {
            return;
        }
        listener.accept(state);
    }

    @Listener
    public void onHover(MouseHover hover) {
        InputHelper.hover(hover, getGlobalX(), getGlobalY(), size, size, boxColor);
        iconColor.setTriggered(boxColor.isTriggered());
    }

    public void setListener(Consumer<Boolean> listener) {
        this.listener = listener;
    }

    public Consumer<Boolean> getListener() {
        return listener;
    }

    public void setState(boolean state) {
        if (this.state == state) {
            return;
        }
        this.state = state;
        if (listener != null) {
            listener.accept(state);
        }
    }

    public boolean getState() {
        return state;
    }

    public void setOffRender(IBoxRenderer offRender) {
        this.offRender = offRender;
    }

    public IBoxRenderer getOffRender() {
        return offRender;
    }

    public void setOnRender(IBoxRenderer onRender) {
        this.onRender = onRender;
    }

    public IBoxRenderer getOnRender() {
        return onRender;
    }

    public void setIconThickness(int iconThickness) {
        this.iconThickness = iconThickness;
    }

    public int getIconThickness() {
        return iconThickness;
    }

    public void setLineThickness(int lineThickness) {
        this.lineThickness = lineThickness;
    }

    public int getLineThickness() {
        return lineThickness;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
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

    public void setLine(Color color) {
        setLine(color, color);
    }

    public void setLine(Color start, Color end) {
        setLineStart(start);
        setLineEnd(end);
    }

    public void setLineStart(Color color) {
        lineColor.setStart(color);
    }

    public void setLineEnd(Color color) {
        lineColor.setEnd(color);
    }

    public void setLineFade(double fadeIn, double fadeOut) {
        lineColor.setFade(fadeIn, fadeOut);
    }

    public void setTriggered(boolean triggered) {
        iconColor.setTriggered(triggered);
        boxColor.setTriggered(triggered);
    }

    public boolean isTriggered() {
        return iconColor.isTriggered();
    }

}