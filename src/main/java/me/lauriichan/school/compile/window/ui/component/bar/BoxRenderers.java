package me.lauriichan.school.compile.window.ui.component.bar;

public final class BoxRenderers {
    
    private BoxRenderers() {}
    
    public static final IBoxRenderer CLOSE = (area, color, offset, size, thickness) -> {
        area.drawLine(offset, offset, size, size, thickness, color);
        area.drawLine(offset, size, size, offset, thickness, color);
    };
    
    public static final IBoxRenderer MINIMIZE = (area, color, offset, size, thickness) -> {
        area.drawLine(offset, size, size, size, thickness, color);
    };

}
