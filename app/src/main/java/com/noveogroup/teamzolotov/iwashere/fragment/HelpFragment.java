package com.noveogroup.teamzolotov.iwashere.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.noveogroup.teamzolotov.iwashere.R;

import butterknife.BindView;

public class HelpFragment extends BaseFragment {

    private final static String LICENSE_TITLE_TAG = "LICENSE_TITLE_TAG";
    private final static String LICENSE_TEXT_TAG = "LICENSE_TEXT_TAG";

    @BindView(R.id.license_title)
    protected TextView licenseTitleTextView;

    @BindView(R.id.license_text)
    protected TextView licenseTextTextView;

    public static HelpFragment newInstance(String licenseTitle, String licenseText) {
        HelpFragment helpFragment = new HelpFragment();

        Bundle bundle = new Bundle();
        bundle.putString(LICENSE_TITLE_TAG, licenseTitle);
        bundle.putString(LICENSE_TEXT_TAG, licenseText);
        helpFragment.setArguments(bundle);

        return helpFragment;
    }

    @Override
    protected void onPostViewCrated(@Nullable Bundle savedInstanceState) {
        String licenseTitle = getArguments().getString(LICENSE_TITLE_TAG, getString(R.string.default_license_title));
        String licenseText = getArguments().getString(LICENSE_TEXT_TAG, getString(R.string.default_license_text));
        licenseTitleTextView.setText(licenseTitle);
        licenseTextTextView.setText(licenseText);
    }

    @Override
    protected int getLayout() {
        return R.layout.help_layout;
    }
}
