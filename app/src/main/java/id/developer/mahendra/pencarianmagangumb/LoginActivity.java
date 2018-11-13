package id.developer.mahendra.pencarianmagangumb;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import id.developer.mahendra.pencarianmagangumb.util.Constant;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.email)
    EditText email;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.login)
    Button login;
    @BindView(R.id.register_button)
    Button registerButton;

    private ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //init firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        // get reference to 'users' node
        databaseReference = database.getReference(Constant.USERS_TABLE);
        //login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLogin();
            }
        });
        //goto register activity
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void handleLogin() {
        final String inputEmail = email.getText().toString().trim();
        final String inputPassword = password.getText().toString().trim();
        //check if email or password is null or lack
        if (TextUtils.isEmpty(inputEmail)) {
            email.setError("Email tidak boleh kosong");
            return;
        }

        if (TextUtils.isEmpty(inputPassword)) {
            password.setError("Password tidak boleh kosong");
            return;
        }
        //show progress bar
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        //sign in process
        auth.signInWithEmailAndPassword(inputEmail, inputPassword)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            // there was an error
                            progressDialog.dismiss();
                            if (inputPassword.length() < 6) {
                                password.setError("password kurang dari 6 karakter");
                            } else {
                                Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            final String uid = task.getResult().getUser().getUid();
                            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    String status = dataSnapshot.child(Constant.USERS_TABLE).child(uid).child("status").getValue(String.class);
                                    if (status.equals("admin")){
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();

                                        startActivity(new Intent(LoginActivity.this, AdminActivity.class));
                                        finish();
                                    }else {
                                        progressDialog.dismiss();
                                        Toast.makeText(LoginActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();

                                        startActivity(new Intent(LoginActivity.this, UserActivity.class));
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
    }
}
