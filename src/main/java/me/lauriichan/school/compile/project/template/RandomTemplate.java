package me.lauriichan.school.compile.project.template;

import java.io.File;

import com.syntaxphoenix.syntaxapi.random.NumberGeneratorType;
import com.syntaxphoenix.syntaxapi.random.RandomNumberGenerator;

final class RandomTemplate extends Template {

    private final RandomNumberGenerator random = NumberGeneratorType.MURMUR.create(System.currentTimeMillis());

    public RandomTemplate() {
        super("Zufälliges Template");
    }

    @Override
    public void setup(String packet, File directory) {
        Template.TEMPLATES.get(random.nextInt(Template.TEMPLATES.size() - 1) + 1).setup(packet, directory);
    }

}
