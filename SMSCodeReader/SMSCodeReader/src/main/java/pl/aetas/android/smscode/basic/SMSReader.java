package pl.aetas.android.smscode.basic;

import android.content.ClipboardManager;
import android.content.Context;
import pl.aetas.android.smscode.analyser.SMSAnalyser;
import pl.aetas.android.smscode.analyser.SMSInfo;
import pl.aetas.android.smscode.parser.SMSCodeParser;
import pl.aetas.android.smscode.resource.CodesRegularExpressionsResource;
import pl.aetas.android.smscode.resource.KnownSendersResource;

/**
 * Main class responsible for processing all incoming SMS for SMSCodeReader app
 */
public class SMSReader {

    private final Clipboard clipboard;
    private final SMSInfoPresenter smsInfoPresenter;
    private final SMSAnalyser smsAnalyser;

    public SMSReader(final SMSAnalyser smsAnalyser, final Clipboard clipboard, final SMSInfoPresenter smsInfoPresenter) {
        if (smsAnalyser == null) throw new NullPointerException("SMSAnalyser cannot be null");
        if (clipboard == null) throw new NullPointerException("Clipboard cannot be null");
        if (smsInfoPresenter == null) throw new NullPointerException("SMSInfoPresenter cannot be null");

        this.clipboard = clipboard;
        this.smsInfoPresenter = smsInfoPresenter;
        this.smsAnalyser = smsAnalyser;
    }

    public static SMSReader getInstance(final Context context) {
        return new SMSReader(new SMSAnalyser(new KnownSendersResource(), new SMSCodeParser(new CodesRegularExpressionsResource())), new Clipboard((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)), new SMSInfoPresenter());
    }

    /**
     * Process SMS with given sender and body to analyse it, save code to clipboard and present information to user
     *
     * @param sender SMS sender
     * @param body   SMS body
     */
    public void readSMS(final String sender, final String body) {
        SMSInfo smsInfo = smsAnalyser.analyse(sender, body);
        if (smsInfo.isSMSWithCode()) {
            clipboard.save(smsInfo.getCode());
            smsInfoPresenter.presentInfoToUserIfChosen(smsInfo);
        }
    }
}