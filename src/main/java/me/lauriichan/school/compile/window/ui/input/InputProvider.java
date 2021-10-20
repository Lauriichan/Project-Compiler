package me.lauriichan.school.compile.window.ui.input;

import java.awt.Frame;
import java.awt.event.InputEvent;
import java.util.ArrayList;
import java.util.HashMap;

import com.syntaxphoenix.syntaxapi.utils.java.tools.Container;

import me.lauriichan.school.compile.window.ui.Component;
import me.lauriichan.school.compile.window.ui.RootPanel;
import me.lauriichan.school.compile.window.ui.input.keyboard.KeyboardListener;
import me.lauriichan.school.compile.window.ui.input.mouse.MouseListener;

public final class InputProvider {

    private final Container<InputEvent> last = Container.of();

    private final HashMap<Class<?>, ArrayList<InputReceiver<?>>> listeners = new HashMap<>();
    private final RootPanel panel;

    public InputProvider(RootPanel panel) {
        this.panel = panel;
        Frame frame = panel.getFrame();
        MouseListener listener = new MouseListener(this);
        frame.addMouseListener(listener);
        frame.addMouseMotionListener(listener);
        frame.addMouseWheelListener(listener);
        frame.addKeyListener(new KeyboardListener(this));
    }

    public RootPanel getPanel() {
        return panel;
    }

    public void register(Component component) {
        InputReceiver<?>[] receivers = InputReceiver.find(component);
        for (InputReceiver<?> receiver : receivers) {
            listeners.computeIfAbsent(receiver.getType(), clz -> new ArrayList<>()).add(0, receiver);
        }
    }

    public void unregister(Component component) {
        for (Class<?> clazz : listeners.keySet()) {
            ArrayList<InputReceiver<?>> list = listeners.get(clazz);
            for (int index = 0; index < list.size(); index++) {
                InputReceiver<?> receiver = list.get(index);
                if (receiver.getInstance() != component) {
                    continue;
                }
                list.remove(index--);
            }
        }
    }

    public boolean isAltDown() {
        return last.isPresent() ? last.get().isAltDown() : false;
    }

    public boolean isShiftDown() {
        return last.isPresent() ? last.get().isShiftDown() : false;
    }

    public boolean isControlDown() {
        return last.isPresent() ? last.get().isControlDown() : false;
    }

    public InputEvent getLast() {
        return last.get();
    }

    public boolean hasLast() {
        return last.isPresent();
    }

    public void receive(Input input, InputEvent event) {
        if (event != null) {
            last.replace(event);
        }
        ArrayList<InputReceiver<?>> list = listeners.get(input.getClass());
        if (list == null || list.isEmpty()) {
            return;
        }
        InputReceiver<?>[] receivers = list.toArray(new InputReceiver<?>[list.size()]);
        for (InputReceiver<?> receiver : receivers) {
            receiver.accept(input);
        }
    }

}
