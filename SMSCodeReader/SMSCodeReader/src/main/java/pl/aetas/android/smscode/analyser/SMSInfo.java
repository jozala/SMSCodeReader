package pl.aetas.android.smscode.analyser;

public class SMSInfo {

    private final boolean senderKnown;
    private final boolean containsCode;
    private String code;

    public SMSInfo(boolean senderKnown, boolean containsCode) {
        this.senderKnown = senderKnown;
        this.containsCode = containsCode;
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
