package pl.aetas.android.smscode.basic;

import pl.aetas.android.smscode.parser.SMSCodeParser;
import pl.aetas.android.smscode.verifier.SMSBodyVerifier;
import pl.aetas.android.smscode.verifier.SMSSenderVerifier;

public class SMSReader {

    private SMSCodeParser smsCodeParser;
    private SMSSenderVerifier smsSenderVerifier;
    private SMSBodyVerifier smsBodyVerifier;
    private Clipboard clipboard;
    private SMSCodePresenter smsCodePresenter;

    public SMSReader(SMSCodeParser smsCodeParser, SMSSenderVerifier smsSenderVerifier, SMSBodyVerifier smsBodyVerifier, Clipboard clipboard, SMSCodePresenter smsCodePresenter) {
        if (smsCodeParser == null) throw new NullPointerException("SMSCodeParser cannot be null");
        if (smsSenderVerifier == null) throw new NullPointerException("SMSBodyVerifier cannot be null");
        if (smsBodyVerifier == null) throw new NullPointerException("SMSBodyVerifier cannot be null");
        if (clipboard == null) throw new NullPointerException("Clipboard cannot be null");
        if (smsCodePresenter == null) throw new NullPointerException("SMSCodePresenter cannot be null");

        this.smsCodeParser = smsCodeParser;
        this.smsSenderVerifier = smsSenderVerifier;
        this.smsBodyVerifier = smsBodyVerifier;
        this.clipboard = clipboard;
        this.smsCodePresenter = smsCodePresenter;
    }

    public static SMSReader getInstance() {
        return new SMSReader(new SMSCodeParser(), new SMSSenderVerifier(), new SMSBodyVerifier(), new Clipboard(), new SMSCodePresenter());
    }

    public void readSMS(String sender, String body) {
        if (!smsSenderVerifier.checkIfSenderKnown(sender)) {
            return;
        }
        if (!smsBodyVerifier.checkIfContainsCode(body)) {
            return;
        }
        String code = smsCodeParser.retrieveCode(body);
        clipboard.save(code);

    }
}
