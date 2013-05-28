package pl.aetas.android.smscode.basic;

import pl.aetas.android.smscode.analyser.SMSAnalyser;
import pl.aetas.android.smscode.analyser.SMSInfo;

public class SMSReader {

    private SMSAnalyser smsAnalyser;
    private Clipboard clipboard;
    private SMSInfoPresenter smsInfoPresenter;

    public SMSReader(SMSAnalyser smsAnalyser, Clipboard clipboard, SMSInfoPresenter smsInfoPresenter) {
        if (smsAnalyser == null) throw new NullPointerException("SMSAnalyser cannot be null");
        if (clipboard == null) throw new NullPointerException("Clipboard cannot be null");
        if (smsInfoPresenter == null) throw new NullPointerException("SMSInfoPresenter cannot be null");

        this.clipboard = clipboard;
        this.smsInfoPresenter = smsInfoPresenter;
        this.smsAnalyser = smsAnalyser;
    }

    public static SMSReader getInstance() {
        return new SMSReader(new SMSAnalyser(), new Clipboard(), new SMSInfoPresenter());
    }

    public void readSMS(String sender, String body) {
        SMSInfo smsInfo = smsAnalyser.analyse(sender, body);
        if (smsInfo.isSenderKnown() && smsInfo.isContainsCode()) {
            clipboard.save(smsInfo.getCode());
            smsInfoPresenter.presentInfoToUserIfChosen(smsInfo);
        }
    }
}