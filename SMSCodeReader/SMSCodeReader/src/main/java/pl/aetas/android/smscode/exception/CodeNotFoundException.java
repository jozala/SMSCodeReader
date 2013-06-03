package pl.aetas.android.smscode.exception;

public class CodeNotFoundException extends Exception {
    public CodeNotFoundException(final String message) {
        super(message);
    }

    public CodeNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
