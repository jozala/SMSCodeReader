package pl.aetas.android.smscode.analyser;

/**
 * Keeps information about SMS after analysis.
 */
public interface SMSInfo {
    /**
     * Returns true if analysed SMS has been recognised as SMS with code.
     *
     * @return true if analysed SMS has been recognised as SMS with code, false otherwise
     */
    boolean isSMSWithCode();

    /**
     * Code of analysed SMS if this SMS has been recognised as SMS. Otherwise it throws {@link IllegalStateException}.
     *
     * @return Code of analysed SMS if this SMS has been recognised as SMS
     * @throws IllegalStateException when this SMSInfo keeps info about SMS not recognised as SMS with code
     */
    String getCode() throws IllegalStateException;

    /**
     * Returns official sender name. It can be name of the Bank instead of phone number of sender.
     *
     * @return official sender name
     */
    String getSenderOfficialName();

    String getBody();
}
