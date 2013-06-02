package pl.aetas.android.smscode.analyser;

import de.akquinet.android.androlog.Log;
import pl.aetas.android.smscode.exception.NoCodesForKnownSenderException;
import pl.aetas.android.smscode.exception.UnknownSenderException;
import pl.aetas.android.smscode.parser.SMSCodeParser;
import pl.aetas.android.smscode.resource.KnownSendersResource;

public class SMSFilter {

    private final KnownSendersResource knownSendersResource;
    private final SMSCodeParser smsCodeParser;
    private final String smsSender;
    private final String smsBody;

    public SMSFilter(final KnownSendersResource knownSendersResource, final SMSCodeParser smsCodeParser, final String smsSender, final String smsBody) {
        this.knownSendersResource = knownSendersResource;
        this.smsCodeParser = smsCodeParser;
        this.smsSender = smsSender;
        this.smsBody = smsBody;
    }

    public boolean checkIfSMSIsRelevantForCodeReader(final String smsSender) {
        if (!knownSendersResource.isSenderKnown(smsSender)) {
            return false;
        }
        try {
            if (!smsCodeParser.checkIfBodyContainsCode()) {
                Log.w(SMSFilter.class.getName(), "Sender is known: " + smsSender + ", but code has not been found in sms body: " + smsBody);
                return false;
            }
        } catch (UnknownSenderException e) {
            Log.e(SMSFilter.class.getName(), "Sender " + smsSender + " should be known (it has been checked earlier, but it fails later)", e);
            throw new RuntimeException(e);
        } catch (NoCodesForKnownSenderException e) {
            Log.e(SMSFilter.class.getName(), "Sender " + smsSender + " is known, but has no codes attached", e);
            throw new RuntimeException(e);
        }

        return true;
    }
}
