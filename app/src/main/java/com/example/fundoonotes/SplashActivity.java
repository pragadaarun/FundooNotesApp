package com.example.fundoonotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import static com.example.fundoonotes.LoginFragment.IS_LOGGED_IN;
import static com.example.fundoonotes.LoginFragment.SHARED_PREFERENCES;

public class SplashActivity extends AppCompatActivity {

    private boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams
                .FLAG_FULLSCREEN);

        new Handler().postDelayed(() -> {
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE);
            isLoggedIn = sharedPreferences.getBoolean(IS_LOGGED_IN, false);
            Intent intent;
            if(isLoggedIn){
                intent = new Intent(SplashActivity.this, NotesActivity.class);
            }else{
                intent = new Intent(SplashActivity.this, MainActivity.class);
            }
            startActivity(intent);
            finish();
        },3000);
    }
}