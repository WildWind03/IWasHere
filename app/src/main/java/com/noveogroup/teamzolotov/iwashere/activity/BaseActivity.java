package com.noveogroup.teamzolotov.iwashere.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.noveogroup.teamzolotov.iwashere.R;

import butterknife.ButterKnife;

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

    protected void showMessage(String title, String message) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog
                .Builder(this)
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

    protected abstract int getLayout();

}
