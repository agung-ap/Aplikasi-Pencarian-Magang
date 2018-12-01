package id.developer.mahendra.pencarianmagangumb;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.developer.mahendra.pencarianmagangumb.model.Magang;
import id.developer.mahendra.pencarianmagangumb.model.Users;
import id.developer.mahendra.pencarianmagangumb.util.Constant;

public class MagangPost extends AppCompatActivity {

    @BindView(R.id.title_post)
    EditText title;
    @BindView(R.id.apply_quota_post)
    EditText applyQuota;
    @BindView(R.id.company_name_post)
    EditText companyName;
    @BindView(R.id.company_email_post)
    EditText companyEmail;
    @BindView(R.id.city_post)
    EditText city;
    @BindView(R.id.salary_post)
    EditText salary;
    @BindView(R.id.requirement_post)
    EditText requirement;
    @BindView(R.id.posting)
    Button posting;

    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    private String userName;
    private Boolean isEdit;
    private ArrayList<Magang> magangData;

    public static final int REQUEST_POST = 41;
    public static final int REQUEST_EDIT = 42;
    private ArrayList<Users> userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magang_post);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Posting Magang");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //init firebase
        auth = FirebaseAuth.getInstance();
        //Inisialisasi ArrayList
        //userId = new ArrayList<>();
        getUserName(auth);
        //getUserId();

        isEdit = getIntent().getBooleanExtra("isEdit", false);

        if (isEdit){
            getPostData();
        }
        //posting magang
        posting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMagangData(data());

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

    private Magang data(){
        String inputTitle = title.getText().toString().trim();
        String inputCompanyName = companyName.getText().toString().trim();
        String inputCompanyEmail = companyEmail.getText().toString().trim();
        String inputCity = city.getText().toString().trim();
        String inputSalary = salary.getText().toString().trim();
        String inputRequirement = requirement.getText().toString().trim();
        String inputApplyQuota = applyQuota.getText().toString().trim();


        Magang posting = new Magang();
        posting.setTitle(inputTitle);
        posting.setCompanyName(inputCompanyName);
        posting.setCity(inputCity);
        posting.setSalary(inputSalary);
        posting.setRequirement(inputRequirement);
        posting.setApplyQuota(Long.parseLong(inputApplyQuota));
        posting.setPostedName(userName);
        posting.setCompanyEmail(inputCompanyEmail);

        return posting;
    }
    //set magang data to database
    private void setMagangData(Magang posting){
        if (!isEdit) {
            databaseReference = FirebaseDatabase.getInstance()
                    .getReference(Constant.MAGANG_POSTING);
            //generate uniq id
            final String key = databaseReference.push().getKey();
            //set magang posting to database
            databaseReference.child(key).child("posting_data").setValue(posting)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                //set users apply count
                                databaseReference.child(key).child("users_apply").child("applyCount")
                                        .setValue(0);
                                Toast.makeText(MagangPost.this, "Posting Berhasil", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {

                            }
                        }
                    });
        }else {
            databaseReference = FirebaseDatabase.getInstance()
                    .getReference(Constant.MAGANG_POSTING);
            databaseReference.child(magangData.get(0).getKey())
                    .child("posting_data")
                    .setValue(posting)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MagangPost.this, "Edit Posting Berhasil", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {

                            }
                        }
                    });
        }
    }

    private void getUserId(){
        databaseReference = FirebaseDatabase.getInstance().getReference(Constant.USERS_TABLE);
        databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //init user
                        Users users = new Users();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            //Mapping data pada DataSnapshot ke dalam objek mahasiswa

                            users.setKey(snapshot.getKey());
                            userId.add(users);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void getUserName(FirebaseAuth auth){
        databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constant.USERS_TABLE);

        databaseReference.child(auth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Users user = dataSnapshot.getValue(Users.class);
                        userName = user.getNama();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void getPostData(){
        Bundle getBundle = getIntent().getExtras();

        //get data from intent
        magangData = new ArrayList<>();
        magangData = getBundle
                .getParcelableArrayList("data");

        title.setText(magangData.get(0).getTitle());
        companyName.setText(magangData.get(0).getCompanyName());
        city.setText(magangData.get(0).getCity());
        salary.setText(magangData.get(0).getSalary());
        requirement.setText(magangData.get(0).getRequirement());
        applyQuota.setText(String.valueOf(magangData.get(0).getApplyQuota()));
        companyEmail.setText(magangData.get(0).getCompanyEmail());
    }

}
