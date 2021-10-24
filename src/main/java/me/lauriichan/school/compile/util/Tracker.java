package me.lauriichan.school.compile.util;
import java.util.Optional;

import com.syntaxphoenix.syntaxapi.reflection.ClassCache;

public final class Tracker {

    private Tracker() {}

    public static Optional<Class<?>> getClassFromStack(int offset) {
        StackTraceElement element = getStack()[3 + offset];
        return element == null ? Optional.empty() : ClassCache.getOptionalClass(element.getClassName());
    }

    public static Optional<Class<?>> getCallerClass() {
        return getClassFromStack(1);
    }

    private static StackTraceElement[] getStack() {
        return new Throwable().getStackTrace();
    }

}