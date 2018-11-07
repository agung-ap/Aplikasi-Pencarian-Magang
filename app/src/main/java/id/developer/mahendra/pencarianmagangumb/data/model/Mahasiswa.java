package id.developer.mahendra.pencarianmagangumb.data.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Mahasiswa implements Parcelable {
    private String nim;
    private String nama;
    private String email;
    private String password;
    private String telp;
    private String alamat;
    private String jurusan;
    private String deskripsi;
    private String linkCv;
    private String Status;
    private String imageURl;

    public Mahasiswa() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getLinkCv() {
        return linkCv;
    }

    public void setLinkCv(String linkCv) {
        this.linkCv = linkCv;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getImageURl() {
        return imageURl;
    }

    public void setImageURl(String imageURl) {
        this.imageURl = imageURl;
    }

    public static Creator<Mahasiswa> getCREATOR() {
        return CREATOR;
    }

    protected Mahasiswa(Parcel in) {
        nim = in.readString();
        nama = in.readString();
        email = in.readString();
        password = in.readString();
        telp = in.readString();
        alamat = in.readString();
        jurusan = in.readString();
        deskripsi = in.readString();
        linkCv = in.readString();
        Status = in.readString();
        imageURl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nim);
        dest.writeString(nama);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(telp);
        dest.writeString(alamat);
        dest.writeString(jurusan);
        dest.writeString(deskripsi);
        dest.writeString(linkCv);
        dest.writeString(Status);
        dest.writeString(imageURl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Mahasiswa> CREATOR = new Creator<Mahasiswa>() {
        @Override
        public Mahasiswa createFromParcel(Parcel in) {
            return new Mahasiswa(in);
        }

        @Override
        public Mahasiswa[] newArray(int size) {
            return new Mahasiswa[size];
        }
    };
}
