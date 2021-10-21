package me.lauriichan.school.compile.window.ui.component;

import me.lauriichan.school.compile.window.input.Listener;
import me.lauriichan.school.compile.window.input.keyboard.KeyboardPress;
import me.lauriichan.school.compile.window.input.mouse.MouseHover;
import me.lauriichan.school.compile.window.ui.Component;
import me.lauriichan.school.compile.window.util.Area;

public final class TextField extends Component {

    private final StringBuffer buffer = new StringBuffer();

    private int cursor = -1;

    @Override
    protected void render(Area area) {
        area.drawText(10, 12, buffer.toString());
    }

    @Override
    protected void update(long deltaTime) {

    }

    @Listener
    public void onKeyboard(KeyboardPress press) {
        if (press.getCharacter() == '\b') {
            if (cursor == -1) {
                return;
            }
            buffer.deleteCharAt(cursor);
            cursor--;
            return;
        }
        buffer.append(press.getCharacter());
        cursor++;
    }

    @Listener
    public void onMove(MouseHover hover) {

    }

}
