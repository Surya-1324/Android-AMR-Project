package com.example.project10;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class WifiSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_settings);

        // Find the ImageView for the green tick
        ImageView greenTickImageView = findViewById(R.id.greenTick);
        TextView connectionStatusTextView = findViewById(R.id.connectionStatusText);

        // Optionally, make both the green tick and the text visible
        greenTickImageView.setVisibility(ImageView.VISIBLE);
        connectionStatusTextView.setVisibility(TextView.VISIBLE);


    }
}
