package pl.aetas.android.smscode.parser;

import pl.aetas.android.smscode.exception.CodeNotFoundException;
import pl.aetas.android.smscode.exception.NoCodesForKnownSenderException;
import pl.aetas.android.smscode.exception.UnknownSenderException;
import pl.aetas.android.smscode.model.CodeRegularExpression;
import pl.aetas.android.smscode.model.CodesRegularExpressions;
import pl.aetas.android.smscode.resource.CodesRegularExpressionsResource;

/**
 * Retrieves SMS code from SMS body
 */
public class SMSCodeParser {

    private final CodesRegularExpressionsResource codesRegularExpressionsResource;

    public SMSCodeParser(final CodesRegularExpressionsResource codesRegularExpressionsResource) {
        this.codesRegularExpressionsResource = codesRegularExpressionsResource;
    }

    public String retrieveCodeFromSMSBodyForKnownSender(String smsBody) throws UnknownSenderException, NoCodesForKnownSenderException, CodeNotFoundException {
        // TODO codesRegularExpressionsResource is in real SenderCodesRegularExpressions
        // TODO Sender object is missing - should be created with its responsibilities
        CodesRegularExpressions codesRegularExpressions = codesRegularExpressionsResource.getCodesRegularExpressions();
        CodeRegularExpression codeRegularExpression = codesRegularExpressions.getMatchingRegularExpression();
        return codeRegularExpression.getCodeFromString(smsBody);
    }

    public boolean checkIfBodyContainsCode() throws UnknownSenderException, NoCodesForKnownSenderException {
        CodesRegularExpressions regularExpressionsForSender = codesRegularExpressionsResource.getCodesRegularExpressions();
        return regularExpressionsForSender.checkIfBodyContainsCode();
    }
}
