package id.developer.mahendra.pencarianmagangumb;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.developer.mahendra.pencarianmagangumb.model.Users;
import id.developer.mahendra.pencarianmagangumb.util.Constant;

public class EditProfilUser extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private static final String TAG = EditProfilUser.class.getSimpleName();

    @BindView(R.id.image_preview_user)
    ImageView imagePreviewUser;
    @BindView(R.id.nama_user)
    EditText name;
    @BindView(R.id.nim_user)
    EditText nim;
    @BindView(R.id.email_user)
    EditText email;
    @BindView(R.id.password_user)
    EditText password;
    @BindView(R.id.telp_user)
    EditText phone;
    @BindView(R.id.address_user)
    EditText address;
    @BindView(R.id.department_user)
    AppCompatSpinner department;
    @BindView(R.id.expertise_user)
    EditText expertise;
    @BindView(R.id.save_user)
    Button save;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    private String departmentSelected;
    public static final int REQUEST_ADD = 20;
    public static final int REQUEST_BACK = 30;
    private static final int PICK_IMAGE_REQUEST = 71;
    private static final int PICK_PDF_REQUEST = 72;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil_user);
        ButterKnife.bind(this);
        //set title in action bar
        getSupportActionBar().setTitle("Edit Profil");
        //add home button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //init firebase
        auth = FirebaseAuth.getInstance();
        //show user data
        getUserData(auth);

        department.setOnItemSelectedListener(this);
        // Spinner Drop down elements
        ArrayList<String> jurusan = new ArrayList<String>();
        jurusan.add("NONE");
        jurusan.add("Teknik Informatika");
        jurusan.add("Sistem Informasi");
        //set data to spinner
        spinnerSetup(jurusan);
        imagePreviewUser.setClickable(true);
        imagePreviewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUserData(auth, data());
                //uploadImage(imageFilePath);
            }
        });
    }

    private void chooseImage() {
        try{
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        }catch (Exception e){
            Log.e(TAG, "chose image exception = " + e.getLocalizedMessage());
        }
    }

    private void spinnerSetup(ArrayList<String > jurusan){
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, jurusan);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        department.setAdapter(dataAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private Users data(){
        String inputName = name.getText().toString().trim();
        String inputNim = nim.getText().toString().trim();
        String inputEmail = email.getText().toString().trim();
        String inputPassword = password.getText().toString().trim();
        String inputAddress = address.getText().toString().trim();
        String inputExpertise = expertise.getText().toString().trim();
        String inputPhone = phone.getText().toString().trim();

        //mapping data
        Users user = new Users();
        user.setNim(inputNim);
        user.setNama(inputName);
        user.setTelp(inputPhone);
        user.setAlamat(inputAddress);
        user.setJurusan(departmentSelected);
        user.setDeskripsi(inputExpertise);
        user.setStatus(Constant.ROLE_USER);

        return user;
    }

    private void getUserData(FirebaseAuth auth){
        email.setVisibility(View.GONE);
        password.setVisibility(View.GONE);

        databaseReference = FirebaseDatabase.getInstance().getReference(Constant.USERS_TABLE);

        databaseReference.child(auth.getUid()).child("users_data")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Users user = dataSnapshot.getValue(Users.class);
                        //show user data from database
                        name.setText(user.getNama());
                        nim.setText(user.getNim());
                        phone.setText(user.getTelp());
                        address.setText(user.getAlamat());
                        expertise.setText(user.getDeskripsi());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

        databaseReference.child(auth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String imageUrl = dataSnapshot.child("image_url").getValue(String.class);
                        //show image if any from database
                        Picasso.get().load(imageUrl)
                                .placeholder(R.drawable.background_image)
                                .error(R.drawable.background_image)
                                .into(imagePreviewUser);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void setUserData(FirebaseAuth auth, Users user){
        databaseReference = FirebaseDatabase.getInstance().getReference(Constant.USERS_TABLE);
        databaseReference.child(auth.getUid())
                .child("users_data")
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            setResult(REQUEST_ADD);
                            finish();
                        }else {

                        }
                    }
                });
    }

    private void setEmailPassword(FirebaseUser currentUser, EditText email, EditText password) {
        if (currentUser != null && !email.getText().toString().trim().equals("")){
            currentUser.updateEmail(email.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(EditProfilUser.this, "Email address is updated.", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(EditProfilUser.this, "Failed to update email!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }else if (email.getText().toString().trim().equals("")) {
            email.setError("Enter email");
        }

        if (currentUser != null && !password.getText().toString().trim().equals("")) {
            if (password.getText().toString().trim().length() < 6) {
                password.setError("Password too short, enter minimum 6 characters");
            } else {
                currentUser.updatePassword(password.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(EditProfilUser.this, "Password is updated", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.i(TAG, "Pass upadte failed : " + task.getException() );
                                    Toast.makeText(EditProfilUser.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        } else if (password.getText().toString().trim().equals("")) {
            password.setError("Enter password");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        departmentSelected = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null ){

            Log.i(TAG, "uri = " + data.getData().toString());
            Intent intent = new Intent(EditProfilUser.this, ImageProfilPreview.class);
            intent.putExtra("URI", data.getData().toString());
            startActivityForResult(intent, ImageProfilPreview.UPLOADED);
        }

        if (requestCode == 21 && resultCode == RESULT_OK){
            Toast.makeText(EditProfilUser.this, "berhasil diupload",
                    Toast.LENGTH_SHORT).show();
        }

    }

}
