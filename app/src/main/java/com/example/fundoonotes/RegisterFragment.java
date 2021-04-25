package com.example.fundoonotes;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.Executor;

public class RegisterFragment extends Fragment {

    private EditText fullName, emailText, passwordText;
    private Button registerButton;
    private TextView loginText;
    FirebaseAuth mAuth;

    private RegisterViewModel mViewModel;

    public static RegisterFragment newInstance() {
        return new RegisterFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.register_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        mAuth = FirebaseAuth.getInstance();

        fullName = (EditText) getView().findViewById(R.id.fullName);
        emailText = (EditText) getView().findViewById(R.id.emailText);
        passwordText = (EditText) getView().findViewById(R.id.passwordText);
        registerButton = (Button) getView().findViewById(R.id.registerButton);
        loginText = (TextView) getView().findViewById(R.id.loginText);

        registerButton.setOnClickListener(v -> {

            String name = fullName.getText().toString();
            String email = emailText.getText().toString();
            String password = passwordText.getText().toString();

            if (TextUtils.isEmpty(name) || name.length() < 3) {
                fullName.setError("Requires Your Name");
                Toast.makeText(getContext(),
                        "Please Enter valid Name",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            if (TextUtils.isEmpty(email) || !email.contains("@")) {
                emailText.setError("Requires Email Address");
                Toast.makeText(getContext(),
                        "Please Enter valid Email Address",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (TextUtils.isEmpty(password)) {
                passwordText.setError("Requires password");
                Toast.makeText(getContext(),
                        "Please Enter valid Password",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 8) {
                passwordText.setError("Enter minimum Eight Characters");
                Toast.makeText(getContext(),
                        "Password should contain atleast 8 Characters",
                        Toast.LENGTH_LONG).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password).
                    addOnCompleteListener(
                            new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Intent intent
                                = new Intent(getContext(),
                                NotesActivity.class);
                        startActivity(intent);
                    } else {

                        Toast.makeText(getContext(),
                                "SignUp Unsuccessful, Please Try Again",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                });


        });


    }

}