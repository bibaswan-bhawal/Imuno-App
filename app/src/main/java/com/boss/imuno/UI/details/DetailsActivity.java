package com.boss.imuno.UI.details;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.boss.imuno.R;
import com.boss.imuno.UI.Main.MainActivity;
import com.boss.imuno.UI.SplashScreenActivity;
import com.boss.imuno.data.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetailsActivity extends AppCompatActivity implements DetailsFragment.OnButtonClickListener {

    private int backPressed = 0;

    private ViewPager2 viewPager;
    private DetailsScreenPagerAdapter pagerAdapter;

    private int Age = 0;
    private int Weight = 0;
    private double Level = 0;
    private int Height = 0;
    private int Minutes = 0;

    private FirebaseFirestore mDb;
    private FirebaseAuth mAuth;
    private int Race;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get a support ActionBar corresponding to this toolbar
        final ActionBar actionBar = getSupportActionBar();

        // Enable the Up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mDb = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        viewPager = findViewById(R.id.detailsPager);
        pagerAdapter = new DetailsScreenPagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.setUserInputEnabled(false);

        if (viewPager.getCurrentItem() == 0){
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(false);
            }
        }

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                if (viewPager.getCurrentItem() == 0){
                    if (actionBar != null) {
                        actionBar.setDisplayHomeAsUpEnabled(false);
                    }
                }else{
                    if (actionBar != null) {
                        actionBar.setDisplayHomeAsUpEnabled(true);
                    }
                }
            }
        });

    }

    @Override
    protected void onResume() {
        backPressed = 0;
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {

            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finishAffinity() on this activity on the second back press and ends the application.

            if(backPressed == 1){
                finishAffinity();
            }else{
                Toast.makeText(this, R.string.quit_application_message, Toast.LENGTH_SHORT).show();
            }
            backPressed++;
        } else {
            // Otherwise, select the previous step.
            backPressed = 0;
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onSupportNavigateUp(){
        if (viewPager.getCurrentItem() == 0) {

            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finishAffinity() on this activity on the second back press and ends the application.

            if(backPressed == 1){
                finishAffinity();
            }else{
                Toast.makeText(this, R.string.quit_application_message, Toast.LENGTH_SHORT).show();
            }
            backPressed++;
        } else {
            // Otherwise, select the previous step.
            backPressed = 0;
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
        return true;
    }

    /** Handles button clicks from fragment using callback **/
    @Override
    public void onButtonClicked(int position, String age, String weight, String height, boolean levelProvided, String level, int race, String minutes) {
        switch (position){
            case 0: // Introduction
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                break;
            case 1: // Collect Age, Weight, and if provided Vitamin D Level
                Age = Integer.parseInt(age);
                Weight = Integer.parseInt(weight);
                Height = Integer.parseInt(height);

                if(levelProvided){
                 Level = Double.parseDouble(level);
                }

                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1, true);
                break;
            case 2:
                Minutes = Integer.parseInt(minutes); // Collect Minutes spent in the sun
                Race = race;
                insertUserData();

                break;
            default:
                viewPager.setCurrentItem(0, true);
                break;
        }
    }

    private void insertUserData(){

        String userId = mAuth.getUid();

        if(userId != null){

            User user = new User(Age, Weight, Level, Height, Race, Minutes);

            pagerAdapter.showProgressBar();

            mDb.collection(getString(R.string.db_collection)).document(userId)
                    .set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            pagerAdapter.hideProgressBar();

                            Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
                            intent.putExtra(getString(R.string.user_key), mAuth.getCurrentUser());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            DetailsActivity.this.finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {  // If adding data fails then user is logged out again and asked to log in prompting the details flow to start again
                            pagerAdapter.hideProgressBar();

                            mAuth.signOut();
                            Toast.makeText(DetailsActivity.this, R.string.add_to_db_error, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(DetailsActivity.this, SplashScreenActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            DetailsActivity.this.finish();
                        }
                    });

        }
    }
}