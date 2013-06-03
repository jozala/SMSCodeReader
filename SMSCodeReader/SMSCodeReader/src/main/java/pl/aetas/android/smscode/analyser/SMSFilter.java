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

    public SMSFilter(final KnownSendersResource knownSendersResource, final SMSCodeParser smsCodeParser, final String smsSender) {
        this.knownSendersResource = knownSendersResource;
        this.smsCodeParser = smsCodeParser;
        this.smsSender = smsSender;
    }

    public boolean checkIfSMSIsRelevantForCodeReader() {
        if (!knownSendersResource.isSenderKnown(smsSender)) {
            return false;
        }
        try {
            if (!smsCodeParser.checkIfBodyContainsCode()) {
                Log.w(SMSFilter.class.getName(), "Sender is known: " + smsSender + ", but code has not been found in sms body");
                return false;
            }
        } catch (UnknownSenderException e) {
            Log.e(SMSFilter.class.getName(), "Sender should be known (it has been checked earlier)", e);
            throw new RuntimeException(e);
        } catch (NoCodesForKnownSenderException e) {
            Log.e(SMSFilter.class.getName(), "Sender is known, but has no codes attached", e);
            throw new RuntimeException(e);
        }

        return true;
    }
}
