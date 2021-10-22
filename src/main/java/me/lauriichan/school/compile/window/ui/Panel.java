package me.lauriichan.school.compile.window.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.concurrent.atomic.AtomicInteger;

import me.lauriichan.school.compile.util.tick.Ticker;
import me.lauriichan.school.compile.window.input.InputProvider;
import me.lauriichan.school.compile.window.ui.bar.SimpleBar;
import me.lauriichan.school.compile.window.ui.util.Area;

public final class Panel extends Component {

    private static final AtomicInteger ID = new AtomicInteger(0);

    private final Frame frame;

    private final RootBar bar;
    private final Pane pane;

    private final InputProvider input;

    private final int id = ID.getAndIncrement();
    private final Ticker renderTick = new Ticker("Render - " + id);
    private final Ticker updateTick = new Ticker("Update - " + id);

    private Color background = Color.BLACK;
    private Font font = new Font("Open Sans", Font.PLAIN, 12);

    public Panel() {
        this(new BasicPane());
    }

    public Panel(Pane pane) {
        this(new SimpleBar(), pane);
    }

    public Panel(RootBar bar) {
        this(bar, new BasicPane());
    }

    public Panel(RootBar bar, Pane pane) {
        setHidden(true);
        setUpdating(true);

        GraphicsConfiguration config = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        frame = new TransferFrame(this, config);
        frame.setUndecorated(true);
        frame.setBackground(new Color(0, 0, 0, 0));
        this.input = new InputProvider(this);
        input.register(this);
        this.pane = pane;
        pane.setInput(this);
        this.bar = bar;
        bar.setInput(this);
        renderTick.add(this::render);
        updateTick.add(this::update);
    }

    @Override
    public final boolean isRoot() {
        return true;
    }

    public int getId() {
        return id;
    }

    public boolean isRunning() {
        return !renderTick.isStopped();
    }

    public void exit() {
        renderTick.stop();
        updateTick.stop();
        hide();
        frame.dispose();
    }

    @Override
    public InputProvider getInput() {
        return input;
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

    public void setBarHeight(int height) {
        bar.setHeight(height);
        pane.setY(height);
    }

    public int getBarHeight() {
        return bar.getHeight();
    }

    @Override
    public void setHidden(boolean hidden) {
        super.setHidden(hidden);
        if (frame != null) {
            frame.setVisible(!hidden);
        }
    }

    public Font getFont() {
        return font;
    }

    public void setFont(Font font) {
        this.font = font;
    }

    public Color getBackground() {
        return background;
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public void setTargetFps(int fps) {
        renderTick.setLength(Math.max(1, Math.floorDiv(1000, Math.max(1, fps))));
    }

    public void setTargetTps(int tps) {
        updateTick.setLength(Math.max(1, Math.floorDiv(1000, Math.max(1, tps))));
    }

    public int getFps() {
        return renderTick.getTps();
    }

    public int getTps() {
        return updateTick.getTps();
    }

    public void show() {
        setHidden(false);
    }

    public void hide() {
        setHidden(true);
    }

    public final Frame getFrame() {
        return frame;
    }

    public final Pane getPane() {
        return pane;
    }

    public final RootBar getBar() {
        return bar;
    }

    private void render(long deltaTime) {
        if (isHidden()) {
            return;
        }
        frame.repaint();
    }

    @Override
    public void update(long deltaTime) {
        if (!isUpdating()) {
            return;
        }
        bar.update(deltaTime);
        pane.update(deltaTime);
    }

    @Override
    public int getGlobalX() {
        return 0;
    }

    @Override
    public int getGlobalY() {
        return 0;
    }

    @Override
    public void render(Area area) {
        area.fill(background);
        bar.render(area.create(0, 0, area.getWidth(), bar.getHeight()));
        pane.render(area.create(0, bar.getHeight(), area.getWidth(), area.getHeight() - bar.getHeight()));
    }

    public void center() {
        center(0);
    }

    public void center(int screen) {
        GraphicsDevice[] devices = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
        if (screen < 0 || screen > devices.length) {
            screen = 0;
        }
        Rectangle bounds = devices[screen].getDefaultConfiguration().getBounds();
        setPosition((bounds.width - getWidth()) / 2 + bounds.x, (bounds.height - getHeight()) / 2 + bounds.y);
    }

    public void minimize() {
        frame.setExtendedState(frame.getExtendedState() | Frame.ICONIFIED);
    }

    public String getTitle() {
        return frame.getTitle();
    }

    public void setTitle(String name) {
        frame.setTitle(name);
    }

    public Image getIcon() {
        return frame.getIconImage();
    }

    public void setIcon(Image image) {
        frame.setIconImage(image);
    }

}
