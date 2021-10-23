package me.lauriichan.school.compile.project.template;

public abstract class HamsterTemplate extends Template {

    public HamsterTemplate(String name) {
        super("Hamster", name);
    }

    public HamsterTemplate(String name, boolean hidden) {
        super("Hamster", name, hidden);
    }

}
