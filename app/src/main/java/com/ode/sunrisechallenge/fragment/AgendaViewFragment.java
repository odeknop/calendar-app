package com.ode.sunrisechallenge.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ode.sunrisechallenge.R;
import com.ode.sunrisechallenge.adapter.AgendaViewAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

public class AgendaViewFragment extends Fragment {

    public static final String TAG = AgendaViewFragment.class.getName();
    OnScrolledListener mCallback;
    RecyclerView mRecyclerView;
    LinearLayoutManager mLinearLayoutManager;
    AgendaViewAdapter mAgendaViewAdapter;

    public static AgendaViewFragment newInstance() {
        return new AgendaViewFragment();
    }

    public interface OnScrolledListener {
        public void onAgendaScrolled(int position, boolean scrolling);
    }

    public AgendaViewFragment() {
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
        mRecyclerView.setAdapter(mAgendaViewAdapter = new AgendaViewAdapter(getActivity()));
        final StickyRecyclerHeadersDecoration headersDecoration = new StickyRecyclerHeadersDecoration(mAgendaViewAdapter);
        mRecyclerView.addItemDecoration(headersDecoration);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mCallback.onAgendaScrolled(mLinearLayoutManager.findFirstVisibleItemPosition(), mScrolling);
                mScrolling = true;
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