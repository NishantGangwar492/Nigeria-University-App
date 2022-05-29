package com.iotait.schoolapp.databinding;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FragmentMobileRechargeBindingImpl extends FragmentMobileRechargeBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = new androidx.databinding.ViewDataBinding.IncludedLayouts(32);
        sIncludes.setIncludes(0, 
            new String[] {"toolbar_layout"},
            new int[] {1},
            new int[] {com.iotait.schoolapp.R.layout.toolbar_layout});
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.nestedScrollView2, 2);
        sViewsWithIds.put(R.id.txt_banner, 3);
        sViewsWithIds.put(R.id.cardView, 4);
        sViewsWithIds.put(R.id.btn_submit, 5);
        sViewsWithIds.put(R.id.txt_close, 6);
        sViewsWithIds.put(R.id.edit_full_name, 7);
        sViewsWithIds.put(R.id.edit_pin1, 8);
        sViewsWithIds.put(R.id.edit_pin2, 9);
        sViewsWithIds.put(R.id.edit_pin3, 10);
        sViewsWithIds.put(R.id.edit_pin4, 11);
        sViewsWithIds.put(R.id.edit_pin5, 12);
        sViewsWithIds.put(R.id.edit_pin6, 13);
        sViewsWithIds.put(R.id.edit_pin7, 14);
        sViewsWithIds.put(R.id.edit_pin8, 15);
        sViewsWithIds.put(R.id.edit_pin9, 16);
        sViewsWithIds.put(R.id.edit_pin10, 17);
        sViewsWithIds.put(R.id.edit_total_amount, 18);
        sViewsWithIds.put(R.id.edit_recharge_card_type, 19);
        sViewsWithIds.put(R.id.edit_mtn, 20);
        sViewsWithIds.put(R.id.view1, 21);
        sViewsWithIds.put(R.id.view2, 22);
        sViewsWithIds.put(R.id.view3, 23);
        sViewsWithIds.put(R.id.view4, 24);
        sViewsWithIds.put(R.id.view5, 25);
        sViewsWithIds.put(R.id.view6, 26);
        sViewsWithIds.put(R.id.view7, 27);
        sViewsWithIds.put(R.id.view8, 28);
        sViewsWithIds.put(R.id.view9, 29);
        sViewsWithIds.put(R.id.view10, 30);
        sViewsWithIds.put(R.id.spin_kit, 31);
    }
    // views
    @NonNull
    private final androidx.constraintlayout.widget.ConstraintLayout mboundView0;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FragmentMobileRechargeBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 32, sIncludes, sViewsWithIds));
    }
    private FragmentMobileRechargeBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 1
            , (android.widget.Button) bindings[5]
            , (androidx.cardview.widget.CardView) bindings[4]
            , (android.widget.EditText) bindings[7]
            , (android.widget.TextView) bindings[20]
            , (android.widget.EditText) bindings[8]
            , (android.widget.EditText) bindings[17]
            , (android.widget.EditText) bindings[9]
            , (android.widget.EditText) bindings[10]
            , (android.widget.EditText) bindings[11]
            , (android.widget.EditText) bindings[12]
            , (android.widget.EditText) bindings[13]
            , (android.widget.EditText) bindings[14]
            , (android.widget.EditText) bindings[15]
            , (android.widget.EditText) bindings[16]
            , (android.widget.TextView) bindings[19]
            , (android.widget.TextView) bindings[18]
            , (com.iotait.schoolapp.databinding.ToolbarLayoutBinding) bindings[1]
            , (androidx.core.widget.NestedScrollView) bindings[2]
            , (com.github.ybq.android.spinkit.SpinKitView) bindings[31]
            , (android.widget.TextView) bindings[3]
            , (android.widget.TextView) bindings[6]
            , (android.view.View) bindings[21]
            , (android.view.View) bindings[30]
            , (android.view.View) bindings[22]
            , (android.view.View) bindings[23]
            , (android.view.View) bindings[24]
            , (android.view.View) bindings[25]
            , (android.view.View) bindings[26]
            , (android.view.View) bindings[27]
            , (android.view.View) bindings[28]
            , (android.view.View) bindings[29]
            );
        setContainedBinding(this.includeToolbar);
        this.mboundView0 = (androidx.constraintlayout.widget.ConstraintLayout) bindings[0];
        this.mboundView0.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
        }
        includeToolbar.invalidateAll();
        requestRebind();
    }

    @Override
    public boolean hasPendingBindings() {
        synchronized(this) {
            if (mDirtyFlags != 0) {
                return true;
            }
        }
        if (includeToolbar.hasPendingBindings()) {
            return true;
        }
        return false;
    }

    @Override
    public boolean setVariable(int variableId, @Nullable Object variable)  {
        boolean variableSet = true;
            return variableSet;
    }

    @Override
    public void setLifecycleOwner(@Nullable androidx.lifecycle.LifecycleOwner lifecycleOwner) {
        super.setLifecycleOwner(lifecycleOwner);
        includeToolbar.setLifecycleOwner(lifecycleOwner);
    }

    @Override
    protected boolean onFieldChange(int localFieldId, Object object, int fieldId) {
        switch (localFieldId) {
            case 0 :
                return onChangeIncludeToolbar((com.iotait.schoolapp.databinding.ToolbarLayoutBinding) object, fieldId);
        }
        return false;
    }
    private boolean onChangeIncludeToolbar(com.iotait.schoolapp.databinding.ToolbarLayoutBinding IncludeToolbar, int fieldId) {
        if (fieldId == BR._all) {
            synchronized(this) {
                    mDirtyFlags |= 0x1L;
            }
            return true;
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
        executeBindingsOn(includeToolbar);
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): includeToolbar
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}