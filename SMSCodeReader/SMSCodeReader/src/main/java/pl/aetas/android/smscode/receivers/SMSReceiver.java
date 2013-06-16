package pl.aetas.android.smscode.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import de.akquinet.android.androlog.Log;
import pl.aetas.android.smscode.db.SMSCodeReaderSQLiteHelper;
import pl.aetas.android.smscode.exception.UnknownSenderException;
import pl.aetas.android.smscode.resource.SendersResource;
import pl.aetas.android.smscode.smsprocessor.SMSProcessor;
import pl.aetas.android.smscode.smsprocessor.SMSProcessorFactory;

public class SMSReceiver extends BroadcastReceiver {

    private static final String SMS_EXTRA_NAME = "pdus";

    @Override
    public void onReceive(Context context, Intent intent) {
        // Get the SMS map from Intent
        Bundle extras = intent.getExtras();

        if (extras != null) {
            // Get received SMS array
            Object[] smsExtra = (Object[]) extras.get(SMS_EXTRA_NAME);
            for (final Object aSmsExtra : smsExtra) {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) aSmsExtra);

                final String body = sms.getMessageBody();
                final String address = sms.getOriginatingAddress();

                SendersResource sendersResource = new SendersResource(new SMSCodeReaderSQLiteHelper(context));
                if (!sendersResource.isSenderKnown(address)) {
                    return;
                }
                SMSProcessorFactory smsProcessorFactory = SMSProcessorFactory.getInstance();
                try {
                    SMSProcessor smsProcessor = smsProcessorFactory.create(context, sendersResource, address, body);
                    smsProcessor.processSMS(body);
                } catch (UnknownSenderException e) {
                    Log.e(SMSReceiver.class.getName(), "Sender should be known (it has been checked earlier)", e);
                    throw new RuntimeException(e);
                }
            }

        }

    }
}
