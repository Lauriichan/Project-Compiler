package me.lauriichan.school.compile.util.file.java;

public enum Security {

    PUBLIC,
    PROTECTED,
    PRIVATE,
    PACKAGE;
    
    private final String low;
    
    private Security() {
        this.low = name().toLowerCase();
    }
    
    public String asName() {
        return low;
    }

}
