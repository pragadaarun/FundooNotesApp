package com.example.fundoonotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

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