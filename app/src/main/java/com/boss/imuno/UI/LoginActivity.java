package com.boss.imuno.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.boss.imuno.R;
import com.boss.imuno.UI.Main.MainActivity;
import com.boss.imuno.UI.details.DetailsActivity;
import com.boss.imuno.Utils.InputValidator;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private String TAG = LoginActivity.class.getSimpleName();

    private TextInputLayout emailTextField;
    private TextInputLayout passwordTextField;

    private String Email;
    private String Password;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        emailTextField = findViewById(R.id.loginEmailTextField);
        passwordTextField = findViewById(R.id.loginPasswordTextField);

        if(emailTextField.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();

        // Enable the Up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseFirestore.getInstance();
    }


    /** checks if a user is logged in already. should never be true but safety check is there anyways just in case.**/
    @Override
    public void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(getString(R.string.user_key),currentUser);
            startActivity(intent);
        }
    }

    /** Handles login button click. Verification of input and error handling is done here too. **/

    public void loginButton(View view){
        Email = String.valueOf(Objects.requireNonNull(emailTextField.getEditText()).getText());
        Password = String.valueOf(Objects.requireNonNull(passwordTextField.getEditText()).getText());

        if(!InputValidator.isEmailValid(Email)){ // Validates email and shows the user error if email doesn't meet criteria. Done so that network call is limited.
            emailTextField.setError(getString(R.string.invalid_email_error));
            emailTextField.setErrorEnabled(true);

            return;
        }

        if(!InputValidator.isPasswordValid(Password)){ // Validates password and shows the user error if password doesn't meet criteria. Done so that network call is limited.
            passwordTextField.setError(getString(R.string.invalid_password_error));
            passwordTextField.setErrorEnabled(true);

            return;
        }

        loginUserFirebase(); // tries to log in user
    }


    /** Logs in user using Firebase Authentication system **/

    private void loginUserFirebase() {

        // Gets days of the week and sets it to the refill day
        setDaySharedPreference();

        final ProgressBar progressBar = findViewById(R.id.loginUserProgressBar);
        progressBar.setVisibility(View.VISIBLE);

        mAuth.signInWithEmailAndPassword(Email, Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            checkDbExists();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, R.string.failed_login_error, Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    //checks if shared preference for refill day exists and if not creates and sets it today

    private void setDaySharedPreference() {

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String refillDay = sharedPref.getString(getString(R.string.refill_day_shared_preference_key), "0");

        if (refillDay != null && refillDay.equals("0")) {

            Calendar c = Calendar.getInstance();
            int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;

            sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(getString(R.string.refill_day_shared_preference_key), String.valueOf(dayOfWeek));
            editor.apply();
        }
    }

    private void checkDbExists() {
        DocumentReference docRef = mDb.collection(getString(R.string.db_collection)).document(Objects.requireNonNull(mAuth.getUid()));

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (Objects.requireNonNull(document).exists()) {

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra(getString(R.string.user_key), mAuth.getCurrentUser());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    } else {
                        Intent intent = new Intent(LoginActivity.this, DetailsActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        LoginActivity.this.finish();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

    public void forgotPasswordButton(View view){
        //TODO: Implement a Forgot Password Flow
    }

}