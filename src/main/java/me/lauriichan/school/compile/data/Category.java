package me.lauriichan.school.compile.data;

public final class Category {

    public static final Category ROOT = new Category("*");

    private final String name;

    public Category(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ISetting of(String name, Class<?> type) {
        return of(name, type, true);
    }

    public ISetting of(String name, Class<?> type, boolean persistent) {
        return ISetting.of(name, this.name, type, persistent);
    }

    public void load(Settings settings, Class<?> type) {
        settings.loadComplex(name, type);
    }

    public void load(Settings settings) {
        settings.loadPrimitives(name);
    }

    public ISetting[] get(Settings settings) {
        return settings.getAll(name);
    }

}
