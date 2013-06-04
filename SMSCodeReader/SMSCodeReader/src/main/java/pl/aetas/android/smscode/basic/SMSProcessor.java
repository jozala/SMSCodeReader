package pl.aetas.android.smscode.basic;

import de.akquinet.android.androlog.Log;
import pl.aetas.android.smscode.exception.CodeNotFoundException;
import pl.aetas.android.smscode.exception.NoCodesForKnownSenderException;
import pl.aetas.android.smscode.exception.UnknownSenderException;
import pl.aetas.android.smscode.parser.SMSCodeParser;

/**
 * Main class responsible for processing all incoming SMS for SMSCodeReader app
 */
public class SMSProcessor {

    private final Clipboard clipboard;
    private final SMSInfoPresenter smsInfoPresenter;
    private final SMSCodeParser smsCodeParser;

    public SMSProcessor(final Clipboard clipboard, final SMSCodeParser smsCodeParser, final SMSInfoPresenter smsInfoPresenter) {
        if (clipboard == null) throw new NullPointerException("Clipboard cannot be null");
        if (smsInfoPresenter == null) throw new NullPointerException("SMSInfoPresenter cannot be null");
        if (smsCodeParser == null) throw new NullPointerException("SMSCodeParser cannot be null");

        this.clipboard = clipboard;
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
            clipboard.save(code);
            smsInfoPresenter.presentInfoToUserIfChosen(code);
        } catch (UnknownSenderException e) {
            Log.e(SMSProcessor.class.getName(), "Sender should be known (it has been checked earlier)", e);
            throw new RuntimeException(e);
        } catch (NoCodesForKnownSenderException e) {
            Log.e(SMSProcessor.class.getName(), "Sender is known, but has no codes attached", e);
            throw new RuntimeException(e);
        } catch (CodeNotFoundException e) {
            Log.e(SMSProcessor.class.getName(), "SMS is relevant for code reader, but code has not been found in message body", e);
            throw new RuntimeException(e);
        }
    }
}