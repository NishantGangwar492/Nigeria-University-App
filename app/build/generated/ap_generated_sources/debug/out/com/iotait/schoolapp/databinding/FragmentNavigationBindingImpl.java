package com.iotait.schoolapp.databinding;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentNavigationBindingImpl extends FragmentNavigationBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.headerlayout, 1);
        sViewsWithIds.put(R.id.userProfilePicture, 2);
        sViewsWithIds.put(R.id.package_id, 3);
        sViewsWithIds.put(R.id.userName, 4);
        sViewsWithIds.put(R.id.userEmail, 5);
        sViewsWithIds.put(R.id.back, 6);
        sViewsWithIds.put(R.id.schoolAnthem, 7);
        sViewsWithIds.put(R.id.questions, 8);
        sViewsWithIds.put(R.id.unnNews, 9);
        sViewsWithIds.put(R.id.articals, 10);
        sViewsWithIds.put(R.id.takeExam, 11);
        sViewsWithIds.put(R.id.LeaderBoard, 12);
        sViewsWithIds.put(R.id.syllabus, 13);
        sViewsWithIds.put(R.id.go_premium, 14);
        sViewsWithIds.put(R.id.weekly_contest, 15);
        sViewsWithIds.put(R.id.chatBot, 16);
        sViewsWithIds.put(R.id.facebook, 17);
        sViewsWithIds.put(R.id.aboutApp, 18);
        sViewsWithIds.put(R.id.unn_faq, 19);
        sViewsWithIds.put(R.id.rateUs, 20);
        sViewsWithIds.put(R.id.ContactUs, 21);
        sViewsWithIds.put(R.id.offline, 22);
        sViewsWithIds.put(R.id.signout, 23);
        sViewsWithIds.put(R.id.btn_earn_point, 24);
        sViewsWithIds.put(R.id.versioncode, 25);
        sViewsWithIds.put(R.id.spin_kit, 26);
    }
    // views
    @NonNull
    private final android.widget.FrameLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentNavigationBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 27, sIncludes, sViewsWithIds));
    }
    private FragmentNavigationBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.LinearLayout) bindings[21]
            , (android.widget.LinearLayout) bindings[12]
            , (android.widget.LinearLayout) bindings[18]
            , (android.widget.LinearLayout) bindings[10]
            , (android.widget.ImageView) bindings[6]
            , (android.widget.Button) bindings[24]
            , (android.widget.LinearLayout) bindings[16]
            , (android.widget.LinearLayout) bindings[17]
            , (android.widget.LinearLayout) bindings[14]
            , (android.widget.LinearLayout) bindings[1]
            , (android.widget.LinearLayout) bindings[22]
            , (android.widget.TextView) bindings[3]
            , (android.widget.LinearLayout) bindings[8]
            , (android.widget.LinearLayout) bindings[20]
            , (android.widget.LinearLayout) bindings[7]
            , (android.widget.LinearLayout) bindings[23]
            , (com.github.ybq.android.spinkit.SpinKitView) bindings[26]
            , (android.widget.LinearLayout) bindings[13]
            , (android.widget.LinearLayout) bindings[11]
            , (android.widget.LinearLayout) bindings[19]
            , (android.widget.LinearLayout) bindings[9]
            , (android.widget.TextView) bindings[5]
            , (android.widget.TextView) bindings[4]
            , (de.hdodenhof.circleimageview.CircleImageView) bindings[2]
            , (android.widget.TextView) bindings[25]
            , (android.widget.LinearLayout) bindings[15]
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