package pl.aetas.android.smscode.basic;

import android.content.ClipboardManager;
import android.content.Context;
import pl.aetas.android.smscode.analyser.SMSAnalyser;
import pl.aetas.android.smscode.analyser.SMSInfo;

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
        return new SMSReader(new SMSAnalyser(), new Clipboard((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE)), new SMSInfoPresenter());
    }

    public void readSMS(final String sender, final String body) {
        SMSInfo smsInfo = smsAnalyser.analyse(sender, body);
        if (smsInfo.isSenderKnown() && smsInfo.isContainsCode()) {
            clipboard.save(smsInfo.getCode());
            smsInfoPresenter.presentInfoToUserIfChosen(smsInfo);
        }
    }
}