package id.developer.mahendra.pencarianmagangumb;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.developer.mahendra.pencarianmagangumb.data.model.Mahasiswa;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.nama)
    EditText nama;
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.registrasi)
    Button registrasi;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

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
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (inputPassword.length() < 6) {
            Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
            return;
        }
        //show progress bar
        progressBar.setVisibility(View.VISIBLE);
        //start register on firebase
        auth.createUserWithEmailAndPassword(inputEmail, inputPassword)
                .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            //when failed on register
                            //hide progressbar
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(RegisterActivity.this, "Register Failed : " + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            //when success on register
                            //hide progressbar
                            progressBar.setVisibility(View.GONE);
                            String inputUid = task.getResult().getUser().getUid();
                            //set database field
                            Mahasiswa user = new Mahasiswa();
                            user.setNim("");
                            user.setNama(inputNama);
                            user.setEmail(inputEmail);
                            user.setPassword(inputPassword);
                            user.setTelp("");
                            user.setAlamat("");
                            user.setJurusan("");
                            user.setDeskripsi("");
                            user.setLinkCv("");
                            user.setStatus("user");
                            user.setImageURl("");
                            //input user data to database users
                            createUser(inputUid, user);
                        }
                    }
                });
    }

    private void createUser(String inputUid, Mahasiswa user) {
        //start saving data on firebase realtime database
        databaseReference.child(inputUid).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegisterActivity.this, "Register Berhasil",
                                    Toast.LENGTH_SHORT).show();

                            startActivity(new Intent(RegisterActivity.this, UserActivity.class));
                            finish();
                        }else {
                            Toast.makeText(RegisterActivity.this, "Register gagal : " + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
