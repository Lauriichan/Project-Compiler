package me.lauriichan.school.compile.util.io;

import java.io.Closeable;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.util.Scanner;
import java.util.function.Consumer;

public final class StreamListener implements Closeable {

    private Consumer<String> delegate;

    private final PrintStream stream;

    private final PipedInputStream input;
    private final PipedOutputStream output;

    private final Scanner scanner;

    private final Thread thread;
    private boolean running = true;

    public StreamListener(String name) throws IOException {
        this.input = new PipedInputStream();
        this.output = new PipedOutputStream(input);
        this.stream = new PrintStream(output);
        this.scanner = new Scanner(input);
        this.thread = new Thread(this::read, name);
        this.thread.start();
    }

    public PrintStream getStream() {
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
        input.close();
        output.close();
    }

}
