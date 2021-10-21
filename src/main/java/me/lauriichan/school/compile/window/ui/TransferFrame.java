package me.lauriichan.school.compile.window.ui;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import me.lauriichan.school.compile.window.ui.util.Area;

final class TransferFrame extends JFrame {

    private static final long serialVersionUID = 6321910456035043734L;

    private final Panel component;

    private BufferedImage image = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    private Graphics2D buffer = image.createGraphics();

    public TransferFrame(Panel component) throws HeadlessException {
        super();
        this.component = component;

    }

    public TransferFrame(Panel component, GraphicsConfiguration config) {
        super(config);
        this.component = component;
    }

    public TransferFrame(Panel component, String title) throws HeadlessException {
        super(title);
        this.component = component;
    }

    public TransferFrame(Panel component, String title, GraphicsConfiguration config) {
        super(title, config);
        this.component = component;
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        image = new BufferedImage(Math.max(1, width), Math.max(1, height), BufferedImage.TYPE_INT_ARGB);
        buffer = image.createGraphics();
        buffer.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }

    @Override
    public void paint(Graphics graphics) {
        graphics.drawImage(image, 0, 0, null);
        component.render(new Area(buffer, component.getBackground(), -1, -1, getWidth(), getHeight()));
    }

}
