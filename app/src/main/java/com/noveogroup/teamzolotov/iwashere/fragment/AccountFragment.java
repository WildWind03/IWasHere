package com.noveogroup.teamzolotov.iwashere.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.noveogroup.teamzolotov.iwashere.R;
import com.noveogroup.teamzolotov.iwashere.activity.Registrable;
import com.noveogroup.teamzolotov.iwashere.model.Profile;

import java.util.logging.Logger;

import butterknife.BindView;
import butterknife.OnClick;

public class AccountFragment extends BaseFragment {

    private final static String PROFILE_KEY = "PROFILE_KEY";
    private static final Logger logger = Logger.getLogger(AccountFragment.class.getName());

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

    @OnClick(R.id.sign_out_button)
    protected void onSignOutButtonClick() {
        Activity activity = getActivity();

        if (activity instanceof Registrable) {
            ((Registrable) activity).onSignOutClicked();
        } else {
            logger.info("Activity of account fragment must implement registrable interface");
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.account_layout;
    }
}
