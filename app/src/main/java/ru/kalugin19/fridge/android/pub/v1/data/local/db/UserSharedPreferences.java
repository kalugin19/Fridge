package ru.kalugin19.fridge.android.pub.v1.data.local.db;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

import ru.kalugin19.fridge.android.pub.v1.injection.qualifiers.ApplicationContext;

/**
 * Для хранения данных пользователя
 *
 * @author Kalugin Valeriy
 */
public class UserSharedPreferences {
    private static final String PREF_FILE_NAME = "user_data";
    private static final String EMAIL = "email";
    private static final String PHOTO_URL = "photo_url";
    private static final String FIRST_INPUT = "first_input";
    private final SharedPreferences mPref;

    @Inject
    UserSharedPreferences(@ApplicationContext Context context) {
        mPref = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void setEmail(String email) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(EMAIL, email);
        editor.apply();
    }

    public String getEmail() {
        String i = mPref.getString(EMAIL, "");
        return mPref.getString(EMAIL, "");
    }

    public void setPhotoUrl(String url) {
        SharedPreferences.Editor editor = mPref.edit();
        editor.putString(PHOTO_URL, url);
        editor.apply();
    }

    public String getPhotoUrl() {
        return mPref.getString(PHOTO_URL, "");
    }

    public void setFirstInput(@SuppressWarnings("SameParameterValue") boolean flag){
        SharedPreferences.Editor editor = mPref.edit();
        editor.putBoolean(FIRST_INPUT, flag);
        editor.apply();
    }

    public boolean getFirstInput(){
        return mPref.getBoolean(FIRST_INPUT, true);
    }
}
