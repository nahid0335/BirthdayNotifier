package com.example.birthdaynotifier.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.birthdaynotifier.R;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SettingsFragment extends Fragment {
    boolean signInClick = false;
    boolean signUpClick = false;
    boolean forgetPasswordClick = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);


        final TextView signInTextView = rootView.findViewById(R.id.textView_settings_signinTitle);
        final TextView signUpTextView = rootView.findViewById(R.id.textView_settings_signUpTitle);
        final TextView forgetPasswordTextView = rootView.findViewById(R.id.textView_settings_forgetPasswordTitle);

        final TextView signInButton = rootView.findViewById(R.id.textView_settings_signIn);
        final TextView signUpButton = rootView.findViewById(R.id.textView_settings_signUp);
        final TextView forgetPasswordButton = rootView.findViewById(R.id.textView_settings_forgetPassword);
        TextView clearDataButton = rootView.findViewById(R.id.textView_settings_clearAllData);

        final TextInputLayout signInEmail = rootView.findViewById(R.id.textInputLayout_settings_signInemail);
        final TextInputLayout signInPassword = rootView.findViewById(R.id.textInputLayout_settings_signInPassword);
        final TextInputLayout signUpEmail = rootView.findViewById(R.id.textInputLayout_settings_signUpEmail);
        final TextInputLayout signUpPassword = rootView.findViewById(R.id.textInputLayout_settings_signUpPassword);
        final TextInputLayout forgetPasswordEmail = rootView.findViewById(R.id.textInputLayout_settings_forgetPasswordEmail);

        final TextView subtitle1 = rootView.findViewById(R.id.textView_settings_signinDetails);
        final TextView subtitle2 = rootView.findViewById(R.id.textView_settings_signinDetails2);
        final TextView subtitle3 = rootView.findViewById(R.id.textView_settings_signinDetails3);


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



        return rootView;
    }
}
