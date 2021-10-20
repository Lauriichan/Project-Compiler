package me.lauriichan.school.compile.util.file;

import java.io.File;

public class DefaultFileBuilder extends FileBuilder<DefaultFileBuilder> {

    public DefaultFileBuilder(File folder, String name) {
        super(folder, name);
    }

    public DefaultFileBuilder(File file) {
        super(file);
    }

    @Override
    protected DefaultFileBuilder instance() {
        return this;
    }

}
