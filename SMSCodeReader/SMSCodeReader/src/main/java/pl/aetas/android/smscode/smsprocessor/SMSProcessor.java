package pl.aetas.android.smscode.smsprocessor;

import de.akquinet.android.androlog.Log;
import pl.aetas.android.smscode.exception.CodeNotFoundException;
import pl.aetas.android.smscode.parser.SMSCodeParser;
import pl.aetas.android.smscode.presenter.SMSInfoPresenter;

/**
 * Main class responsible for processing all incoming SMS for SMSCodeReader app
 */
public class SMSProcessor {

    private final SMSInfoPresenter smsInfoPresenter;
    private final SMSCodeParser smsCodeParser;

    public SMSProcessor(final SMSCodeParser smsCodeParser, final SMSInfoPresenter smsInfoPresenter) {
        if (smsInfoPresenter == null) throw new NullPointerException("SMSInfoPresenter cannot be null");
        if (smsCodeParser == null) throw new NullPointerException("SMSCodeParser cannot be null");

        this.smsInfoPresenter = smsInfoPresenter;
        this.smsCodeParser = smsCodeParser;
    }

    /**
     * Process SMS with given sender and body to analyse it, save code to clipboard and present information to user
     */
    public void processSMS(String smsBody) {
        if (!smsCodeParser.checkIfBodyContainsCode(smsBody)) {
            Log.w(SMSProcessor.class.getName(), "Sender is known, but code has not been found in sms body");
            return;
        }

        try {
            String code = smsCodeParser.retrieveCodeFromSMSBody(smsBody);
            smsInfoPresenter.presentInfoToUser(code);
        } catch (CodeNotFoundException e) {
            Log.e(SMSProcessor.class.getName(), "SMS is relevant for code reader, but code has not been found in message body", e);
            throw new RuntimeException(e);
        }
    }
}