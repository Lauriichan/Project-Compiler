package me.lauriichan.school.compile.data.json;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.syntaxphoenix.syntaxapi.json.JsonObject;
import com.syntaxphoenix.syntaxapi.json.JsonValue;
import com.syntaxphoenix.syntaxapi.json.ValueType;
import com.syntaxphoenix.syntaxapi.json.io.JsonParser;
import com.syntaxphoenix.syntaxapi.json.io.JsonWriter;
import com.syntaxphoenix.syntaxapi.json.value.JsonNull;
import com.syntaxphoenix.syntaxapi.reflection.AbstractReflect;
import com.syntaxphoenix.syntaxapi.utils.io.TextDeserializer;
import com.syntaxphoenix.syntaxapi.utils.java.Primitives;

import me.lauriichan.school.compile.data.Serialize;
import me.lauriichan.school.compile.util.InstanceCreator;
import me.lauriichan.school.compile.util.Singleton;

public final class JsonIO {

    private JsonIO() {}

    private static final List<JsonConverter<?, ?>> CONVERTERS = Collections.synchronizedList(new ArrayList<>());

    public static final TextDeserializer<JsonValue<?>> PARSER = new JsonSerializer(new JsonParser());
    public static final JsonWriter WRITER = new JsonWriter().setPretty(true).setSpaces(true).setIndent(2);

    public static void register(JsonConverter<?, ?> converter) {
        if (CONVERTERS.contains(converter)) {
            return;
        }
        CONVERTERS.add(converter);
    }

    public static JsonConverter<?, ?> get(Class<?> json, Class<?> object) {
        for (JsonConverter<?, ?> converter : CONVERTERS) {
            if (json != null && !converter.getJsonType().isAssignableFrom(json)) {
                continue;
            }
            if (!converter.getObjectType().isAssignableFrom(object)) {
                continue;
            }
            return converter;
        }
        return null;
    }

    public static JsonValue<?> fromObject(Object object) {
        if (object == null) {
            return JsonNull.get();
        }
        Class<?> clazz = object.getClass();
        if(Primitives.isInstance(object)) {
            return JsonValue.fromPrimitive(object);
        }
        ArrayList<Field> fields = new ArrayList<>();
        putClass(fields, clazz);
        if (fields.isEmpty()) {
            return JsonNull.get();
        }
        if (fields.size() == 1) {
            return getValueAsJson(object, fields.get(0));
        }
        JsonObject output = new JsonObject();
        for (Field field : fields) {
            JsonValue<?> value = getValueAsJson(object, field);
            if (value.hasType(ValueType.NULL)) {
                continue;
            }
            output.set(field.getName(), value);
        }
        return output;
    }

    public static Object toObject(JsonValue<?> value, Class<?> clazz) {
        if(Primitives.isInstance(clazz)) {
            return value.getValue();
        }
        ArrayList<Field> fields = new ArrayList<>();
        putClass(fields, clazz);
        try {
            Object object = InstanceCreator.create(clazz, Singleton.getInjects());
            if (fields.isEmpty()) {
                return object;
            }
            if (fields.size() == 1) {
                setValue(object, fields.get(0), value);
                return object;
            }
            JsonObject jsonObject = (JsonObject) value;
            for (Field field : fields) {
                setValue(object, field, jsonObject.get(field.getName()));
            }
            return object;
        } catch (Exception e) {
            return null;
        }
    }

    /*
     * Field resolver
     */

    private static void putClass(ArrayList<Field> fields, Class<?> clazz) {
        putFields(fields, clazz.getFields());
        putFields(fields, clazz.getDeclaredFields());
        if(clazz.getSuperclass() == null) {
            return;
        }
        putClass(fields, clazz.getSuperclass());
    }

    private static void putFields(ArrayList<Field> fields, Field[] addition) {
        for (Field field : addition) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            if (field.getAnnotation(Serialize.class) == null) {
                continue;
            }
            fields.add(field);
        }
    }

    /*
     * Reflections
     */

    private static JsonValue<?> getValueAsJson(Object instance, Field field) {
        Object object = getValue(instance, field);
        if (object == null) {
            return JsonNull.get();
        }
        if(Primitives.isInstance(object)) {
            return JsonValue.fromPrimitive(object);
        }
        JsonConverter<?, ?> converter = get(null, object.getClass());
        if (converter == null) {
            converter = get(null, field.getType());
            if (converter == null) {
                return JsonNull.get();
            }
        }
        JsonValue<?> output = converter.asAbstractJson(object);
        if (output == null) {
            return JsonNull.get();
        }
        return output;
    }

    private static Object getValue(Object instance, Field field) {
        try {
            if (field.isAccessible()) {
                return field.get(instance);
            }
            field.setAccessible(true);
            Object value = field.get(instance);
            field.setAccessible(false);
            return value;
        } catch (IllegalArgumentException | IllegalAccessException exp) {
            return null;
        }
    }

    private static void setValue(Object instance, Field field, JsonValue<?> value) {
        if(value == null) {
            return;
        }
        Class<?> type = field.getType();
        Object converted = type.equals(value.getClass()) ? value : value.getValue();
        if (!Primitives.isInstance(type) && !type.equals(value.getClass())) {
            JsonConverter<?, ?> converter = get(value.getClass(), type);
            if (converter == null) {
                return;
            }
            converted = converter.fromAbstractJson(value);
            if (converted == null) {
                return;
            }
        }
        setValue(instance, field, converted);
    }
    
    public static void setValue(Object instance, Field field, Object value) {
        try {
            int modifier = field.getModifiers();
            if (Modifier.isFinal(field.getModifiers())) {
                AbstractReflect.FIELD.setFieldValue(field, "modify", modifier & ~Modifier.FINAL);
                if (field.isAccessible()) {
                    field.set(instance, value);
                    AbstractReflect.FIELD.setFieldValue(field, "modify", modifier);
                    return;
                }
                field.setAccessible(true);
                field.set(instance, value);
                field.setAccessible(false);
                AbstractReflect.FIELD.setFieldValue(field, "modify", modifier);
                return;
            }
            if (field.isAccessible()) {
                field.set(instance, value);
                return;
            }
            field.setAccessible(true);
            field.set(instance, value);
            field.setAccessible(false);
        } catch (IllegalArgumentException | IllegalAccessException exp) {
            return;
        }
    }

}
