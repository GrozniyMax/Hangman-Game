package backend.academy.io.output;

import backend.academy.lozalization.Localize;

@Localize("help")
public class HelpPrinter {

    @Localize
    private String text;

    @SuppressWarnings("checkstyle:RegexpSinglelineJava")
    public void printMessage() {
        // Здесь не логирование, а вывод с консоли
        System.out.println(text);
    }
}
