package id.developer.mahendra.pencarianmagangumb;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    @BindView(R.id.open_apply)
    Button openApply;

    private ArrayList<Magang> magangData;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magang_detail_admin);
        ButterKnife.bind(this);
        //set home button enable
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //get data from intent
        if (savedInstanceState == null){
            Bundle getBundle = getIntent().getExtras();
            //get data from intent
            magangData = new ArrayList<>();
            magangData = getBundle
                    .getParcelableArrayList(getString(R.string.GET_SELECTED_ITEM));
        }
        //set actionbar title
        getSupportActionBar().setTitle(magangData.get(0).getTitle());
        //show magang detail data
        showMagangDetail();
        //check if isApply enable
        isApplyEnable();
        openApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setApplyCount(0);
                openApply.setClickable(false);
                openApply.setBackground(null);
                openApply.setTextColor(getResources().getColor(R.color.button_color));
                openApply.setText("lowongan ini berhasil di buka");
            }
        });
    }
    //set apply count = 0
    private void setApplyCount(long i) {
        databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constant.MAGANG_POSTING);
        //set applyCount value = 0
        databaseReference.child(magangData.get(0).getKey())
                .child("users_apply")
                .child("applyCount").setValue(i);
    }
    //check database when ApplyCount >= 30
    private void isApplyEnable() {
        databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constant.MAGANG_POSTING);
        //get applyCount value
        databaseReference.child(magangData.get(0).getKey())
                .child("users_apply").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count = (long) dataSnapshot.child("applyCount").getValue();
                if (count >= magangData.get(0).getApplyQuota()){
                    openApply.setClickable(true);

                }else {
                    openApply.setClickable(false);
                    openApply.setBackground(null);
                    openApply.setTextColor(getResources().getColor(R.color.button_color));
                    openApply.setText("jumlah pengguna yang apply saat ini kurang dari " +
                            magangData.get(0).getApplyQuota());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //show magang posting data data
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
                showDeleteApply();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    //show delete dialog when press delete menu
    private void showDeleteApply(){
        //Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //Chain together various setter methods to set the dialog characteristics
        builder.setMessage("Apakah anda ingin Menghapus Posting ini ?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteMagangPost();
                Toast.makeText(getApplicationContext(), "Apply Berhasil", Toast.LENGTH_SHORT).show();
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
    //delete magang post
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


}
