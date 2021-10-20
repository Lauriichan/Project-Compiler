package me.lauriichan.school.compile.data.converter;

import java.io.File;

import com.syntaxphoenix.syntaxapi.json.value.JsonString;

import me.lauriichan.school.compile.data.json.JsonConverter;

public final class FileConverter extends JsonConverter<JsonString, File> {

    public FileConverter() {
        super(JsonString.class, File.class);
    }

    @Override
    protected JsonString asJson(File object) {
        return new JsonString(object.getPath());
    }

    @Override
    protected File fromJson(JsonString json) {
        return new File(json.getValue());
    }

}
