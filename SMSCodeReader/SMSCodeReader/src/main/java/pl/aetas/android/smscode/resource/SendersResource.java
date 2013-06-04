package pl.aetas.android.smscode.resource;

import pl.aetas.android.smscode.exception.UnknownSenderException;
import pl.aetas.android.smscode.model.Sender;

/**
 * Class responsible for getting information about known senders available
 */
public class SendersResource {
    public boolean isSenderKnown(final String senderName) {
        // TODO to be implemented by invoking DAO methods
        throw new IllegalAccessError("Not implemented");
    }

    public Sender getSender(final String senderName) throws UnknownSenderException {
        // TODO to be implemented by invoking DAO methods
        throw new IllegalAccessError("Not implemented");
    }

    public static SendersResource getInstance() {
        // TODO change this implementation to get singleton
        return new SendersResource();
    }
}
