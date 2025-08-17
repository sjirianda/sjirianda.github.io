package com.example.myapplication;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SMSPermissionActivity extends AppCompatActivity {

    private static final int SMS_PERMISSION_CODE = 1;
    private static final String PHONE_NUMBER = "5551234567"; // Use emulator or test number

    private Button requestPermissionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        requestPermissionButton = findViewById(R.id.buttonRequestSmsPermission);

        // Set up button click to trigger permission request
        requestPermissionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestSmsPermission();
            }
        });
    }

    private void checkAndRequestSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // Request SMS permission
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{android.Manifest.permission.SEND_SMS},
                    SMS_PERMISSION_CODE
            );
        } else {
            // Permission already granted
            sendSMSNotification("🎯 Goal achieved! You hit your target weight!");
            navigateToDataDisplayActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendSMSNotification("🎯 Goal achieved! You hit your target weight!");
            } else {
                Toast.makeText(this, "SMS permission denied. App will continue without SMS alerts.", Toast.LENGTH_LONG).show();
            }
            // Proceed with the rest of the app regardless
            navigateToDataDisplayActivity();
        }
    }

    private void sendSMSNotification(String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(PHONE_NUMBER, null, message, null, null);
            Toast.makeText(this, "SMS Sent Successfully!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to send SMS: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void navigateToDataDisplayActivity() {
        Intent intent = new Intent(SMSPermissionActivity.this, DataDisplayActivity.class);
        startActivity(intent);
        finish(); // Close this activity
    }
}
