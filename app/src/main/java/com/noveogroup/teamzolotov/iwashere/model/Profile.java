package com.noveogroup.teamzolotov.iwashere.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Profile implements Parcelable {
    private final String email;
    private final String username;

    public Profile(String email, String username) {
        this.email = email;
        this.username = username;
    }

    protected Profile(Parcel in) {
        email = in.readString();
        username = in.readString();
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
    }
}
