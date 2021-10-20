package me.lauriichan.school.compile.window.ui;

import java.awt.Color;

import me.lauriichan.school.compile.window.ui.util.Area;

public final class Button extends Component {

    private Color color = Color.WHITE;

    @Override
    protected void render(Area area) {
        area.drawRectangle(0, 0, getWidth(), getHeight(), color);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
