package id.developer.mahendra.pencarianmagangumb;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.developer.mahendra.pencarianmagangumb.model.Magang;
import id.developer.mahendra.pencarianmagangumb.util.Constant;

public class MagangDetailAdminActivity extends AppCompatActivity {
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

    private ArrayList<Magang> magangData;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magang_detail_admin);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();

        if (savedInstanceState == null){
            Bundle getBundle = getIntent().getExtras();
            //get data from intent
            magangData = new ArrayList<>();
            magangData = getBundle
                    .getParcelableArrayList(getString(R.string.GET_SELECTED_ITEM));
        }

        getSupportActionBar().setTitle(magangData.get(0).getTitle());
        showMagangDetail();
    }

    private void showMagangDetail(){
        titleDetail.setText(magangData.get(0).getTitle());
        companyNameDetail.setText(magangData.get(0).getCompanyName());
        cityDetail.setText(magangData.get(0).getCity());
        salaryDetail.setText("Salary " + magangData.get(0).getSalary());
        requirementDetail.setText(magangData.get(0).getRequirement());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.magang_detail_admin, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.edit_magang_posting:
                Intent intent = new Intent(MagangDetailAdminActivity.this
                        , MagangPost.class);
                intent.putExtra("isEdit", true);
                intent.putParcelableArrayListExtra("data",magangData);
                startActivityForResult(intent, MagangPost.REQUEST_EDIT);
                break;
            case R.id.delete_magang_posting:
                deleteMagangPost();
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteMagangPost(){
        databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constant.MAGANG_POSTING)
                .child(magangData.get(0).getKey());
        databaseReference.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(MagangDetailAdminActivity.this, "Data berhasil di hapus", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
