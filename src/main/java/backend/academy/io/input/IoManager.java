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

    // Предложения к вводу
    private String difficutyInputMsg = "Введите сложность из набора(цифра - количество жизней)";
    private String needTipsInputMsg = "Нужны ли вам подсказки?";
    private String booleanInputMsg=" Введите (да/нет) :";
    private String letterInputMsg="Введите букву: ";
    private String categoryInputMsg="Выберите категорию слов из набора ";

    // Сообщения о инвалидном вводе
    private String invalidDifficultyInputMsg="Вы некорректно ввели сложность. Попробуйте еще раз";
    private String invalidBooleanInputMsg="Вы некорректное значение. Попробуйте еще раз";
    private String invalidLetterInputMsg="Вы ввели букву некорректно. Попробуйте еще раз. Необходимо вести 1 букву в любом регистре";
    private String invalidCategoryInputMsg="Вы ввели категорию неправильно. Пожалуйста попробуйте еще раз";


    // Сообщения о выборе сообщения по умолчанию
    private String defaultDifficultyInputMsg1="Была выбрана сложность ";
    private String defaultDifficultyInputMsg2=" как сложность по умолчанию";
    private String defaultBooleanInputMsg="Было установлено значение по умолчанию ";
    private String defaultCategoryInputMsg="Была выбрана категория по умолчанию: ";


    //Дополнительные сообщения
    private String wrongAlphabetMsg = "Вы ввели букву из другого алфавита. Игра поддерживает только русские буквы";

    public static IoManager defaultIoManager() {
        return new IoManager(new BufferedReader(new InputStreamReader(System.in)),
            new PrintStream(System.out));
    }

    public Difficulty readDifficulty() throws IOException {
        for (int i = 0; i < INCORRECT_ATTEMPS_COUNT; i++) {
            try {
                printStream.print(difficutyInputMsg + Arrays.toString(Difficulty.values()) + " :");
                return Difficulty.valueOfRussian(bufferedReader.readLine().strip().toUpperCase());
            } catch (IllegalArgumentException e) {
                printStream.println(invalidDifficultyInputMsg);
            }
        }

        printStream.println(defaultDifficultyInputMsg1 + Difficulty.EASY + defaultDifficultyInputMsg2);
        return Difficulty.EASY;
    }

    /**
     * Метод для чтения необходимости вводить подсказки
     */
    public Boolean readNeedTips() throws IOException {
        return readBoolean(needTipsInputMsg, true, invalidBooleanInputMsg);
    }

    public Boolean readBoolean(String question, Boolean defaultValue, String invalidInputText) throws IOException {
        for (int i = 0; i < INCORRECT_ATTEMPS_COUNT; i++) {
            printStream.print(question + booleanInputMsg);
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
        printStream.printf(defaultBooleanInputMsg +  defaultValue);
        return defaultValue;
    }

    public Character readLetter() throws IOException {
        printStream.print(letterInputMsg);
        String line = bufferedReader.readLine().strip().toUpperCase();
        if (line.length() == 1) {

            if ((Character.isLetter(line.charAt(0))) && Character.UnicodeBlock.of(line.charAt(0)).equals(Character.UnicodeBlock.CYRILLIC)) {
                return line.charAt(0);
            }else {
                printStream.print(wrongAlphabetMsg);
                return readLetter();
            }
        }
        else {
            printStream.println(invalidLetterInputMsg);
            // Я надеюсь не надо задумываться над тем,
            // что пользователь будет так долго вводить неправильно, что выскочит StackOverFlowException
            return readLetter();
        }
    }

    public String readCategory(WordsStorage wordsStorage) throws IOException {
        for (int i = 0; i < INCORRECT_ATTEMPS_COUNT; i++) {
            printStream.print( categoryInputMsg+ wordsStorage.categories()  + " :");
            String line = bufferedReader.readLine().strip().toUpperCase();
            if (wordsStorage.hasCategory(line)) {
                return line;
            } else {
                System.out.println(invalidCategoryInputMsg);
            }
        }
        String category = wordsStorage.getRandomCategory();
        printStream.println( defaultCategoryInputMsg+ category);
        return category;

    }

    public void print(String text) {
        printStream.println(text);
    }

}
