package me.lauriichan.school.compile.project;

public final class ProjectInfo {
    
    private final String name;
    private final String packageName;
    private final String directory;
    
    public ProjectInfo(String name, String packageName, String directory) {
        this.name = name;
        this.packageName = packageName;
        this.directory = directory;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPackageName() {
        return packageName;
    }
    
    public String getDirectory() {
        return directory;
    }

}
