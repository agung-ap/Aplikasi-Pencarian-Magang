package id.developer.mahendra.pencarianmagangumb.fragment.admin;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.developer.mahendra.pencarianmagangumb.AdminActivity;
import id.developer.mahendra.pencarianmagangumb.EditProfilAdmin;
import id.developer.mahendra.pencarianmagangumb.EditProfilUser;
import id.developer.mahendra.pencarianmagangumb.R;
import id.developer.mahendra.pencarianmagangumb.data.model.PhotoUsers;
import id.developer.mahendra.pencarianmagangumb.data.model.Users;
import id.developer.mahendra.pencarianmagangumb.util.Constant;

public class ProfilAdmin extends Fragment {
    @BindView(R.id.admin_profil_image)
    ImageView adminImage;
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
        getAdminProfil(auth);

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profil, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.edit_profil) {
            Intent intent = new Intent(getActivity(), EditProfilAdmin.class);
            startActivityForResult(intent, EditProfilAdmin.REQUEST_ADD);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getAdminProfil(FirebaseAuth auth){
        final FirebaseUser currentUser = auth.getCurrentUser();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constant.USERS_TABLE);

        databaseReference.child(currentUser.getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);

                adminName.setText(user.getNama());
                adminStatus.setText(user.getStatus());
                adminTelp.setText(user.getTelp());
                adminAddress.setText(user.getAlamat());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference databaseImageReference = FirebaseDatabase.getInstance()
                .getReference(Constant.USERS_PHOTO_TABLE);

        databaseImageReference.child(currentUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        PhotoUsers user = dataSnapshot.getValue(PhotoUsers.class);
                        Picasso.get()
                                .load(user.getImageUrl())
                                .placeholder(R.drawable.background_image)
                                .into(adminImage);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EditProfilAdmin.REQUEST_ADD)
        {
            Toast.makeText(getActivity(), "berhasil di simpan",
                    Toast.LENGTH_SHORT).show();

        } else if (requestCode == EditProfilUser.REQUEST_BACK) {

        }
        /*
        if (requestCode == PICK_IMAGE_REQUEST && data.getData() == null){
            if (data.getData() != null) {
                Intent intent = new Intent(getActivity(), ImageProfilPreview.class);
                intent.putExtra("URI", data.getData().toString());
                startActivityForResult(intent, ImageProfilPreview.UPLOADED);
            }else {
                Toast.makeText(getActivity(), "URI is null", Toast.LENGTH_SHORT).show();
            }
        }*/
    }
}
