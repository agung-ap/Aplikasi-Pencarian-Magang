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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.developer.mahendra.pencarianmagangumb.data.model.Magang;
import id.developer.mahendra.pencarianmagangumb.data.model.Users;
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
    private DatabaseReference databaseReference;
    private ProgressDialog progressDialog;

    private static final String TAG = RegisterActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        //init firebase auth
        auth = FirebaseAuth.getInstance();
        //register button
        registrasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //handle register process
            handleRegister();
            }
        });
    }
    //set users_data field
    private Users data(String inputNama){

        Users user = new Users();
        user.setNim(Constant.NONE);
        user.setNama(inputNama);
        user.setTelp(Constant.NONE);
        user.setAlamat(Constant.NONE);
        user.setJurusan(Constant.NONE);
        user.setDeskripsi(Constant.NONE);
        user.setStatus(Constant.ROLE_USER);

        return user;
    }

    private void handleRegister() {
        final String inputNama = nama.getText().toString().trim();
        final String inputEmail = email.getText().toString().trim();
        final String inputPassword = password.getText().toString().trim();

        if (TextUtils.isEmpty(inputEmail)) {
            email.setError("Enter email address!");
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
        progressDialog.setMessage("Please wait...");
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
                            databaseReference = FirebaseDatabase.getInstance().getReference(Constant.USERS_TABLE);
                            //input email user to database
                            databaseReference.child(inputUid)
                                    .child("email").setValue(inputEmail);
                            //input password user to database
                            databaseReference.child(inputUid)
                                    .child("password").setValue(inputPassword);
                            //input user data to database users
                            createUser(inputUid, data(inputNama));
                        }
                    }
                });
    }
    //create users_data field in database
    private void createUser(final String inputUid, final Users user) {
        //start saving data on firebase realtime database
        databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constant.USERS_TABLE);
        databaseReference.child(inputUid)
                .child("users_data")
                .setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            //create image field
                            createImage(inputUid);
                            //create cv field
                            createCv(inputUid);
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
    //create cv field in firebase
    private void createCv(String inputUid) {
        databaseReference = FirebaseDatabase.getInstance().getReference(Constant.USERS_TABLE);
        databaseReference.child(inputUid)
                .child("cv_url").setValue(Constant.NONE).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.i(TAG, "save cv field is success");

                }else {
                    Log.e(TAG, "save cv field is failed : " + task.getException());
                }
            }
        });
    }
    //create image field in firebase
    private void createImage(String inputUid){
        databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constant.USERS_TABLE);
        databaseReference.child(inputUid)
                .child("image_url").setValue(Constant.NONE)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Log.i(TAG, "save image field is success");
                }else {
                    Log.e(TAG, "save image field is failed : " + task.getException());
                }
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
