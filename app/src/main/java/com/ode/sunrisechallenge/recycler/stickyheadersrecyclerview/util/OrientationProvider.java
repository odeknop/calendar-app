package com.ode.sunrisechallenge.recycler.stickyheadersrecyclerview.util;

import com.ode.sunrisechallenge.recycler.RecyclerView;

/**
 * Interface for getting the orientation of a RecyclerView from its LayoutManager
 */
public interface OrientationProvider {

  int getOrientation(RecyclerView recyclerView);

}
