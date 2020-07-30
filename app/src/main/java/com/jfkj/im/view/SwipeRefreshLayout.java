package com.jfkj.im.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;

public class SwipeRefreshLayout extends androidx.swiperefreshlayout.widget.SwipeRefreshLayout {
    public SwipeRefreshLayout(@NonNull Context context) {
        super(context);
    }

    public SwipeRefreshLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean canChildScrollUp() {
        View target = getChildAt(0);
        if (target instanceof AbsListView) {
            final AbsListView absListView = (AbsListView) target;
            return absListView.getChildCount() > 0
                    && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                    .getTop() < absListView.getPaddingTop());
        } else
            return ViewCompat.canScrollVertically(target,-1);
//            return target.canScrollVertically(-1);
    }
}
