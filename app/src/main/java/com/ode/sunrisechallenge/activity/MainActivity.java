package com.ode.sunrisechallenge.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ode.sunrisechallenge.fragment.MainFragment;
import com.ode.sunrisechallenge.model.IAccount;
import com.ode.sunrisechallenge.utils.NavigationUtils;


public class MainActivity extends AppCompatActivity {

    private IAccount mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getIntent() != null) {
            mAccount = getIntent().getParcelableExtra(NavigationUtils.USER_PROFILE);
        }

        getSupportFragmentManager()
                .beginTransaction()
                .add(android.R.id.content, MainFragment.newInstance(mAccount), MainFragment.TAG)
                .commit();
    }
}