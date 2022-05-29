package com.iotait.schoolapp.databinding;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class DialogResultBindingImpl extends DialogResultBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.frame_image, 1);
        sViewsWithIds.put(R.id.linearLayout6, 2);
        sViewsWithIds.put(R.id.linearLayout7, 3);
        sViewsWithIds.put(R.id.txt_score, 4);
        sViewsWithIds.put(R.id.res_out_of, 5);
        sViewsWithIds.put(R.id.lin, 6);
        sViewsWithIds.put(R.id.exam_title, 7);
        sViewsWithIds.put(R.id.subjectlist, 8);
        sViewsWithIds.put(R.id.spenttime, 9);
        sViewsWithIds.put(R.id.score, 10);
        sViewsWithIds.put(R.id.get_unn_app_txt, 11);
        sViewsWithIds.put(R.id.result_ok, 12);
        sViewsWithIds.put(R.id.ok_result_dialog, 13);
        sViewsWithIds.put(R.id.resultdismiss, 14);
        sViewsWithIds.put(R.id.result_dialog_dismiss, 15);
        sViewsWithIds.put(R.id.result_dialog_share, 16);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public DialogResultBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 17, sIncludes, sViewsWithIds));
    }
    private DialogResultBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.TextView) bindings[7]
            , (android.widget.ImageView) bindings[1]
            , (android.widget.TextView) bindings[11]
            , (android.widget.LinearLayout) bindings[6]
            , (android.widget.LinearLayout) bindings[2]
            , (android.widget.LinearLayout) bindings[3]
            , (android.widget.Button) bindings[13]
            , (android.widget.TextView) bindings[5]
            , (android.widget.Button) bindings[15]
            , (android.widget.Button) bindings[16]
            , (com.balysv.materialripple.MaterialRippleLayout) bindings[12]
            , (com.balysv.materialripple.MaterialRippleLayout) bindings[14]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[0]
            , (android.widget.TextView) bindings[10]
            , (android.widget.TextView) bindings[9]
            , (android.widget.TextView) bindings[8]
            , (android.widget.TextView) bindings[4]
            );
        this.root.setTag(null);
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