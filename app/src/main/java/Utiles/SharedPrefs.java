package Utiles;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefs implements Constants {

    private SharedPreferences preferences;
    private static SharedPrefs mySharedPrefs;

    private SharedPrefs(Context context){
        preferences = context.getApplicationContext()
                .getSharedPreferences(SP_FILE, Context.MODE_PRIVATE);
    }

    public static SharedPrefs getInstance(Context context){
        if (mySharedPrefs == null)
            mySharedPrefs = new SharedPrefs(context);

        return mySharedPrefs;
    }

    public String getStrSP(String key, String defValue) {
        return this.preferences.getString(key, defValue);
    }

    public void putStringSP(String key, String value) {
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putString(key, value);
        editor.apply();
    }
}
