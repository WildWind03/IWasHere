package com.noveogroup.teamzolotov.iwashere.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.noveogroup.teamzolotov.iwashere.database.RegionOrmLiteOpenHelper;

/**
 * Created by dserov on 01/08/16.
 */
public abstract class BaseDatabaseFragment extends BaseFragment {
    protected RegionOrmLiteOpenHelper openHelper;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        if (openHelper == null) {
            openHelper = OpenHelperManager.getHelper(getContext(), RegionOrmLiteOpenHelper.class);
        }
        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (openHelper != null) {
            OpenHelperManager.releaseHelper();
            openHelper = null;
        }
    }
}
