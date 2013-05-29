package pl.aetas.android.smscode.analyser;

/**
 * This class keeps information about the SMS which is not recognizable as an SMS with code
 */
public class SMSWithoutCodeInfo implements SMSInfo {

    private SMSWithoutCodeInfo() {
    }

    public static SMSInfo createSMSWithoutCodeInfo() {
        return new SMSWithoutCodeInfo();
    }

    @Override
    public boolean isSMSWithCode() {
        return false;
    }

    @Override
    public String getCode() throws IllegalStateException {
        throw new IllegalStateException("Code cannot be given as this is SMSWithoutCodeInfo");
    }

    @Override
    public String getSenderOfficialName() {
        throw new IllegalStateException("No known sender as this is SMSWithoutCodeInfo");
    }

    @Override
    public String getBody() {
        throw new IllegalStateException("No body as this is SMSWithoutCodeInfo");
    }
}
