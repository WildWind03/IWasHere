package com.noveogroup.teamzolotov.iwashere.util;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

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
