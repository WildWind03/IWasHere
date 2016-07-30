package com.noveogroup.teamzolotov.iwashere.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.noveogroup.teamzolotov.iwashere.R;
import com.noveogroup.teamzolotov.iwashere.model.Profile;

import butterknife.BindView;

public class AccountFragment extends BaseFragment {

    private final static String PROFILE_KEY = "PROFILE_KEY";

    @BindView(R.id.username_text_view)
    protected TextView usernameTextView;

    @BindView(R.id.email_text_view)
    protected TextView emailTextView;

    public static AccountFragment newInstance(Profile profile) {
        AccountFragment accountFragment = new AccountFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(PROFILE_KEY, profile);
        accountFragment.setArguments(bundle);
        return accountFragment;
    }

    @Override
    protected void onPostViewCrated(@Nullable Bundle savedInstanceState) {
        Profile profile = getArguments().getParcelable(PROFILE_KEY);
        if (null != profile) {
            usernameTextView.setText(profile.getUsername());
            emailTextView.setText(profile.getEmail());
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.account_layout;
    }
}
