package backend.academy.game;

import backend.academy.clearable.AutoClearable;
import backend.academy.Setupable;
import backend.academy.io.input.IoManager;
import backend.academy.io.output.OutputFormer;
import backend.academy.io.output.storage.OutputStorage;
import backend.academy.io.output.storage.ResourcesOutputStorage;
import backend.academy.lozalization.Localize;
import backend.academy.word.storage.ResourceWordStorage;
import backend.academy.word.Word;
import backend.academy.word.storage.WordsStorage;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.jspecify.annotations.NonNull;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Log4j2
@Localize("localization.Game")
public class Game implements AutoClearable, Setupable<GameSetupParams> {

    private int frameIndex=0;
    private int frameStep=0;
    private int tipCount=0;
    private int currentHP=0;
    private boolean guessedLetter=false;

    @Localize
    private Difficulty difficulty;

    @Getter
    @NonNull
    private WordsStorage wordsStorage;

    @Localize
    @NonNull
    private OutputFormer outputFormer;

    @Localize
    @NonNull
    private IoManager ioManager;

    private final Set<Character> wrongLetters = new HashSet<>();

    private final Set<Character> correctLetters = new HashSet<>();

    private Word word=null;

    private Set<Character> wordSet=new HashSet<>();

    private Boolean needTips;


    @Localize
    private String winMsg ="Ура. Вы победили";

    @Localize
    private String loseMsg = "К сожалению, у вас жизни закончились";

    @Localize
    private String repeatLetterMsg = "Вы уже ввели эту букву";

    @Localize
    private String needTipMsg = "Нужна подсказка?";

    @Localize
    private String invalidBoolValueInput = "Попробуйте еще раз, пожалуйста";

    @Localize
    private String runOutOfTipsMsg = "К сожалению подсказки закончились(дальше только своими силами)";

    @Override
    public void clear() {
        frameIndex=0;
        frameStep=0;
        word = null;
        difficulty = null;
        currentHP=0;
        wrongLetters.clear();
        correctLetters.clear();
    }

    public Game(@NonNull IoManager ioManager, @NonNull WordsStorage wordsStorage, @NonNull OutputStorage outputStorage) {
        this.ioManager = ioManager;
        this.wordsStorage = wordsStorage;
        this.outputFormer = new OutputFormer(outputStorage, wrongLetters,correctLetters);
    }

    public static Game withDefauls() throws IOException {
        return new Game(IoManager.defaultIoManager(),
            new ResourceWordStorage(),
            new ResourcesOutputStorage());
    }

    @Override
    public void setup(GameSetupParams gameSetupParams) {
        this.word = wordsStorage.getRandomWord(gameSetupParams.category());
        for (Character c:word.word().toUpperCase().toCharArray()){
            wordSet.add(c);
        }
        this.difficulty = gameSetupParams.difficulty();
        this.currentHP=difficulty.triesCount();
        this.needTips=gameSetupParams.needTips();
        this.frameStep = this.difficulty.calculateStep(this.outputFormer.getNumberOfFrames());
        this.outputFormer.setup(this.word);

        log.info("Game setup with difficulty:{} , needTips:{}", difficulty, needTips);

    }

    public void run() throws IOException {
        ioManager.print(outputFormer.createOutput(frameIndex));
        while (currentHP>0) {
            round();
            if (checkWin()){
                ioManager.print(winMsg);
                return;
            }else if(!guessedLetter && needTips && word.hasTip(tipCount+1) && currentHP>0) {
                askForTip();
            }
            if (currentHP<=0){
                ioManager.print(outputFormer.createOutputForLastFrame());
            }
            log.info("Current game HP:{}, wrong-letters:{}, right-letters:{}", currentHP, wrongLetters, correctLetters);
        }

        ioManager.print(loseMsg);
    }

    private void round() throws IOException {
        Character letter = ioManager.readLetter();
        log.info("Got letter:{}", letter.toString());
        if (wrongLetters.contains(letter)||correctLetters.contains(letter)) {
            ioManager.print(repeatLetterMsg);
            return;
        }
        if (word.word().toUpperCase().contains(letter.toString().toUpperCase())) {
            correctLetters.add(letter);
            this.guessedLetter = true;
        }else {
            wrongLetters.add(letter);
            frameIndex += frameStep;
            currentHP --;
            this.guessedLetter=false;
        }
        ioManager.print(outputFormer.createOutput(frameIndex));
    }

    private void askForTip() throws IOException {

        boolean needTip = ioManager.readBoolean(needTipMsg, true, invalidBoolValueInput);
        log.info("Got value {}", needTip);
        if (needTip) {
            ioManager.print(
            Optional.of(word.getTip(++tipCount))
                .orElse(runOutOfTipsMsg));
        }
    }

    private Boolean checkWin(){
        return wordSet.equals(correctLetters);
    }



}
