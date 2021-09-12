package com.boss.imuno.UI.register;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class RegisterScreenPagerAdapter extends FragmentStateAdapter {

    public RegisterScreenPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return RegisterFragment.newInstance(position);
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
