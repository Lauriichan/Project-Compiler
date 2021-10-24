package me.lauriichan.school.compile.window.ui.util;

public final class BoxRenderers {

    private BoxRenderers() {}

    public static final IBoxRenderer CROSS = (area, color, offset, size, thickness) -> {
        area.drawLine(offset, offset, size + offset, size + offset, thickness, color);
        area.drawLine(offset, size + offset, size + offset, offset, thickness, color);
    };

    public static final IBoxRenderer UNDERSCORE = (area, color, offset, size, thickness) -> {
        area.drawLine(offset, size + offset, size + offset, size + offset, thickness, color);
    };

    public static final IBoxRenderer CHECKMARK = (area, color, offset, size, thickness) -> {
        size -= 2;
        offset += 1;
        area.drawLine(size / 2 + offset, size + offset, size + offset, offset, thickness, color);
        area.drawLine(offset, size / 2 + offset, size / 2 + offset, size + offset, thickness, color);
    };

}
