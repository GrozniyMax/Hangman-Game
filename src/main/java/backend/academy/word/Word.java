package backend.academy.word;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jspecify.annotations.Nullable;

@ToString
@RequiredArgsConstructor
public class Word {

    @Getter
    private String word;

    @Getter
    private String category;

    @Nullable
    private List<String> tips = new ArrayList<>();

    public Word(String word, String category, @Nullable String[] tips) {
        this.word = Objects.requireNonNull(word).toUpperCase();
        this.category = Objects.requireNonNull(category).toUpperCase();
        this.tips = tips == null ? new ArrayList<>() : Arrays.asList(tips);
    }

    /**
     * Метод возвращает подсказку
     *
     * @param index индекс подсказки, если она есть.
     * @return Подсказку если есть, иначе null
     */
    public String getTip(int index) {
        try {
            return tips.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    public boolean hasTip(int index) {
        return (Optional.ofNullable(tips).isPresent()) && (index >= 0 && index < tips.size());
    }

    public boolean hasTips() {
        return (Optional.ofNullable(tips).isPresent()) && (!tips.isEmpty());
    }

    @Override public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Word word1 = (Word) o;

        return word.equalsIgnoreCase(word1.word) && category.equalsIgnoreCase(word1.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(word, category, tips);
    }
}
