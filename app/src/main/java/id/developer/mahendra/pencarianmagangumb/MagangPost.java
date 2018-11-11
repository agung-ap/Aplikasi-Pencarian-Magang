package id.developer.mahendra.pencarianmagangumb;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

import butterknife.BindView;
import butterknife.ButterKnife;
import id.developer.mahendra.pencarianmagangumb.data.helper.FirebaseHelper;
import id.developer.mahendra.pencarianmagangumb.data.model.Magang;
import id.developer.mahendra.pencarianmagangumb.data.model.Users;
import id.developer.mahendra.pencarianmagangumb.util.Constant;

public class MagangPost extends AppCompatActivity {

    @BindView(R.id.title_post)
    EditText title;
    @BindView(R.id.company_name_post)
    EditText companyName;
    @BindView(R.id.city_post)
    EditText city;
    @BindView(R.id.salary_post)
    EditText salary;
    @BindView(R.id.requirement_post)
    EditText requirement;
    @BindView(R.id.posting)
    Button posting;

    private FirebaseAuth auth;
    private String userName;
    private Boolean isEdit;
    private ArrayList<Magang> magangData;

    public static final int REQUEST_POST = 41;
    public static final int REQUEST_EDIT = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magang_post);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Posting Magang");
        //init firebase
        auth = FirebaseAuth.getInstance();
        getUserName(auth);

        isEdit = getIntent().getBooleanExtra("isEdit", false);

        if (isEdit){
            getPostData();
        }

        //posting magang
        posting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setMagangData(auth, data());

            }
        });

    }

    private Magang data(){

        String inputTitle = title.getText().toString().trim();
        String inputCompanyName = companyName.getText().toString().trim();
        String inputCity = city.getText().toString().trim();
        String inputSalary = salary.getText().toString().trim();
        String inputRequirement = requirement.getText().toString().trim();

        Magang posting = new Magang();
        posting.setTitle(inputTitle);
        posting.setCompanyName(inputCompanyName);
        posting.setCity(inputCity);
        posting.setSalary(inputSalary);
        posting.setRequirement(inputRequirement);
        posting.setPostedName(userName);

        return posting;
    }

    private void setMagangData(FirebaseAuth auth, Magang posting){
        final FirebaseUser currentUser = auth.getCurrentUser();
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constant.MAGANG_POSTING);
        databaseReference.child(databaseReference.push().getKey()).setValue(posting)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            setResult(REQUEST_POST);
                            finish();
                        }else {

                        }
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
    }

}
