package com.bluezooweb.jdiamond.Activites;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bluezooweb.jdiamond.R;

public class SendSms extends AppCompatActivity {

    private static final String TAG = "Send Sms";
    EditText et_phone, et_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sms);

        Button submit = findViewById(R.id.sendSubmit);
        et_message = findViewById(R.id.message);
        et_phone = findViewById(R.id.phone);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = et_message.getText().toString();
                String phone = et_phone.getText().toString();

                sendSms(message, phone);

            }
        });

    }

    private void sendSms(String message, String phone) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            try {
                SmsManager smsMgrVar = SmsManager.getDefault();
                smsMgrVar.sendTextMessage(phone, null, message, null, null);
                Toast.makeText(getApplicationContext(), "Message Sent",
                        Toast.LENGTH_LONG).show();
            } catch (Exception ErrVar) {
                Log.i(TAG, "sendSms: error: " + ErrVar.getLocalizedMessage());
                Toast.makeText(getApplicationContext(), ErrVar.getMessage().toString(),
                        Toast.LENGTH_LONG).show();
                ErrVar.printStackTrace();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 10);
            }
        }

    }

}
