package pl.aetas.android.smscode.exception;

public class UnknownSenderException extends Exception {
    public UnknownSenderException(final String message) {
        super(message);
    }

    public UnknownSenderException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
