package me.lauriichan.school.compile.window.view;

import java.util.ArrayList;

import me.lauriichan.school.compile.window.ui.BasicPane;
import me.lauriichan.school.compile.window.ui.Component;
import me.lauriichan.school.compile.window.ui.util.Area;

public final class ViewManager extends Component {

    private final ArrayList<View> views = new ArrayList<>();
    private int current = 0;

    public void add(View view) {
        if (views.contains(view)) {
            return;
        }
        views.add(view);
    }

    public void remove(View view) {
        int index = views.indexOf(view);
        if (index == -1) {
            return;
        }
        if (views.size() == 1) {
            current = 0;
            views.clear();
            return;
        }
        if (index >= current) {
            current--;
        }
        views.remove(index);
    }

    public int getViewCount() {
        return views.size();
    }

    public void select(int id) {
        current = Math.min(views.size() - 1, Math.max(0, id));
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

}
