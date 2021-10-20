package me.lauriichan.school.compile.data;

final class ValueSetting implements ISetting {

    private final String name;
    private final String category;
    private final Class<?> type;

    private Object value;
    private boolean persistent = false;
    private boolean valid = true;

    public ValueSetting(String name, String category, Class<?> type) {
        this(name, category, type, null);
    }

    public <E> ValueSetting(String name, String category, Class<E> type, E value) {
        this.name = name;
        this.category = category;
        this.type = type;
        this.value = value;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCategory() {
        return category;
    }
    
    @Override
    public Class<?> getType() {
        return type;
    }

    @Override
    public boolean isPersistent() {
        return persistent;
    }

    @Override
    public ValueSetting setPersistent(boolean persistent) {
        this.persistent = persistent;
        return this;
    }

    @Override
    public boolean isValid() {
        return valid;
    }

    @Override
    public Object get() {
        return value;
    }

    @Override
    public boolean set(Object value) {
        if (value == null || !type.isAssignableFrom(value.getClass())) {
            return false;
        }
        this.value = value;
        return true;
    }
    
    void clear() {
        this.value = null;
        this.persistent = false;
        this.valid = false;
    }

}
