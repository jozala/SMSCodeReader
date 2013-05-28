package pl.aetas.android.smscode.analyser;

/**
 * This class keeps information about SMS which has been correctly recognised as a SMS with code
 */
public class SMSWithCodeInfo implements SMSInfo {
    private final String code;
    private final String body;
    private final String senderName;

    public SMSWithCodeInfo(final String senderName, final String body, final String code) {
        this.code = code;
        this.body = body;
        this.senderName = senderName;
    }

    @Override
    public boolean isSMSWithCode() {
        return true;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getSenderOfficialName() {
        return senderName;
    }

    @Override
    public String getBody() {
        return body;
    }
}
