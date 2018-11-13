package id.developer.mahendra.pencarianmagangumb.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class UsersApply implements Parcelable {
    private String magangPostId;
    private String userId;
    private String userName;
    private String title;
    private String company;

    public UsersApply() {
    }


    protected UsersApply(Parcel in) {
        magangPostId = in.readString();
        userId = in.readString();
        userName = in.readString();
        title = in.readString();
        company = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(magangPostId);
        parcel.writeString(userId);
        parcel.writeString(userName);
        parcel.writeString(title);
        parcel.writeString(company);
    }

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public static Creator<UsersApply> getCREATOR() {
        return CREATOR;
    }
}
