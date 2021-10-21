package me.lauriichan.school.compile.window.input.mouse;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import me.lauriichan.school.compile.window.input.InputProvider;

public final class MouseListener extends MouseAdapter {

    private final InputProvider provider;

    private int motionX = 0, motionY = 0;
    private int hoverX = 0, hoverY = 0;

    private int button = 0;

    public MouseListener(InputProvider provider) {
        this.provider = provider;
    }

    public InputProvider getProvider() {
        return provider;
    }

    @Override
    public void mousePressed(MouseEvent event) {
        event.consume();
        if (event.getButton() == MouseEvent.NOBUTTON) {
            return;
        }
        button = event.getButton();
        motionX = event.getX();
        motionY = event.getY();
        provider.receive(
            new MousePress(provider, event.getX(), event.getY(), event.getXOnScreen(), event.getYOnScreen(), event.getButton()), event);
    }

    @Override
    public void mouseClicked(MouseEvent event) {
        event.consume();
        if (event.getButton() == MouseEvent.NOBUTTON) {
            return;
        }
        provider.receive(
            new MouseClick(provider, event.getX(), event.getY(), event.getXOnScreen(), event.getYOnScreen(), event.getButton()), event);
    }

    @Override
    public void mouseReleased(MouseEvent event) {
        event.consume();
        if (event.getButton() == MouseEvent.NOBUTTON) {
            return;
        }
        provider.receive(
            new MouseRelease(provider, event.getX(), event.getY(), event.getXOnScreen(), event.getYOnScreen(), event.getButton()), event);
    }

    @Override
    public void mouseDragged(MouseEvent event) {
        event.consume();
        int btn = button;
        MouseDrag drag = new MouseDrag(provider, motionX, motionY, event.getX(), event.getY(), event.getXOnScreen(), event.getYOnScreen(),
            btn);
        provider.receive(drag, event);
        if (drag.isConsumed() || btn == MouseEvent.NOBUTTON) {
            return;
        }
        provider.receive(new MouseClick(provider, drag.getOldX(), drag.getOldY(), drag.getScreenX(), drag.getScreenY(), btn), event);
    }

    @Override
    public void mouseMoved(MouseEvent event) {
        event.consume();
        MouseHover hover = new MouseHover(provider, hoverX, hoverY, event.getX(), event.getY(), event.getXOnScreen(), event.getYOnScreen());
        hoverX = event.getX();
        hoverY = event.getY();
        provider.receive(hover, event);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent event) {
        event.consume();
        provider.receive(new MouseScroll(provider, event.getX(), event.getY(), event.getXOnScreen(), event.getYOnScreen(),
            event.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL ? event.getUnitsToScroll() : event.getScrollAmount(),
            event.getPreciseWheelRotation()), event);
    }

}
