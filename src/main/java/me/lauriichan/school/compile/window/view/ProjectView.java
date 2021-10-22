package me.lauriichan.school.compile.window.view;

import java.awt.Color;

import me.lauriichan.school.compile.window.ui.BasicPane;
import me.lauriichan.school.compile.window.ui.component.goemetry.Rectangle;

public final class ProjectView extends View {

    public ProjectView() {
        super("Project");
    }

    @Override
    protected void onSetup(BasicPane pane, int width, int height) {
        Rectangle rectangle = new Rectangle(Color.GREEN);
        rectangle.setSize(width, height);
        System.out.println(width + " / " + height);
        pane.addChild(rectangle);
    }

}
