package com.ode.sunrisechallenge.recycler.stickyheadersrecyclerview.util;

import com.ode.sunrisechallenge.recycler.LinearLayoutManager;
import com.ode.sunrisechallenge.recycler.RecyclerView;

/**
 * OrientationProvider for ReyclerViews who use a LinearLayoutManager
 */
public class LinearLayoutOrientationProvider implements OrientationProvider {

  @Override
  public int getOrientation(RecyclerView recyclerView) {
    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

    if (layoutManager instanceof LinearLayoutManager) {
      return ((LinearLayoutManager) layoutManager).getOrientation();
    } else {
      throw new IllegalStateException("StickyListHeadersDecoration can only be used with a " +
          "LinearLayoutManager.");
    }
  }

}
