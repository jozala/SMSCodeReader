package pl.aetas.android.smscode.model;

public class CodesRegularExpressions {
    private final String smsBody;

    public CodesRegularExpressions(final String smsBody) {
        if (smsBody == null) throw new NullPointerException("smsBody cannot be null");
        this.smsBody = smsBody;
    }

    public CodeRegularExpression getMatchingRegularExpression() {
        throw new IllegalAccessError("Not implemented");

    }

    public boolean checkIfBodyContainsCode() {
        throw new IllegalAccessError("Not implemented");
    }
}
