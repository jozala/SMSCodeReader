package pl.aetas.android.smscode.analyser;

import de.akquinet.android.androlog.Log;
import pl.aetas.android.smscode.exception.NoCodesForKnownSenderException;
import pl.aetas.android.smscode.exception.UnknownSenderException;
import pl.aetas.android.smscode.parser.SMSCodeParser;
import pl.aetas.android.smscode.resource.SendersResource;

public class SMSFilter {

    private final SendersResource sendersResource;
    private final SMSCodeParser smsCodeParser;
    private final String smsSenderName;

    public SMSFilter(final SendersResource sendersResource, final SMSCodeParser smsCodeParser, final String smsSenderName) {
        this.sendersResource = sendersResource;
        this.smsCodeParser = smsCodeParser;
        this.smsSenderName = smsSenderName;
    }

    public boolean checkIfSMSIsRelevantForCodeReader() {
        if (!sendersResource.isSenderKnown(smsSenderName)) {
            return false;
        }
        try {
            if (!smsCodeParser.checkIfBodyContainsCode()) {
                Log.w(SMSFilter.class.getName(), "Sender is known: " + smsSenderName + ", but code has not been found in sms body");
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
