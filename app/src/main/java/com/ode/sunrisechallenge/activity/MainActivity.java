package com.ode.sunrisechallenge.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.ode.sunrisechallenge.R;
import com.ode.sunrisechallenge.fragment.AgendaViewFragment;
import com.ode.sunrisechallenge.fragment.CalendarViewFragment;
import com.ode.sunrisechallenge.view.SunriseView;

import org.joda.time.DateTime;


public class MainActivity extends AppCompatActivity implements CalendarViewFragment.OnDaySelectedListener, AgendaViewFragment.OnScrolledListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SunriseView mainView = (SunriseView) findViewById(R.id.sunrise_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setLogo(R.drawable.ic_launcher);
        getSupportActionBar().setTitle("Sunrise Challenge");

        if(findViewById(R.id.calendar_fragment_container) != null && findViewById(R.id.agenda_fragment_container) != null) {
            if(savedInstanceState != null) {
                return;
            }

            CalendarViewFragment calendarViewFragment = CalendarViewFragment.newInstance();
            getSupportFragmentManager().beginTransaction().
                    add(R.id.calendar_fragment_container, calendarViewFragment, calendarViewFragment.TAG).commit();

            AgendaViewFragment agendaViewFragment = AgendaViewFragment.newInstance();
            getSupportFragmentManager().beginTransaction().
                    add(R.id.agenda_fragment_container, agendaViewFragment, agendaViewFragment.TAG).commit();
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