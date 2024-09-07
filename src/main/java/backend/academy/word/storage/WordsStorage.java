package backend.academy.word.storage;

import backend.academy.word.Word;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

@Getter @ToString
@Log4j2
public class WordsStorage {

    private Map<String, List<Word>> words = new HashMap<>();


    public void load(Properties properties) {

        long wordCount = properties.keySet().stream().map(Object::toString).filter(s -> s.contains("word")).count();

        Word w;
        for (Long i=1L; i <= wordCount; i++) {
            try {
                w = new Word(
                    properties.getProperty(createKey(i,"word")).toUpperCase(),
                    properties.getProperty(createKey(i,"category")).toUpperCase(),
                    Optional.ofNullable(properties.getProperty(createKey(i, "tips")))
                        .map((tips)->tips.split(" *; *"))
                        .get());

                words.putIfAbsent(w.category(), new ArrayList<>());
                words.get(w.category()).add(w);
            } catch (NullPointerException e) {
                // Если что то не так с этим словом, то его удобнее пропустить
                log.warn("Error while loading word {} with property {}", i, e.getMessage());
            }
        }

    }

    public void load(String filepath) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(filepath));
    }

    public Word getRandomWord(String category) {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        List<Word> list = words.get(category);
        return list.get(random.nextInt(0, list.size()));
    }

    private String createKey(Object ... parts){
        StringBuilder key = new StringBuilder();
        Arrays.stream(parts).limit(parts.length-1).forEach((part)->key.append(part).append('.'));
        key.append(parts[parts.length-1]);
        return key.toString();
    }

    public Boolean hasCategory(String category) {
        return words.containsKey(category);
    }

    public Set<String> categories(){
        return words.keySet();
    }

    public String getRandomCategory() {
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());

        return words.keySet().stream().toList().get(random.nextInt(0,words.size()));
    }

    public Locale getCurrentLocale() {
        return Locale.of("ru");
    }
}
