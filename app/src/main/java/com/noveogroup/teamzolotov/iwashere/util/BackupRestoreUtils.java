package com.noveogroup.teamzolotov.iwashere.util;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public final class BackupRestoreUtils {
    private final static String STORAGE_URL = "gs://iwashere-da39d.appspot.com";
    private final static String IMAGES_CHILD_KEY = "regions_database";

    private BackupRestoreUtils() {
        throw new UnsupportedOperationException("Trying to create instance of utility class");
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

    public static void restore(FirebaseUser firebaseUser, File toFile, final OnRestoreSuccessfully onRestoreSuccessfully, final OnRestoreFailed onRestoreFailed) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReferenceFromUrl(STORAGE_URL);
        StorageReference imagesStorageReference = storageReference.child(firebaseUser.getUid()).child(IMAGES_CHILD_KEY);

        imagesStorageReference.getFile(toFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                onRestoreSuccessfully.handle();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                onRestoreFailed.handle(exception);
            }
        });
    }

    public interface OnRestoreFailed {
        void handle(Exception e);
    }

    public interface OnRestoreSuccessfully {
        void handle();
    }

    public interface OnBackupFailed {
        void handle(Exception e);
    }

    public interface OnBackupSuccess {
        void handle();
    }
}
