package com.boss.imuno.UI.register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.boss.imuno.R;
import com.boss.imuno.UI.details.DetailsActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity implements RegisterFragment.OnButtonClickListener{

    private String emailText;
    private String passwordText;

    private FirebaseAuth mAuth;
    private ViewPager2 viewPager;

    private boolean creatingUser = false;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        actionBar = getSupportActionBar();

        // Enable the Up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Setup ViewPager
        viewPager = findViewById(R.id.registerPager);
        FragmentStateAdapter pagerAdapter = new RegisterScreenPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setUserInputEnabled(false);

        // Initialize Firebase Authentication Service
        mAuth = FirebaseAuth.getInstance();
    }


    /** Handles back navigation for ViewPager2 **/
    @Override
    public boolean onSupportNavigateUp(){
        if(!creatingUser){
            if (viewPager.getCurrentItem() == 0) {
                // If the user is currently looking at the first step, allow the system to handle the
                // Back button. This calls finish() on this activity and pops the back stack.
                super.onSupportNavigateUp();
            } else {
                // Otherwise, select the previous step.
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            }
        }
        return true;
    }

    /** Handles button clicks from fragment through a callback **/
    @Override
    public void onButtonClicked(int position, String text) {
        switch(position){
            case 0: // Email entry fragment button click
                emailText = text;
                viewPager.setCurrentItem(viewPager.getCurrentItem()  + 1, true);
                break;

            case 1: // Create account fragment button click
            passwordText = text;
            registerUserFirebase();
            break;
        }
    }

    /** Creates user using Firebase Authentication system **/
    private void registerUserFirebase(){

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK) - 1;

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.refill_day_shared_preference_key), String.valueOf(dayOfWeek));
        editor.apply();

        creatingUser = true;
        final ProgressBar progressBar = findViewById(R.id.registerUserProgressBar);
        progressBar.setVisibility(View.VISIBLE);

        actionBar.setDisplayShowHomeEnabled(false);

        mAuth.createUserWithEmailAndPassword(emailText, passwordText)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            progressBar.setVisibility(View.GONE);

                            //start detailsActivity
                            Intent intent = new Intent(RegisterActivity.this, DetailsActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            RegisterActivity.this.finish();
                        } else {
                            creatingUser = false;
                            actionBar.setDisplayShowHomeEnabled(true);
                            progressBar.setVisibility(View.GONE);
                            // Shows user toast message to inform an error occurred creating the account.
                            Toast.makeText(RegisterActivity.this, R.string.account_creation_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}