package com.boss.imuno.UI.register;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.boss.imuno.R;
import com.boss.imuno.Utils.InputValidator;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class RegisterFragment extends Fragment {

    OnButtonClickListener mCallback;
    private int pagePosition;

    public RegisterFragment() {
        // Required empty public constructor
    }

    private TextInputLayout emailTextField;
    private TextInputLayout passwordTextField;

    public interface OnButtonClickListener {
        void onButtonClicked(int position,String text);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnButtonClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnItemClickListener");
        }
    }

    public static RegisterFragment newInstance(int position) {
        RegisterFragment registerFragment = new RegisterFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(String.valueOf(R.string.register_pager_position_key), position);
        registerFragment.setArguments(bundle);
        return registerFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        if(pagePosition == 0){
            emailTextField.post(new Runnable() {
                @Override
                public void run() {
                    emailTextField.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(emailTextField, InputMethodManager.SHOW_IMPLICIT);
                }
            });
        } else if(pagePosition == 1){
            passwordTextField.post(new Runnable() {
                @Override
                public void run() {
                    passwordTextField.requestFocus();
                    InputMethodManager inputMethodManager = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.showSoftInput(passwordTextField, InputMethodManager.SHOW_IMPLICIT);
                }
            });
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        pagePosition = 0;
        if (getArguments() != null) {
            pagePosition = getArguments().getInt(String.valueOf(R.string.register_pager_position_key));
        }

        View root;

        switch (pagePosition){
            case 0:
            default:
                root = inflater.inflate(R.layout.fragment_register_email, container, false);

                emailTextField = root.findViewById(R.id.registerEmailTextField);
                Button nextButton = root.findViewById(R.id.nextButton);

                final int finalPagePosition = pagePosition;

                nextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(InputValidator.isEmailValid(String.valueOf(Objects.requireNonNull(emailTextField.getEditText()).getText()))){
                            emailTextField.setErrorEnabled(false);
                            mCallback.onButtonClicked(finalPagePosition, String.valueOf(emailTextField.getEditText().getText()));
                        } else{
                            emailTextField.setErrorEnabled(true);
                            emailTextField.setError(getString(R.string.invalid_email_error));
                        }
                    }
                });

                break;
            case 1:
                root = inflater.inflate(R.layout.fragment_register_password, container, false);

                passwordTextField = root.findViewById(R.id.registerPasswordTextField);
                Button registerButton = root.findViewById(R.id.registerButton);

                final int finalPagePosition2 = pagePosition;

                registerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(InputValidator.isPasswordValid(String.valueOf(Objects.requireNonNull(passwordTextField.getEditText()).getText()))) {
                            passwordTextField.setErrorEnabled(false);
                            mCallback.onButtonClicked(finalPagePosition2, String.valueOf(passwordTextField.getEditText().getText()));
                        } else{
                            passwordTextField.setErrorEnabled(true);
                            passwordTextField.setError(getString(R.string.invalid_password_error));
                        }
                    }
                });

                break;
        }

        return root;
    }

}