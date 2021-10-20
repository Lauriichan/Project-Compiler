package me.lauriichan.school.compile.data;

public interface ISetting {

    String getName();

    String getCategory();

    boolean isPersistent();
    
    ISetting setPersistent(boolean persistent);

    boolean isValid();

    Object get();
    
    Class<?> getType();
    
    boolean set(Object value);

    default <E> E getAs(Class<E> clazz) {
        Object object = get();
        return hasType(clazz) ? clazz.cast(object) : null;
    }

    default boolean hasType(Class<?> clazz) {
        Object object = get();
        return object != null && clazz.isAssignableFrom(object.getClass());
    }

    default String asCompact() {
        return getCategory() + '.' + getName();
    }

    static ISetting of(String name, String category, Class<?> type) {
        return of(name, category, type, true);
    }

    static ISetting of(String name, String category, Class<?> type, boolean persistent) {
        if(type == null || name == null) {
            return NullSetting.NULL;
        }
        ValueSetting setting = new ValueSetting(name, category == null ? Category.ROOT.getName() : category, type);
        setting.setPersistent(persistent);
        return setting;
    }

}
