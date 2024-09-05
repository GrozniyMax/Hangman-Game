package backend.academy.output;

import backend.academy.Clearable;
import backend.academy.Setupable;
import backend.academy.output.storage.OutputStorage;
import backend.academy.word.Word;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor(onConstructor_ = {@NonNull})
public class OutputFormer implements Clearable, Setupable<Word> {

    @NonNull
    private final OutputStorage outputStorage;

    private Word word;


    @NonNull
    private final Set<Character> wrongLetters;
    @NonNull
    private final Set<Character> rightLetters;

    private Boolean isDebug = false;

    public void setup(Word word) {
        this.word = word;
    }

    public void enableDebug(Boolean enable){
        isDebug = enable;
    }

    public String createOutput(int frameIndex) {
        StringBuilder builder = new StringBuilder();
        builder.append(formDelimiter(isDebug));
        builder.append(String.format("КАТЕГОРИЯ: %s \n", word.category()));
        builder.append(formFirstLine());
        outputStorage.getFrame(frameIndex).forEach(line->builder.append(line).append("\n"));
        builder.append("\n");
        formLastLines().forEach((line)->builder.append(line).append("\n"));
        return builder.toString();
    }

    public String createOutputForLastFrame() {
        return createOutput(outputStorage.getFramesCount()-1);
    }


    private String getCrossedSymbol(Character symbol) {
        return String.format("%c̶",symbol);
    }

    private String formFirstLine() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("    ");
        wrongLetters.stream().map(this::getCrossedSymbol).forEach(stringBuilder::append);
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    private List<String> formLastLines(){
        StringBuilder firstLine = new StringBuilder("   ");
        StringBuilder secondLine = new StringBuilder("   ");
        for (Character letter : word.word().toCharArray()) {
            firstLine.append(" ");
            if (rightLetters.contains(letter)) firstLine.append(letter);
            else firstLine.append(" ");
            secondLine.append(" ").append("⎺");
        }
        return List.of(firstLine.toString(), secondLine.toString());
    }

    private String formDelimiter(Boolean isDebugging){
        if (isDebugging) {
            return "\n\n----------------------------------------------------------------\n\n";
        }else {
            return "\033[H\033[2J";
        }
    }

    public Integer getNumberOfFrames(){
        return this.outputStorage.getFramesCount();
    }



    @Override
    public void clear() {
        wrongLetters.clear();
        rightLetters.clear();
        clearClearableFields();
    }
}
