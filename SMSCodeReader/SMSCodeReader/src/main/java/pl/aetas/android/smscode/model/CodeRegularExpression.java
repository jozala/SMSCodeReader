package pl.aetas.android.smscode.model;

import pl.aetas.android.smscode.exception.CodeNotFoundException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeRegularExpression {
    private final Pattern pattern;
    private final int relevantGroupNumber;

    CodeRegularExpression(final String regExp, int relevantGroupNumber) {
        if (regExp == null) throw new NullPointerException("regExp cannot be null");
        if (relevantGroupNumber < 1) throw new IllegalArgumentException("group number cannot be < 1");

        this.relevantGroupNumber = relevantGroupNumber;
        pattern = Pattern.compile(regExp);
    }

    public String getCodeFromString(String stringToParse) throws CodeNotFoundException {
        final Matcher matcher = pattern.matcher(stringToParse);
        final boolean matches = matcher.matches();
        if (!matches) {
            throw new CodeNotFoundException("Code has not been found in: \"" + stringToParse + "\". It should be checked " +
                    "first if this code exists in the given string.");
        }
        return matcher.group(relevantGroupNumber);
    }

    boolean matches(final String stringToMatch) {
        final Matcher matcher = pattern.matcher(stringToMatch);
        return matcher.matches();
    }
}
