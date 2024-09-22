package backend.academy.io.input;

public record ReadLetterOutput(Boolean userAskedForTip, Character letter) {

    public ReadLetterOutput(Character letter) {
        this(false, letter);
    }

    public ReadLetterOutput(Boolean userAskedForTip) {
        this(userAskedForTip, null);
    }
}
