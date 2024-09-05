package backend.academy;

import backend.academy.game.Game;
import backend.academy.game.GameSetupParams;
import backend.academy.io.input.IoManager;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import java.io.IOException;
import java.util.Arrays;

@Log4j2
public class Main {

    public static Boolean debugEnabled = Boolean.FALSE;

    public static void main(String[] args) throws IOException {

        debugEnabled = Arrays.stream(args).filter(arg->arg.equals("--debug")).count()==1;
        if (!debugEnabled){
            Configurator.setRootLevel(org.apache.logging.log4j.Level.OFF);
        }

        boolean restart = true;
        while (restart) {
            try {
                IoManager ioManager = IoManager.defaultIoManager();
                var game = Game.withDefauls();


                var params = GameSetupParams.builder()
                    .difficulty(ioManager.readDifficulty())
                    .needTips(ioManager.readNeedTips())
                    .category(ioManager.readCategory(game.wordsStorage()));

                game.setup(params.build());
                game.run();

                restart = ioManager.readBoolean("Играем еще раз?", true, "Попробуйте еще раз");
                game.clear();
            } catch (IOException e) {
                if (debugEnabled) log.warn("IOException occurs: "+e.getMessage());
                System.err.println("Возникла непредвиденная ошибка связанная с вводом/выводом");
            }catch (Exception e) {
                if (debugEnabled) log.warn("Exception {} occurs: {}", e.getClass().getSimpleName(), e.getMessage());
            }
        }
    }

}
