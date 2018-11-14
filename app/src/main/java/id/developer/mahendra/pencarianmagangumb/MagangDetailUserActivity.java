package id.developer.mahendra.pencarianmagangumb;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import java.util.AbstractQueue;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.developer.mahendra.pencarianmagangumb.data.model.Magang;
import id.developer.mahendra.pencarianmagangumb.data.model.Users;
import id.developer.mahendra.pencarianmagangumb.data.model.UsersApply;
import id.developer.mahendra.pencarianmagangumb.data.model.UsersApplyValidation;
import id.developer.mahendra.pencarianmagangumb.util.Constant;

public class MagangDetailUserActivity extends AppCompatActivity {
    @BindView(R.id.apply)
    Button apply;
    @BindView(R.id.title_detail)
    TextView titleDetail;
    @BindView(R.id.company_name_detail)
    TextView companyNameDetail;
    @BindView(R.id.city_detail)
    TextView cityDetail;
    @BindView(R.id.salary_detail)
    TextView salaryDetail;
    @BindView(R.id.requirement_detail)
    TextView requirementDetail;
    @BindView(R.id.isApply_validation)
    ProgressBar isApplyValidation;

    private ArrayList<Magang> magangData;
    private String username;
    private FirebaseAuth auth;

    private final static String TAG = MagangDetailUserActivity.class.getSimpleName();
    private ArrayList<Magang> magangArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magang_detail_user);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //init firebase auth
        auth = FirebaseAuth.getInstance();
        getUserName(auth);
        //preference = new StorePreference(this);
        if (savedInstanceState == null){
            Bundle getBundle = getIntent().getExtras();

            //get data from intent
            magangData = new ArrayList<>();
            magangData = getBundle
                    .getParcelableArrayList(getString(R.string.GET_SELECTED_ITEM));
        }

        //show progress bar
        isApplyValidation.setVisibility(View.VISIBLE);
        apply.setVisibility(View.GONE);
        getUserValidationData(auth);
        //set action bar title
        getSupportActionBar().setTitle(magangData.get(0).getTitle());
        //show magang detail
        showMagangDetail();

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogApply(view);

            }
        });
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

    private void showMagangDetail() {
        titleDetail.setText(magangData.get(0).getTitle());
        companyNameDetail.setText(magangData.get(0).getCompanyName());
        cityDetail.setText(magangData.get(0).getCity());
        salaryDetail.setText("Salary : " + magangData.get(0).getSalary());
        requirementDetail.setText(magangData.get(0).getRequirement());
    }

    private UsersApply data(){
        UsersApply apply = new UsersApply();
        apply.setMagangPostId(magangData.get(0).getKey());
        apply.setUserId(auth.getUid());
        apply.setUserName(username);
        apply.setTitle(magangData.get(0).getTitle());
        apply.setCompany(magangData.get(0).getCompanyName());
        return apply;
    }

    private UsersApplyValidation applyValidationData(){
        UsersApplyValidation validation = new UsersApplyValidation();
        validation.setApply(true);
        return validation;
    }

    private void setApply(FirebaseAuth auth, UsersApply usersApply,
                          UsersApplyValidation usersApplyValidation){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constant.USERS_APPLY_TABLE);
        databaseReference.child(databaseReference.push().getKey()).setValue(usersApply)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                        }else {

                        }
                    }
                });

        final DatabaseReference userApplyValidationReference = FirebaseDatabase.getInstance()
                .getReference(Constant.USERS_APPLY_VALIDATION_TABLE);
        userApplyValidationReference.child(auth.getUid())
                .child(magangData.get(0).getKey())
                .setValue(usersApplyValidation)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){

                        }else {

                        }
                    }
                });
    }

    private void showDialogApply(final View view){
        //Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Apakah anda ingin Apply posting ini ?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setApply(auth, data(), applyValidationData());
                Toast.makeText(MagangDetailUserActivity.this, "Apply Berhasil", Toast.LENGTH_SHORT).show();
                //preference.setFirstRun(false);
            }

        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        //show alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void getUserValidationData(final FirebaseAuth auth){
        final FirebaseUser currentUser = auth.getCurrentUser();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constant.USERS_APPLY_VALIDATION_TABLE);

        databaseReference.child(currentUser.getUid())
                //.child(magangData.get(0).getKey())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (magangData.get(0).getKey() != null){
                                userValidation(auth, magangData.get(0).getKey());
                            }else {
                                apply.setVisibility(View.VISIBLE);
                            }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void userValidation(FirebaseAuth auth, String postingId){
        final FirebaseUser currentUser = auth.getCurrentUser();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constant.USERS_APPLY_VALIDATION_TABLE);

        databaseReference.child(currentUser.getUid())
                .child(postingId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        UsersApplyValidation validation = dataSnapshot.getValue(UsersApplyValidation.class);

                        if (validation.isApply()){
                            isApplyValidation.setVisibility(View.GONE);
                            apply.setVisibility(View.VISIBLE);
                            apply.setClickable(false);
                            apply.setBackground(null);
                            apply.setTextColor(Color.parseColor("#FF4C62C6"));
                            apply.setText(R.string.isApply);
                        }else {
                            isApplyValidation.setVisibility(View.GONE);
                            apply.setVisibility(View.VISIBLE);
                            apply.setClickable(true);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void getUserName(FirebaseAuth auth){
        final FirebaseUser currentUser = auth.getCurrentUser();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constant.USERS_TABLE);

        databaseReference.child(currentUser.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Users users = dataSnapshot.getValue(Users.class);
                        username = users.getNama();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getUserValidationData(auth);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
