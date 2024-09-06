package backend.academy.game;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;

@Builder
@Getter
@RequiredArgsConstructor(onConstructor_ = {@NonNull})
public class GameSetupParams {

    @NonNull
    private String category;
    @NonNull
    private Difficulty difficulty;
    @NonNull
    private Boolean needTips;

}
