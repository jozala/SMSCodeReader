package pl.aetas.android.smscode.basic;

import pl.aetas.android.smscode.model.Sender;

public class SMSInfoPresenter {
    private final Sender sender;
    private final String smsBody;

    public SMSInfoPresenter(final Sender sender, final String smsBody) {
        this.sender = sender;
        this.smsBody = smsBody;
    }

    public void presentInfoToUserIfChosen(String code) {
        // TODO to be implemented
        throw new IllegalAccessError("Not implemented");
    }
}
