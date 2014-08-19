package com.micdm.smsgraphs.messages;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import com.micdm.smsgraphs.misc.Logger;

public class MessageIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            String address = getAddress(intent);
            if (address.equals(MessageReader.SERVICE_NUMBER)) {
                startMessageService(context);
            }
        }
    }

    private String getAddress(Intent intent) {
        Object[] pdus = (Object[]) intent.getExtras().get("pdus");
        SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[0]);
        return message.getDisplayOriginatingAddress();
    }

    private void startMessageService(final Context context) {
        Logger.debug("New SMS message received, starting service...");
        Intent intent = new Intent(context, MessageService.class);
        context.startService(intent);
    }
}
