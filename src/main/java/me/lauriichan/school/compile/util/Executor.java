package me.lauriichan.school.compile.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.syntaxphoenix.syntaxapi.utils.java.Exceptions;

public final class Executor {

    public static final ExecutorService SERVICE = Executors.newCachedThreadPool();

    private Executor() {}

    public static void execute(Task action) {
        SERVICE.submit(() -> {
            try {
                action.execute();
            } catch (Exception exp) {
                System.err.println("Something went wrong while executing a task");
                System.err.println(Exceptions.stackTraceToString(exp));
            }
        });
    }

    @FunctionalInterface
    public static interface Task {

        void execute() throws Exception;

    }

}
