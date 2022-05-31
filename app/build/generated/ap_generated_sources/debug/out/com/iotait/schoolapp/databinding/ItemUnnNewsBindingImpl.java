package com.iotait.schoolapp.databinding;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ItemUnnNewsBindingImpl extends ItemUnnNewsBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.textoverview, 3);
        sViewsWithIds.put(R.id.Read_More, 4);
    }
    // views
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    @NonNull
    private final android.widget.TextView mboundView1;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public ItemUnnNewsBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 5, sIncludes, sViewsWithIds));
    }
    private ItemUnnNewsBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.TextView) bindings[4]
            , (android.widget.TextView) bindings[2]
            , (android.widget.TextView) bindings[3]
            );
        this.dateLatestNews.setTag(null);
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView1 = (android.widget.TextView) bindings[1];
        this.mboundView1.setTag(null);
        setRootTag(root);
        // listeners
        invalidateAll();
    }

    @Override
    public void invalidateAll() {
        synchronized(this) {
                mDirtyFlags = 0x2L;
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
        if (BR.newsitem == variableId) {
            setNewsitem((com.iotait.schoolapp.ui.homepage.ui.home.model.Datum) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setNewsitem(@Nullable com.iotait.schoolapp.ui.homepage.ui.home.model.Datum Newsitem) {
        this.mNewsitem = Newsitem;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.newsitem);
        super.requestRebind();
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
        com.iotait.schoolapp.ui.homepage.ui.home.model.Datum newsitem = mNewsitem;
        java.lang.String newsitemGetPostDate = null;
        java.lang.String newsitemGetPostTitle = null;

        if ((dirtyFlags & 0x3L) != 0) {



                if (newsitem != null) {
                    // read newsitem.getPostDate()
                    newsitemGetPostDate = newsitem.getPostDate();
                    // read newsitem.getPostTitle()
                    newsitemGetPostTitle = newsitem.getPostTitle();
                }
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            com.iotait.schoolapp.helper.UIHelper.changeTimeFormat(this.dateLatestNews, newsitemGetPostDate);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView1, newsitemGetPostTitle);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): newsitem
        flag 1 (0x2L): null
    flag mapping end*/
    //end
}