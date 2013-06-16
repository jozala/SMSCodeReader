package pl.aetas.android.smscode.presenter;

import android.text.ClipboardManager;

public class Clipboard {

    private final ClipboardManager clipboardManager;

    public Clipboard(final ClipboardManager clipboardManager) {
        this.clipboardManager = clipboardManager;
    }

    public void save(final CharSequence stringToSave) {
        clipboardManager.setText(stringToSave);
    }
}
