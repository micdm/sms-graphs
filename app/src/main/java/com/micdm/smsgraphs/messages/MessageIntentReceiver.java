package com.micdm.smsgraphs.messages;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import com.micdm.smsgraphs.data.Message;
import com.micdm.smsgraphs.db.DbHelper;
import com.micdm.smsgraphs.db.writers.DbOperationWriter;

public class MessageIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            String address = getAddress(intent);
            if (address.equals(MessageReader.SERVICE_NUMBER)) {
                checkForNewMessages(context);
            }
        }
    }

    private String getAddress(Intent intent) {
        Object[] pdus = (Object[]) intent.getExtras().get("pdus");
        SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[0]);
        return message.getDisplayOriginatingAddress();
    }

    private void checkForNewMessages(Context context) {
        DbHelper dbHelper = new DbHelper(context);
        final DbOperationWriter writer = new DbOperationWriter(dbHelper);
        MessageReader reader = new MessageReader(context, new MessageReader.OnMessageListener() {
            @Override
            public void onProgress(int total, int current) {}
            @Override
            public void onMessage(Message message) {
                writer.write(message);
            }
        });
        reader.read();
    }
}
