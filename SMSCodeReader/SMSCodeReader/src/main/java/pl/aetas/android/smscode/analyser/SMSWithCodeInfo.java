package pl.aetas.android.smscode.analyser;

/**
 * This class keeps information about SMS which has been correctly recognised as a SMS with code
 */
public class SMSWithCodeInfo implements SMSInfo {
    private final String code;
    private final String body;
    private final String sender;

    public SMSWithCodeInfo(final String sender, final String body, final String code) {
        this.code = code;
        this.body = body;
        this.sender = sender;
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
    public String getSender() {
        return sender;
    }

    @Override
    public String getBody() {
        return body;
    }
}
