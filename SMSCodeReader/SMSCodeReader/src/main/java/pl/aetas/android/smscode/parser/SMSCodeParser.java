package pl.aetas.android.smscode.parser;

import pl.aetas.android.smscode.resource.CodesRegularExpressionsResource;

/**
 * Parse SMS body and retrieve important information
 */
public class SMSCodeParser {

    private final CodesRegularExpressionsResource codesRegularExpressionsResource;

    public SMSCodeParser(final CodesRegularExpressionsResource codesRegularExpressionsResource) {
        this.codesRegularExpressionsResource = codesRegularExpressionsResource;
    }

    public String retrieveCodeFromSMSBodyForKnownSender(final String sender, String smsBody) {
        throw new IllegalAccessError("Not implemented");
    }

    public boolean checkIfBodyContainsCode(final String smsBody) {
        throw new IllegalAccessError("Not implemented");
    }
}
