package backend.academy.game;

import backend.academy.io.input.IoManager;
import backend.academy.io.output.storage.OutputStorage;
import backend.academy.io.output.storage.ResourcesOutputStorage;
import backend.academy.word.storage.MultilanguageWordStorage;
import backend.academy.word.storage.WordsStorage;
import java.util.Locale;
import lombok.NoArgsConstructor;


@SuppressWarnings("checkstyle:MultipleStringLiterals")
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

    public GameBuilder defaultIoManager() {
        this.ioManager = IoManager.defaultIoManager();
        return this;
    }

    public GameBuilder defaultOutputStorage() {
        this.outputStorage = new ResourcesOutputStorage();
        return this;
    }

    public GameBuilder defaultWordsStorage() {
        this.wordsStorage = new MultilanguageWordStorage(Locale.getDefault(), "words.words");
        return this;
    }

    public GameBuilder defaultMultilanguageWordsStorage(Locale locale) {
        this.wordsStorage = new MultilanguageWordStorage(locale, "words.words");
        return this;
    }

    public Game build() {
        if (wordsStorage == null || ioManager == null || outputStorage == null) {
            throw new IllegalStateException("You must set up all the required parameters");
        }
        return new Game(ioManager, wordsStorage, outputStorage);
    }

}
