package backend.academy.word.storage;

import backend.academy.Main;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Properties;
import lombok.extern.log4j.Log4j2;

/**
 * Хранилище слов в виде файла word.properties в resources
 */

@Log4j2
public class ResourceWordStorage extends WordsStorage {

    public ResourceWordStorage(String path) throws IOException {
        Properties properties = new Properties();

        var stream = Main.class.getResourceAsStream(path);
        if (Objects.isNull(stream)) {
            log.warn("Cannot find words.properties file");
        }
        properties.load(
            new InputStreamReader(Main.class.getResourceAsStream(path), StandardCharsets.UTF_8));
        this.load(properties);
    }
}
