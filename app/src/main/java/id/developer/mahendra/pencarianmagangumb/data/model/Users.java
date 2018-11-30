package id.developer.mahendra.pencarianmagangumb.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Users implements Parcelable {
    private String key;
    private String nim;
    private String nama;
    private String telp;
    private String alamat;
    private String jurusan;
    private String deskripsi;
    //private String linkCv;
    private String Status;
    //private String imageUrl;

    public Users() {
    }

    protected Users(Parcel in) {
        key = in.readString();
        nim = in.readString();
        nama = in.readString();
        telp = in.readString();
        alamat = in.readString();
        jurusan = in.readString();
        deskripsi = in.readString();
        Status = in.readString();
    }

    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNim() {
        return nim;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getTelp() {
        return telp;
    }

    public void setTelp(String telp) {
        this.telp = telp;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getJurusan() {
        return jurusan;
    }

    public void setJurusan(String jurusan) {
        this.jurusan = jurusan;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(key);
        parcel.writeString(nim);
        parcel.writeString(nama);
        parcel.writeString(telp);
        parcel.writeString(alamat);
        parcel.writeString(jurusan);
        parcel.writeString(deskripsi);
        parcel.writeString(Status);
    }
}
