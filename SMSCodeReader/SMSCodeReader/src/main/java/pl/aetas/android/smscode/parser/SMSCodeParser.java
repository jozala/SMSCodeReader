package pl.aetas.android.smscode.parser;

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

    public String retrieveCodeFromSMSBodyForKnownSender() throws UnknownSenderException, NoCodesForKnownSenderException {
        CodesRegularExpressions codesRegularExpressions = codesRegularExpressionsResource.getCodesRegularExpressions();
        CodeRegularExpression codeRegularExpression = codesRegularExpressions.getMatchingRegularExpression();
        return codeRegularExpression.getCodeFromString();
    }

    public boolean checkIfBodyContainsCode() throws UnknownSenderException, NoCodesForKnownSenderException {
        CodesRegularExpressions regularExpressionsForSender = codesRegularExpressionsResource.getCodesRegularExpressions();
        return regularExpressionsForSender.checkIfBodyContainsCode();
    }
}
