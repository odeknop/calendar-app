package com.ode.sunrisechallenge.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ode.sunrisechallenge.R;
import com.ode.sunrisechallenge.fragment.AgendaViewFragment;
import com.ode.sunrisechallenge.fragment.CalendarViewFragment;
import com.ode.sunrisechallenge.model.IAccount;
import com.ode.sunrisechallenge.utils.NavigationUtils;


public class MainActivity extends AppCompatActivity implements CalendarViewFragment.OnDaySelectedListener, AgendaViewFragment.OnScrolledListener {

    private IAccount mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();
        if(getIntent() != null) {
            mAccount = getIntent().getParcelableExtra(NavigationUtils.USER_PROFILE);
        }
        if(findViewById(R.id.calendar_fragment_container) != null && findViewById(R.id.agenda_fragment_container) != null) {
            if(savedInstanceState != null) {
                return;
            }

            CalendarViewFragment calendarViewFragment = CalendarViewFragment.newInstance();
            getSupportFragmentManager().beginTransaction().
                    add(R.id.calendar_fragment_container, calendarViewFragment, CalendarViewFragment.TAG).commit();

            AgendaViewFragment agendaViewFragment = AgendaViewFragment.newInstance(mAccount);
            getSupportFragmentManager().beginTransaction().
                    add(R.id.agenda_fragment_container, agendaViewFragment, AgendaViewFragment.TAG).commit();
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setLogo(R.drawable.ic_launcher);
            actionBar.setTitle("Sunrise Challenge");
        }
    }

    @Override
    public void onDaySelected(int position) {
        AgendaViewFragment agendaViewFragment = (AgendaViewFragment) getSupportFragmentManager().findFragmentByTag(AgendaViewFragment.TAG);
        if(agendaViewFragment != null) {
            agendaViewFragment.setSelected(position);
        }
    }

    @Override
    public void onAgendaScrolled(int position, boolean scrolling) {
        CalendarViewFragment calendarViewFragment = (CalendarViewFragment) getSupportFragmentManager().findFragmentByTag(CalendarViewFragment.TAG);
        if(calendarViewFragment != null) {
            calendarViewFragment.setSelected(position, scrolling);
        }
    }
}