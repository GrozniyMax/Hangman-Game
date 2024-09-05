package backend.academy;

import backend.academy.game.Game;
import backend.academy.game.GameSetupParams;
import backend.academy.io.input.IoManager;
import java.io.IOException;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) throws IOException {

        boolean isDebug = Arrays.stream(args).filter(arg->arg.equals("--debug")).count()==1;
        Boolean restart = true;

        while (restart) {
            try {
                IoManager ioManager = IoManager.defaultIoManager();
                var game = Game.withDefauls();

                if (Arrays.binarySearch(args, "--debug") != -1) {
                    game.setMode(isDebug ? "debug" : "play");
                }

                var params = GameSetupParams.builder()
                    .difficulty(ioManager.readDifficulty())
                    .needTips(ioManager.readNeedTips())
                    .category(ioManager.readCategory(game.wordsStorage()));

                game.setup(params.build());
                game.run();

                restart = ioManager.readBoolean("Играем еще раз?", true, "Попробуйте еще раз");
                game.clear();
            } catch (IOException e) {
                if (!isDebug) continue;
                System.err.println("Возникла непредвиденная ошибка связанная с вводом/выводом");
                e.printStackTrace();
            }
        }
    }

}
