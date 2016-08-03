package com.noveogroup.teamzolotov.iwashere.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.noveogroup.teamzolotov.iwashere.database.RegionOrmLiteOpenHelper;

/**
 * Created by dserov on 02/08/16.
 */
public abstract class BaseDatabaseActivity extends BaseActivity {
    protected RegionOrmLiteOpenHelper openHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (openHelper == null) {
            openHelper = OpenHelperManager.getHelper(this, RegionOrmLiteOpenHelper.class);
        }
    }

    @Override
    protected void onDestroy() {
        if (openHelper != null) {
            OpenHelperManager.releaseHelper();
            openHelper = null;
        }
        super.onDestroy();
    }
}
