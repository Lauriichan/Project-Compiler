package me.lauriichan.school.compile.window.util;

import java.util.Locale;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

import com.syntaxphoenix.syntaxapi.random.NumberGeneratorType;
import com.syntaxphoenix.syntaxapi.random.RandomNumberGenerator;

public final class TestDiagnostic implements Diagnostic<JavaFileObject> {
    
    private static final RandomNumberGenerator RANDOM = NumberGeneratorType.MURMUR.create(System.currentTimeMillis());
    
    private final Kind kind = Kind.values()[RANDOM.nextInt(5)];
    private final long startPosition = RANDOM.nextLong();
    private final long endPosition = RANDOM.nextLong(startPosition, startPosition * 2);
    private final long position = RANDOM.nextLong(startPosition, endPosition);
    private final long lineNumber = RANDOM.nextLong(startPosition / 120, endPosition / 120);
    private final long columnNumber = position % lineNumber;
    private final JavaFileObject object = new TestJavaFileObject();
    
    @Override
    public Kind getKind() {
        return kind;
    }

    @Override
    public JavaFileObject getSource() {
        return object;
    }

    @Override
    public long getPosition() {
        return position;
    }

    @Override
    public long getStartPosition() {
        return startPosition;
    }

    @Override
    public long getEndPosition() {
        return endPosition;
    }

    @Override
    public long getLineNumber() {
        return lineNumber;
    }

    @Override
    public long getColumnNumber() {
        return columnNumber;
    }

    @Override
    public String getCode() {
        return "placeholder(code)";
    }

    @Override
    public String getMessage(Locale locale) {
        return "This is a placeholder line\n2x";
    }

}
