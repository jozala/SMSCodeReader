package pl.aetas.android.smscode.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import pl.aetas.android.smscode.basic.SMSProcessor;

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

                String body = sms.getMessageBody();
                String address = sms.getOriginatingAddress();

                SMSProcessor smsProcessor = SMSProcessor.getInstance(context, address, body);
                smsProcessor.readSMS();

            }

        }

    }
}
