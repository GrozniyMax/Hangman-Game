package backend.academy;

import backend.academy.game.Difficulty;
import backend.academy.io.input.IoManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class IoManagerTest {

    @Mock
    BufferedReader bufferedReader;

    @Mock
    PrintStream printStream;

    private List<String> output = new LinkedList<>();
    private IoManager ioManager;

    @BeforeEach
    void setUp() {
        Mockito.reset(bufferedReader);
        Mockito.reset(printStream);
        output.clear();
        Mockito.doAnswer(invocationOnMock -> {
                output.add((String) invocationOnMock.getArguments()[0]);
                return null;
            }).when(printStream)
            .println(Mockito.anyString());
        ioManager = new IoManager(bufferedReader, printStream);
    }

    @Nested
    public class ReadDifficultyTest {
        /**
         * Сразу корректный ввод. Должно вернуться {@code Difficulty.EASY}
         */
        @Test
        public void correctDifficulty_ShouldReturnEasyImmediately() throws IOException {
            Mockito.when(bufferedReader.readLine()).thenReturn("Легко");

            Difficulty actualDifficulty = ioManager.readDifficulty();

            Assertions.assertEquals(actualDifficulty, Difficulty.EASY);
            org.assertj.core.api.Assertions.assertThat(output.size()).isEqualTo(0);
        }

        /**
         * Корректный ввод со второй попытки. Должен вывести сообщение об ошибке и затем вернуть {@code Difficulty.EASY}
         */
        @Test
        public void correctDifficultyOnSecondTime_ShouldPrintAndThenOnceReturnEasy() throws IOException {
            Mockito.when(bufferedReader.readLine())
                .thenReturn("ghjkl")
                .thenReturn("Легко");

            Difficulty actualDifficulty = ioManager.readDifficulty();

            Assertions.assertEquals(actualDifficulty, Difficulty.EASY);
            org.assertj.core.api.Assertions.assertThat(output.size()).isEqualTo(1);
            assertThat(output.get(0)).isEqualTo("Вы некорректно ввели сложность. Попробуйте еще раз");
        }

        /**
         * Полностью некорректный ввод. Должен вывести сообщение об ошибке 3 раза, потом сообщение о выборе по умолчанию
         * и затем вернуть сложность по умолчанию ({@code Difficulty.EASY}
         */
        @Test
        public void totallyIncorrectInput_ShouldPrintAndThen3timesReturnEasy() throws IOException {
            Mockito.when(bufferedReader.readLine())
                .thenReturn("ghjkl")
                .thenReturn("dfghjk")
                .thenReturn("qwerty");

            Difficulty actualDifficulty = ioManager.readDifficulty();

            Assertions.assertEquals(actualDifficulty, Difficulty.EASY);
            org.assertj.core.api.Assertions.assertThat(output.size()).isEqualTo(4);
        }
    }

    @Nested
    public class ReadBooleanTest {

        public static Stream<Arguments> correctBooleanImmediately() {
            return Stream.of(
                Arguments.of("true", true),
                Arguments.of("yes", true),
                Arguments.of("y", true),
                Arguments.of("д", true),
                Arguments.of("да", true),
                Arguments.of("false", false),
                Arguments.of("no", false),
                Arguments.of("n", false),
                Arguments.of("нет", false),
                Arguments.of("н", false)
            );
        }

        @ParameterizedTest
        @MethodSource("correctBooleanImmediately")
        public void correctInput_ShouldReturnExpectedValueImmediately(String input, boolean expected)
            throws IOException {
            Mockito.when(bufferedReader.readLine())
                .thenReturn(input);

            Boolean actualValue = ioManager.readBoolean("question", true, "error");

            Assertions.assertEquals(expected, actualValue);
            Assertions.assertEquals(output.size(), 0);
        }

        @ParameterizedTest
        @MethodSource("correctBooleanImmediately")
        public void correctInputOnSecondTry_ShouldPrintErrorOnceAndReturnExpectedValue(String input, boolean expected)
            throws IOException {
            Mockito.when(bufferedReader.readLine())
                .thenReturn("qwerty")
                .thenReturn(input);

            Boolean actualValue = ioManager.readBoolean("question", true, "error");

            Assertions.assertEquals(expected, actualValue);
            Assertions.assertEquals(output.size(), 1);
        }

        @Test
        public void totallyIncorrectInput_ShouldPrintAndThen3timesReturnFalse() throws IOException {
            Mockito.when(bufferedReader.readLine())
                .thenReturn("qwerty")
                .thenReturn("asdfgh")
                .thenReturn("zxcvbn");

            Boolean actualValue = ioManager.readBoolean("question", false, "error");

            Assertions.assertFalse(actualValue);
            Assertions.assertEquals(3, output.size());
        }

    }

}
