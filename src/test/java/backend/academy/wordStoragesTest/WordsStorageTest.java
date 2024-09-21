package backend.academy.wordStoragesTest;

import backend.academy.word.Word;
import backend.academy.word.storage.WordsStorage;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WordsStorageTest {

    @Test
    public void loadFromProperties_ShouldHaveOnlyOneWord() {
        Properties properties = new Properties();
        properties.setProperty("1.word", "apple");
        properties.setProperty("1.category", "fruit");
        properties.setProperty("1.tips", "red;green");

        WordsStorage storage = new WordsStorage();
        storage.load(properties);

        assertTrue(storage.hasCategory("FRUIT"));
        assertEquals(1, storage.words().get("FRUIT").size());
    }

    @Test
    public void loadFromCorrectProperties_ShouldHaveExpectedInnerStructure() {
        Properties properties = new Properties();
        properties.setProperty("1.word", "apple");
        properties.setProperty("1.category", "fruit");
        properties.setProperty("1.tips", "red;green");
        properties.setProperty("2.word", "pen");
        properties.setProperty("2.category", "things");
        properties.setProperty("2.tips", "black;blue");
        properties.setProperty("3.word", "orange");
        properties.setProperty("3.category", "fruit");
        properties.setProperty("3.tips", "orange");

        WordsStorage storage = new WordsStorage();
        storage.load(properties);
        var expectedFruitCategory = List.of(new Word("apple", "fruit", new String[]{"red","green"}),
            new Word("orange", "fruit", new String[]{"orange"}));
        var expectedThingsCategory = List.of(new Word("pen", "things", new String[]{"black", "blue"}));
        var expectedInnerMap = Map.of("FRUIT", expectedFruitCategory, "THINGS", expectedThingsCategory);

        assertEquals(storage.words(), expectedInnerMap);
    }


    @Test
    public void loadFromCorrectProperties_ShouldHave2Categories() {
        Properties properties = new Properties();
        properties.setProperty("1.word", "apple");
        properties.setProperty("1.category", "fruit");

        WordsStorage storage = new WordsStorage();
        storage.load(properties);

        assertTrue(storage.hasCategory("FRUIT"));
        assertFalse(storage.hasCategory("VEGETABLE"));
    }

    @Test
    public void loadFromIncorrectProperties_ShouldHaveNoCategories() {
        Properties properties = new Properties();
        properties.setProperty("1.word", "apple");

        WordsStorage storage = new WordsStorage();
        storage.load(properties);

        assertFalse(storage.hasCategory("FRUIT"));
    }

    @Test
    public void emptyWords_ShouldThrowNullPointerException() {
        Properties properties = new Properties();
        properties.setProperty("1.word", "apple");
        properties.setProperty("1.category", "fruit");

        WordsStorage storage = new WordsStorage();
        storage.load(properties);

        Exception exception = assertThrows(NullPointerException.class, () -> {
            storage.getRandomWord("VEGETABLE");
        });
    }

    @Test
    public void getRandomWordFromNonExistingCategory_ShouldThrowNullPointerException() {
        Properties properties = new Properties();
        properties.setProperty("1.word", "apple");
        properties.setProperty("1.category", "fruit");

        WordsStorage storage = new WordsStorage();
        storage.load(properties);

        Exception exception = assertThrows(NullPointerException.class, () -> {
            storage.getRandomWord("NON_EXISTENT");
        });

        String expectedMessage = "Cannot invoke \"java.util.List.size()\" because \"list\" is null";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

}
