package me.lauriichan.school.compile.window.ui;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import me.lauriichan.school.compile.window.ui.util.Area;

final class TransferFrame extends Frame {

    private static final long serialVersionUID = 6321910456035043734L;

    private final Component component;

    public TransferFrame(Component component) throws HeadlessException {
        super();
        if (!component.isRoot()) {
            throw new IllegalArgumentException("Component has to be root!");
        }
        this.component = component;
    }

    public TransferFrame(Component component, GraphicsConfiguration config) {
        super(config);
        if (!component.isRoot()) {
            throw new IllegalArgumentException("Component has to be root!");
        }
        this.component = component;
    }

    public TransferFrame(Component component, String title) throws HeadlessException {
        super(title);
        if (!component.isRoot()) {
            throw new IllegalArgumentException("Component has to be root!");
        }
        this.component = component;
    }

    public TransferFrame(Component component, String title, GraphicsConfiguration config) {
        super(title, config);
        if (!component.isRoot()) {
            throw new IllegalArgumentException("Component has to be root!");
        }
        this.component = component;
    }

    @Override
    public void paint(Graphics graphics) {
        graphics.clearRect(getX(), getY(), getWidth(), getHeight());
        component.render(new Area((Graphics2D) graphics, -1, -1, getWidth(), getHeight()));
    }

}
