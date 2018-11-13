package id.developer.mahendra.pencarianmagangumb.fragment.admin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import id.developer.mahendra.pencarianmagangumb.AdminActivity;
import id.developer.mahendra.pencarianmagangumb.R;
import id.developer.mahendra.pencarianmagangumb.adapter.ApplyListAdapter;
import id.developer.mahendra.pencarianmagangumb.data.model.ApplyNotification;
import id.developer.mahendra.pencarianmagangumb.data.model.Magang;
import id.developer.mahendra.pencarianmagangumb.data.model.Users;
import id.developer.mahendra.pencarianmagangumb.data.model.UsersApply;
import id.developer.mahendra.pencarianmagangumb.util.Constant;

public class NotificationFragment extends Fragment {
    private static final String TAG = NotificationFragment.class.getSimpleName();

    private ArrayList<UsersApply> applyList;
    private ApplyListAdapter applyListAdapter;
    private RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //init arraylist
        applyList = new ArrayList<>();
        applyListAdapter = new ApplyListAdapter(getActivity());

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

        recyclerView.setAdapter(applyListAdapter);
        return view;
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
}
