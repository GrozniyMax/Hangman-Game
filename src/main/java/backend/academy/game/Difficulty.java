package backend.academy.game;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Difficulty {

    EASY(10, "Легко"),
    MEDIUM(5, "Средне"),
    HARD(3, "Сложно");

    private final int triesCount;
    private final String russianName;

    public int calculateStep(Integer maxTriesCount){
        if ((maxTriesCount<0)||(maxTriesCount<triesCount)) throw new IllegalArgumentException("Argument must be positive");
        return maxTriesCount/ triesCount;
    }

    @Override
    public String toString() {
        return this.russianName()+String.format(" - %d ", triesCount);
    }

    public static Difficulty valueOfRussian(String russianName){
        return Arrays.stream(Difficulty.values())
            .filter(difficulty -> difficulty.russianName.equalsIgnoreCase(russianName))
            .findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
