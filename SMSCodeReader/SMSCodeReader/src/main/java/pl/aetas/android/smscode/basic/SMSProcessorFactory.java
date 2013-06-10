package pl.aetas.android.smscode.basic;

import android.content.ClipboardManager;
import android.content.Context;
import pl.aetas.android.smscode.exception.UnknownSenderException;
import pl.aetas.android.smscode.model.CodesRegularExpressions;
import pl.aetas.android.smscode.model.Sender;
import pl.aetas.android.smscode.parser.SMSCodeParser;
import pl.aetas.android.smscode.resource.SendersResource;

public final class SMSProcessorFactory {
    private SMSProcessorFactory() {
        // private to enforce using static factory method
    }

    public static SMSProcessorFactory getInstance() {
        return new SMSProcessorFactory();
    }


    public SMSProcessor create(final Context context, final String senderName, final String smsBody) throws UnknownSenderException {
        final Sender sender = retrieveSender(senderName);
        final SMSCodeParser smsCodeParser = createSMSCodeParser(sender.getCodesRegularExpressions());
        final Clipboard clipboard = createClipboard(context);
        final SMSInfoPresenter smsInfoPresenter = createSMSInfoPresenter(sender, smsBody);
        return new SMSProcessor(clipboard, smsCodeParser, smsInfoPresenter);
    }

    private SMSInfoPresenter createSMSInfoPresenter(final Sender sender, final String smsBody) {
        return new SMSInfoPresenter(sender, smsBody);
    }

    private Clipboard createClipboard(final Context context) {
        return new Clipboard((ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE));
    }

    private SMSCodeParser createSMSCodeParser(final CodesRegularExpressions codesRegularExpressions) {
        return new SMSCodeParser(codesRegularExpressions);
    }

    private Sender retrieveSender(final String senderName) throws UnknownSenderException {
        final SendersResource sendersResource = SendersResource.getInstance();
        return sendersResource.getSenderByName(senderName);
    }
}
