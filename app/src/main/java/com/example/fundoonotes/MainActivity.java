package com.example.fundoonotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private EditText emailText, passwordText;
    private Button loginButton, registerButton;
    private CheckBox showPassword;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        emailText = (EditText) findViewById(R.id.emailText);
        passwordText = (EditText) findViewById(R.id.passwordText);
        loginButton = (Button) findViewById(R.id.loginButton);
        registerButton = (Button) findViewById(R.id.registerButton);
        showPassword = (CheckBox) findViewById(R.id.showPassword);

        showPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean value) {
                if (value)
                {
                    // Show Password

                    passwordText.setTransformationMethod(HideReturnsTransformationMethod
                            .getInstance());
                }
                else
                {
                    // Hide Password
                    passwordText.setTransformationMethod(PasswordTransformationMethod
                            .getInstance());
                }
            }
        });

        loginButton.setOnClickListener(v -> {
            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();

            if (TextUtils.isEmpty(email) || !email.contains("@")) {
                Toast.makeText(getApplicationContext(),
                        "Please Enter valid Email Address",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(),
                        "Please Enter valid Password",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 8) {
                Toast.makeText(getApplicationContext(),
                        "Password should contain atleast 8 Characters",
                        Toast.LENGTH_LONG).show();
                return;
            }

            mAuth.signInWithEmailAndPassword(email, password).
                    addOnCompleteListener(
                            task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),
                                            "Login Successful!!",
                                            Toast.LENGTH_LONG)
                                            .show();

                                    Intent intent
                                            = new Intent(MainActivity.this,
                                            NotesActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            "Login Failed!",
                                            Toast.LENGTH_SHORT)
                                            .show();
                                    return;
                                }
                            });
        });

        registerButton.setOnClickListener(v-> {

            RegisterFragment registerFragment = new RegisterFragment();
            FragmentManager registerFragmentManager = getSupportFragmentManager();
            FragmentTransaction registerFragmentTransaction = registerFragmentManager
                    .beginTransaction();
            registerFragmentTransaction.replace(R.id.register_fragment,
                    new RegisterFragment())
                    .addToBackStack(null).commit();
        });

    }
}