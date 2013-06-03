package pl.aetas.android.smscode.model;

import pl.aetas.android.smscode.exception.NoCodesForKnownSenderException;
import pl.aetas.android.smscode.exception.UnknownSenderException;

public class Sender {

    private final String senderName;
    private final String officialName;
    private final CodesRegularExpressions codesRegularExpressions;

    public Sender(final String senderName, final String officialName, final CodesRegularExpressions codesRegularExpressions) {
        if (senderName == null) throw new NullPointerException("Sender name cannot be null");
        if (officialName == null) throw new NullPointerException("Official name cannot be null");
        if (codesRegularExpressions == null) throw new NullPointerException("codesRegularExpressions cannot be null");

        this.senderName = senderName;
        this.officialName = officialName;
        this.codesRegularExpressions = codesRegularExpressions;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getOfficialName() {
        return officialName;
    }

    public CodesRegularExpressions getCodesRegularExpressions() throws UnknownSenderException, NoCodesForKnownSenderException {
        return codesRegularExpressions;
    }
}
