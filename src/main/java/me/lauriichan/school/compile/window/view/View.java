package me.lauriichan.school.compile.window.view;

import me.lauriichan.school.compile.window.ui.BasicPane;
import me.lauriichan.school.compile.window.ui.Component;

public abstract class View {

    protected final BasicPane pane = new BasicPane();

    private ViewManager manager;
    private boolean setup = false;
    private boolean locked = false;
    
    private String title;

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
    
    public void setTitle(String title) {
        if(title == null) {
            return;
        }
        this.title = title;
        manager.updateTitle(this);
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

    public final void setLocked(boolean locked) {
        if (this.locked == locked) {
            return;
        }
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

    protected void onLock(BasicPane pane) {
        defaultPaneLock(pane);
    }

    protected void onUnlock(BasicPane pane) {
        defaultPaneLock(pane);
    }

    protected final void defaultPaneLock(BasicPane pane) {
        boolean state = locked;
        Component[] components = pane.getChildren();
        for (Component component : components) {
            component.setUpdating(!state);
            component.setHidden(state);
        }
    }

    protected abstract void onSetup(BasicPane pane, int width, int height);
    
    protected void exit() {}

}
