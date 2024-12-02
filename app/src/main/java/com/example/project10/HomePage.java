package com.example.project10;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONObject;
import java.net.URI;
import java.net.URISyntaxException;



public class HomePage extends AppCompatActivity {

    private WebSocketClient mWebSocketClient;
    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);
        mTextView = findViewById(R.id.textView);

        // Connect to the WebSocket server
        connectWebSocket();
    }
    private void connectWebSocket() {
        URI uri;
        try {
            // Replace <linux_ip> with the actual IP address of the Linux device
            uri = new URI("ws://192.168.10.54:9090");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        // Create the WebSocket client
        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.d("WebSocket", "Opened connection");
                // Send a subscription request to the topic on the rosbridge server
                String subscribeMessage = "{\"op\": \"subscribe\", \"topic\": \"/example_topic\"}";
                mWebSocketClient.send(subscribeMessage);
            }

            @Override
            public void onMessage(String message) {
                Log.d("WebSocket", "Received: " + message);

                // Update the UI with the received message
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Display the message in the TextView
                        mTextView.setText("Received: " + message);
                    }
                });
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.d("WebSocket", "Closed: " + reason);
            }

            @Override
            public void onError(Exception ex) {
                Log.d("WebSocket", "Error: " + ex.getMessage());
            }
        };

        // Start the WebSocket connection
        mWebSocketClient.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mWebSocketClient != null) {
            mWebSocketClient.close();
        }
    }



}