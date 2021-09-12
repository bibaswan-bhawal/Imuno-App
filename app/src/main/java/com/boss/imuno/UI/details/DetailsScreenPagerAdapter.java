package com.boss.imuno.UI.details;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;


public class DetailsScreenPagerAdapter extends FragmentStateAdapter {

    private DetailsFragment detailsFragment;

    public DetailsScreenPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        detailsFragment = DetailsFragment.newInstance(position);
        return detailsFragment;
    }

    public void showProgressBar(){
        detailsFragment.showProgressBar();
    }

    public void hideProgressBar(){
        detailsFragment.hideProgressBar();
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}