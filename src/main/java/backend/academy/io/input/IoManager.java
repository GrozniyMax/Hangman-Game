package backend.academy.io.input;

import backend.academy.game.Difficulty;
import backend.academy.word.storage.WordsStorage;
import lombok.RequiredArgsConstructor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;

@RequiredArgsConstructor
public class IoManager {

    private static final int INCORRECT_ATTEMPS_COUNT = 3;

    private final BufferedReader bufferedReader;
    private final PrintStream printStream;

    public static IoManager defaultIoManager() {
        return new IoManager(new BufferedReader(new InputStreamReader(System.in)),
            new PrintStream(System.out));
    }

    public Difficulty readDifficulty() throws IOException {
        for (int i = 0; i < INCORRECT_ATTEMPS_COUNT; i++) {
            try {
                printStream.print("Введите сложность из набора(цифра - количество жизней)" + Arrays.toString(Difficulty.values()) + " :");
                return Difficulty.valueOfRussian(bufferedReader.readLine().strip().toUpperCase());
            } catch (IllegalArgumentException e) {
                printStream.println("Вы некорректно ввели сложность. Попробуйте еще раз");
            }
        }

        printStream.println("Была выбрана сложность " + Difficulty.EASY + " как сложность по умолчанию");
        return Difficulty.EASY;
    }

    /**
     * Метод для чтения необходимости вводить подсказки
     */
    public Boolean readNeedTips() throws IOException {
        return readBoolean("Нужны ли вам подсказки?", true, "Вы некорректное значение. Попробуйте еще раз");
    }

    public Boolean readBoolean(String question, Boolean defaultValue, String invalidInputText) throws IOException {
        for (int i = 0; i < INCORRECT_ATTEMPS_COUNT; i++) {
            printStream.print(question + " Введите (да/нет) :");
            String input = bufferedReader.readLine().strip();
            if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("yes") ||
                input.equalsIgnoreCase("y") ||
                input.equalsIgnoreCase("да") || input.equalsIgnoreCase("д")) {
                return true;
            } else if (input.equalsIgnoreCase("false") || input.equalsIgnoreCase("no") ||
                input.equalsIgnoreCase("n") || input.equalsIgnoreCase("нет") ||
                input.equalsIgnoreCase("н")) {
                return false;
            } else {
                printStream.println(invalidInputText);
            }
        }
        printStream.printf("Было установлено значение по умолчанию (%b)", defaultValue);
        return defaultValue;
    }

    public Character readLetter() throws IOException {
        printStream.print("Введите букву: ");
        String line = bufferedReader.readLine().strip().toUpperCase();
        if (line.length() == 1) {

            if ((Character.isLetter(line.charAt(0))) && Character.UnicodeBlock.of(line.charAt(0)).equals(Character.UnicodeBlock.CYRILLIC)) {
                return line.charAt(0);
            }else {
                printStream.print("Вы ввели букву из другого алфавита. Игра поддерживает только русские буквы");
                return readLetter();
            }
        }
        else {

            printStream.println("Вы ввели букву некорректно. Попробуйте еще раз." +
                " Необходимо вести 1 букву в любом регистре");
            // Я надеюсь не надо задумываться над тем,
            // что пользователь будет так долго вводить неправильно, что выскочит StackOverFlowException
            return readLetter();
        }
    }

    public String readCategory(WordsStorage wordsStorage) throws IOException {
        for (int i = 0; i < INCORRECT_ATTEMPS_COUNT; i++) {
            printStream.print("Выберите категорию слов из набора " + wordsStorage.categories()  + " :");
            String line = bufferedReader.readLine().strip().toUpperCase();
            if (wordsStorage.hasCategory(line)) {
                return line;
            } else {
                System.out.println("Вы ввели категорию неправильно. Пожалуйста попробуйте еще раз");
            }
        }
        String category = wordsStorage.getRandomCategory();
        printStream.println("Была выбрана категория по умолчанию: " + category);
        return category;

    }

    public void print(String text) {
        printStream.println(text);
    }

}
