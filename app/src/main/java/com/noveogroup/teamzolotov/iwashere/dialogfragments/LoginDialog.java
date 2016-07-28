package com.noveogroup.teamzolotov.iwashere.dialogfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.noveogroup.teamzolotov.iwashere.R;
import com.noveogroup.teamzolotov.iwashere.fragments.BaseFragment;


public class LoginDialog extends BaseFragment {

    public static LoginDialog newInstance() {
        return new LoginDialog();
    }

    @Override
    protected void onPostViewCrated(@Nullable Bundle savedInstanceState) {
        //getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    protected int getLayout() {
        return R.layout.login_layout;
    }
}
