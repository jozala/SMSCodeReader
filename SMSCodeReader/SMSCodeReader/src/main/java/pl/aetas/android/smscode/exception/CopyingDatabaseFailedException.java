package pl.aetas.android.smscode.exception;

public class CopyingDatabaseFailedException extends RuntimeException {
    public CopyingDatabaseFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
