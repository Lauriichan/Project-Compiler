package me.lauriichan.school.compile.data.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.stream.Collectors;

import com.syntaxphoenix.syntaxapi.json.JsonValue;
import com.syntaxphoenix.syntaxapi.json.io.JsonParser;
import com.syntaxphoenix.syntaxapi.utils.io.TextDeserializer;

final class JsonSerializer implements TextDeserializer<JsonValue<?>> {

    private final JsonParser parser;

    public JsonSerializer(JsonParser parser) {
        this.parser = parser;
    }

    @Override
    public JsonValue<?> fromReader(Reader reader) throws IOException {
        return parser.fromReader(new StringReader(asString(reader)));
    }

    private String asString(Reader reader) {
        return asBuffered(reader).lines().collect(Collectors.joining(System.lineSeparator()));
    }

    private BufferedReader asBuffered(Reader reader) {
        return reader instanceof BufferedReader ? (BufferedReader) reader : new BufferedReader(reader);
    }

}
