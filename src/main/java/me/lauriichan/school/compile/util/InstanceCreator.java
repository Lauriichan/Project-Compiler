package me.lauriichan.school.compile.util;

import java.lang.reflect.Constructor;

import com.syntaxphoenix.syntaxapi.utils.java.Arrays;

public final class InstanceCreator {

    private InstanceCreator() {};

    public static <T> T create(Class<T> clazz, Object... arguments) throws Exception {
        Constructor<?>[] constructors = Arrays.merge(Constructor[]::new, clazz.getConstructors(), clazz.getDeclaredConstructors());
        Class<?>[] classes = new Class<?>[arguments.length];
        for (int index = 0; index < arguments.length; index++) {
            classes[index] = arguments[index].getClass();
        }
        int max = classes.length;
        Constructor<?> builder = null;
        int args = 0;
        int[] argIdx = new int[max];
        for (Constructor<?> constructor : constructors) {
            int count = constructor.getParameterCount();
            if (count > max || count < args) {
                continue;
            }
            int[] tmpIdx = new int[max];
            for (int idx = 0; idx < max; idx++) {
                tmpIdx[idx] = -1;
            }
            Class<?>[] types = constructor.getParameterTypes();
            int tmpArgs = 0;
            for (int index = 0; index < count; index++) {
                for (int idx = 0; idx < max; idx++) {
                    if (!types[index].equals(classes[idx])) {
                        continue;
                    }
                    tmpIdx[idx] = index;
                    tmpArgs++;
                }
            }
            if (tmpArgs != count) {
                continue;
            }
            argIdx = tmpIdx;
            args = tmpArgs;
            builder = constructor;
        }
        if (builder == null) {
            return null;
        }
        boolean previous = builder.isAccessible();
        if (!previous) {
            builder.setAccessible(true);
        }
        Object out = args == 0 ? builder.newInstance() : null;
        if (out != null) {
            if (!previous) {
                builder.setAccessible(false);
            }
            return clazz.cast(out);
        }
        Object[] parameters = new Object[args];
        for (int idx = 0; idx < max; idx++) {
            if (argIdx[idx] == -1) {
                continue;
            }
            parameters[argIdx[idx]] = arguments[idx];
        }
        out = builder.newInstance(parameters);
        if (!previous) {
            builder.setAccessible(false);
        }
        return clazz.cast(out);
    }

}