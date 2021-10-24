package me.lauriichan.school.compile.window.input;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import me.lauriichan.school.compile.window.ui.IComponent;

final class InputReceiver<I extends Input> {

    private static final InputReceiver<?>[] EMPTY = new InputReceiver<?>[0];

    private final Class<I> type;
    private final IComponent instance;
    private final Method method;

    private InputReceiver(Class<I> type, IComponent instance, Method method) {
        this.type = type;
        this.method = method;
        this.instance = instance;
    }

    public Class<I> getType() {
        return type;
    }

    public IComponent getInstance() {
        return instance;
    }

    public boolean canReceive() {
        return !instance.isHidden();
    }

    public Method getMethod() {
        return method;
    }

    public void accept(Input input) {
        if (!type.isAssignableFrom(input.getClass())) {
            return;
        }
        if (!method.isAccessible()) {
            method.setAccessible(true);
            try {
                method.invoke(instance, input);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ignore) {
                // Ignore
            } finally {
                method.setAccessible(false);
            }
            return;
        }
        try {
            method.invoke(instance, input);
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ignore) {
            // Ignore
        }
    }

    public static InputReceiver<?>[] find(IComponent instance) {
        if (instance == null) {
            return EMPTY;
        }
        Class<?> clazz = instance.getClass();
        ArrayList<InputReceiver<?>> receivers = new ArrayList<>();
        ArrayList<Method> visited = new ArrayList<>();
        find(receivers, visited, instance, clazz.getMethods());
        find(receivers, visited, instance, clazz.getDeclaredMethods());
        visited.clear();
        return receivers.toArray(new InputReceiver<?>[receivers.size()]);
    }

    private static void find(ArrayList<InputReceiver<?>> receivers, ArrayList<Method> visited, IComponent instance, Method[] methods) {
        for (Method method : methods) {
            if (visited.contains(method)) {
                continue;
            }
            visited.add(method);
            if (method.getAnnotation(Listener.class) == null || Modifier.isStatic(method.getModifiers())
                || method.getParameterCount() != 1) {
                continue;
            }
            Class<?> type = method.getParameterTypes()[0];
            if (Modifier.isAbstract(type.getModifiers()) || !Input.class.isAssignableFrom(type)) {
                continue;
            }
            receivers.add(new InputReceiver<>(type.asSubclass(Input.class), instance, method));
        }
    }

}
