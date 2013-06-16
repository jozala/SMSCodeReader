package pl.aetas.android.smscode.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.widget.Toast;
import pl.aetas.android.smscode.R;
import pl.aetas.android.smscode.presenter.Clipboard;

import java.text.MessageFormat;


public class SMSCodeReceivedAlertActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        final String smsCode = this.getIntent().getStringExtra("smsCode");
        final String smsSender = this.getIntent().getStringExtra("smsSender");
        final String smsBody = this.getIntent().getStringExtra("smsBody");

        alertBuilder.setTitle(getString(R.string.smsPassword))
                .setMessage(prepareUserMessage(smsCode, smsSender, smsBody))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        Clipboard clipboard = new Clipboard((ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE));
                        clipboard.save(smsCode);
                        informUser(smsCode);
                        finish();
                    }
                }).setNegativeButton(getString(R.string.rejectCode), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        alertBuilder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            public void onCancel(DialogInterface dialog) {
                finish();

            }
        });
        alertBuilder.show();

    }

    private CharSequence prepareUserMessage(String smsCode, String smsSender, String smsBody) {
        String message = MessageFormat.format(getString(R.string.doYouWantToCopyCodeToClipboard), smsCode, smsSender);
        message += "\n\n" + smsBody;

        return message;
    }

    private void informUser(String smsCode) {
        String message = String.format(getApplicationContext().getString(R.string.smsCodeReceivedAndSavedInClipboard), smsCode);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}