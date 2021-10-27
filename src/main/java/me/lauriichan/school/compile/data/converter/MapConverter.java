package me.lauriichan.school.compile.data.converter;

import java.util.HashMap;

import com.syntaxphoenix.syntaxapi.json.JsonEntry;
import com.syntaxphoenix.syntaxapi.json.JsonObject;
import com.syntaxphoenix.syntaxapi.json.ValueType;
import com.syntaxphoenix.syntaxapi.utils.java.Primitives;

import me.lauriichan.school.compile.data.json.JsonConverter;

@SuppressWarnings("rawtypes")
public class MapConverter extends JsonConverter<JsonObject, HashMap> {

    public MapConverter() {
        super(JsonObject.class, HashMap.class);
    }

    @Override
    protected JsonObject asJson(HashMap object) {
        JsonObject json = new JsonObject();
        if (object.isEmpty()) {
            return json;
        }
        for (Object rawKey : object.keySet()) {
            if (!(rawKey instanceof String)) {
                continue;
            }
            String key = (String) rawKey;
            Object value = object.get(key);
            if (value == null || !Primitives.isInstance(value)) {
                continue;
            }
            json.set(key, value);
        }
        return json;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected HashMap fromJson(JsonObject json) {
        HashMap map = new HashMap();
        for (JsonEntry<?> value : json) {
            if (value.getType() == ValueType.NULL || !value.getType().isPrimitive()) {
                continue;
            }
            map.put(value.getKey(), value.getValue().getValue());
        }
        return map;
    }

}
