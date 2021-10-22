package me.lauriichan.school.compile.window.view;

import me.lauriichan.school.compile.window.ui.BasicPane;

public abstract class View {

    protected final String title;
    protected final BasicPane pane = new BasicPane();

    private ViewManager manager;
    private boolean setup = false;

    public View(String title) {
        this.title = title;
    }

    final void setManager(ViewManager manager) {
        this.manager = manager;
    }

    public boolean isRegistered() {
        return manager != null;
    }

    public ViewManager getManager() {
        return manager;
    }

    public String getTitle() {
        return title;
    }

    public BasicPane getPane() {
        return pane;
    }

    public final boolean isSetup() {
        return setup;
    }

    public final void setup() {
        if (setup) {
            return;
        }
        setup = true;
        onSetup(pane, pane.getWidth(), pane.getHeight());
    }

    protected abstract void onSetup(BasicPane pane, int width, int height);

}
