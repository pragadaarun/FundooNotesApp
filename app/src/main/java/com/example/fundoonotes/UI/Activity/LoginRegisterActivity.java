package com.example.fundoonotes.UI.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.fundoonotes.R;
import com.example.fundoonotes.UI.Fragments.RegisterFragment;
import com.example.fundoonotes.UI.Fragments.LoginFragment;

public class LoginRegisterActivity extends AppCompatActivity {

    private static final String LOGIN_FRAGMENT_TAG = "LoginFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLoginFragment();
    }

    public void navigateToRegister() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,new RegisterFragment()).
                addToBackStack(null).commit();
    }

    public void initLoginFragment() {
        if(getSupportFragmentManager().findFragmentByTag(LOGIN_FRAGMENT_TAG) == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container,
                            new LoginFragment(), LOGIN_FRAGMENT_TAG).commit();
        }
    }


}