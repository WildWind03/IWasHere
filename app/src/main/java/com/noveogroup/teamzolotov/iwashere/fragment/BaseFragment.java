package com.noveogroup.teamzolotov.iwashere.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.noveogroup.teamzolotov.iwashere.R;

import butterknife.ButterKnife;

public abstract class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayout(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // CR1: Unbinder
        ButterKnife.bind(this, view);
        onPostViewCrated(savedInstanceState);
    }

    protected void showMessage(String message) {
        if (null != message) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    protected void showMessage(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog
                .Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(R.string.ok_message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

        alertDialogBuilder.show();
    }

    protected void showMessage(int titleResId, int messageResId) {
        showMessage(getString(titleResId), getString(messageResId));
    }

    protected void showMessage(int titleResId, String message) {
        showMessage(getString(titleResId), message);
    }

    protected abstract void onPostViewCrated(@Nullable final Bundle savedInstanceState);

    protected abstract int getLayout();

}
