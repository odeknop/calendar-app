package com.ode.sunrisechallenge.fragment;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.LinearLayout;

import com.ode.sunrisechallenge.model.IAccount;
import com.ode.sunrisechallenge.recycler.Scroller;
import android.widget.TextView;

import com.ode.sunrisechallenge.R;
import com.ode.sunrisechallenge.adapter.AgendaViewAdapter;
import com.ode.sunrisechallenge.recycler.LinearLayoutManager;
import com.ode.sunrisechallenge.recycler.RecyclerView;
import com.ode.sunrisechallenge.recycler.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.ode.sunrisechallenge.utils.NavigationUtils;

import org.joda.time.LocalDate;

public class AgendaViewFragment extends Fragment {

    public static final String TAG = AgendaViewFragment.class.getName();

    private IAccount mAccount;
    OnScrolledListener mCallback;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLinearLayoutManager;
    AgendaViewAdapter mAgendaViewAdapter;

    public static AgendaViewFragment newInstance(IAccount account) {
        Bundle b = new Bundle();
        b.putParcelable(NavigationUtils.USER_PROFILE, account);
        AgendaViewFragment f = new AgendaViewFragment();
        f.setArguments(b);
        return f;
    }

    public interface OnScrolledListener {
        void onAgendaScrolled(int position, boolean scrolling);
    }

    public AgendaViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAccount = getArguments().getParcelable(NavigationUtils.USER_PROFILE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnScrolledListener) activity;
        } catch (Exception e) {
            throw new ClassCastException(activity.toString() + " must implement OnDaySelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_agenda, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAgendaViewAdapter = new AgendaViewAdapter(getActivity(), mAccount.getEventManager()));
        final StickyRecyclerHeadersDecoration headersDecoration = new StickyRecyclerHeadersDecoration(mAgendaViewAdapter);
        mRecyclerView.addItemDecoration(headersDecoration);

        LocalDate lc = new LocalDate();

        final Scroller s = mRecyclerView.getScroller();
        final boolean[] extend = {true};

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mCallback.onAgendaScrolled(mLinearLayoutManager.findFirstVisibleItemPosition(), mScrolling);
                mScrolling = true;

                float curVeloY = s.getCurrVelocity();
                int curY = s.getCurrY();
                int finalY = s.getFinalY();

                int res = Math.abs(curY - finalY);

                if(res <= 3 && res > 0 && extend[0] && curVeloY > 0) {
                    extend[0] = false;
                    final View fv = mRecyclerView.getChildAt(0);
                    int extend = fv.getBottom();
                    s.setFinalY(finalY + extend);
                    s.extendDuration((int) (extend / (curVeloY)));
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    extend[0] = true;
                }
            }
        });

        mAgendaViewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecoration.invalidateHeaders();
            }
        });

    }

    boolean mScrolling = true;

    public void setSelected(int position) {
        mScrolling = false;
        mLinearLayoutManager.scrollToPositionWithOffset(position, 0);
    }
}