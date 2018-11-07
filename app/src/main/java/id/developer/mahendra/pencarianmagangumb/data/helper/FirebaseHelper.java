package id.developer.mahendra.pencarianmagangumb.data.helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import id.developer.mahendra.pencarianmagangumb.RegisterActivity;
import id.developer.mahendra.pencarianmagangumb.UserActivity;
import id.developer.mahendra.pencarianmagangumb.data.model.Mahasiswa;

public class FirebaseHelper {
    private static FirebaseHelper instance;
    private static Context getContext;

    public static FirebaseHelper get() {
        return instance;
    }

    public void context(Context getContext){
        this.getContext = getContext;
    }
    /*
    public void createUser(String inputUid, Mahasiswa user, Class<?> cls) {
        //start saving data on firebase realtime database
        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child(inputUid).setValue(user)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext, "Register Berhasil",
                                    Toast.LENGTH_SHORT).show();

                            getContext.startActivity(new Intent(getContext, cls));
                            //getContext.finish();
                        }else {
                            Toast.makeText(getContext, "Register gagal : " + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }*/
}
