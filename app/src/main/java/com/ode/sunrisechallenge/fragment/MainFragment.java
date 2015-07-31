package com.ode.sunrisechallenge.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.ode.sunrisechallenge.R;
import com.ode.sunrisechallenge.Sunrise;
import com.ode.sunrisechallenge.adapter.AgendaAdapter;
import com.ode.sunrisechallenge.adapter.CalendarWeekAdapter;
import com.ode.sunrisechallenge.model.IAccount;
import com.ode.sunrisechallenge.utils.NavigationUtils;
import com.ode.sunrisechallenge.view.EViewState;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

/**
 * Created by ode on 29/07/15.
 */
public class MainFragment extends Fragment {

    public static final String TAG = MainFragment.class.getName();

    private LinearLayoutManager mLinearLayoutManager;
    private GridView mCalendarWeekView;
    private CalendarWeekAdapter mCalendarWeekAdapter;
    private AgendaAdapter mAgendaAdapter;
    private RecyclerView mAgendaView;
    private int mDividerHeight;
    private EViewState mCurrentState = EViewState.Normal;
    private int mFirst;

    public static MainFragment newInstance(IAccount account) {
        Bundle args = new Bundle();
        args.putParcelable(NavigationUtils.USER_PROFILE, account);
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        IAccount account = getArguments().getParcelable(NavigationUtils.USER_PROFILE);
        mDividerHeight = getResources().getDimensionPixelSize(R.dimen.grid_divider_height);
        mAgendaAdapter = new AgendaAdapter(getActivity(), account.getEventManager());
        mCalendarWeekAdapter = new CalendarWeekAdapter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initToolbar(view);
        initAgendaView(view);
        initCalendarWeekView(view);
        initStickyHeaders();
    }

    private void initCalendarWeekView(View view) {
        mCalendarWeekView = (GridView) view.findViewById(R.id.calendar_week_view);
        mCalendarWeekView.setAdapter(mCalendarWeekAdapter);
        mCalendarWeekView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCalendarWeekAdapter.setSelected(position);
                mLinearLayoutManager.scrollToPositionWithOffset(position, 0);
                mAgendaView.stopScroll();
            }
        });

        mCalendarWeekView.getLayoutParams().height = getWeekCalendarHeight(EViewState.Normal);

        mCalendarWeekView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (mCurrentState == EViewState.Normal && scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    expand(mCalendarWeekView, EViewState.Full, 0);
                }

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void initAgendaView(View view) {
        mAgendaView = (RecyclerView) view.findViewById(R.id.agenda_view);
        mAgendaView.setLayoutManager(mLinearLayoutManager = new LinearLayoutManager(getActivity()));
        mAgendaView.setAdapter(mAgendaAdapter);
        mAgendaView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int first = mLinearLayoutManager.findFirstVisibleItemPosition();
                if (first == mFirst || first == RecyclerView.NO_POSITION) return;
                mFirst = first;
                mCalendarWeekView.getHandler().post(new Runnable() {
                    @Override
                    public void run() {
                        mCalendarWeekAdapter.setSelected(mFirst);
                        mCalendarWeekView.smoothScrollToPosition(mFirst);
                    }
                });
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mCurrentState == EViewState.Full && newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    expand(mCalendarWeekView, EViewState.Normal, 0);
                }
            }
        });
    }

    private void initStickyHeaders() {
        final StickyRecyclerHeadersDecoration headersDecoration = new StickyRecyclerHeadersDecoration(mAgendaAdapter);
        mAgendaView.addItemDecoration(headersDecoration);
        mAgendaAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecoration.invalidateHeaders();
            }
        });
    }

    private void initToolbar(View view) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if(actionBar != null) {
            actionBar.setLogo(R.drawable.ic_launcher);
            actionBar.setTitle("Sunrise Challenge");
        }
    }

    private int getWeekCalendarHeight(EViewState state) {
        switch (state) {
            case Normal:
                return (Sunrise.ROW_HEIGHT + mDividerHeight) * 2;
            case Medium:
                return (Sunrise.ROW_HEIGHT + mDividerHeight) * 3;
            case Full:
                return (Sunrise.ROW_HEIGHT + mDividerHeight) * 5;
        }
        return 0;
    }

    private void expand(final View view, final EViewState targetState, int offset) {

        if (mCurrentState == targetState || mCurrentState == EViewState.Animating)
            return;

        final int targetHeight = getWeekCalendarHeight(targetState);
        final int initialHeight = view.getHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(targetHeight > initialHeight) {
                    view.getLayoutParams().height = initialHeight + (int) ((targetHeight - initialHeight) * interpolatedTime);
                } else {
                    view.getLayoutParams().height = initialHeight - (int) ((initialHeight - targetHeight ) * interpolatedTime);
                }
                view.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mCurrentState = EViewState.Animating;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mCurrentState = targetState;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        a.setDuration(((int) (targetHeight / getResources().getDisplayMetrics().density)));
        a.setStartOffset(offset);
        view.startAnimation(a);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout: {
                startActivity(NavigationUtils.logout(getActivity()));
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
