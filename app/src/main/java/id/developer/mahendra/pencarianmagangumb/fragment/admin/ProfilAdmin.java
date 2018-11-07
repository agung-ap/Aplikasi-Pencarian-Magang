package id.developer.mahendra.pencarianmagangumb.fragment.admin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.developer.mahendra.pencarianmagangumb.AdminActivity;
import id.developer.mahendra.pencarianmagangumb.R;
import id.developer.mahendra.pencarianmagangumb.data.model.Mahasiswa;

public class ProfilAdmin extends Fragment {
    @BindView(R.id.admin_profil_name)
    TextView adminName;
    @BindView(R.id.admin_profil_status)
    TextView adminStatus;
    @BindView(R.id.admin_profil_email)
    TextView adminEmail;
    @BindView(R.id.admin_profil_phone)
    TextView adminTelp;
    @BindView(R.id.admin_profil_address)
    TextView adminAddress;

    private FirebaseAuth auth;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil_admin, container, false);
        ButterKnife.bind(this, view);

        ((AdminActivity)getActivity()).getSupportActionBar().setTitle("Profil");
        setHasOptionsMenu(true);

        auth = FirebaseAuth.getInstance();
        setAdminProfil(auth);

        return view;
    }

    private void setAdminProfil(FirebaseAuth auth){
        final FirebaseUser currentUser = auth.getCurrentUser();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("users");

        databaseReference.child(currentUser.getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Mahasiswa user = dataSnapshot.getValue(Mahasiswa.class);

                adminName.setText(user.getNama());
                adminStatus.setText(user.getStatus());
                adminEmail.setText(user.getEmail());
                adminTelp.setText(user.getTelp());
                adminAddress.setText(user.getAlamat());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
