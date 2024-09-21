package backend.academy.wordStoragesTest;

import backend.academy.word.Word;
import backend.academy.word.storage.MultilanguageWordStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MultiLanguageWordStorageTest {


    @Test
    public void unsupportedLocale_ShouldHaveEnglishLocale() {
        MultilanguageWordStorage wordStorage = new MultilanguageWordStorage(Locale.FRENCH, "multiLanguageWordStorageTest");
        var actualLocale = wordStorage.getCurrentLocale();

        var expectedLocale = Locale.ENGLISH;

        Assertions.assertEquals(actualLocale, expectedLocale);
    }

    @Test
    public void supportedLocale_ShouldHaveRussianLocale() {
        MultilanguageWordStorage wordStorage = new MultilanguageWordStorage(Locale.of("ru"), "multiLanguageWordStorageTest");
        var actualLocale = wordStorage.getCurrentLocale();

        var expectedLocale = Locale.of("ru");

        Assertions.assertEquals(actualLocale, expectedLocale);
    }

    @Test
    public void en_ShouldHaveExpectedInnerStructure(){
        MultilanguageWordStorage wordStorage = new MultilanguageWordStorage(Locale.ENGLISH, "multiLanguageWordStorageTest");
        var actualInnerStructure = wordStorage.words();

        var expectedFruitCategory = List.of(new Word("apple", "fruit", new String[]{"red","green"}),
            new Word("orange", "fruit", new String[]{"orange"}));
        var expectedThingsCategory = List.of(new Word("pen", "things", new String[]{"black", "blue"}));
        var expectedInnerMap = Map.of("FRUIT", expectedFruitCategory, "THINGS", expectedThingsCategory);

        Assertions.assertEquals(actualInnerStructure, expectedInnerMap);
    }

    @Test
    public void ru_ShouldHaveExpectedInnerStructure(){
        MultilanguageWordStorage wordStorage = new MultilanguageWordStorage(Locale.of("ru"), "multiLanguageWordStorageTest");
        var actualInnerStructure = wordStorage.words();

        var expectedFruitCategory = List.of(new Word("яблоко", "фрукты", new String[]{"зеленое","красное"}),
            new Word("апельсин", "фрукты", new String[]{"оранжевый"}));
        var expectedThingsCategory = List.of(new Word("ручка", "вещи", new String[]{"черная", "синяя"}));
        var expectedInnerMap = Map.of("ФРУКТЫ", expectedFruitCategory, "ВЕЩИ", expectedThingsCategory);

        Assertions.assertEquals(actualInnerStructure, expectedInnerMap);
    }

}
