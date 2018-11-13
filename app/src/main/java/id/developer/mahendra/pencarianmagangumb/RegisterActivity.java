package id.developer.mahendra.pencarianmagangumb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.developer.mahendra.pencarianmagangumb.data.model.CvUsers;
import id.developer.mahendra.pencarianmagangumb.data.model.Magang;
import id.developer.mahendra.pencarianmagangumb.data.model.Users;
import id.developer.mahendra.pencarianmagangumb.data.model.PhotoUsers;
import id.developer.mahendra.pencarianmagangumb.data.model.UsersApply;
import id.developer.mahendra.pencarianmagangumb.data.model.UsersApplyValidation;
import id.developer.mahendra.pencarianmagangumb.util.Constant;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.nama)
    EditText nama;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.registrasi)
    Button registrasi;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;
    private ArrayList<Magang> dataMagang;

    private static final String TAG = RegisterActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        //init firebase auth
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        getMagangId();
        // get reference to 'users' node
        databaseReference = database.getReference("users");
        //register button
        registrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //handle register process
            handleRegister();
            }
        });
    }

    private void handleRegister() {
        final String inputNama = nama.getText().toString().trim();
        final String inputEmail = email.getText().toString().trim();
        final String inputPassword = password.getText().toString().trim();

        if (TextUtils.isEmpty(inputEmail)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(inputPassword)) {
            password.setError("Password Tidak boleh kosong");
            return;
        }
        if (inputPassword.length() < 6) {
            password.setError("password kurang dari 6 karakter");
            return;
        }
        //show progress bar
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        //start register on firebase
        auth.createUserWithEmailAndPassword(inputEmail, inputPassword)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            //when failed on register
                            //hide progressbar
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Register Failed : " + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            //when success on register
                            String inputUid = task.getResult().getUser().getUid();
                            //set database field
                            Users user = new Users();
                            user.setNim(Constant.NONE);
                            user.setNama(inputNama);
                            user.setEmail(inputEmail);
                            user.setPassword(inputPassword);
                            user.setTelp(Constant.NONE);
                            user.setAlamat(Constant.NONE);
                            user.setJurusan(Constant.NONE);
                            user.setDeskripsi(Constant.NONE);
                            user.setStatus(Constant.ROLE_USER);
                            //mapping users image database
                            PhotoUsers userImage = new PhotoUsers();
                            userImage.setImageUrl(Constant.NONE);
                            //mapping users cv database
                            CvUsers userCv = new CvUsers();
                            userCv.setCvUrl(Constant.NONE);
                            //mapping users apply database
                            UsersApplyValidation usersApplyValidation = new UsersApplyValidation();
                            usersApplyValidation.setApply(false);
                            //input user data to database users
                            createUser(inputUid, user, userImage,
                                    userCv, usersApplyValidation);
                        }
                    }
                });
    }

    private void createUser(final String inputUid, final Users user,
                            final PhotoUsers userImage,
                            final CvUsers userCv, final UsersApplyValidation usersApplyValidation) {
        //start saving data on firebase realtime database
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constant.USERS_TABLE);
        databaseReference.child(inputUid).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            //create image table
                            createImageTable(inputUid, userImage);
                            createCvTable(inputUid,userCv);
                            createApplyValidationTable(inputUid, usersApplyValidation);
                            //hide progress bar
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Register Berhasil",
                                    Toast.LENGTH_SHORT).show();

                            Log.i(TAG, "Register is Success");
                            startActivity(new Intent(RegisterActivity.this, UserActivity.class));
                            finish();
                        }else {
                            progressDialog.dismiss();
                            Log.e(TAG, "Register gagal : " + task.getException());
                            Toast.makeText(RegisterActivity.this, "Register gagal",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void createCvTable(String inputUid, CvUsers userCv) {
        final DatabaseReference databaseImageReference = FirebaseDatabase.getInstance().getReference(Constant.USERS_CV_TABLE);
        databaseImageReference.child(inputUid).setValue(userCv).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.i(TAG, "create cv table is success");

                }else {
                    Log.e(TAG, "create cv table is failed : " + task.getException());
                }
            }
        });
    }

    private void createImageTable(String inputUid, PhotoUsers userImage){
        final DatabaseReference databaseImageReference = FirebaseDatabase.getInstance()
                .getReference(Constant.USERS_PHOTO_TABLE);
        databaseImageReference.child(inputUid).setValue(userImage).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.i(TAG, "create image table is success");
                }else {
                    Log.e(TAG, "create image table is failed : " + task.getException());
                }
            }
        });
    }

    private void createApplyValidationTable(String inputUid, UsersApplyValidation usersApplyValidation){
        final DatabaseReference databaseImageReference = FirebaseDatabase.getInstance()
                .getReference(Constant.USERS_APPLY_VALIDATION_TABLE);
        
        for (int i = 0; i < dataMagang.size();i++){
            databaseImageReference.child(inputUid)
                    .child(dataMagang.get(i).getKey())
                    .setValue(usersApplyValidation).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Log.i(TAG, "create apply validation table is success");
                    }else {
                        Log.e(TAG, "create apply validation table is failed : " + task.getException());
                    }
                }

            });
        }

    }

    private void getMagangId(){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constant.MAGANG_POSTING);

        databaseReference
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //Inisialisasi ArrayList
                        dataMagang = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            //Mapping data pada DataSnapshot ke dalam objek mahasiswa
                            Magang posting = snapshot.getValue(Magang.class);

                            posting.setKey(snapshot.getKey());
                            dataMagang.add(posting);
                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }
}
