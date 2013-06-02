package pl.aetas.android.smscode.model;

public class CodeRegularExpression {
    private final String regExp;
    private final String stringToParse;

    public CodeRegularExpression(final String regExp, final String stringToParse) {
        this.regExp = regExp;
        this.stringToParse = stringToParse;
    }

    public String getCodeFromString() {
        throw new IllegalAccessError("Not implemented");
    }
}
