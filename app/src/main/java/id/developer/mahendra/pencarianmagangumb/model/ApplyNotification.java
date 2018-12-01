package id.developer.mahendra.pencarianmagangumb.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ApplyNotification implements Parcelable {
    private String magangId;
    private String userId;
    private String userName;
    private String title;
    private String company;

    public ApplyNotification() {
    }

    protected ApplyNotification(Parcel in) {
        magangId = in.readString();
        userId = in.readString();
        userName = in.readString();
        title = in.readString();
        company = in.readString();
    }

    public static final Creator<ApplyNotification> CREATOR = new Creator<ApplyNotification>() {
        @Override
        public ApplyNotification createFromParcel(Parcel in) {
            return new ApplyNotification(in);
        }

        @Override
        public ApplyNotification[] newArray(int size) {
            return new ApplyNotification[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(magangId);
        parcel.writeString(userId);
        parcel.writeString(userName);
        parcel.writeString(title);
        parcel.writeString(company);
    }

    public String getMagangId() {
        return magangId;
    }

    public void setMagangId(String magangId) {
        this.magangId = magangId;
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

    public static Creator<ApplyNotification> getCREATOR() {
        return CREATOR;
    }
}
