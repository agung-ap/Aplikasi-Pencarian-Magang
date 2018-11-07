package id.developer.mahendra.pencarianmagangumb.fragment.user;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
import id.developer.mahendra.pencarianmagangumb.ImageProfilPreview;
import id.developer.mahendra.pencarianmagangumb.R;
import id.developer.mahendra.pencarianmagangumb.UserActivity;
import id.developer.mahendra.pencarianmagangumb.data.model.Mahasiswa;

import static android.app.Activity.RESULT_OK;

public class ProfilUser extends Fragment {
    @BindView(R.id.user_profil_image)
    ImageView userImage;
    @BindView(R.id.user_profil_name)
    TextView userName;
    @BindView(R.id.user_profil_nim)
    TextView userNim;
    @BindView(R.id.user_profil_email)
    TextView userEmail;
    @BindView(R.id.user_profil_phone)
    TextView userPhone;
    @BindView(R.id.user_profil_address)
    TextView userAddress;
    @BindView(R.id.user_profil_department)
    TextView userDepartment;
    @BindView(R.id.user_profil_expertise)
    TextView userExpertise;

    private FirebaseAuth auth;

    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil_user, container, false);
        ButterKnife.bind(this, view);

        ((UserActivity)getActivity()).getSupportActionBar().setTitle("Profil");
        setHasOptionsMenu(true);

        auth = FirebaseAuth.getInstance();
        setUserProfil(auth);

        userImage.setClickable(true);
        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
                //Toast.makeText(getActivity(), "Profile Image Clicked", Toast.LENGTH_SHORT).show();
            }
        });



        return view;
    }

    private void setUserProfil(FirebaseAuth auth){
        final FirebaseUser currentUser = auth.getCurrentUser();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("users");

        databaseReference.child(currentUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Mahasiswa user = dataSnapshot.getValue(Mahasiswa.class);

                        userName.setText(user.getNama());
                        userNim.setText(user.getNim());
                        userEmail.setText(user.getEmail());
                        userPhone.setText(user.getTelp());
                        userAddress.setText(user.getAlamat());
                        userDepartment.setText(user.getJurusan());
                        userExpertise.setText(user.getDeskripsi());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        //get image file
        databaseReference.child(currentUser.getUid()).child("imageURI")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String imagePath = dataSnapshot.getValue(String.class);

                        //Log.i("IMAGE PATH", "path " + imagePath);
                        Picasso.get()
                                .load(imagePath)
                                .placeholder(R.drawable.background_image)
                                .into(userImage);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {

            //Log.i("IMAGE PATH ", "path : " + filePath.toString());
            Intent intent = new Intent(getActivity(), ImageProfilPreview.class);
            intent.putExtra("URI", data.getData().toString());
            startActivity(intent);
            /*
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                userImage.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }*/
        }
    }
}
