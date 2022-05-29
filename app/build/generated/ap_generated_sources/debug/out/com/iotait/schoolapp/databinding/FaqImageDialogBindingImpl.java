package com.iotait.schoolapp.databinding;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class FaqImageDialogBindingImpl extends FaqImageDialogBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.scrollViewfaq, 1);
        sViewsWithIds.put(R.id.lin2, 2);
        sViewsWithIds.put(R.id.faq_question_1, 3);
        sViewsWithIds.put(R.id.faq_answer_1, 4);
        sViewsWithIds.put(R.id.faq_question_2, 5);
        sViewsWithIds.put(R.id.faq_answer_2, 6);
        sViewsWithIds.put(R.id.faq_question_3, 7);
        sViewsWithIds.put(R.id.faq_answer_3, 8);
        sViewsWithIds.put(R.id.faq_question_4, 9);
        sViewsWithIds.put(R.id.faq_answer_4, 10);
        sViewsWithIds.put(R.id.result_ok, 11);
        sViewsWithIds.put(R.id.ok_result_dialog_faq, 12);
    }
    // views
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public FaqImageDialogBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 13, sIncludes, sViewsWithIds));
    }
    private FaqImageDialogBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.TextView) bindings[4]
            , (android.widget.TextView) bindings[6]
            , (android.widget.TextView) bindings[8]
            , (android.widget.TextView) bindings[10]
            , (android.widget.TextView) bindings[3]
            , (android.widget.TextView) bindings[5]
            , (android.widget.TextView) bindings[7]
            , (android.widget.TextView) bindings[9]
            , (android.widget.LinearLayout) bindings[2]
            , (android.widget.Button) bindings[12]
            , (com.balysv.materialripple.MaterialRippleLayout) bindings[11]
            , (androidx.constraintlayout.widget.ConstraintLayout) bindings[0]
            , (android.widget.ScrollView) bindings[1]
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