package pl.aetas.android.smscode.basic;

public class SMSInfoPresenter {
    private final String senderDisplayName;
    private final String smsBody;

    public SMSInfoPresenter(final String senderDisplayName, final String smsBody) {
        this.senderDisplayName = senderDisplayName;
        this.smsBody = smsBody;
    }

    public void presentInfoToUserIfChosen(String code) {
        throw new IllegalAccessError("Not implemented");
    }
}
