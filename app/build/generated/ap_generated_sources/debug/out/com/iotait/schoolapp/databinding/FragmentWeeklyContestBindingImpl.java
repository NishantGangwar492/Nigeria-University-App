package com.iotait.schoolapp.databinding;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentWeeklyContestBindingImpl extends FragmentWeeklyContestBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.appBarLayout, 1);
        sViewsWithIds.put(R.id.toolbar, 2);
        sViewsWithIds.put(R.id.backbtn, 3);
        sViewsWithIds.put(R.id.leaderboardfromweekly, 4);
        sViewsWithIds.put(R.id.coinview, 5);
        sViewsWithIds.put(R.id.points, 6);
        sViewsWithIds.put(R.id.tvfull, 7);
        sViewsWithIds.put(R.id.spin_kit, 8);
        sViewsWithIds.put(R.id.countdown, 9);
        sViewsWithIds.put(R.id.txt_contest_close, 10);
        sViewsWithIds.put(R.id.closeCountdown, 11);
    }
    // views
    @NonNull
    private final android.widget.FrameLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentWeeklyContestBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 12, sIncludes, sViewsWithIds));
    }
    private FragmentWeeklyContestBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.google.android.material.appbar.AppBarLayout) bindings[1]
            , (android.widget.ImageButton) bindings[3]
            , (android.widget.TextView) bindings[11]
            , (android.widget.LinearLayout) bindings[5]
            , (android.widget.TextView) bindings[9]
            , (android.widget.ImageView) bindings[4]
            , (android.widget.TextView) bindings[6]
            , (com.github.ybq.android.spinkit.SpinKitView) bindings[8]
            , (androidx.appcompat.widget.Toolbar) bindings[2]
            , (android.widget.Button) bindings[7]
            , (android.widget.TextView) bindings[10]
            );
        this.mboundView0 = (android.widget.FrameLayout) bindings[0];
        this.mboundView0.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x1L;
        }
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
            return variableSet;
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
        }
        return false;
    }

    @Override
    protected void executeBindings() {
        long dirtyFlags = 0;
        synchronized(this) {
            dirtyFlags = mDirtyFlags;
            mDirtyFlags = 0;
        }
        // batch finished
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): null
    flag mapping end*/
    //end
}