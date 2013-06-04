package pl.aetas.android.smscode.parser;

import pl.aetas.android.smscode.exception.CodeNotFoundException;
import pl.aetas.android.smscode.exception.NoCodesForKnownSenderException;
import pl.aetas.android.smscode.exception.UnknownSenderException;
import pl.aetas.android.smscode.model.CodeRegularExpression;
import pl.aetas.android.smscode.model.CodesRegularExpressions;
import pl.aetas.android.smscode.model.Sender;

/**
 * Retrieves SMS code from SMS body
 */
public class SMSCodeParser {

    private final Sender sender;

    public SMSCodeParser(final Sender sender) {
        if (sender == null) throw new NullPointerException("sender cannot be null");

        this.sender = sender;
    }

    public String retrieveCodeFromSMSBody(String smsBody) throws UnknownSenderException, NoCodesForKnownSenderException, CodeNotFoundException {
        CodesRegularExpressions codesRegularExpressions = sender.getCodesRegularExpressions();
        CodeRegularExpression codeRegularExpression = codesRegularExpressions.getMatchingRegularExpression(smsBody);
        return codeRegularExpression.getCodeFromString(smsBody);
    }

    public boolean checkIfBodyContainsCode(String smsBody) {
        CodesRegularExpressions regularExpressionsForSender = sender.getCodesRegularExpressions();
        return regularExpressionsForSender.checkIfBodyContainsCode(smsBody);
    }
}
