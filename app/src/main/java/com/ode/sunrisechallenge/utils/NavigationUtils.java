package com.ode.sunrisechallenge.utils;

import android.content.Context;
import android.content.Intent;

import com.ode.sunrisechallenge.activity.LoginActivity;
import com.ode.sunrisechallenge.activity.MainActivity;
import com.ode.sunrisechallenge.model.IAccount;

/**
 * Created by ode on 14/07/15.
 */
public class NavigationUtils {

    public static final String USER_PROFILE = "user_profile";
    public static final String LOGOUT = "logout";

    public static Intent navigateToMainView(Context context, IAccount account) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(NavigationUtils.USER_PROFILE, account);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    public static Intent logout(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }
}