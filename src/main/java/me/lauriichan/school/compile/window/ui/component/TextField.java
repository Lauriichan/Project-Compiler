package me.lauriichan.school.compile.window.ui.component;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.io.IOException;

import me.lauriichan.school.compile.window.input.Listener;
import me.lauriichan.school.compile.window.input.keyboard.KeyboardPress;
import me.lauriichan.school.compile.window.input.mouse.MouseButton;
import me.lauriichan.school.compile.window.input.mouse.MouseClick;
import me.lauriichan.school.compile.window.ui.Component;
import me.lauriichan.school.compile.window.ui.animation.BlinkAnimation;
import me.lauriichan.school.compile.window.ui.util.Area;
import me.lauriichan.school.compile.window.ui.util.ICharFilter;
import me.lauriichan.school.compile.window.ui.util.ICharMapper;
import me.lauriichan.school.compile.window.ui.util.TextRender;

public final class TextField extends Component {

    private final StringBuffer buffer = new StringBuffer();
    private final BlinkAnimation<Color> blink = new BlinkAnimation<>();

    private ICharFilter filter = null;
    private ICharMapper mapper = null;

    private boolean returnAllowed = false;
    private boolean spaceAllowed = true;
    private boolean tabAllowed = false;

    private int limit = -1;

    private int cursor = 0;

    private Color background = Color.BLACK;
    private Color shadow = Color.BLACK;

    public TextField() {
        blink.setStart(Color.WHITE);
        blink.setEnd(new Color(255, 255, 255, 50));
        blink.setHidden(new Color(0, 0, 0, 0));
        blink.setBlink(0.5, 0.5);
    }

    @Override
    public void render(Area area) {
        TextRender render = area.drawText(10, 12, buffer.toString(), 20);
        renderCursor(10, 12, area, render);
    }

    private void renderCursor(int x, int y, Area area, TextRender render) {
        int height = render.getHeight();
        int curHeight = (height / 3) * 2;
        if (render.getLines() == 0) {
            area.drawLine(x, (y / 3) * 2, x, y + curHeight, 2, blink.getValue());
            return;
        }
        int id = render.getLineId(cursor);
        int index = (cursor - render.getLineIndex(id)) + 1;
        String line = render.getLine(id);
        int length = line.length();
        String subLine = line.substring(0, index > length ? length : index);
        int tx = render.getMetrics().stringWidth(subLine) + x;
        int amount = height * id;
        area.drawLine(tx, ((y / 3) * 2) + amount, tx, y + amount + curHeight, 2, blink.getValue());
    }

    @Override
    public void update(long deltaTime) {
        blink.tick(deltaTime);
    }

    public void setBackground(Color background) {
        this.background = background;
    }

    public Color getBackground() {
        return background;
    }

    public void setShadow(Color shadow) {
        this.shadow = shadow;
    }

    public Color getShadow() {
        return shadow;
    }

    public void setFilter(ICharFilter filter) {
        this.filter = filter;
    }

    public ICharFilter getFilter() {
        return filter;
    }

    public void setMapper(ICharMapper mapper) {
        this.mapper = mapper;
    }

    public ICharMapper getMapper() {
        return mapper;
    }

    public void setTabAllowed(boolean tabAllowed) {
        this.tabAllowed = tabAllowed;
    }

    public boolean isTabAllowed() {
        return tabAllowed;
    }

    public void setReturnAllowed(boolean returnAllowed) {
        this.returnAllowed = returnAllowed;
    }

    public boolean isReturnAllowed() {
        return returnAllowed;
    }

    public void setSpaceAllowed(boolean spaceAllowed) {
        this.spaceAllowed = spaceAllowed;
    }

    public boolean isSpaceAllowed() {
        return spaceAllowed;
    }

    @Listener
    public void onKeyboard(KeyboardPress press) {
        if (!blink.isTriggered()) {
            return;
        }
        switch (press.getCode()) {
        case KeyEvent.VK_LEFT:
        case KeyEvent.VK_KP_LEFT:
            if (cursor != 0) {
                cursor--;
            }
            return;
        case KeyEvent.VK_RIGHT:
        case KeyEvent.VK_KP_RIGHT:
            if (cursor != buffer.length()) {
                cursor++;
            }
            return;
        case KeyEvent.VK_BACK_SPACE:
            if (cursor == 0) {
                return;
            }
            if (press.isControlDown() && !Character.isWhitespace(buffer.charAt(cursor - 1))) {
                int end = cursor;
                int start = 0;
                for (int index = end - 1; index >= 0; index--) {
                    char current = buffer.charAt(index);
                    if (!Character.isWhitespace(current)) {
                        continue;
                    }
                    start = (current == ' ') ? index : index + 1;
                    break;
                }
                buffer.delete(start, end);
                cursor -= (end - start);
                return;
            }
            buffer.deleteCharAt(--cursor);
            return;
        case KeyEvent.VK_V:
            if (!press.isControlDown()) {
                break;
            }
            Clipboard board = Toolkit.getDefaultToolkit().getSystemClipboard();
            if (!board.isDataFlavorAvailable(DataFlavor.stringFlavor)) {
                return;
            }
            try {
                char[] chars = ((String) board.getData(DataFlavor.stringFlavor)).toCharArray();
                for (char character : chars) {
                    append(character);
                }
            } catch (UnsupportedFlavorException | IOException | ClassCastException ignore) {
                return;
            }
            return;
        default:
            break;
        case KeyEvent.VK_ESCAPE:
        case KeyEvent.VK_CIRCUMFLEX:
        case 16:
            return;
        }
        if (!press.hasChar()) {
            return;
        }
        if (press.isControlDown()) {
            if (!press.isAltDown()) {
                return;
            }
            switch (press.getCode()) {
            case KeyEvent.VK_0:
            case KeyEvent.VK_2:
            case KeyEvent.VK_3:
            case KeyEvent.VK_7:
            case KeyEvent.VK_8:
            case KeyEvent.VK_9:
            case KeyEvent.VK_M:
            case KeyEvent.VK_Q:
            case KeyEvent.VK_PLUS:
            case KeyEvent.VK_ASTERISK:
            case KeyEvent.VK_LESS:
                break;
            default:
                return;
            }
        }
        append(press.getChar());
    }

    private void append(char character) {
        if (buffer.length() == limit || !isSpaceAllowed() && character == ' ' || !isTabAllowed() && character == '\t'
            || !isReturnAllowed() && character == '\n' || (filter != null && filter.test(character))) {
            return;
        }
        buffer.insert(cursor, mapper == null ? character : mapper.map(character));
        cursor++;
    }

    @Listener
    public void onClick(MouseClick click) {
        if (click.getButton() != MouseButton.LEFT) {
            return;
        }
        blink.setTriggered(isInside(click.getX(), click.getY()));
    }

}
