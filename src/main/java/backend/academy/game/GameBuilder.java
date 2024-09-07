package backend.academy.game;

import backend.academy.io.input.IoManager;
import backend.academy.io.output.OutputFormer;
import backend.academy.io.output.storage.OutputStorage;
import backend.academy.io.output.storage.ResourcesOutputStorage;
import backend.academy.word.storage.MultilanguageWordStorage;
import backend.academy.word.storage.WordsStorage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Locale;


@NoArgsConstructor
public class GameBuilder {

    private IoManager ioManager;

    private OutputStorage outputStorage;

    private WordsStorage wordsStorage;

    public GameBuilder ioManager(IoManager ioManager) {
        this.ioManager = ioManager;
        return this;
    }

    public GameBuilder outputStorage(OutputStorage outputStorage) {
        this.outputStorage = outputStorage;
        return this;
    }

    public GameBuilder wordsStorage(WordsStorage wordsStorage) {
        this.wordsStorage = wordsStorage;
        return this;
    }

    public GameBuilder defaultIoManager(){
        this.ioManager = IoManager.defaultIoManager();
        return this;
    }

    public GameBuilder defaultOutputStorage(){
        this.outputStorage = new ResourcesOutputStorage();
        return this;
    }

    public GameBuilder defaultWordsStorage(){
        this.wordsStorage = new MultilanguageWordStorage(Locale.getDefault());
        return this;
    }

    public GameBuilder multilanguageWordsStorage(Locale locale){
        this.wordsStorage = new MultilanguageWordStorage(locale);
        return this;
    }

    public Game build() {
        if (wordsStorage == null||ioManager == null||outputStorage == null) {
            throw new IllegalStateException("You must set up all the required parameters");
        }
        return new Game(ioManager, wordsStorage, outputStorage);
    }

}
