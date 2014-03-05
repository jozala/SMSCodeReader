package pl.aetas.android.smscode.exception;

public class LoadingDatabaseFailedException extends RuntimeException {
    public LoadingDatabaseFailedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
