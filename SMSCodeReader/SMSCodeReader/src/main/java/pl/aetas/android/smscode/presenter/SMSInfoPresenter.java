package pl.aetas.android.smscode.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;
import pl.aetas.android.smscode.R;
import pl.aetas.android.smscode.activity.SMSCodeReceivedAlertActivity;
import pl.aetas.android.smscode.model.Sender;

public class SMSInfoPresenter {
    private final Sender sender;
    private final String smsBody;
    private final boolean automaticallyCopySMSCode;
    private final boolean presentFullSMSContent;
    private final Clipboard clipboard;
    private final Context context;

    public static SMSInfoPresenter createPresenterWithAutoCopyAndBasicCodeInfo(final Context context,
                                                                               final Clipboard clipboard,
                                                                               final Sender sender,
                                                                               final String smsBody) {
        return new SMSInfoPresenter(context, clipboard, sender, smsBody, true, false);
    }

    public static SMSInfoPresenter createPresenterWithAutoCopyAndFullSMSContent(final Context context,
                                                                                final Clipboard clipboard,
                                                                                final Sender sender,
                                                                                final String smsBody) {
        return new SMSInfoPresenter(context, clipboard, sender, smsBody, true, true);
    }

    public static SMSInfoPresenter createPresenterWithAskingIfCopyAndBasicCodeInfo(final Context context,
                                                                                   final Clipboard clipboard,
                                                                                   final Sender sender,
                                                                                   final String smsBody) {
        return new SMSInfoPresenter(context, clipboard, sender, smsBody, false, false);
    }

    public static SMSInfoPresenter createPresenterWithAskingIfCopyAndFullSMSContent(final Context context,
                                                                                    final Clipboard clipboard,
                                                                                    final Sender sender,
                                                                                    final String smsBody) {
        return new SMSInfoPresenter(context, clipboard, sender, smsBody, false, true);
    }

    private SMSInfoPresenter(final Context context, final Clipboard clipboard, final Sender sender, final String smsBody,
                             final boolean autoCopySMSCode, final boolean presentFullSMSContent) {
        if (context == null) throw new NullPointerException("Context cannot be null");
        if (clipboard == null) throw new NullPointerException("Clipboard cannot be null");
        if (sender == null) throw new NullPointerException("Sender cannot be null");
        if (smsBody == null) throw new NullPointerException("smsBody cannot be null");

        this.context = context;
        this.clipboard = clipboard;
        this.sender = sender;
        this.smsBody = smsBody;
        this.automaticallyCopySMSCode = autoCopySMSCode;
        this.presentFullSMSContent = presentFullSMSContent;
    }

    public void presentInfoToUser(String code) {
        if (!isSenderActiveInPreferences(sender.getSenderName())) {
            return;
        }
        if (automaticallyCopySMSCode) {
            clipboard.save(code);
            String message = prepareUserMessage(code);
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        } else {
            Intent askUserIfCopyIntent = createTransparentLayerActivityIntent();
            askUserIfCopyIntent.putExtra("smsCode", code);
            askUserIfCopyIntent.putExtra("smsSender", sender.getOfficialName());
            askUserIfCopyIntent.putExtra("smsBody", smsBody);
            context.startActivity(askUserIfCopyIntent);
        }
    }

    private boolean isSenderActiveInPreferences(final String senderName) {
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(senderName, true);
    }

    private String prepareUserMessage(final String code) {
        String message = String.format(context.getString(R.string.smsCodeReceivedAndSavedInClipboard), code);
        if (presentFullSMSContent) {
            message += "\n\n" + smsBody;
        }
        return message;
    }

    private Intent createTransparentLayerActivityIntent() {
        final Intent intent = new Intent(context, SMSCodeReceivedAlertActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }
}
