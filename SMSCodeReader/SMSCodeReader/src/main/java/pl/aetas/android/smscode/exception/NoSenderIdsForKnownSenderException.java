package pl.aetas.android.smscode.exception;

public class NoSenderIdsForKnownSenderException extends RuntimeException {
    public NoSenderIdsForKnownSenderException(String detailMessage) {
        super(detailMessage);
    }
}
