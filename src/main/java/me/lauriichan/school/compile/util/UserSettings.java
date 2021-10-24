package me.lauriichan.school.compile.util;

import me.lauriichan.school.compile.data.Category;
import me.lauriichan.school.compile.data.ISetting;
import me.lauriichan.school.compile.data.Settings;

public final class UserSettings {

    public static final UserSettings INSTANCE = new UserSettings();
    public static final Category USER_SETTINGS = new Category("user");

    private final Settings settings = Singleton.get(Settings.class);

    private UserSettings() {}

    private <E> E getOrDefault(String name, Class<E> type, E fallback) {
        E value = get(name, type);
        if (value == null && fallback != null) {
            set(name, type, fallback);
            return fallback;
        }
        return value;
    }

    private <E> E get(String name, Class<E> type) {
        ISetting setting = settings.get(name, USER_SETTINGS);
        return setting.isValid() ? setting.getAs(type) : null;
    }

    private <E> void set(String name, Class<E> type, E value) {
        settings.put(USER_SETTINGS.of(name, type, true)).set(value);
    }

    public static void load() {
        USER_SETTINGS.load(INSTANCE.settings);
    }

    public static String getString(String name) {
        return INSTANCE.getOrDefault(name, String.class, "");
    }

    public static void setString(String name, String value) {
        INSTANCE.set(name, String.class, value);
    }

    public static boolean getBoolean(String name) {
        return INSTANCE.getOrDefault(name, Boolean.class, false);
    }

    public static void setBoolean(String name, boolean value) {
        INSTANCE.set(name, Boolean.class, value);
    }

}
