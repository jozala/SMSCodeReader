package pl.aetas.android.smscode.model;

import pl.aetas.android.smscode.exception.CodeNotFoundException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeRegularExpression {
    private final Pattern pattern;

    public CodeRegularExpression(final String regExp) {
        if (regExp == null) throw new NullPointerException("regExp cannot be null");

        pattern = Pattern.compile(regExp);
    }

    public String getCodeFromString(String stringToParse) throws CodeNotFoundException {
        final Matcher matcher = pattern.matcher(stringToParse);
        final boolean matches = matcher.matches();
        if (!matches) {
            throw new CodeNotFoundException("Code has not been found in: \"" + stringToParse + "\". It should be checked " +
                    "first if this code exists in the given string.");
        }
        return matcher.group(2);
    }
}
