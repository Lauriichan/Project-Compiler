package me.lauriichan.school.compile.window.view;

import me.lauriichan.school.compile.window.ui.BasicPane;

public abstract class View {

    protected final String title;
    protected final BasicPane pane = new BasicPane();

    public View(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public BasicPane getPane() {
        return pane;
    }

}
