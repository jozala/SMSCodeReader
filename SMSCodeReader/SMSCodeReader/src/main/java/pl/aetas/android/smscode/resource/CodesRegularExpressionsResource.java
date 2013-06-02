package pl.aetas.android.smscode.resource;

import pl.aetas.android.smscode.exception.NoCodesForKnownSenderException;
import pl.aetas.android.smscode.exception.UnknownSenderException;
import pl.aetas.android.smscode.model.CodesRegularExpressions;

public class CodesRegularExpressionsResource {

    private final String sender;

    public CodesRegularExpressionsResource(final String sender) {
        this.sender = sender;
    }

    public CodesRegularExpressions getCodesRegularExpressionsForSender() throws UnknownSenderException, NoCodesForKnownSenderException {
        throw new IllegalAccessError("Not implemented");
    }
}
