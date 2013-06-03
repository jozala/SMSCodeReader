package pl.aetas.android.smscode.model;

import java.util.Collections;
import java.util.Set;

public class CodesRegularExpressions {
    private final Set<CodeRegularExpression> regularExpressions;

    public CodesRegularExpressions(final Set<CodeRegularExpression> regularExpressions) {
        if (regularExpressions == null) throw new NullPointerException("regularExpressions cannot be null");
        this.regularExpressions = Collections.unmodifiableSet(regularExpressions);
    }

    public CodeRegularExpression getMatchingRegularExpression(String smsBody) {
        for (CodeRegularExpression regularExpression : regularExpressions) {
            if (regularExpression.matches(smsBody)) {
                return regularExpression;
            }
        }
        throw new IllegalStateException("Matching regular expression for this smsBody does not exists. " +
                "Check with the #checkIfBodyContainsCode method first.");
    }

    public boolean checkIfBodyContainsCode(String smsBody) {
        for (CodeRegularExpression regularExpression : regularExpressions) {
            if (regularExpression.matches(smsBody)) {
                return true;
            }
        }
        return false;
    }
}
