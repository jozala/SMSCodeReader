package pl.aetas.android.smscode.analyser;

public class SMSInfo {

    private final boolean senderKnown;
    private final boolean containsCode;
    private final String code;

    public SMSInfo(final boolean senderKnown, final boolean containsCode, final String code) {
        this.senderKnown = senderKnown;
        this.containsCode = containsCode;
        this.code = code;
    }

    public boolean isSenderKnown() {
        return senderKnown;
    }

    public boolean isContainsCode() {
        return containsCode;
    }

    public String getCode() {
        return code;
    }
}
