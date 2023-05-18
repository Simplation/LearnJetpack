package com.simplation.mvvm.app.weight.preference;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Checkable;
import android.widget.CompoundButton;

import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.core.content.res.TypedArrayUtils;
import androidx.preference.PreferenceViewHolder;
import androidx.preference.TwoStatePreference;

import com.simplation.mvvm.R;
import com.simplation.mvvm.app.util.SettingUtil;

import static androidx.annotation.RestrictTo.Scope.LIBRARY;

@SuppressLint("RestrictedApi")
public class CheckBoxPreference extends TwoStatePreference {
    private final Listener mListener = new Listener();
    CompoundButton checkBoxView;

    public CheckBoxPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CheckBoxPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        final TypedArray a = context.obtainStyledAttributes(attrs,
                R.styleable.CheckBoxPreference, defStyleAttr, defStyleRes);

        setSummaryOn(TypedArrayUtils.getString(a, R.styleable.CheckBoxPreference_summaryOn,
                R.styleable.CheckBoxPreference_android_summaryOn));

        setSummaryOff(TypedArrayUtils.getString(a, R.styleable.CheckBoxPreference_summaryOff,
                R.styleable.CheckBoxPreference_android_summaryOff));

        setDisableDependentsState(TypedArrayUtils.getBoolean(a,
                R.styleable.CheckBoxPreference_disableDependentsState,
                R.styleable.CheckBoxPreference_android_disableDependentsState, false));

        a.recycle();
    }

    public CheckBoxPreference(Context context, AttributeSet attrs) {
        this(context, attrs, TypedArrayUtils.getAttr(context, R.attr.checkBoxPreferenceStyle,
                android.R.attr.checkBoxPreferenceStyle));
    }

    public CheckBoxPreference(Context context) {
        this(context, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(PreferenceViewHolder holder) {
        super.onBindViewHolder(holder);

        syncCheckboxView(holder.findViewById(android.R.id.checkbox));

        syncSummaryView(holder);
    }

    /**
     * @hide
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @RestrictTo(LIBRARY)
    @Override
    protected void performClick(View view) {
        super.performClick(view);
        syncViewIfAccessibilityEnabled(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void syncViewIfAccessibilityEnabled(View view) {
        AccessibilityManager accessibilityManager = (AccessibilityManager)
                getContext().getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (!accessibilityManager.isEnabled()) {
            return;
        }

        View checkboxView = view.findViewById(android.R.id.checkbox);
        syncCheckboxView(checkboxView);

        View summaryView = view.findViewById(android.R.id.summary);
        syncSummaryView(summaryView);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void syncCheckboxView(View view) {
        if (view instanceof CompoundButton) {
            checkBoxView = ((CompoundButton) view);
            checkBoxView.setOnCheckedChangeListener(null);
        }
        if (view instanceof Checkable) {
            ((Checkable) view).setChecked(mChecked);
        }
        if (view instanceof CompoundButton) {
            checkBoxView.setOnCheckedChangeListener(mListener);
            checkBoxView.setButtonTintList(SettingUtil.INSTANCE.getColorStateList(getContext()));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void setBottonColor() {
        if (checkBoxView != null) {
            checkBoxView.setButtonTintList(SettingUtil.INSTANCE.getColorStateList(getContext()));
        }
    }

    private class Listener implements CompoundButton.OnCheckedChangeListener {
        Listener() {
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (!callChangeListener(isChecked)) {
                // Listener didn't like it, change it back.
                // CompoundButton will make sure we don't recurse.
                buttonView.setChecked(!isChecked);
                return;
            }
            CheckBoxPreference.this.setChecked(isChecked);
        }
    }
}