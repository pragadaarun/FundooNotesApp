package com.example.fundoonotes;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private EditText emailText, passwordText;
    private TextView signUpText;
    private Button loginButton;
    private CheckBox showPassword;

    private FirebaseAuth mAuth;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);

    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        emailText = (EditText) getView().findViewById(R.id.emailText);
        passwordText = (EditText) getView().findViewById(R.id.passwordText);
        loginButton = (Button) getView().findViewById(R.id.loginButton);
        signUpText = (TextView) getView().findViewById(R.id.signUpText);
        showPassword = (CheckBox) getView().findViewById(R.id.showPassword);

        showPassword.setOnCheckedChangeListener(new CompoundButton
                .OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton,
                                         boolean value) {
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

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                            task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(),
                                            "Login Successful!!",
                                            Toast.LENGTH_LONG)
                                            .show();

                                    Intent intent
                                            = new Intent(getContext(),
                                            NotesActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getContext(),
                                            "Login Failed!",
                                            Toast.LENGTH_SHORT)
                                            .show();
                                    return;
                                }
                            });
        });

        signUpText.setOnClickListener(v-> {

            ((MainActivity) getActivity()).navigateToRegister();

//            RegisterFragment registerFragment = new RegisterFragment();
//            FragmentManager registerFragmentManager = getFragmentManager();
//            FragmentTransaction registerFragmentTransaction = registerFragmentManager
//                    .beginTransaction();
//            registerFragmentTransaction.replace(R.id.register_fragment, registerFragment)
//                    .addToBackStack(null).commit();

        });
    }
}