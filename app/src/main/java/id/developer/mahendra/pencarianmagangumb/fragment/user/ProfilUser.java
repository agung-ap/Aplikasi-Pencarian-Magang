package id.developer.mahendra.pencarianmagangumb.fragment.user;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.developer.mahendra.pencarianmagangumb.EditProfilUser;
import id.developer.mahendra.pencarianmagangumb.ImageProfilPreview;
import id.developer.mahendra.pencarianmagangumb.R;
import id.developer.mahendra.pencarianmagangumb.UserActivity;
import id.developer.mahendra.pencarianmagangumb.data.model.CvUsers;
import id.developer.mahendra.pencarianmagangumb.data.model.Users;
import id.developer.mahendra.pencarianmagangumb.data.model.PhotoUsers;
import id.developer.mahendra.pencarianmagangumb.util.Constant;

import static android.app.Activity.RESULT_OK;

public class ProfilUser extends Fragment {
    private static final int PICK_PDF_REQUEST = 70;
    private static final String TAG = ProfilUser.class.getSimpleName();
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
    @BindView(R.id.user_profil_cv)
    TextView userCv;
    @BindView(R.id.upload_cv_user)
    Button uploadCv;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profil_user, container, false);
        ButterKnife.bind(this, view);

        ((UserActivity)getActivity()).getSupportActionBar().setTitle("Profil");
        setHasOptionsMenu(true);

        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());

        showUserProfil(auth);

        uploadCv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chosePDFFile();
            }
        });

        return view;
    }

    private void chosePDFFile() {
        try{
            Intent intent = new Intent();
            intent.setType("application/pdf");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select File"), PICK_PDF_REQUEST);
        }catch (Exception e){
            Log.e(TAG, "chose image exception = " + e.getLocalizedMessage());
        }
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
            Intent intent = new Intent(getActivity(), EditProfilUser.class);
            startActivityForResult(intent, EditProfilUser.REQUEST_ADD);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void showUserProfil(FirebaseAuth auth){
        databaseReference = FirebaseDatabase.getInstance().getReference(Constant.USERS_TABLE);
        databaseReference.child(auth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String imageUrl = dataSnapshot.child("image_url").getValue(String.class);
                        String cvUrl = dataSnapshot.child("cv_url").getValue(String.class);
                        //show email
                        userEmail.setText(email);
                        //load image
                        Picasso.get()
                                .load(imageUrl)
                                .placeholder(R.drawable.background_image)
                                .error(R.drawable.background_image)
                                .into(userImage);
                        //load cv notification
                        if (cvUrl.equals(Constant.NONE)){
                            userCv.setText(R.string.cv_is_empty);
                        }else {
                            userCv.setText(R.string.cv_is_not_empty);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        databaseReference.child(auth.getUid()).child("users_data")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Users user = dataSnapshot.getValue(Users.class);

                        userName.setText(user.getNama());
                        userNim.setText(user.getNim());
                        userPhone.setText(user.getTelp());
                        userAddress.setText(user.getAlamat());
                        userDepartment.setText(user.getJurusan());
                        userExpertise.setText(user.getDeskripsi());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == EditProfilUser.REQUEST_ADD && resultCode == RESULT_OK)
        {
            Toast.makeText(getActivity(), "berhasil di simpan",
                    Toast.LENGTH_SHORT).show();

        } else if (requestCode == EditProfilUser.REQUEST_BACK ) {

        }
        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null){

                progressDialog.setMessage("Uploading...");
                progressDialog.show();
                setUploadCv(data.getData());
        }
    }

    private void setUploadCv(final Uri filePath){
        if(filePath != null)
        {
            storageReference = FirebaseStorage.getInstance()
                    .getReference()
                    .child("cv/"+ UUID.randomUUID().toString());
            storageReference.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //Toast.makeText(ImageProfilPreview.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //hide progress bar
                                    progressDialog.dismiss();
                                    //uploading to database
                                    createUserCv(auth.getUid(), uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            //Toast.makeText(.t, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }

    public void createUserCv(String inputUid, String cvUrl) {
        //start saving data on firebase realtime database
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constant.USERS_TABLE);
        databaseReference.child(inputUid).child("cv_url").setValue(cvUrl)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.i(TAG, "Task is Success = " + task.getResult());
                            Toast.makeText(getActivity(), "Upload cv berhasil", Toast.LENGTH_SHORT).show();
                        }else {
                            Log.e(TAG, "error when uploading cv url = " + task.getException());
                        }
                    }
                });
    }
}
