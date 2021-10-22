package me.lauriichan.school.compile.window.view;

import java.awt.Color;

import me.lauriichan.school.compile.window.ui.BasicPane;
import me.lauriichan.school.compile.window.ui.component.goemetry.Rectangle;

public final class MainView extends View {

    public MainView() {
        super("Manager");
    }

    @Override
    protected void onSetup(BasicPane pane, int width, int height) {
        Rectangle rectangle = new Rectangle(Color.CYAN);
        rectangle.setSize(width, height);
        pane.addChild(rectangle);
    }

}
