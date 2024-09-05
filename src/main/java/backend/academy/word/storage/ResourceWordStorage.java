package backend.academy.word.storage;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

/**
 * Хранилище слов в виде файла word.properties в resources
 */

public class ResourceWordStorage extends WordsStorage {

    public ResourceWordStorage() throws IOException {
        Properties properties = new Properties();
        properties.load(new InputStreamReader(Main.class.getResourceAsStream("/words.properties"), StandardCharsets.UTF_8));
        this.load(properties);

    }
}
