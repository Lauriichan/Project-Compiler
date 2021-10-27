package me.lauriichan.school.compile.data.translation;

import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.apache.commons.io.file.PathUtils;

import com.syntaxphoenix.syntaxapi.json.JsonValue;
import com.syntaxphoenix.syntaxapi.utils.java.Streams;

import me.lauriichan.school.compile.Main;
import me.lauriichan.school.compile.data.Serialize;
import me.lauriichan.school.compile.data.json.JsonIO;
import me.lauriichan.school.compile.util.UserSettings;

public final class Translation {

    private static final ArrayList<Translation> TRANSLATIONS = new ArrayList<>();
    private static final Translation EMPTY = new Translation();

    public static void load() {
        try {
            URI uri = Main.class.getResource("/translation").toURI();
            Path root = uri.getScheme().equals("jar") ? FileSystems.getFileSystem(uri).getPath("/translation") : Paths.get(uri);
            Stream<Path> walk = Files.walk(root, 1);
            for (Iterator<Path> iterator = walk.iterator(); iterator.hasNext();) {
                Path path = iterator.next();
                if (PathUtils.isDirectory(path)) {
                    continue;
                }
                String content = Streams.toString(path.toUri().toURL().openStream());
                JsonValue<?> value = JsonIO.PARSER.fromString(content);
                Object object = JsonIO.toObject(value, Translation.class);
                if (object == null || !(object instanceof Translation)) {
                    continue;
                }
                Translation translation = (Translation) object;
                System.out.println(translation.getName() + " / " + translation.getCode());
                TRANSLATIONS.add(translation);
            }
            walk.close();
        } catch (Exception exp) {
            System.err.println("Failed to load translations");
            System.err.println(exp);
        }
    }

    public static String getDefaultCode() {
        String code = UserSettings.getString("language");
        return (!code.isEmpty() && has(code)) ? code : "en-uk";
    }

    public static void setDefaultCode(String name) {
        UserSettings.setString("language", name);
    }

    public static Translation getDefault() {
        Translation translation = get(getDefaultCode());
        return translation == null ? EMPTY : translation;
    }

    public static void setDefault(Translation project) {
        setDefaultCode(project.getCode());
    }

    public static Translation get(String name) {
        for (int index = 0; index < TRANSLATIONS.size(); index++) {
            Translation translation = TRANSLATIONS.get(index);
            if (translation.getCode().equalsIgnoreCase(name) || translation.getName().equalsIgnoreCase(name)) {
                return translation;
            }
        }
        return null;
    }

    public static boolean has(String name) {
        return get(name) != null;
    }

    @Serialize
    private final String code = "";
    @Serialize
    private final String name = "";
    @Serialize
    private final HashMap<String, String> keys = new HashMap<>();

    public String translate(String text) {
        int count = 0;
        for (Entry<String, String> entry : keys.entrySet()) {
            if (!text.contains(entry.getKey())) {
                continue;
            }
            text = text.replace(entry.getKey(), entry.getValue());
            count++;
        }
        return count == 0 ? text : translate(text);
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

}
