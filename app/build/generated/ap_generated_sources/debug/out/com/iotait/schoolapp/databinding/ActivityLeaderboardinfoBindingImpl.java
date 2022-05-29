package com.iotait.schoolapp.databinding;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityLeaderboardinfoBindingImpl extends ActivityLeaderboardinfoBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.appBarLayout, 1);
        sViewsWithIds.put(R.id.toolbar, 2);
        sViewsWithIds.put(R.id.backbutton, 3);
        sViewsWithIds.put(R.id.toolbartext, 4);
        sViewsWithIds.put(R.id.circleImageView2, 5);
        sViewsWithIds.put(R.id.linearLayout5, 6);
        sViewsWithIds.put(R.id.usernameleaderboard, 7);
        sViewsWithIds.put(R.id.cardView2, 8);
        sViewsWithIds.put(R.id.imageView2, 9);
        sViewsWithIds.put(R.id.userScore, 10);
        sViewsWithIds.put(R.id.takeTimeuser, 11);
        sViewsWithIds.put(R.id.linearLayout6, 12);
        sViewsWithIds.put(R.id.sub4, 13);
        sViewsWithIds.put(R.id.linearLayout7, 14);
        sViewsWithIds.put(R.id.sub1, 15);
        sViewsWithIds.put(R.id.sub2, 16);
        sViewsWithIds.put(R.id.sub3, 17);
        sViewsWithIds.put(R.id.spin_kit, 18);
        sViewsWithIds.put(R.id.textView12, 19);
        sViewsWithIds.put(R.id.myList, 20);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityLeaderboardinfoBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 21, sIncludes, sViewsWithIds));
    }
    private ActivityLeaderboardinfoBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.google.android.material.appbar.AppBarLayout) bindings[1]
            , (android.widget.ImageButton) bindings[3]
            , (androidx.cardview.widget.CardView) bindings[8]
            , (de.hdodenhof.circleimageview.CircleImageView) bindings[5]
            , (android.widget.ImageView) bindings[9]
            , (android.widget.LinearLayout) bindings[6]
            , (android.widget.LinearLayout) bindings[12]
            , (android.widget.LinearLayout) bindings[14]
            , (android.widget.ListView) bindings[20]
            , (com.github.ybq.android.spinkit.SpinKitView) bindings[18]
            , (android.widget.TextView) bindings[15]
            , (android.widget.TextView) bindings[16]
            , (android.widget.TextView) bindings[17]
            , (android.widget.TextView) bindings[13]
            , (android.widget.TextView) bindings[11]
            , (android.widget.TextView) bindings[19]
            , (androidx.appcompat.widget.Toolbar) bindings[2]
            , (android.widget.TextView) bindings[4]
            , (android.widget.TextView) bindings[10]
            , (android.widget.TextView) bindings[7]
            );
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
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