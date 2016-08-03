package com.noveogroup.teamzolotov.iwashere.util;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

public class RestoreUtils {
    private final static String STORAGE_URL = "gs://iwashere-da39d.appspot.com";
    private final static String IMAGES_CHILD_KEY = "regions_database";

    private RestoreUtils() {

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
}
