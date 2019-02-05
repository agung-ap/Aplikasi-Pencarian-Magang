package id.developer.mahendra.pencarianmagangumb;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


import butterknife.BindView;
import butterknife.ButterKnife;
import de.cketti.mailto.EmailIntentBuilder;
import id.developer.mahendra.pencarianmagangumb.model.Magang;
import id.developer.mahendra.pencarianmagangumb.model.Users;
import id.developer.mahendra.pencarianmagangumb.model.UsersApply;
import id.developer.mahendra.pencarianmagangumb.model.UsersApplyValidation;
import id.developer.mahendra.pencarianmagangumb.util.Constant;

public class NotificationDetailActivity extends AppCompatActivity {
    private static final String TAG = NotificationDetailActivity.class.getSimpleName();
    private ArrayList<UsersApply> usersApplyList;
    private String magangPostId, userId , emailUser;

    private DatabaseReference databaseReference;

    @BindView(R.id.toolbarImage)
    ImageView toolImage;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    //magang post info
    @BindView(R.id.title_notification)
    TextView title;
    @BindView(R.id.company_notification)
    TextView company;
    @BindView(R.id.city_notification)
    TextView city;
    @BindView(R.id.salary_notification)
    TextView salary;
    @BindView(R.id.requirement_notification)
    TextView requirement;

    //user info
    @BindView(R.id.user_nim_notification)
    TextView userNim;
    @BindView(R.id.user_email_notification)
    TextView userEmail;
    @BindView(R.id.user_phone_notification)
    TextView userPhone;
    @BindView(R.id.user_department_notification)
    TextView userDepartment;
    @BindView(R.id.user_expertise_notification)
    TextView userExpertise;
    @BindView(R.id.user_cv_notification)
    Button userCv;

    @BindView(R.id.reset_apply)
    Button resetApply;
    @BindView(R.id.decline)
    Button decline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null){
            Bundle bundle = getIntent().getExtras();

            usersApplyList = new ArrayList<>();
            usersApplyList = bundle.getParcelableArrayList(getString(R.string.GET_SELECTED_ITEM));
        }

        userId = usersApplyList.get(0).getUserId();
        magangPostId = usersApplyList.get(0).getMagangPostId();
        //add collapsing toolbar name
        collapsingToolbarLayout.setTitle(usersApplyList.get(0).getUserName());

        getUserImage(userId);
        getUserData(userId);
        getUserCv(userId);
        getCompanyData(magangPostId);

        resetApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String resetUserMessage = "apakah anda akan mereset kuota apply pengguna ini ?";
                Log.i(TAG, "email = " + emailUser);
                showDialog(resetUserMessage,emailUser);
            }
        });

        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String declined = "apakah anda akan menolak kandidat ini ? " +
                        "jika iya beritahu lewat email";
                showDialog(declined,emailUser);
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

    private void getUserImage(String userId){
        databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constant.USERS_TABLE);

        databaseReference.child(userId).child("image_url")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String imageUrl = dataSnapshot.getValue(String.class);

                        Picasso.get().load(imageUrl)
                                .placeholder(R.drawable.background_image)
                                .error(R.drawable.background_image)
                                .into(toolImage);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void getUserCv(String userId){
        databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constant.USERS_TABLE);

        databaseReference.child(userId).child("cv_url")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final String cvUrl = dataSnapshot.getValue(String.class);

                        userCv.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(NotificationDetailActivity.this, ViewUserCv.class);
                                intent.putExtra("cv", cvUrl);
                                //startActivity(intent);
                                Toast.makeText(NotificationDetailActivity.this,
                                        "User Cv " + cvUrl, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void getUserData(final String userId){
        databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constant.USERS_TABLE);

        databaseReference.child(userId).child("users_data")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        final Users users = dataSnapshot.getValue(Users.class);

                        //show user data
                        userNim.setText(users.getNim());
                        userPhone.setText(users.getTelp());
                        userDepartment.setText(users.getJurusan());
                        userExpertise.setText(users.getDeskripsi());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void getCompanyData(String magangPostId){
        databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constant.MAGANG_POSTING);

        databaseReference.child(magangPostId)
                .child("posting_data")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Magang post = dataSnapshot.getValue(Magang.class);

                        title.setText(post.getTitle());
                        company.setText(post.getCompanyName());
                        city.setText(post.getCity());
                        salary.setText(post.getSalary());
                        requirement.setText(post.getRequirement());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void showDialog(String message, final String email){
        //Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Chain together various setter methods to set the dialog characteristics
        builder.setMessage(message);
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                /*EmailIntentBuilder.from(NotificationDetailActivity.this)
                        .to(email)
                        .cc("mahendrabudiarto96@gmail.com")
                        .subject("Acceptance email")
                        .body("Write your text here")
                        .start();*/

                //validation.setApply(false);
                setUsersApplyValidation();
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

    private void setUsersApplyValidation(){
        databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constant.USERS_TABLE)
                .child(userId)
                .child("magang_apply");

        databaseReference.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(NotificationDetailActivity.this, "User Apply berhasi di reset", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}

