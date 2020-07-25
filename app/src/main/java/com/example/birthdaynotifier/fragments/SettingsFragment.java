package com.example.birthdaynotifier.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.birthdaynotifier.AddEditActivity;
import com.example.birthdaynotifier.OnlineActivity;
import com.example.birthdaynotifier.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {
    boolean signInClick = false;
    boolean signUpClick = false;
    boolean forgetPasswordClick = false;

    TextInputEditText signInEmailET,signInPasswordET,signUpEmailET,signUpPasswordET,forgetPasswordET;
    TextInputLayout signInEmail, signInPassword,signUpEmail,signUpPassword,forgetPasswordEmail;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        progressBar = rootView.findViewById(R.id.progressBar_settings_onlineProgress);

        final TextView signInTextView = rootView.findViewById(R.id.textView_settings_signinTitle);
        final TextView signUpTextView = rootView.findViewById(R.id.textView_settings_signUpTitle);
        final TextView forgetPasswordTextView = rootView.findViewById(R.id.textView_settings_forgetPasswordTitle);

        final TextView signInButton = rootView.findViewById(R.id.textView_settings_signIn);
        final TextView signUpButton = rootView.findViewById(R.id.textView_settings_signUp);
        final TextView forgetPasswordButton = rootView.findViewById(R.id.textView_settings_forgetPassword);
        TextView clearDataButton = rootView.findViewById(R.id.textView_settings_clearAllData);

        signInEmail = rootView.findViewById(R.id.textInputLayout_settings_signInemail);
        signInPassword = rootView.findViewById(R.id.textInputLayout_settings_signInPassword);
        signUpEmail = rootView.findViewById(R.id.textInputLayout_settings_signUpEmail);
        signUpPassword = rootView.findViewById(R.id.textInputLayout_settings_signUpPassword);
        forgetPasswordEmail = rootView.findViewById(R.id.textInputLayout_settings_forgetPasswordEmail);

        final TextView subtitle1 = rootView.findViewById(R.id.textView_settings_signinDetails);
        final TextView subtitle2 = rootView.findViewById(R.id.textView_settings_signinDetails2);
        final TextView subtitle3 = rootView.findViewById(R.id.textView_settings_signinDetails3);

        signInEmailET = rootView.findViewById(R.id.textInputEditText_settings_signInEmail);
        signInPasswordET = rootView.findViewById(R.id.textInputEditText_settings_signInPassword);
        signUpEmailET = rootView.findViewById(R.id.textInputEditText_settings_signUpEmail);
        signUpPasswordET = rootView.findViewById(R.id.textInputEditText_settings_signUpPassword);
        forgetPasswordET = rootView.findViewById(R.id.textInputEditText_settings_forgetPasswordEmail);


        signInTextView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_keyboard_arrow_up_24,0);
        signUpTextView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_keyboard_arrow_up_24,0);
        forgetPasswordTextView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_keyboard_arrow_up_24,0);

        subtitle1.setVisibility(View.GONE);
        signInEmail.setVisibility(View.GONE);
        signInPassword.setVisibility(View.GONE);
        signInButton.setVisibility(View.GONE);

        subtitle2.setVisibility(View.GONE);
        signUpEmail.setVisibility(View.GONE);
        signUpPassword.setVisibility(View.GONE);
        signUpButton.setVisibility(View.GONE);


        subtitle3.setVisibility(View.GONE);
        forgetPasswordEmail.setVisibility(View.GONE);
        forgetPasswordButton.setVisibility(View.GONE);


        signInTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(signInClick){
                    signInTextView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_keyboard_arrow_up_24,0);

                    subtitle1.setVisibility(View.GONE);
                    signInEmail.setVisibility(View.GONE);
                    signInPassword.setVisibility(View.GONE);
                    signInButton.setVisibility(View.GONE);

                    signInClick = false;

                }else{
                    signInTextView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_keyboard_arrow_down_24,0);

                    subtitle1.setVisibility(View.VISIBLE);
                    signInEmail.setVisibility(View.VISIBLE);
                    signInPassword.setVisibility(View.VISIBLE);
                    signInButton.setVisibility(View.VISIBLE);

                    signInClick = true;
                }
            }
        });

        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(signUpClick){
                    signUpTextView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_keyboard_arrow_up_24,0);

                    subtitle2.setVisibility(View.GONE);
                    signUpEmail.setVisibility(View.GONE);
                    signUpPassword.setVisibility(View.GONE);
                    signUpButton.setVisibility(View.GONE);

                    signUpClick = false;

                }else{
                    signUpTextView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_keyboard_arrow_down_24,0);

                    subtitle2.setVisibility(View.VISIBLE);
                    signUpEmail.setVisibility(View.VISIBLE);
                    signUpPassword.setVisibility(View.VISIBLE);
                    signUpButton.setVisibility(View.VISIBLE);

                    signUpClick = true;
                }
            }
        });

        forgetPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(forgetPasswordClick){
                    forgetPasswordTextView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_keyboard_arrow_up_24,0);

                    subtitle3.setVisibility(View.GONE);
                    forgetPasswordEmail.setVisibility(View.GONE);
                    forgetPasswordButton.setVisibility(View.GONE);

                    forgetPasswordClick = false;

                }else{
                    forgetPasswordTextView.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_keyboard_arrow_down_24,0);

                    subtitle3.setVisibility(View.VISIBLE);
                    forgetPasswordEmail.setVisibility(View.VISIBLE);
                    forgetPasswordButton.setVisibility(View.VISIBLE);

                    forgetPasswordClick = true;
                }
            }
        });


        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                final String email = Objects.requireNonNull(signInEmailET.getText()).toString().trim();
                String password = Objects.requireNonNull(signInPasswordET.getText()).toString().trim();
                if(isSignInValid()){
                    mAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if(task.isSuccessful()){
                                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                                        //Toast.makeText(getContext(),"Sign In successfully done !!",Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getContext(), OnlineActivity.class);
                                        intent.putExtra(OnlineActivity.FIREBASE_USER, firebaseUser);
                                        intent.putExtra(OnlineActivity.FIREBASE_Email, email);
                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email = Objects.requireNonNull(signUpEmailET.getText()).toString().trim();
                final String password = Objects.requireNonNull(signUpPasswordET.getText()).toString().trim();
                if(isSignUpValid()){
                    mAuth.createUserWithEmailAndPassword(email,password)
                            .addOnCompleteListener((Activity) Objects.requireNonNull(getContext()), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if(task.isSuccessful()){
                                        Toast.makeText(getContext(),"Sign up successfully done !!",Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });


        forgetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email = Objects.requireNonNull(forgetPasswordET.getText()).toString().trim();
                if(isForgetPasswordValid()){
                    mAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if(task.isSuccessful()){
                                        Toast.makeText(getContext(),"Please check your email to reset password !!",Toast.LENGTH_LONG).show();
                                    }else{
                                        Toast.makeText(getContext(), Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }else {
                    progressBar.setVisibility(View.GONE);
                }
            }
        });



        return rootView;
    }

    private boolean isSignInValid() {
        String email = Objects.requireNonNull(signInEmailET.getText()).toString().trim();
        String password = Objects.requireNonNull(signInPasswordET.getText()).toString().trim();

        if (email.isEmpty()) {
            signInEmail.setError("Field can't be empty !!");
            return false;
        }else if(password.isEmpty()){
            signInPassword.setError("Field can't be empty !!");
            return false;
        }else if (password.length() < 6) {
            signInPassword.setError("Password is too short !!");
            return false;
        } else if (password.length()>15) {
            signInPassword.setError("Password is too long !!");
            return false;
        }else {
            signInEmail.setError(null);
            signInPassword.setError(null);
            return true;
        }
    }

    private boolean isSignUpValid() {
        String email = Objects.requireNonNull(signUpEmailET.getText()).toString().trim();
        String password = Objects.requireNonNull(signUpPasswordET.getText()).toString().trim();

        if (email.isEmpty()) {
            signUpEmail.setError("Field can't be empty !!");
            return false;
        }else if(password.isEmpty()){
            signUpPassword.setError("Field can't be empty !!");
            return false;
        }else if (password.length() < 6) {
            signUpPassword.setError("Password is too short !!");
            return false;
        } else if (password.length()>15) {
            signUpPassword.setError("Password is too long !!");
            return false;
        }else {
            signUpEmail.setError(null);
            signUpPassword.setError(null);
            return true;
        }
    }

    private boolean isForgetPasswordValid() {
        String email = Objects.requireNonNull(forgetPasswordET.getText()).toString().trim();

        if (email.isEmpty()) {
            forgetPasswordEmail.setError("Field can't be empty !!");
            return false;
        }else {
            forgetPasswordEmail.setError(null);
            return true;
        }
    }


}
