package backend.academy;

import backend.academy.word.Word;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WordTest {

    @Test
    public void test_initialization_with_valid_data() {
        Word word = new Word("apple", "fruit", new String[] {"red", "green"});
        assertEquals("APPLE", word.word());
        assertEquals("FRUIT", word.category());
    }

    // Word object initialization with null tips
    @Test
    public void test_initialization_with_null_tips() {
        Word word = new Word("pen", "things", null);
        assertEquals("PEN", word.word());
        assertEquals("THINGS", word.category());
    }

    // Retrieving a tip within the valid index range
    @Test
    public void test_get_tip_within_valid_index() {
        Word word = new Word("apple", "fruit", new String[] {"red", "green"});
        assertEquals("red", word.getTip(0));
        assertEquals("green", word.getTip(1));
    }

    // Checking if a tip exists within the valid index range
    @Test
    public void test_has_tip_within_valid_index() {
        Word word = new Word("apple", "fruit", new String[] {"red", "green"});
        assertTrue(word.hasTip(0));
        assertTrue(word.hasTip(1));
    }

    // Retrieving a tip with an index out of bounds
    @Test
    public void test_get_tip_out_of_bounds() {
        Word word = new Word("apple", "fruit", new String[] {"red", "green"});
        assertNull(word.getTip(2));
    }

    // Retrieving a tip when tips list is empty
    @Test
    public void test_get_tip_when_tips_empty() {
        Word word = new Word("pen", "things", null);
        assertNull(word.getTip(0));
    }

    // Checking if a tip exists with a negative index
    @Test
    public void test_has_tip_with_negative_index() {
        Word word = new Word("apple", "fruit", new String[] {"red", "green"});
        assertFalse(word.hasTip(-1));
    }

    @Test
    public void test_has_tip_out_of_bounds() {
        Word word = new Word("apple", "fruit", new String[] {"red", "green"});
        assertFalse(word.hasTip(2));
    }

}
