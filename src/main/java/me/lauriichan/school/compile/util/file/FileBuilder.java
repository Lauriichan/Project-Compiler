package me.lauriichan.school.compile.util.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import com.syntaxphoenix.syntaxapi.utils.java.Files;

public abstract class FileBuilder<E extends FileBuilder<E>> {

    private final File file;
    private final PrintStream stream;

    public FileBuilder(File path, String name) {
        this(new File(path, name));
    }

    public FileBuilder(File file) {
        this.file = file;
        try {
            Files.createFile(file);
            this.stream = new PrintStream(file);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Can't create file", e);
        }
    }
    
    protected abstract E instance();

    public File getFile() {
        return file;
    }

    public PrintStream getStream() {
        return stream;
    }

    public E add(String text) {
        stream.print(text);
        return instance();
    }

    public E add(char character) {
        stream.print(character);
        return instance();
    }
    
    public E space() {
        return add(' ');
    }

    public E next() {
        stream.println();
        return instance();
    }
    
    public E flush() {
        stream.flush();
        return instance();
    }
    
    public void exit() {
        flush();
        stream.close();
    }

}
