package com.example.fundoonotes;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterFragment extends Fragment {

    private EditText fullName, emailText, passwordText, phoneText;
    private Button registerButton;
    private TextView loginText;
    FirebaseAuth mFirebaseAuth;

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

        mFirebaseAuth = FirebaseAuth.getInstance();

        fullName = (EditText) getView().findViewById(R.id.fullName);
        phoneText = (EditText) getView().findViewById(R.id.phoneText);
        emailText = (EditText) getView().findViewById(R.id.emailText);
        passwordText = (EditText) getView().findViewById(R.id.passwordText);
        registerButton = (Button) getView().findViewById(R.id.registerButton);
        loginText = (TextView) getView().findViewById(R.id.loginText);

        registerButton.setOnClickListener(this::registerUser);

        loginText.setOnClickListener(v -> {

            LoginFragment loginFragment = new LoginFragment();
            FragmentManager loginFragmentManager = getFragmentManager();
            FragmentTransaction loginFragmentTransaction = loginFragmentManager
                    .beginTransaction();
            loginFragmentTransaction.replace(R.id.register_fragment, loginFragment)
                    .addToBackStack(null).commit();


        });


    }

    private void registerUser(View v) {

        String name = fullName.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String phone = phoneText.getText().toString();

        if (name.isEmpty()) {
            fullName.setError("Please enter name id");
            fullName.requestFocus();
        } else if (name.matches("[0-9~`!@#$%^&*()_+={}:;<>,.?/]*")) {
            fullName.setError("Please Enter Valid Name");
            fullName.requestFocus();
        } else if (email.isEmpty() && !(password.isEmpty())) {
            fullName.setError("Please Enter Email id");
            fullName.requestFocus();
        } else if (!email.matches("^[a-zA-Z]+([._+-]{0,1}[a-zA-Z0-9]+)*@[a-zA-Z0-9]+.[a-zA-Z]{2,4}+(?:\\.[a-z]{2,}){0,1}$")) {
            fullName.setError("Please enter valid email id");
            fullName.requestFocus();
        } else if (password.isEmpty() && !(email.isEmpty())) {
            passwordText.setError("Please Enter Password");
            passwordText.requestFocus();
        } else if (!password.matches("(^(?=.*[A-Z]))(?=.*[0-9])(?=.*[a-z])(?=.*[@*&^%#-*+!]{1}).{8,}$")) {
            passwordText.setError("Valid Password should contain at least 8 characters");
            passwordText.requestFocus();
        } else if (phone.isEmpty()) {
            phoneText.setError("Please Enter Phone Number");
            phoneText.requestFocus();
        } else if (!phone.matches("(([0-9]{2})?)[0-9]{10}")) {
            phoneText.setError("Please Enter Valid Phone Number with country code");
            phoneText.requestFocus();
        } else if (email.isEmpty() && password.isEmpty()) {
            Toast.makeText(getContext(), "Fields Are Empty!",
                    Toast.LENGTH_SHORT).show();
        }
        mFirebaseAuth.createUserWithEmailAndPassword(email, password).
                addOnCompleteListener(
                        task -> {
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
                        });

    }
}