package com.iardel.contactbook;

import android.app.Activity;
import android.content.SharedPreferences;

class UserPreference {

    private SharedPreferences prefs;

    UserPreference(Activity activity) {
        prefs = activity.getSharedPreferences("USER_PREFERENCES", Activity.MODE_PRIVATE);
    }

    void setName(String name) {
        prefs.edit().putString("userName", name).apply();
    }

    String getName() {
        return prefs.getString("userName", "userName");
    }

    void setEmail(String email) {
        prefs.edit().putString("userEmail", email).apply();
    }

    String getEmail() {
        return prefs.getString("userEmail", "userEmail");
    }

    void setPhoto(String photo) {
        prefs.edit().putString("userPhoto", photo).apply();
    }

    String getPhoto() {
        return prefs.getString("userPhoto", null);
    }

    void clearPreferences() {
        prefs.edit().clear().apply();
    }


}
