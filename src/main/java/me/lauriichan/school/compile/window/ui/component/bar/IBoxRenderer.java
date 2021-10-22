package me.lauriichan.school.compile.window.ui.component.bar;

import java.awt.Color;

import me.lauriichan.school.compile.window.ui.util.Area;

@FunctionalInterface
public interface IBoxRenderer {
    
    void render(Area area, Color color, int offset, int size, int thickness);
    
}