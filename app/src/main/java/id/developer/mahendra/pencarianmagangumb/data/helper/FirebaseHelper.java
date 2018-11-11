package id.developer.mahendra.pencarianmagangumb.data.helper;

import android.content.Context;

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
    public void createUser(String inputUid, Users user, Class<?> cls) {
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
