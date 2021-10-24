package me.lauriichan.school.compile.window.ui.util;

import java.awt.Color;

@FunctionalInterface
public interface IBoxRenderer {
    
    void render(Area area, Color color, int offset, int size, int thickness);
    
}