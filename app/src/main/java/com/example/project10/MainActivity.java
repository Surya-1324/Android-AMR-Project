package com.example.project10;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WifiManager wifiManager;
    private RecyclerView wifiRecyclerView;
    private WifiNetworkAdapter wifiNetworkAdapter;
    private List<String> uniqueNetworks = new ArrayList<>();
    private ProgressBar progressBar;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the Wi-Fi manager
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        // Initialize the RecyclerView and set its layout manager
        wifiRecyclerView = findViewById(R.id.wifiRecyclerView);
        wifiRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize the ProgressBar
        progressBar = findViewById(R.id.progressBar);

        // Initially, set up the adapter with an empty list (RecyclerView will be empty initially)
        wifiNetworkAdapter = new WifiNetworkAdapter(this, uniqueNetworks);
        wifiRecyclerView.setAdapter(wifiNetworkAdapter);

        // Set up the scan button click listener
        Button scanButton = findViewById(R.id.scan_button);
        scanButton.setOnClickListener(view -> scanForWifiNetworks());

        // Register the receiver for Wi-Fi scan results
        registerReceiver(wifiScanReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        // Check and request location permissions if necessary
        checkAndRequestPermissions();
    }

    private void scanForWifiNetworks() {
        // Clear the existing list of networks before starting a new scan
        uniqueNetworks.clear();
        wifiNetworkAdapter.notifyDataSetChanged(); // Notify adapter to clear RecyclerView

        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true); // Enable Wi-Fi if it's not enabled
        }

        // Show the ProgressBar while scanning
        progressBar.setVisibility(View.VISIBLE);

        wifiManager.startScan(); // Start Wi-Fi scan
    }

    private final BroadcastReceiver wifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Hide the ProgressBar when scanning is done
            progressBar.setVisibility(View.GONE);

            List<ScanResult> scanResults = wifiManager.getScanResults();
            uniqueNetworks.clear(); // Clear the list of networks

            for (ScanResult result : scanResults) {
                String ssid = result.SSID;

                // Check if the SSID is already in the list to avoid duplicates
                if (!uniqueNetworks.contains(ssid) && ssid.length()>0) {
                    uniqueNetworks.add(ssid); // Add SSID to the list
                }
            }

            // Update RecyclerView with new Wi-Fi networks
            wifiNetworkAdapter = new WifiNetworkAdapter(context, uniqueNetworks);
            wifiRecyclerView.setAdapter(wifiNetworkAdapter);

            checkWifiConnectionAndStartActivity(context);
        }
    };
    private void checkWifiConnectionAndStartActivity(Context context) {
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        if (wifiInfo != null && wifiInfo.getNetworkId() != -1) {
            // The device is connected to a Wi-Fi network
            String ssid = wifiInfo.getSSID();
            if (ssid != null && !ssid.isEmpty()) {
                // Wi-Fi is connected, start a new activity
                Intent intent = new Intent(context, WifiSettingsActivity.class); // Replace with your activity class
                context.startActivity(intent);
            }
        }
    }

    private void checkAndRequestPermissions() {
        // Check for location permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // If permissions are not granted, request them
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            // If permission already granted, start scanning
            scanForWifiNetworks();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, start scanning
                scanForWifiNetworks();
            } else {
                // Permission denied, show a message to the user
                Toast.makeText(this, "Location permission is required to scan Wi-Fi networks", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Unregister the receiver to avoid memory leaks
        unregisterReceiver(wifiScanReceiver);
    }
}
