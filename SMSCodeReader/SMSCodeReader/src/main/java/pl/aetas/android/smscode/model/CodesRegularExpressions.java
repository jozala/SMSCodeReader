package pl.aetas.android.smscode.model;

import java.util.Collections;
import java.util.Set;

public class CodesRegularExpressions {
    private final String smsBody;
    private final Set<CodeRegularExpression> regularExpressions;

    public CodesRegularExpressions(final String smsBody, final Set<CodeRegularExpression> regularExpressions) {
        if (smsBody == null) throw new NullPointerException("smsBody cannot be null");
        if (regularExpressions == null) throw new NullPointerException("regularExpressions cannot be null");
        this.smsBody = smsBody;
        this.regularExpressions = Collections.unmodifiableSet(regularExpressions);
    }

    public CodeRegularExpression getMatchingRegularExpression() {
        for (CodeRegularExpression regularExpression : regularExpressions) {
            if (regularExpression.matches(smsBody)) {
                return regularExpression;
            }
        }
        throw new IllegalStateException("Matching regular expression for this smsBody does not exists. " +
                "Check with the #checkIfBodyContainsCode method first.");
    }

    public boolean checkIfBodyContainsCode() {
        for (CodeRegularExpression regularExpression : regularExpressions) {
            if (regularExpression.matches(smsBody)) {
                return true;
            }
        }
        return false;
    }
}
