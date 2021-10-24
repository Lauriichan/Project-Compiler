package me.lauriichan.school.compile.window.view;

import me.lauriichan.school.compile.window.ui.Pane;

public abstract class View<E extends Pane> {

    protected final E pane;

    private ViewManager manager;
    private boolean setup = false;
    private boolean locked = false;

    private String title;

    public View(String title, E pane) {
        this.title = title;
        this.pane = pane;
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

    public void setTitle(String title) {
        if (title == null) {
            return;
        }
        this.title = title;
        manager.updateTitle(this);
    }

    public E getPane() {
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

    public final void setLocked(boolean locked) {
        this.locked = locked;
        if (locked) {
            onLock(pane);
            return;
        }
        onUnlock(pane);
    }

    public final boolean isLocked() {
        return locked;
    }

    public final void lock() {
        setLocked(true);
    }

    public final void unlock() {
        setLocked(false);
    }

    protected void onLock(E pane) {
        defaultPaneLock(true, pane);
    }

    protected void onUnlock(E pane) {
        defaultPaneLock(false, pane);
    }

    protected final void defaultPaneLock(boolean state, E pane) {
        pane.setHidden(state);
        pane.setUpdating(!state);
        pane.applyChildren();
    }

    protected void onSetup(E pane, int width, int height) {}

    protected void exit() {}

}
