package com.noveogroup.teamzolotov.iwashere.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.noveogroup.teamzolotov.iwashere.R;

public class AccountFragment extends BaseFragment {


    public static AccountFragment newInstance() {
        return new AccountFragment();
    }

    @Override
    protected void onPostViewCrated(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected int getLayout() {
        return R.layout.account_layout;
    }
}
