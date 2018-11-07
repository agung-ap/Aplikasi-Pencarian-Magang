package id.developer.mahendra.pencarianmagangumb.fragment.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.developer.mahendra.pencarianmagangumb.UserActivity;
import id.developer.mahendra.pencarianmagangumb.R;

public class DaftarLowonganPekerjaan extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_daftar_lowongan_kerja, container, false);

        ((UserActivity)getActivity()).getSupportActionBar().setTitle("Home");
        setHasOptionsMenu(true);

        return view;
    }
}
