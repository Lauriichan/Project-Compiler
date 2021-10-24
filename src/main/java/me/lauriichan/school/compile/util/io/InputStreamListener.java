package me.lauriichan.school.compile.util.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.function.Consumer;

public final class InputStreamListener implements Closeable {

    private Consumer<String> delegate;

    private final InputStream stream;
    private final Scanner scanner;

    private final Thread thread;
    private boolean running = true;

    public InputStreamListener(InputStream stream, String name) throws IOException {
        this.stream = stream;
        this.scanner = new Scanner(stream);
        this.thread = new Thread(this::read, name);
        this.thread.start();
    }

    public InputStream getStream() {
        return stream;
    }

    private void read() {
        while (running) {
            try {
                String line = scanner.nextLine();
                if (line == null) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        break;
                    }
                    continue;
                }
                if (delegate == null) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        break;
                    }
                    continue;
                }
                delegate.accept(line);
            } catch (Exception exp) {
                continue;
            }
        }
    }

    public void setDelegate(Consumer<String> delegate) {
        this.delegate = delegate;
    }

    public boolean isClosed() {
        return !running;
    }

    @Override
    public void close() throws IOException {
        running = false;
        thread.interrupt();
        scanner.close();
        stream.close();
    }

}
