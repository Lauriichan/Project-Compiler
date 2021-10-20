package me.lauriichan.school.compile.data.converter;

import java.util.ArrayList;

import com.syntaxphoenix.syntaxapi.json.JsonArray;
import com.syntaxphoenix.syntaxapi.json.JsonValue;
import com.syntaxphoenix.syntaxapi.json.ValueType;
import com.syntaxphoenix.syntaxapi.utils.java.Primitives;

import me.lauriichan.school.compile.data.json.JsonConverter;

@SuppressWarnings("rawtypes")
public final class ListConverter extends JsonConverter<JsonArray, ArrayList> {

    public ListConverter() {
        super(JsonArray.class, ArrayList.class);
    }

    @Override
    protected JsonArray asJson(ArrayList object) {
        JsonArray array = new JsonArray();
        if (object.isEmpty()) {
            return array;
        }
        for (Object value : object) {
            if (value == null) {
                continue;
            }
            if (!Primitives.isInstance(value)) {
                return array;
            }
            array.add(value);
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected ArrayList fromJson(JsonArray json) {
        ArrayList list = new ArrayList();
        ValueType type = null;
        for (JsonValue<?> value : json) {
            if (value.getType() == ValueType.NULL || (type != null && !value.hasType(type))) {
                continue;
            }
            if (!value.isPrimitive()) {
                continue;
            }
            type = value.getType();
            list.add(value.getValue());
        }
        return list;
    }

}
