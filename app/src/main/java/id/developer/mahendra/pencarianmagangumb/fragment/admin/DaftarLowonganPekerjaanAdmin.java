package id.developer.mahendra.pencarianmagangumb.fragment.admin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.developer.mahendra.pencarianmagangumb.AdminActivity;
import id.developer.mahendra.pencarianmagangumb.R;
import id.developer.mahendra.pencarianmagangumb.UserActivity;

public class DaftarLowonganPekerjaanAdmin extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daftar_lowongan_kerja_admin, container, false);

        ((AdminActivity)getActivity()).getSupportActionBar().setTitle("Home");
        setHasOptionsMenu(true);

        return view;
    }
}
