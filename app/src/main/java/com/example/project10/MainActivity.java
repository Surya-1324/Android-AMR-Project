package com.example.project10;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int[] images = new int[] {
                R.drawable.astra,  // replace with your image resources
                R.drawable.astra,
                R.drawable.astra
        };
        // Initialize ViewPager2
        ViewPager2 viewPager = findViewById(R.id.viewPager);

        // Set the adapter for ViewPager2
        ImageAdapter adapter = new ImageAdapter(images);
        viewPager.setAdapter(adapter);

        // Initialize TabLayout
        TabLayout tabLayout = findViewById(R.id.tabLayout);


        // Link ViewPager2 and TabLayout (dots indicator)
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            // Empty, no specific configuration for tabs
        }).attach();

        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                int currentItem = viewPager.getCurrentItem();
                int nextItem = (currentItem == images.length - 1) ? 0 : currentItem + 1;
                viewPager.setCurrentItem(nextItem, true);
                viewPager.postDelayed(this, 3000);  // Auto-change every 3 seconds
            }
        }, 1000);
        new Handler().postDelayed(() -> {

            Intent intent = new Intent(MainActivity.this, SignIn.class);
            startActivity(intent);

            finish();
        }, 3000);
    }
    }



