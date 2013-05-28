package pl.aetas.android.smscode.analyser;

/**
 * This class keeps information about the SMS which is not recognizable as an SMS with code
 */
public class SMSWithoutCodeInfo implements SMSInfo {

    @Override
    public boolean isSMSWithCode() {
        return false;
    }

    @Override
    public String getCode() {
        throw new IllegalStateException("Code cannot be given as this is SMSWithoutCodeInfo");
    }

    @Override
    public String getSender() {
        throw new IllegalStateException("No known sender as this is SMSWithoutCodeInfo");
    }

    @Override
    public String getBody() {
        throw new IllegalStateException("No body as this is SMSWithoutCodeInfo");
    }
}
