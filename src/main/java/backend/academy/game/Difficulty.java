package backend.academy.game;

import backend.academy.lozalization.Localize;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.Arrays;

@Getter
@RequiredArgsConstructor
@Localize("localization.Difficulty")
public enum Difficulty {

    EASY(10, "Легко"),
    MEDIUM(5, "Средне"),
    HARD(3, "Сложно");

    private final int triesCount;

    @Localize()
    private final String localizedName;

    public int calculateStep(Integer maxTriesCount){
        if (maxTriesCount<0) throw new IllegalArgumentException("Argument must be positive");
        if (maxTriesCount<triesCount) throw new IllegalArgumentException("maxTriesCount must be greater than tries count");
        return maxTriesCount/triesCount;
    }

    @Override
    public String toString() {
        return this.localizedName()+String.format(" - %d ", triesCount);
    }

    public static Difficulty valueOfLocalizeName(String localizedName){
        return Arrays.stream(Difficulty.values())
            .filter(difficulty -> difficulty.localizedName.equalsIgnoreCase(localizedName))
            .findFirst().orElseThrow(IllegalArgumentException::new);
    }
}
