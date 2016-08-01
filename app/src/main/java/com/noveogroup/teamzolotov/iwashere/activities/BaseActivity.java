package com.noveogroup.teamzolotov.iwashere.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.noveogroup.teamzolotov.iwashere.database.RegionOrmLiteOpenHelper;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayout());

        ButterKnife.bind(this);
    }

    protected void showToast(String message) {
        if (null != message) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    protected abstract int getLayout();

}
