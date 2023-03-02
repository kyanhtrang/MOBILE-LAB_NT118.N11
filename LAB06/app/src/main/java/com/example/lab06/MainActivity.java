package com.example.lab06;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.telephony.SmsMessage;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Console;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver broadcastReceiver;
    private IntentFilter filter;

    private void processRecieve(Context context, Intent intent)
    {
        Toast.makeText(context, getString(R.string.you_have_a_new_message),
        Toast.LENGTH_LONG).show();

        TextView tvContent = (TextView) findViewById(R.id.tv_content);

        final String SMS_EXTRA = "pdus";
        Bundle bundle = intent.getExtras();

        Object[] message = (Object[]) bundle.get(SMS_EXTRA);
        String sms ="";

        SmsMessage smsMsg;
        for(int i = 0; i <message.length; i++)
        {
            if(android.os.Build.VERSION.SDK_INT >= 23)
                smsMsg = SmsMessage.createFromPdu((byte[]) message[i], "");
            else
                smsMsg = SmsMessage.createFromPdu((byte[]) message[i]);

            String msgBody = smsMsg.getMessageBody();
            String address = smsMsg.getDisplayOriginatingAddress();
            sms += address + ":\n" + msgBody +"\n";
        }

        tvContent.setText(sms);
    }

    private void initBroadcastReceiver()
    {
        filter = new IntentFilter("android.provider.Telephony, SMS_RECEIVER");
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                processRecieve(context, intent);
            }
        };
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(broadcastReceiver == null)
        {
            initBroadcastReceiver();
        }
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected  void onStop()
    {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initBroadcastReceiver();
    }
}