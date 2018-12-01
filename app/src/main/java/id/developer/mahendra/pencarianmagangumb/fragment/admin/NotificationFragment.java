package id.developer.mahendra.pencarianmagangumb.fragment.admin;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import id.developer.mahendra.pencarianmagangumb.AdminActivity;
import id.developer.mahendra.pencarianmagangumb.NotificationDetailActivity;
import id.developer.mahendra.pencarianmagangumb.R;
import id.developer.mahendra.pencarianmagangumb.adapter.ApplyListAdapter;
import id.developer.mahendra.pencarianmagangumb.model.ApplyNotification;
import id.developer.mahendra.pencarianmagangumb.model.Magang;
import id.developer.mahendra.pencarianmagangumb.model.Users;
import id.developer.mahendra.pencarianmagangumb.model.UsersApply;
import id.developer.mahendra.pencarianmagangumb.util.Constant;
import id.developer.mahendra.pencarianmagangumb.util.PdfPrint;

public class NotificationFragment extends Fragment implements ApplyListAdapter.DataListener{
    private static final String TAG = NotificationFragment.class.getSimpleName();
    private final int REQUEST_CODE_ASK_PERMISSIONS = 111;

    private PdfPrint pdfPrint;

    private ArrayList<UsersApply> applyList;
    private ApplyListAdapter applyListAdapter;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //init arraylist
        applyList = new ArrayList<>();
        applyListAdapter = new ApplyListAdapter(getActivity(),this);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification_admin, container, false);

        ((AdminActivity)getActivity()).getSupportActionBar().setTitle("Notification");
        setHasOptionsMenu(true);

        recyclerView = (RecyclerView)view.findViewById(R.id.notification_list_admin);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        //get users apply
        getUsersApply();
        //print to pdf
        pdfPrint = new PdfPrint(getActivity(),TAG, applyList);
        recyclerView.setAdapter(applyListAdapter);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.download_report);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadReport();
            }
        });
        return view;
    }

    private void downloadReport() {
        try {
            createPdfWrapper();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private void getUsersApply(){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constant.USERS_APPLY_TABLE);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        UsersApply apply = snapshot.getValue(UsersApply.class);

                        Log.i(TAG, "magang post id = " + apply.getMagangPostId());
                        Log.i(TAG, "user id = " + apply.getUserId());

                        applyList.add(apply);
                        applyListAdapter.setNotificationData(applyList);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
    }

    private void getUserData(String uid){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constant.USERS_TABLE);

        databaseReference.child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Users users = dataSnapshot.getValue(Users.class);

                        ApplyNotification notification = new ApplyNotification();
                        notification.setUserName(users.getNama());

                        //applyList.add(notification);
                        //applyListAdapter.setNotificationData(applyList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void getMagangData(String postId){
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference(Constant.MAGANG_POSTING);

        databaseReference.child(postId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Magang post = dataSnapshot.getValue(Magang.class);

                        ApplyNotification notification = new ApplyNotification();
                        notification.setTitle(post.getTitle());
                        notification.setCompany(post.getCompanyName());

                        //applyList.add(notification);
                        //applyListAdapter.setNotificationData(applyList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public void onClick(UsersApply dataPosition) {
        Bundle bundle = new Bundle();

        ArrayList<UsersApply> notificationModel = new ArrayList<>();
        notificationModel.add(dataPosition);

        bundle.putParcelableArrayList(getString(R.string.GET_SELECTED_ITEM), notificationModel);
        //send data via intent
        Intent intent = new Intent(this.getActivity(), NotificationDetailActivity.class);
        intent.putExtras(bundle);
        //intent.putExtra("user status", );
        startActivity(intent);
    }

    private void createPdfWrapper() throws FileNotFoundException,DocumentException {

        int hasWriteStoragePermission = ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_CONTACTS)) {
                    showMessageOKCancel("You need to allow access to Storage",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(new String[]{
                                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                REQUEST_CODE_ASK_PERMISSIONS);
                                    }
                                }
                            });
                    return;
                }

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_ASK_PERMISSIONS);
            }
            return;
        }else {

            pdfPrint.createPdf();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    try {
                        createPdfWrapper();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "WRITE_EXTERNAL Permission Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getActivity())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}
