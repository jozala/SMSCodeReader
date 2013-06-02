package pl.aetas.android.smscode.exception;

public class NoCodesForKnownSenderException extends Exception {
    public NoCodesForKnownSenderException(final String message) {
        super(message);
    }

    public NoCodesForKnownSenderException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
