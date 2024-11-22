package com.example.project10;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;

import java.util.List;

public class WifiNetworkAdapter extends RecyclerView.Adapter<WifiNetworkAdapter.WifiViewHolder> {

    private Context context;
    private List<String> wifiList;
    private WifiManager wifiManager;

    public WifiNetworkAdapter(Context context, List<String> wifiList) {
        this.context = context;
        this.wifiList = wifiList;
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    }

    @Override
    public WifiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the wifi network item layout
        View view = LayoutInflater.from(context).inflate(R.layout.wifi_network_item, parent, false);
        return new WifiViewHolder(view);
    }

    @Override
    public void onBindViewHolder(WifiViewHolder holder, int position) {
        String ssid = wifiList.get(position);
        holder.ssidText.setText(ssid);

        // Set click listener to open Wi-Fi picker
        holder.itemView.setOnClickListener(v -> openWifiSettings());
    }

    @Override
    public int getItemCount() {
        return wifiList.size();
    }

    private void openWifiSettings() {
        // Open Wi-Fi settings to allow the user to select the SSID and enter the password
        Intent intent = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);
        context.startActivity(intent);
    }

    // ViewHolder class to hold references to the views
    public static class WifiViewHolder extends RecyclerView.ViewHolder {
        TextView ssidText;

        public WifiViewHolder(View itemView) {
            super(itemView);
            ssidText = itemView.findViewById(R.id.ssidText);
        }
    }
}
