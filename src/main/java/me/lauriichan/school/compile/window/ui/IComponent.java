package me.lauriichan.school.compile.window.ui;

import me.lauriichan.school.compile.window.ui.util.Area;

public interface IComponent {
    
    boolean isHidden();
    
    void setHidden(boolean hidden);

    void render(Area area);

    void update(long deltaTime);

}
