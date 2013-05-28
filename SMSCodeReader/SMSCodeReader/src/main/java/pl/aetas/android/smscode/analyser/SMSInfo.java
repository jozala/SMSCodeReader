package pl.aetas.android.smscode.analyser;

public interface SMSInfo {
    boolean isSMSWithCode();

    String getCode();

    String getSender();

    String getBody();
}
