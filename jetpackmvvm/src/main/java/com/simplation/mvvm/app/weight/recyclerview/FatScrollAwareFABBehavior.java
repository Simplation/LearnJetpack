package com.simplation.mvvm.app.weight.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.jetbrains.annotations.NotNull;

/**
 * 自定义 Behavior  FAB 上滑显示 下滑隐藏
 */
public class FatScrollAwareFABBehavior extends FloatingActionButton.Behavior {

    public FatScrollAwareFABBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NotNull CoordinatorLayout coordinatorLayout,
                                       @NotNull FloatingActionButton child,
                                       @NotNull View directTargetChild,
                                       @NotNull View target,
                                       int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL ||
                super.onStartNestedScroll(coordinatorLayout, child, directTargetChild, target, nestedScrollAxes);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onNestedScroll(@NotNull CoordinatorLayout coordinatorLayout, @NotNull FloatingActionButton child,
                               @NotNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed,
                dyUnconsumed);

        if (dyConsumed > 0 && child.getVisibility() == View.VISIBLE) {
            child.setVisibility(View.INVISIBLE);
        } else if (dyConsumed < 0 && child.getVisibility() != View.VISIBLE) {
            child.show();
        }
    }
}
