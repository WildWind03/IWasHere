package com.noveogroup.teamzolotov.iwashere;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * Created by dserov on 01/08/16.
 */
public class FragmentUtils {

    public static void replaceFragment(Fragment fragment, int resId, FragmentManager manager, String tag) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(resId, fragment, tag);
        transaction.commit();
    }

    public static void addFragment(Fragment fragment, int resId, FragmentManager manager, String tag) {
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(resId, fragment, tag);
        transaction.commit();
    }
}
