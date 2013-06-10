package pl.aetas.android.smscode.parser;

import pl.aetas.android.smscode.exception.CodeNotFoundException;
import pl.aetas.android.smscode.model.CodeRegularExpression;
import pl.aetas.android.smscode.model.CodesRegularExpressions;

/**
 * Retrieves information from SMS body
 */
public class SMSCodeParser {

    private final CodesRegularExpressions codesRegularExpressions;

    public SMSCodeParser(final CodesRegularExpressions codesRegularExpressions) {
        if (codesRegularExpressions == null) throw new NullPointerException("codesRegularExpressions cannot be null");
        this.codesRegularExpressions = codesRegularExpressions;
    }

    public String retrieveCodeFromSMSBody(String smsBody) throws CodeNotFoundException {
        CodeRegularExpression codeRegularExpression = codesRegularExpressions.getMatchingRegularExpression(smsBody);
        return codeRegularExpression.getCodeFromString(smsBody);
    }

    public boolean checkIfBodyContainsCode(String smsBody) {
        return codesRegularExpressions.checkIfBodyContainsCode(smsBody);
    }
}
