package me.lauriichan.school.compile.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import com.syntaxphoenix.syntaxapi.json.JsonEntry;
import com.syntaxphoenix.syntaxapi.json.JsonObject;
import com.syntaxphoenix.syntaxapi.json.JsonValue;
import com.syntaxphoenix.syntaxapi.json.ValueType;
import com.syntaxphoenix.syntaxapi.utils.java.Files;

import me.lauriichan.school.compile.data.json.JsonIO;

public final class Settings {

    private final ConcurrentHashMap<String, ISetting> settings = new ConcurrentHashMap<>();
    private final File file = new File("config/settings.json");

    private JsonObject root;

    public Settings() {
        Files.createFile(file);
    }

    public ISetting get(String compact) {
        return settings.getOrDefault(compact, NullSetting.NULL);
    }

    public void delete(String compact) {
        ISetting setting = settings.remove(compact);
        if (setting == null || !(setting instanceof ValueSetting)) {
            return;
        }
        ((ValueSetting) setting).clear();
    }

    public ISetting get(String name, String category) {
        return get(category + '.' + name);
    }

    public ISetting[] getAll(String category) {
        Enumeration<String> iterator = settings.keys();
        ArrayList<ISetting> output = new ArrayList<>();
        while (iterator.hasMoreElements()) {
            String key = iterator.nextElement();
            if (!key.startsWith(category)) {
                continue;
            }
            output.add(settings.get(key));
        }
        return output.toArray(new ISetting[output.size()]);
    }

    public void load(Category category, Class<?> type) {
        category.load(this, type);
    }

    public void loadCategory(String category, Class<?> type) {
        String categoryKey = '#' + category;
        if (root == null || !root.has(categoryKey, ValueType.OBJECT)) {
            return;
        }
        JsonObject section = (JsonObject) root.get(categoryKey);
        for (JsonEntry<?> entry : section.entries()) {
            ISetting setting = ISetting.of(entry.getKey(), category, type, true);
            put(setting);
            Object object = JsonIO.toObject(entry.getValue(), type);
            if (object == null) {
                continue;
            }
            setting.set(object);
        }

    }

    public ISetting put(ISetting setting) {
        String compact = setting.asCompact();
        ISetting current = get(compact);
        if (current.isValid()) {
            return current;
        }
        settings.put(compact, setting);
        return setting;
    }

    public void load() {
        JsonObject object;
        try {
            JsonValue<?> value = JsonIO.PARSER.fromFile(file);
            if (value == null || !value.hasType(ValueType.OBJECT)) {
                return;
            }
            object = (JsonObject) value;
        } catch (IOException | IllegalArgumentException e) {
            return;
        }
        this.root = object;
        for (JsonEntry<?> entry : object.entries()) {
            if (entry.getValue() == null) {
                continue;
            }
            String name = entry.getKey();
            if (!name.startsWith("#")) {
                ISetting setting = get(name, "*");
                if (setting.isValid() && setting.isPersistent()) {
                    Object value = JsonIO.toObject(entry.getValue(), setting.getType());
                    if (value == null) {
                        continue;
                    }
                    setting.set(value);
                }
                continue;
            }
            if (!entry.getType().isType(ValueType.OBJECT)) {
                continue;
            }
            name = name.substring(1);
            JsonObject section = (JsonObject) entry.getValue();
            for (JsonEntry<?> sectionEntry : section.entries()) {
                String id = sectionEntry.getKey();
                ISetting setting = get(id, name);
                if (setting.isValid() && setting.isPersistent()) {
                    Object value = JsonIO.toObject(entry.getValue(), setting.getType());
                    if (value == null) {
                        continue;
                    }
                    setting.set(value);
                }
                continue;
            }
        }
    }

    public void save() {
        ISetting[] settings = this.settings.values().toArray(new ISetting[this.settings.size()]);
        JsonObject root = new JsonObject();
        for (ISetting setting : settings) {
            if (!setting.isPersistent() || !setting.isValid()) {
                continue;
            }
            JsonValue<?> value = JsonIO.fromObject(setting.get());
            if (value.hasType(ValueType.NULL)) {
                continue;
            }
            String category = setting.getCategory();
            if (category.equals("*")) {
                root.set(setting.getName(), value);
                continue;
            }
            category = '#' + category;
            if (!root.has(category, ValueType.OBJECT)) {
                root.set(category, new JsonObject());
            }
            ((JsonObject) root.get(category)).set(setting.getName(), value);
        }
        try {
            JsonIO.WRITER.toFile(root, file);
        } catch (IOException e) {
            return;
        }
    }

}
