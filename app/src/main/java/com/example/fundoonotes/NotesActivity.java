package com.example.fundoonotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

import static com.example.fundoonotes.LoginFragment.IS_LOGGED_IN;
import static com.example.fundoonotes.LoginFragment.SHARED_PREFS;

public class NotesActivity extends AppCompatActivity {

    private Button logOut;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        logOut = (Button) findViewById(R.id.logoutButton);

        logOut.setOnClickListener(v -> {
            mAuth.getInstance().signOut();
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(IS_LOGGED_IN,false);
            editor.apply();
            Intent backToMain = new Intent(NotesActivity.this, MainActivity.class);
            startActivity(backToMain);
            finish();

        });

    }
}