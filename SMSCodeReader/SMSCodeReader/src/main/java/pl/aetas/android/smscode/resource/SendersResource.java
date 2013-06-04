package pl.aetas.android.smscode.resource;

import pl.aetas.android.smscode.exception.UnknownSenderException;
import pl.aetas.android.smscode.model.Sender;

/**
 * Class responsible for getting information about known senders available
 */
public class SendersResource {
    public boolean isSenderKnown(final String senderName) {
        throw new IllegalAccessError("Not implemented");
    }

    public Sender getSender(final String senderName) throws UnknownSenderException {
        throw new IllegalAccessError("Not implemented");
    }

    public static SendersResource getInstance() {
        return new SendersResource();
    }
}
