package id.developer.mahendra.pencarianmagangumb.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UsersApply implements Parcelable {
    private String magangPostId;
    private String userId;

    public UsersApply() {
    }

    protected UsersApply(Parcel in) {
        magangPostId = in.readString();
        userId = in.readString();
    }

    public static final Creator<UsersApply> CREATOR = new Creator<UsersApply>() {
        @Override
        public UsersApply createFromParcel(Parcel in) {
            return new UsersApply(in);
        }

        @Override
        public UsersApply[] newArray(int size) {
            return new UsersApply[size];
        }
    };

    public String getMagangPostId() {
        return magangPostId;
    }

    public void setMagangPostId(String magangPostId) {
        this.magangPostId = magangPostId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(magangPostId);
        parcel.writeString(userId);
    }


}
