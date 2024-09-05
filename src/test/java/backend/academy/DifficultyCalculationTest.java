package backend.academy;

import backend.academy.game.Difficulty;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

public class DifficultyCalculationTest {

    //Функции поставщики для тестов
    static Stream<Arguments> validDataProviderForEasyTest() {
        return Stream.of(
            Arguments.of(10, 1),
            Arguments.of(11, 1),
            Arguments.of(15, 1),
            Arguments.of(19, 1),
            Arguments.of(20, 2),
            Arguments.of(29, 2),
            Arguments.of(30, 3));
    }

    static Stream<Arguments> validDataProviderForMediumTest() {
        return Stream.of(
            Arguments.of(5, 1),
            Arguments.of(9, 1),
            Arguments.of(10, 2),
            Arguments.of(14, 2),
            Arguments.of(15, 3),
            Arguments.of(18, 3),
            Arguments.of(20, 4),
            Arguments.of(21, 4)
        );
    }

    static Stream<Arguments> validDataProviderForHardTest() {
        return Stream.of(
            Arguments.of(3, 1),
            Arguments.of(5, 1),
            Arguments.of(6, 2),
            Arguments.of(7, 2),
            Arguments.of(9, 3)
        );
    }

    //Тесты для валидных аргументов

    @ParameterizedTest
    @MethodSource("validDataProviderForEasyTest")
    void Easy_ValidArgument_ShouldReturnExpectedValue(Integer givenMaxTriesCount, Integer expectedFramesStep) {
        Difficulty difficulty = Difficulty.EASY;

        int actualFrameStep = difficulty.calculateStep(givenMaxTriesCount);

        assertEquals(expectedFramesStep, actualFrameStep);
    }

    @ParameterizedTest
    @MethodSource("validDataProviderForMediumTest")
    void Medium_ValidArgument_ShouldReturnExpectedValue(Integer givenMaxTriesCount, Integer expectedFramesStep) {
        Difficulty difficulty = Difficulty.MEDIUM;

        int actualFrameStep = difficulty.calculateStep(givenMaxTriesCount);

        assertEquals(expectedFramesStep, actualFrameStep);
    }

    @ParameterizedTest
    @MethodSource("validDataProviderForHardTest")
    void Hard_ValidArgument_ShouldReturnExpectedValue(Integer givenMaxTriesCount, Integer expectedFramesStep) {
        Difficulty difficulty = Difficulty.HARD;

        int actualFrameStep = difficulty.calculateStep(givenMaxTriesCount);

        assertEquals(expectedFramesStep, actualFrameStep);
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -100500, 0, 1, 4, 5, 9})
    void Easy_InvalidArgument_ShouldThrowIllegalArgumentException(int givenMaxTriesCount) {
        Difficulty difficulty = Difficulty.EASY;

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> difficulty.calculateStep(givenMaxTriesCount))
            .withMessage("Argument must be positive");
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -100500, 0, 1, 3, 4})
    void Medium_InvalidArgument_ShouldThrowIllegalArgumentException(int givenMaxTriesCount) {
        Difficulty difficulty = Difficulty.EASY;

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> difficulty.calculateStep(givenMaxTriesCount))
            .withMessage("Argument must be positive");
    }

    @ParameterizedTest
    @ValueSource(ints = {Integer.MIN_VALUE, -100500, 0, 1, 2, 3})
    void Hard_InvalidArgument_ShouldThrowIllegalArgumentException(int givenMaxTriesCount) {
        Difficulty difficulty = Difficulty.EASY;

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> difficulty.calculateStep(givenMaxTriesCount))
            .withMessage("Argument must be positive");
    }



}
