package me.lauriichan.school.compile.window.view;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.BiConsumer;

import me.lauriichan.school.compile.util.TriConsumer;
import me.lauriichan.school.compile.window.ui.BasicPane;
import me.lauriichan.school.compile.window.ui.Component;
import me.lauriichan.school.compile.window.ui.Pane;
import me.lauriichan.school.compile.window.ui.util.Area;

public final class ViewManager extends Component {

    private final ArrayList<View> views = new ArrayList<>();
    private int current = 0;

    private TriConsumer<Boolean, Integer, String> delegate;
    private BiConsumer<Integer, String> updater;

    public void add(View view) {
        if (views.contains(view)) {
            return;
        }
        int index = views.size();
        views.add(view);
        view.setManager(this);
        view.getPane().setSize(getWidth(), getHeight());
        if (getInput() != null) {
            view.getPane().setInput(this);
            view.setup();
        }
        view.setLocked(true);
        if (delegate == null) {
            return;
        }
        delegate.accept(true, index, view.getTitle());
    }

    public <E extends View> Optional<E> get(Class<E> clazz) {
        return views.stream().filter(view -> clazz.isAssignableFrom(view.getClass())).findFirst().map(clazz::cast);
    }

    public void setDelegate(TriConsumer<Boolean, Integer, String> delegate) {
        this.delegate = delegate;
    }

    public TriConsumer<Boolean, Integer, String> getDelegate() {
        return delegate;
    }

    public void setUpdater(BiConsumer<Integer, String> updater) {
        this.updater = updater;
    }

    public BiConsumer<Integer, String> getUpdater() {
        return updater;
    }

    void updateTitle(View view) {
        if (updater == null) {
            return;
        }
        int index = views.indexOf(view);
        if (index == -1) {
            return;
        }
        updater.accept(index, view.getTitle());
    }

    @Override
    public void setInput(Component component) {
        super.setInput(component);
        for (int index = 0; index < views.size(); index++) {
            View view = views.get(index);
            Pane pane = view.getPane();
            if (pane.getInput() != null) {
                continue;
            }
            pane.setInput(this);
            view.setup();
        }
    }

    public void remove(View view) {
        int index = views.indexOf(view);
        if (index == -1) {
            return;
        }
        if (views.size() == 1) {
            current = 0;
            views.get(0).getPane().setInput(null);
            views.clear();
            if (delegate == null) {
                return;
            }
            delegate.accept(false, index, view.getTitle());
            return;
        }
        if (index <= current) {
            current--;
        }
        views.remove(index).getPane().setInput(null);
        if (delegate == null) {
            return;
        }
        delegate.accept(false, index, view.getTitle());
    }

    public View get(int index) {
        return views.get(index);
    }

    public int getViewCount() {
        return views.size();
    }

    public void select(int id) {
        int tmp = Math.min(views.size() - 1, Math.max(0, id));
        if (tmp != current) {
            if (current < views.size()) {
                views.get(current).lock();
            }
            views.get(tmp).unlock();
        }
        current = tmp;
    }

    @Override
    public boolean isHidden() {
        return views.isEmpty();
    }

    @Override
    public boolean isUpdating() {
        return !views.isEmpty();
    }

    @Override
    public void setWidth(int width) {
        for (int index = 0; index < views.size(); index++) {
            views.get(index).getPane().setWidth(width);
        }
        super.setWidth(width);
    }

    @Override
    public void setHeight(int height) {
        for (int index = 0; index < views.size(); index++) {
            views.get(index).getPane().setHeight(height);
        }
        super.setHeight(height);
    }

    @Override
    public void render(Area area) {
        BasicPane pane = views.get(current).getPane();
        if (pane.isHidden()) {
            return;
        }
        pane.render(area);
    }

    @Override
    public void update(long deltaTime) {
        BasicPane pane = views.get(current).getPane();
        if (!pane.isUpdating()) {
            return;
        }
        pane.update(deltaTime);
    }

    @Override
    public void exit() {
        for (View view : views) {
            view.exit();
        }
        views.clear();
    }

}
