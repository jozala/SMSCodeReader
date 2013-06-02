package pl.aetas.android.smscode.basic;

import android.content.ClipboardManager;
import android.content.Context;
import de.akquinet.android.androlog.Log;
import pl.aetas.android.smscode.analyser.SMSFilter;
import pl.aetas.android.smscode.exception.NoCodesForKnownSenderException;
import pl.aetas.android.smscode.exception.UnknownSenderException;
import pl.aetas.android.smscode.parser.SMSCodeParser;
import pl.aetas.android.smscode.resource.CodesRegularExpressionsResource;
import pl.aetas.android.smscode.resource.KnownSendersResource;

/**
 * Main class responsible for processing all incoming SMS for SMSCodeReader app
 */
public class SMSReader {

    private final Clipboard clipboard;
    private final SMSInfoPresenter smsInfoPresenter;
    private final SMSFilter smsFilter;
    private final SMSCodeParser smsCodeParser;

    public SMSReader(final SMSFilter smsFilter, final Clipboard clipboard, final SMSCodeParser smsCodeParser, final SMSInfoPresenter smsInfoPresenter) {
        if (smsFilter == null) throw new NullPointerException("SMSFilter cannot be null");
        if (clipboard == null) throw new NullPointerException("Clipboard cannot be null");
        if (smsInfoPresenter == null) throw new NullPointerException("SMSInfoPresenter cannot be null");
        if (smsCodeParser == null) throw new NullPointerException("SMSCodeParser cannot be null");

        this.clipboard = clipboard;
        this.smsInfoPresenter = smsInfoPresenter;
        this.smsFilter = smsFilter;
        this.smsCodeParser = smsCodeParser;
    }

    public static SMSReader getInstance(final Context context, final String sender, final String smsBody) {
        SMSCodeParser smsCodeParser = new SMSCodeParser(new CodesRegularExpressionsResource(sender));
        return new SMSReader(new SMSFilter(new KnownSendersResource(), smsCodeParser, sender, smsBody), new Clipboard((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)), smsCodeParser, new SMSInfoPresenter());
    }

    /**
     * Process SMS with given sender and body to analyse it, save code to clipboard and present information to user
     */
    public void readSMS(String sender, String body) {
        if (!smsFilter.checkIfSMSIsRelevantForCodeReader(sender)) {
            return;
        }

        try {
            String code = smsCodeParser.retrieveCodeFromSMSBodyForKnownSender();
            clipboard.save(code);
            smsInfoPresenter.presentInfoToUserIfChosen(sender, body, code);
        } catch (UnknownSenderException e) {
            Log.e(SMSReader.class.getName(), "Sender " + sender + " should be known (it has been checked earlier)", e);
            throw new RuntimeException(e);
        } catch (NoCodesForKnownSenderException e) {
            Log.e(SMSReader.class.getName(), "Sender " + sender + " is known, but has no codes attached", e);
            throw new RuntimeException(e);
        }
    }
}