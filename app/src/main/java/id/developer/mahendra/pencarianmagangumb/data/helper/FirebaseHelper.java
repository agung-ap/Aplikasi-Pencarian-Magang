package id.developer.mahendra.pencarianmagangumb.data.helper;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import id.developer.mahendra.pencarianmagangumb.data.model.Users;

public class FirebaseHelper {
    private static FirebaseHelper instance;
    private Context getContext;
    private DatabaseReference databaseReference;
    private String inputUid;
    private Users userData;

    public static FirebaseHelper get() {
        return instance;
    }

    public FirebaseHelper context(Context getContext){
        this.getContext = getContext;
        return instance;
    }

    public FirebaseHelper databaseReference(DatabaseReference databaseReference,
                                            String inputUid, Users userData){
        this.databaseReference = databaseReference;
        this.inputUid = inputUid;
        this.userData = userData;

        return instance;
    }

    public DatabaseReference createUser() {
        databaseReference.child(inputUid).setValue(userData);
        return databaseReference;
    }

    public void completeListener(final Class cls){
        createUser().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
