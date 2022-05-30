package com.iotait.schoolapp.databinding;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ActivityLoginBindingImpl extends ActivityLoginBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.guideline, 1);
        sViewsWithIds.put(R.id.textView2, 2);
        sViewsWithIds.put(R.id.linearLayout, 3);
        sViewsWithIds.put(R.id.edit_email, 4);
        sViewsWithIds.put(R.id.edit_password, 5);
        sViewsWithIds.put(R.id.btn_login, 6);
        sViewsWithIds.put(R.id.txt_forgot_password, 7);
        sViewsWithIds.put(R.id.spin_kit, 8);
        sViewsWithIds.put(R.id.textView3, 9);
        sViewsWithIds.put(R.id.havving_issue_text, 10);
        sViewsWithIds.put(R.id.btn_open_whatsapp, 11);
        sViewsWithIds.put(R.id.linearLayout3, 12);
        sViewsWithIds.put(R.id.btn_google, 13);
        sViewsWithIds.put(R.id.btn_facebook, 14);
        sViewsWithIds.put(R.id.txt_dont_have_ac, 15);
        sViewsWithIds.put(R.id.spin_kitForGoogleAndFacebook, 16);
    }
    // views
    @NonNull
    private final androidx.core.widget.NestedScrollView mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ActivityLoginBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 17, sIncludes, sViewsWithIds));
    }
    private ActivityLoginBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (com.facebook.login.widget.LoginButton) bindings[14]
            , (android.widget.LinearLayout) bindings[13]
            , (android.widget.Button) bindings[6]
            , (android.widget.TextView) bindings[11]
            , (android.widget.EditText) bindings[4]
            , (android.widget.EditText) bindings[5]
            , (androidx.constraintlayout.widget.Guideline) bindings[1]
            , (android.widget.TextView) bindings[10]
            , (android.widget.LinearLayout) bindings[3]
            , (android.widget.LinearLayout) bindings[12]
            , (com.github.ybq.android.spinkit.SpinKitView) bindings[8]
            , (com.github.ybq.android.spinkit.SpinKitView) bindings[16]
            , (android.widget.TextView) bindings[2]
            , (android.widget.TextView) bindings[9]
            , (android.widget.LinearLayout) bindings[15]
            , (android.widget.TextView) bindings[7]
            );
        this.mboundView0 = (androidx.core.widget.NestedScrollView) bindings[0];
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