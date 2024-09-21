package backend.academy.io.input;

import backend.academy.game.Difficulty;
import backend.academy.lozalization.Localize;
import backend.academy.word.storage.MultilanguageWordStorage;
import backend.academy.word.storage.WordsStorage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Locale;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Localize("localization.IoManager")
public class IoManager {

    private static final int INCORRECT_ATTEMPS_COUNT = 3;

    private final BufferedReader bufferedReader;
    private final PrintStream printStream;

    private static final IoManager DEFAULT_IO_MANAGER =
        new IoManager(new BufferedReader(new InputStreamReader(System.in)),
            new PrintStream(System.out));

    public static IoManager defaultIoManager() {
        return DEFAULT_IO_MANAGER;
    }

    @Setter
    private Locale locale;

    //Выводимые сообщения

    // Предложения к вводу
    @Localize
    private String difficutyInputMsg = "Введите сложность из набора(цифра - количество жизней)";
    @Localize
    private String needTipsInputMsg = "Нужны ли вам подсказки?";
    @Localize
    private String booleanInputMsg = " Введите (да/нет) :";
    @Localize
    private String letterInputMsg = "Введите букву: ";
    @Localize
    private String categoryInputMsg = "Выберите категорию слов из набора ";
    @Localize
    private String languageInputMsg = "Введите язык, слова из которого хотите указывать";

    // Сообщения о инвалидном вводе
    @Localize
    private String invalidDifficultyInputMsg = "Вы некорректно ввели сложность. Попробуйте еще раз";
    @Localize
    private String invalidBooleanInputMsg = "Вы некорректное значение. Попробуйте еще раз";
    @Localize
    private String invalidLetterInputMsg =
        "Вы ввели букву некорректно. Попробуйте еще раз. Необходимо вести 1 букву в любом регистре";
    @Localize
    private String invalidCategoryInputMsg = "Вы ввели категорию неправильно. Пожалуйста попробуйте еще раз";

    @Localize
    private String invalidLanguageInputMsg = "Простите, но такой язык не поддерживается";

    // Сообщения о выборе сообщения по умолчанию
    @Localize
    private String defaultDifficultyInputMsg1 = "Была выбрана сложность ";
    @Localize
    private String defaultDifficultyInputMsg2 = " как сложность по умолчанию";
    @Localize
    private String defaultBooleanInputMsg = "Было установлено значение по умолчанию ";
    @Localize
    private String defaultCategoryInputMsg = "Была выбрана категория по умолчанию: ";

    @Localize
    private String defaultLanguageInputMsg = "Был выбран язык по умолчанию";

    //Дополнительные сообщения
    @Localize
    private String wrongAlphabetMsg = "Вы ввели букву из другого алфавита. Игра поддерживает только русские буквы";

    public Difficulty readDifficulty() throws IOException {
        for (int i = 0; i < INCORRECT_ATTEMPS_COUNT; i++) {
            try {
                printStream.print(difficutyInputMsg + Arrays.toString(Difficulty.values()) + " :");
                return Difficulty.valueOfLocalizeName(bufferedReader.readLine().strip().toUpperCase());
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
            if (input.equalsIgnoreCase("true") || input.equalsIgnoreCase("yes")
                || input.equalsIgnoreCase("y") || input.equalsIgnoreCase("да")
                || input.equalsIgnoreCase("д")) {
                return true;
            } else if (input.equalsIgnoreCase("false") || input.equalsIgnoreCase("no")
                || input.equalsIgnoreCase("n") || input.equalsIgnoreCase("нет")
                || input.equalsIgnoreCase("н")) {
                return false;
            } else {
                printStream.println(invalidInputText);
            }
        }
        printStream.printf(defaultBooleanInputMsg + defaultValue);
        return defaultValue;
    }

    public Character readLetter() throws IOException {
        printStream.print(letterInputMsg);
        String line = bufferedReader.readLine().strip().toUpperCase();
        if (line.length() == 1) {

            if ((Character.isLetter(line.charAt(0)))
                && Character.UnicodeBlock.of(line.charAt(0)).equals(MultilanguageWordStorage.ofLanguage(locale))) {
                return line.charAt(0);
            } else {
                printStream.println(wrongAlphabetMsg);
                return readLetter();
            }
        } else {
            printStream.println(invalidLetterInputMsg);
            // Я надеюсь не надо задумываться над тем,
            // что пользователь будет так долго вводить неправильно, что выскочит StackOverFlowException
            return readLetter();
        }
    }

    public String readCategory(WordsStorage wordsStorage) throws IOException {
        for (int i = 0; i < INCORRECT_ATTEMPS_COUNT; i++) {
            printStream.print(categoryInputMsg + wordsStorage.categories() + " :");
            String line = bufferedReader.readLine().strip().toUpperCase();
            if (wordsStorage.hasCategory(line)) {
                return line;
            } else {
                printStream.println(invalidCategoryInputMsg);
            }
        }
        String category = wordsStorage.getRandomCategory();
        printStream.println(defaultCategoryInputMsg + category);
        return category;
    }

    public Locale readLocale() throws IOException {
        String input;
        Locale readLocale;
        for (int i = 0; i < INCORRECT_ATTEMPS_COUNT; i++) {
            try {
                printStream.print(languageInputMsg + ": ");
                input = bufferedReader.readLine().strip();
                readLocale = Locale.of(input);
                if (MultilanguageWordStorage.hasLanguage(readLocale)) {
                    return readLocale;
                }
            } catch (Exception e) {
                printStream.println(wrongAlphabetMsg);
            }
            printStream.println(invalidLanguageInputMsg);

        }
        printStream.println(defaultLanguageInputMsg + Locale.getDefault());
        return Locale.getDefault();
    }

    public void print(String text) {
        printStream.println(text);
    }

}
