package me.lauriichan.school.compile.util;

import java.util.HashMap;

public final class Singleton {

    public static final Singleton INSTANCE = new Singleton();

    public static <E> E get(Class<E> clazz) {
        return INSTANCE.buildOrGet(clazz);
    }

    public static boolean inject(Object object) {
        return INSTANCE.put(object);
    }
    
    public static Object[] getInjects() {
        return INSTANCE.getInjectsImpl();
    }

    private final HashMap<Class<?>, Object> instances = new HashMap<>();
    private final DynamicArray<Object> array = new DynamicArray<>();

    private Singleton() {}
    
    private Object[] getInjectsImpl() {
        return array.asArray();
    }

    public boolean create(Class<?> clazz) {
        try {
            Object object = InstanceCreator.create(clazz, array.asArray());
            if (object == null) {
                return false;
            }
            instances.put(clazz, object);
            array.add(object);
            return true;
        } catch (Exception exp) {
            return false;
        }
    }

    public boolean put(Object object) {
        int index = array.indexOf(object);
        if (index == -1) {
            return false;
        }
        array.add(object);
        return true;
    }

    public <E> E buildOrGet(Class<E> clazz) {
        if (instances.containsKey(clazz) || create(clazz)) {
            return clazz.cast(instances.get(clazz));
        }
        return null;
    }

}
