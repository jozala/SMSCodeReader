package pl.aetas.android.smscode.presenter;

import android.content.ClipData;
import android.content.ClipboardManager;

public class Clipboard {
    private final static String CLIPDATA_LABEL = "SMSCodeReader code";

    private final ClipboardManager clipboardManager;

    public Clipboard(final ClipboardManager clipboardManager) {
        this.clipboardManager = clipboardManager;
    }

    public void save(final CharSequence stringToSave) {
        clipboardManager.setPrimaryClip(ClipData.newPlainText(CLIPDATA_LABEL, stringToSave));
    }
}
