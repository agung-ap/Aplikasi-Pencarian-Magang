package id.developer.mahendra.pencarianmagangumb.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Magang implements Parcelable {
    private String key;
    private String title;
    private String companyName;
    private String city;
    private String salary;
    private String requirement;
    private String postedName;
    private String companyEmail;
    private String date;
    private long applyQuota;

    public Magang() {
    }

    protected Magang(Parcel in) {
        key = in.readString();
        title = in.readString();
        companyName = in.readString();
        city = in.readString();
        salary = in.readString();
        requirement = in.readString();
        postedName = in.readString();
        companyEmail = in.readString();
        date = in.readString();
        applyQuota = in.readLong();
    }

    public static final Creator<Magang> CREATOR = new Creator<Magang>() {
        @Override
        public Magang createFromParcel(Parcel in) {
            return new Magang(in);
        }

        @Override
        public Magang[] newArray(int size) {
            return new Magang[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getPostedName() {
        return postedName;
    }

    public void setPostedName(String postedName) {
        this.postedName = postedName;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getApplyQuota() {
        return applyQuota;
    }

    public void setApplyQuota(long applyQuota) {
        this.applyQuota = applyQuota;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeString(title);
        parcel.writeString(companyName);
        parcel.writeString(city);
        parcel.writeString(salary);
        parcel.writeString(requirement);
        parcel.writeString(postedName);
        parcel.writeString(companyEmail);
        parcel.writeString(date);
        parcel.writeLong(applyQuota);
    }
}
