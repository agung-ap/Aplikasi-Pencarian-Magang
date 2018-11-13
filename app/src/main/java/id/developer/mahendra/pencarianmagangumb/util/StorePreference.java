package id.developer.mahendra.pencarianmagangumb.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import id.developer.mahendra.pencarianmagangumb.R;

public class StorePreference {
    Context context;
    SharedPreferences prefs;

    public StorePreference(Context context) {
        prefs = PreferenceManager.getDefaultSharedPreferences(context);
        this.context = context;
    }

    public void setFirstRun(Boolean input) {
        SharedPreferences.Editor editor = prefs.edit();
        String key = context.getResources().getString(R.string.first_apply);
        editor.putBoolean(key, input);
        editor.commit();
    }

    public Boolean getFirstRun() {
        String key = context.getResources().getString(R.string.first_apply);
        return prefs.getBoolean(key, true);
    }
}
