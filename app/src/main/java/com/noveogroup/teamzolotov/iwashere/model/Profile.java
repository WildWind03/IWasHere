package com.noveogroup.teamzolotov.iwashere.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Profile implements Parcelable {
    private final String email;
    private final String username;
    private final String password;
    private final String uid;

    public Profile(String email, String username, String password, String uid) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.uid = uid;
    }

    protected Profile(Parcel in) {
        email = in.readString();
        username = in.readString();
        password = in.readString();
        uid = in.readString();
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(email);
        parcel.writeString(username);
        parcel.writeString(password);
        parcel.writeString(uid);
    }
}
