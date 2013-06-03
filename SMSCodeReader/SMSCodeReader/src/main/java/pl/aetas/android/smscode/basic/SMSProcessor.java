package pl.aetas.android.smscode.basic;

import android.content.ClipboardManager;
import android.content.Context;
import de.akquinet.android.androlog.Log;
import pl.aetas.android.smscode.analyser.SMSFilter;
import pl.aetas.android.smscode.exception.CodeNotFoundException;
import pl.aetas.android.smscode.exception.NoCodesForKnownSenderException;
import pl.aetas.android.smscode.exception.UnknownSenderException;
import pl.aetas.android.smscode.model.Sender;
import pl.aetas.android.smscode.parser.SMSCodeParser;
import pl.aetas.android.smscode.resource.SendersResource;

/**
 * Main class responsible for processing all incoming SMS for SMSCodeReader app
 */
public class SMSProcessor {

    private final Clipboard clipboard;
    private final SMSInfoPresenter smsInfoPresenter;
    private final SMSFilter smsFilter;
    private final SMSCodeParser smsCodeParser;

    public SMSProcessor(final SMSFilter smsFilter, final Clipboard clipboard, final SMSCodeParser smsCodeParser, final SMSInfoPresenter smsInfoPresenter) {
        if (smsFilter == null) throw new NullPointerException("SMSFilter cannot be null");
        if (clipboard == null) throw new NullPointerException("Clipboard cannot be null");
        if (smsInfoPresenter == null) throw new NullPointerException("SMSInfoPresenter cannot be null");
        if (smsCodeParser == null) throw new NullPointerException("SMSCodeParser cannot be null");

        this.clipboard = clipboard;
        this.smsInfoPresenter = smsInfoPresenter;
        this.smsFilter = smsFilter;
        this.smsCodeParser = smsCodeParser;
    }

    public static SMSProcessor getInstance(final Context context, final String senderName, final String smsBody) {
        SendersResource sendersResource = new SendersResource();
        Sender sender = sendersResource.getSender(senderName);
        SMSCodeParser smsCodeParser = new SMSCodeParser(sender);
        SMSFilter smsFilter = new SMSFilter(sendersResource, smsCodeParser, senderName, smsBody);
        Clipboard clipboard = new Clipboard((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE));
        SMSInfoPresenter smsInfoPresenter = new SMSInfoPresenter(senderName, smsBody);
        return new SMSProcessor(smsFilter, clipboard, smsCodeParser, smsInfoPresenter);
    }

    /**
     * Process SMS with given sender and body to analyse it, save code to clipboard and present information to user
     */
    public void processSMS(String smsBody) {
        if (!smsFilter.checkIfSMSIsRelevantForCodeReader()) {
            return;
        }

        try {
            String code = smsCodeParser.retrieveCodeFromSMSBodyForKnownSender(smsBody);
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