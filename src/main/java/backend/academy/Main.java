package backend.academy;

import backend.academy.game.GameBuilder;
import backend.academy.game.GameSetupParams;
import backend.academy.io.input.IoManager;
import backend.academy.io.output.HelpPrinter;
import backend.academy.lozalization.Localizator;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.core.config.Configurator;

@Log4j2
@UtilityClass
public class Main {

    public static Boolean debugEnabled = Boolean.FALSE;

    public static void main(String[] args) throws IOException {

        Localizator localizator = new Localizator(Locale.getDefault());

        debugEnabled = Arrays.stream(args).filter(arg -> arg.equals("--debug")).count() == 1;
        if (!debugEnabled) {
            Configurator.setRootLevel(org.apache.logging.log4j.Level.OFF);
        }
        if (Arrays.asList(args).contains("--help")) {
            HelpPrinter helpPrinter = new HelpPrinter();
            localizator.localizate(helpPrinter);
            helpPrinter.printMessage();
        }

        boolean restart = true;
        while (restart) {
            try {

                IoManager ioManager = IoManager.defaultIoManager();
                localizator.localizate(ioManager);
                localizator.clear();

                var game = new GameBuilder()
                    .ioManager(ioManager)
                    .defaultOutputStorage()
                    .defaultMultilanguageWordsStorage(ioManager.readLocale())
                    .build();

                localizator.localizate(game);
                localizator.clear();

                var params = GameSetupParams.builder()
                    .difficulty(ioManager.readDifficulty())
                    .needTips(ioManager.readNeedTips())
                    .category(ioManager.readCategory(game.wordsStorage()));

                game.setup(params.build());
                game.run();

                restart = ioManager.readBoolean("Играем еще раз?", true, "Попробуйте еще раз");
                game.fullClear();
            } catch (IOException e) {
                if (debugEnabled) {
                    log.warn("IOException occurs: " + e.getMessage());
                }
                System.err.println("Возникла непредвиденная ошибка связанная с вводом/выводом");
            } catch (Exception e) {
                if (debugEnabled) {
                    log.warn("Exception {} occurs: {}", e.getClass().getSimpleName(), e.getMessage());
                }
            }
        }
    }
}
