package com.example.project10;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class SignIn extends AppCompatActivity {
    private boolean isPasswordVisible = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);
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
        }, 3000);
        EditText passwordEditText = findViewById(R.id.password);
        passwordEditText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Calculate if the touch is in the drawableEnd area
                int drawableEndStart = passwordEditText.getWidth()
                        - passwordEditText.getPaddingEnd()
                        - passwordEditText.getCompoundDrawablesRelative()[2].getIntrinsicWidth();
                if (event.getX() >= drawableEndStart) {
                    togglePasswordVisibility(passwordEditText);
                    return true; // Event handled
                }
            }
            return false; // Let the EditText handle other events
        });
    }
    private void togglePasswordVisibility(EditText passwordEditText) {
        if (isPasswordVisible) {
            // Hide the password
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null, null, getDrawable(R.drawable.visibility_close), null
            );
        } else {
            // Show the password
            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null, null, getDrawable(R.drawable.apple), null
            );
        }
        isPasswordVisible = !isPasswordVisible;

        // Preserve cursor position after toggling
        passwordEditText.setSelection(passwordEditText.getText().length());
    }

    public void register(View view) {


    }

    public void signup(View view) {
        Intent intent = new Intent(SignIn.this, SignUp.class);
        startActivity(intent);
    }
}
