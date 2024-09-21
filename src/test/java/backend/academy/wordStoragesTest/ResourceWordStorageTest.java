package backend.academy.wordStoragesTest;

import backend.academy.word.Word;
import backend.academy.word.storage.ResourceWordStorage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ResourceWordStorageTest {

    private static final String resourcePath = "/resource-word-storage.properties";

    private static ResourceWordStorage resourceWordStorage;

    @BeforeAll
    static void setUp() throws IOException {
        resourceWordStorage = new ResourceWordStorage(resourcePath);
    }


    @Test
    public void wordsStorage_ShouldHaveExpectedInnerStructure(){
        var actualInnerStructure = resourceWordStorage.words();

        var expectedFruitCategory = List.of(new Word("apple", "fruit", new String[]{"red","green"}),
            new Word("orange", "fruit", new String[]{"orange"}));
        var expectedThingsCategory = List.of(new Word("pen", "things", new String[]{"black", "blue"}));
        var expectedInnerMap = Map.of("FRUIT", expectedFruitCategory, "THINGS", expectedThingsCategory);

        Assertions.assertEquals(actualInnerStructure, expectedInnerMap);
    }

    @Test
    public void wordsStorage_ShouldHaveSize3(){
        int actualSize= resourceWordStorage.words().values().stream().map(Collection::size).reduce(Integer::sum).get();

        Assertions.assertEquals(actualSize, 3);
    }
}
