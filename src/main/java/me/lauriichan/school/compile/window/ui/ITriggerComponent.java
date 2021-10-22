package me.lauriichan.school.compile.window.ui;

import me.lauriichan.school.compile.window.input.mouse.MouseButton;

public interface ITriggerComponent extends IComponent {

    void setTriggered(boolean triggered);

    boolean isTriggered();

    void click(MouseButton button);

    void setAction(MouseButton button, Runnable action);

    Runnable getAction(MouseButton button);

}
