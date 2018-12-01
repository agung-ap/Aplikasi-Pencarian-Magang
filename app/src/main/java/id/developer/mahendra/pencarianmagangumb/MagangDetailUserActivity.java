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

import java.util.ArrayList;
import java.util.EventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.developer.mahendra.pencarianmagangumb.model.Magang;
import id.developer.mahendra.pencarianmagangumb.model.Users;
import id.developer.mahendra.pencarianmagangumb.model.UsersApply;
import id.developer.mahendra.pencarianmagangumb.model.UsersApplyValidation;
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
    @BindView(R.id.apply_quota_detail)
    TextView applyQuotaDetail;
    @BindView(R.id.requirement_detail)
    TextView requirementDetail;
    @BindView(R.id.isApply_validation)
    ProgressBar isApplyValidation;

    private ArrayList<Magang> magangData;
    private String username, usernim;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    private final static String TAG = MagangDetailUserActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magang_detail_user);
        ButterKnife.bind(this);
        //display homebutton
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
        //get user validation
        getUserValidationData(auth);
        //check if applyCount has 30
        getApplyCount();
        //set action bar title
        getSupportActionBar().setTitle(magangData.get(0).getTitle());
        //show amount of users apply for this posting
        showUsersAmount();
        //show magang detail
        showMagangDetail();
        //apply this posting
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogApply(view);

            }
        });
    }
    //get users amount for this posting
    private void showUsersAmount() {
        databaseReference = FirebaseDatabase.getInstance().getReference(Constant.MAGANG_POSTING);
        databaseReference.child(magangData.get(0).getKey())
                .child("users_apply").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = (long) dataSnapshot.child("applyCount").getValue();
                applyQuotaDetail.setText("jumlah pendaftar saat ini : " + count);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //method for increment applycount every users click apply button
    private void setApplyCount() {
        databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constant.MAGANG_POSTING);
        //save database reference for increment applyCount
        final DatabaseReference counterRef = databaseReference
                .child(magangData.get(0).getKey()).child("users_apply");
        //get applyCount value
        databaseReference.child(magangData.get(0).getKey())
                .child("users_apply").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = (long) dataSnapshot.child("applyCount").getValue();
                //increment apply count in post magang database
                counterRef.child("applyCount").setValue(++count);
                finish();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void getApplyCount(){
        databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constant.MAGANG_POSTING);
        //get applyCount value
        databaseReference.child(magangData.get(0).getKey())
                .child("users_apply").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = (long) dataSnapshot.child("applyCount").getValue();
                if (count >= magangData.get(0).getApplyQuota()){
                    isApplyValidation.setVisibility(View.GONE);
                    apply.setVisibility(View.VISIBLE);
                    apply.setClickable(false);
                    apply.setBackground(null);
                    apply.setTextColor(getResources().getColor(R.color.button_color));
                    apply.setText(R.string.applyCount);
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
        applyQuotaDetail.setText("jumlah pendaftar saat ini : " + magangData.get(0).getApplyQuota());
        requirementDetail.setText(magangData.get(0).getRequirement());
    }

    private UsersApply data(){
        UsersApply apply = new UsersApply();
        apply.setMagangPostId(magangData.get(0).getKey());
        apply.setUserId(auth.getUid());
        apply.setUserName(username);
        apply.setUserNim(usernim);
        apply.setTitle(magangData.get(0).getTitle());
        apply.setCompany(magangData.get(0).getCompanyName());
        return apply;
    }
    //set apply to database
    private void setApply(final FirebaseAuth auth, UsersApply usersApply){
        databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constant.USERS_APPLY_TABLE);

        databaseReference.child(databaseReference.push().getKey()).setValue(usersApply)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            //set users apply validation
                            UsersApplyValidation validation = new UsersApplyValidation();
                            validation.setApply(magangData.get(0).getKey());

                            databaseReference = FirebaseDatabase.getInstance()
                                    .getReference(Constant.USERS_TABLE);
                            databaseReference.child(auth.getUid())
                                    .child("magang_apply")
                                    .child(databaseReference.push().getKey())
                                    .setValue(validation);
                            //increment apply count every users apply
                            setApplyCount();
                        }else {
                            Toast.makeText(MagangDetailUserActivity.this, "apply gagal", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    //show apply dialog
    private void showDialogApply(final View view){
        //Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Apakah anda ingin Apply posting ini ?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                setApply(auth, data());
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
    //get user validation from database users
    private void getUserValidationData(final FirebaseAuth auth){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constant.USERS_TABLE);
        //matching post id and isApply
        databaseReference.child(auth.getUid())
                .child("magang_apply")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //initialisation array list for save list of users apply
                        ArrayList<UsersApplyValidation> getValidation = new ArrayList<>();
                        //looping us
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            UsersApplyValidation data = snapshot.getValue(UsersApplyValidation.class);
                            //input value to array list
                            getValidation.add(data);
                        }
                        //check is arraylist getvalidation is not 0 and not more than 3
                        if (getValidation.size() != 0 && getValidation.size() < 3){
                            userValidation(getValidation);

                        }else if (getValidation.size() >= 2){
                            isApplyValidation.setVisibility(View.GONE);
                            apply.setVisibility(View.VISIBLE);
                            apply.setClickable(false);
                            apply.setBackground(null);
                            apply.setTextColor(getResources().getColor(R.color.md_red_500));
                            apply.setText("melampau batas maksimal apply");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }
    //set user validation
    private void userValidation(ArrayList<UsersApplyValidation> getValidation){
        for (int i = 0; i < getValidation.size(); i++){
            if (getValidation.get(i).isApply()
                    .equals(magangData.get(0).getKey())){
                isApplyValidation.setVisibility(View.GONE);
                apply.setVisibility(View.VISIBLE);
                apply.setClickable(false);
                apply.setBackground(null);
                apply.setTextColor(getResources().getColor(R.color.button_color));
                apply.setText(R.string.isApply);
            }else if (getValidation.get(i).isApply()
                    .equals("false")){
                isApplyValidation.setVisibility(View.GONE);
                apply.setVisibility(View.VISIBLE);
                apply.setClickable(true);
            }
        }
    }

    private void getUserName(FirebaseAuth auth){
        databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constant.USERS_TABLE);

        databaseReference.child(auth.getUid()).child("users_data")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Users users = dataSnapshot.getValue(Users.class);
                        username = users.getNama();
                        usernim = users.getNim();
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
    protected void onResume() {
        super.onResume();
        getUserValidationData(auth);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
