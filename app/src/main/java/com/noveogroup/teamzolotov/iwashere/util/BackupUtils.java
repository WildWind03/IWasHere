package com.noveogroup.teamzolotov.iwashere.util;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class BackupUtils {
    private final static String STORAGE_URL = "gs://iwashere-da39d.appspot.com";
    private final static String IMAGES_CHILD_KEY = "regions_database";
    private BackupUtils() {

    }

    public static void backup(File file, FirebaseUser firebaseUser, final OnBackupFailed onBackupFailed, final OnBackupSuccess onBackupSuccess) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(STORAGE_URL);
        StorageReference imagesStorageReference = storageReference.child(firebaseUser.getUid()).child(IMAGES_CHILD_KEY);

        UploadTask uploadTask = imagesStorageReference.putFile(Uri.fromFile(file));

        uploadTask
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        onBackupFailed.handle(e);
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        onBackupSuccess.handle();
                    }
                });
    }

    public interface OnBackupFailed {
        void handle(Exception e);
    }

    public interface OnBackupSuccess {
        void handle();
    }
}
