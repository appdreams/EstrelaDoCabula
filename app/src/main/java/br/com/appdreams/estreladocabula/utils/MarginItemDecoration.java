package br.com.appdreams.estreladocabula.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Paulo on 09/04/17.
 */

public class MarginItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpacing;

    public MarginItemDecoration(int spacing) {
        mSpacing = spacing;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        outRect.left = mSpacing;
        //outRect.top = mSpacing;
        outRect.right = mSpacing;
        //outRect.bottom = mSpacing;
    }
}