package me.lauriichan.school.compile.window.ui;

import java.awt.Color;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;

import me.lauriichan.school.compile.window.ui.input.InputProvider;
import me.lauriichan.school.compile.window.ui.input.Listener;
import me.lauriichan.school.compile.window.ui.input.mouse.MouseButton;
import me.lauriichan.school.compile.window.ui.input.mouse.MouseDrag;
import me.lauriichan.school.compile.window.ui.util.Area;

public final class RootPanel extends Component {

    private final Frame frame;
    private final Pane pane;

    private final InputProvider input;

    public RootPanel() {
        this(new BasicPane());
    }

    public RootPanel(Pane pane) {
        GraphicsConfiguration config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        frame = new TransferFrame(this, config);
        frame.setBackground(Color.BLACK);
        frame.setUndecorated(true);
        this.input = new InputProvider(this);
        input.register(this);
        this.pane = pane;
        pane.setInput(this);
    }

    @Override
    public InputProvider getInput() {
        return input;
    }

    @Override
    public boolean isRoot() {
        return true;
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        frame.setLocation(x, getY());
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        frame.setLocation(getX(), y);
    }

    @Override
    public void setHeight(int height) {
        super.setHeight(height);
        pane.setHeight(height);
        frame.setSize(getWidth(), height);
    }

    @Override
    public void setWidth(int width) {
        super.setWidth(width);
        pane.setWidth(width);
        frame.setSize(width, getHeight());
    }

    public boolean isShown() {
        return frame.isVisible();
    }

    public void show() {
        frame.setVisible(true);
        frame.createBufferStrategy(4);
    }

    public void hide() {
        frame.setVisible(false);
    }

    public final Frame getFrame() {
        return frame;
    }

    public final Pane getPane() {
        return pane;
    }

    public void render() {
        frame.repaint();
    }

    public void update(long deltaTime) {
        pane.update(deltaTime);
    }

    @Override
    protected void render(Area area) {
        pane.render(area);
    }

    @Listener
    public void onDrag(MouseDrag drag) {
        if (drag.getButton() != MouseButton.LEFT) {
            return;
        }
        setPosition(getX() + drag.getX() - drag.getOldX(), getY() + drag.getY() - drag.getOldY());
    }

}
