package com.noveogroup.teamzolotov.iwashere.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.noveogroup.teamzolotov.iwashere.R;

public class SplashFragment extends BaseFragment {

    public static SplashFragment newInstance() {
        return new SplashFragment();
    }

    @Override
    protected void onPostViewCrated(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected int getLayout() {
        return R.layout.splash_screen;
    }
}
