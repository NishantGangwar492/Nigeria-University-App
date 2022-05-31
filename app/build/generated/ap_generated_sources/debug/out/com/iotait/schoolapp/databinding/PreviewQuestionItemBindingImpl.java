package com.iotait.schoolapp.databinding;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class PreviewQuestionItemBindingImpl extends PreviewQuestionItemBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.question_no, 15);
        sViewsWithIds.put(R.id.scenario_text, 16);
        sViewsWithIds.put(R.id.linear_option, 17);
        sViewsWithIds.put(R.id.layout_option_a, 18);
        sViewsWithIds.put(R.id.layout_option_b, 19);
        sViewsWithIds.put(R.id.layout_option_c, 20);
        sViewsWithIds.put(R.id.layout_option_d, 21);
        sViewsWithIds.put(R.id.answer_layout, 22);
        sViewsWithIds.put(R.id.txt_ans_exp, 23);
    }
    // views
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    @NonNull
    private final android.widget.TextView mboundView10;
    @NonNull
    private final android.widget.TextView mboundView12;
    @NonNull
    private final android.widget.TextView mboundView4;
    @NonNull
    private final android.widget.TextView mboundView6;
    @NonNull
    private final android.widget.TextView mboundView8;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public PreviewQuestionItemBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 24, sIncludes, sViewsWithIds));
    }
    private PreviewQuestionItemBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.LinearLayout) bindings[22]
            , (android.widget.ImageView) bindings[13]
            , (android.widget.ImageView) bindings[5]
            , (android.widget.ImageView) bindings[7]
            , (android.widget.ImageView) bindings[9]
            , (android.widget.ImageView) bindings[11]
            , (android.widget.ImageView) bindings[3]
            , (android.widget.ImageView) bindings[1]
            , (android.widget.LinearLayout) bindings[18]
            , (android.widget.LinearLayout) bindings[19]
            , (android.widget.LinearLayout) bindings[20]
            , (android.widget.LinearLayout) bindings[21]
            , (android.widget.LinearLayout) bindings[17]
            , (android.widget.TextView) bindings[15]
            , (android.widget.TextView) bindings[16]
            , (android.widget.TextView) bindings[23]
            , (android.widget.TextView) bindings[14]
            , (android.widget.TextView) bindings[2]
            );
        this.imgAnswerExaplaination.setTag(null);
        this.imgOptiona.setTag(null);
        this.imgOptionb.setTag(null);
        this.imgOptionc.setTag(null);
        this.imgOptiond.setTag(null);
        this.imgQuestion.setTag(null);
        this.imgScenario.setTag(null);
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView10 = (android.widget.TextView) bindings[10];
        this.mboundView10.setTag(null);
        this.mboundView12 = (android.widget.TextView) bindings[12];
        this.mboundView12.setTag(null);
        this.mboundView4 = (android.widget.TextView) bindings[4];
        this.mboundView4.setTag(null);
        this.mboundView6 = (android.widget.TextView) bindings[6];
        this.mboundView6.setTag(null);
        this.mboundView8 = (android.widget.TextView) bindings[8];
        this.mboundView8.setTag(null);
        this.txtAnsExpDetails.setTag(null);
        this.txtScenarioDetails.setTag(null);
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
        if (BR.questionitem == variableId) {
            setQuestionitem((com.iotait.schoolapp.ui.homepage.ui.question.details.models.QuestionItem) variable);
        }
        else {
            variableSet = false;
        }
            return variableSet;
    }

    public void setQuestionitem(@Nullable com.iotait.schoolapp.ui.homepage.ui.question.details.models.QuestionItem Questionitem) {
        this.mQuestionitem = Questionitem;
        synchronized(this) {
            mDirtyFlags |= 0x1L;
        }
        notifyPropertyChanged(BR.questionitem);
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
        boolean questionitemGetOptionbImageJavaLangObjectNull = false;
        boolean questionitemGetOptiondImageJavaLangObjectNullBooleanTrueQuestionitemGetOptiondImageEqualsJavaLangString = false;
        boolean questionitemGetOptiondImageJavaLangObjectNull = false;
        boolean questionitemGetOptiondImageEqualsJavaLangString = false;
        com.iotait.schoolapp.ui.homepage.ui.question.details.models.QuestionItem questionitem = mQuestionitem;
        java.lang.String questionitemGetOptionC = null;
        int questionitemGetOptionbImageJavaLangObjectNullBooleanTrueQuestionitemGetOptionbImageEqualsJavaLangStringViewGONEViewVISIBLE = 0;
        boolean questionitemGetOptioncImageEqualsJavaLangString = false;
        boolean questionitemGetOptionaImageJavaLangObjectNull = false;
        java.lang.String questionitemGetOptiondImage = null;
        java.lang.String questionitemGetQuestion = null;
        boolean questionitemGetQuestionImageJavaLangObjectNull = false;
        int questionitemGetOptionaImageJavaLangObjectNullBooleanTrueQuestionitemGetOptionaImageEqualsJavaLangStringViewGONEViewVISIBLE = 0;
        java.lang.String questionitemGetOptionB = null;
        java.lang.String questionitemGetOptioncImage = null;
        java.lang.String questionitemGetExplainImage = null;
        boolean questionitemGetOptionbImageEqualsJavaLangString = false;
        boolean questionitemGetOptioncImageJavaLangObjectNull = false;
        int questionitemGetOptioncImageJavaLangObjectNullBooleanTrueQuestionitemGetOptioncImageEqualsJavaLangStringViewGONEViewVISIBLE = 0;
        java.lang.String questionitemGetScenarioImage = null;
        java.lang.String questionitemGetQuestionImage = null;
        boolean questionitemGetQuestionImageJavaLangObjectNullBooleanTrueQuestionitemGetQuestionImageEqualsJavaLangString = false;
        java.lang.String questionitemGetExplaination = null;
        java.lang.String questionitemGetOptionA = null;
        boolean questionitemGetOptioncImageJavaLangObjectNullBooleanTrueQuestionitemGetOptioncImageEqualsJavaLangString = false;
        int questionitemGetQuestionImageJavaLangObjectNullBooleanTrueQuestionitemGetQuestionImageEqualsJavaLangStringViewGONEViewVISIBLE = 0;
        boolean questionitemGetOptionaImageJavaLangObjectNullBooleanTrueQuestionitemGetOptionaImageEqualsJavaLangString = false;
        boolean questionitemGetOptionaImageEqualsJavaLangString = false;
        java.lang.String questionitemGetOptionbImage = null;
        boolean questionitemGetOptionbImageJavaLangObjectNullBooleanTrueQuestionitemGetOptionbImageEqualsJavaLangString = false;
        int questionitemGetOptiondImageJavaLangObjectNullBooleanTrueQuestionitemGetOptiondImageEqualsJavaLangStringViewGONEViewVISIBLE = 0;
        java.lang.String questionitemGetScenario = null;
        java.lang.String questionitemGetOptionD = null;
        java.lang.String questionitemGetOptionaImage = null;
        boolean questionitemGetQuestionImageEqualsJavaLangString = false;

        if ((dirtyFlags & 0x3L) != 0) {



                if (questionitem != null) {
                    // read questionitem.getOptionC()
                    questionitemGetOptionC = questionitem.getOptionC();
                    // read questionitem.getOptiond_image()
                    questionitemGetOptiondImage = questionitem.getOptiond_image();
                    // read questionitem.getQuestion()
                    questionitemGetQuestion = questionitem.getQuestion();
                    // read questionitem.getOptionB()
                    questionitemGetOptionB = questionitem.getOptionB();
                    // read questionitem.getOptionc_image()
                    questionitemGetOptioncImage = questionitem.getOptionc_image();
                    // read questionitem.getExplain_image()
                    questionitemGetExplainImage = questionitem.getExplain_image();
                    // read questionitem.getScenario_image()
                    questionitemGetScenarioImage = questionitem.getScenario_image();
                    // read questionitem.getQuestion_image()
                    questionitemGetQuestionImage = questionitem.getQuestion_image();
                    // read questionitem.getExplaination()
                    questionitemGetExplaination = questionitem.getExplaination();
                    // read questionitem.getOptionA()
                    questionitemGetOptionA = questionitem.getOptionA();
                    // read questionitem.getOptionb_image()
                    questionitemGetOptionbImage = questionitem.getOptionb_image();
                    // read questionitem.getScenario()
                    questionitemGetScenario = questionitem.getScenario();
                    // read questionitem.getOptionD()
                    questionitemGetOptionD = questionitem.getOptionD();
                    // read questionitem.getOptiona_image()
                    questionitemGetOptionaImage = questionitem.getOptiona_image();
                }


                // read questionitem.getOptiond_image() == null
                questionitemGetOptiondImageJavaLangObjectNull = (questionitemGetOptiondImage) == (null);
                // read questionitem.getOptionc_image() == null
                questionitemGetOptioncImageJavaLangObjectNull = (questionitemGetOptioncImage) == (null);
                // read questionitem.getQuestion_image() == null
                questionitemGetQuestionImageJavaLangObjectNull = (questionitemGetQuestionImage) == (null);
                // read questionitem.getOptionb_image() == null
                questionitemGetOptionbImageJavaLangObjectNull = (questionitemGetOptionbImage) == (null);
                // read questionitem.getOptiona_image() == null
                questionitemGetOptionaImageJavaLangObjectNull = (questionitemGetOptionaImage) == (null);
            if((dirtyFlags & 0x3L) != 0) {
                if(questionitemGetOptiondImageJavaLangObjectNull) {
                        dirtyFlags |= 0x8L;
                }
                else {
                        dirtyFlags |= 0x4L;
                }
            }
            if((dirtyFlags & 0x3L) != 0) {
                if(questionitemGetOptioncImageJavaLangObjectNull) {
                        dirtyFlags |= 0x2000L;
                }
                else {
                        dirtyFlags |= 0x1000L;
                }
            }
            if((dirtyFlags & 0x3L) != 0) {
                if(questionitemGetQuestionImageJavaLangObjectNull) {
                        dirtyFlags |= 0x800L;
                }
                else {
                        dirtyFlags |= 0x400L;
                }
            }
            if((dirtyFlags & 0x3L) != 0) {
                if(questionitemGetOptionbImageJavaLangObjectNull) {
                        dirtyFlags |= 0x80000L;
                }
                else {
                        dirtyFlags |= 0x40000L;
                }
            }
            if((dirtyFlags & 0x3L) != 0) {
                if(questionitemGetOptionaImageJavaLangObjectNull) {
                        dirtyFlags |= 0x20000L;
                }
                else {
                        dirtyFlags |= 0x10000L;
                }
            }
        }
        // batch finished

        if ((dirtyFlags & 0x4L) != 0) {

                if (questionitemGetOptiondImage != null) {
                    // read questionitem.getOptiond_image().equals("")
                    questionitemGetOptiondImageEqualsJavaLangString = questionitemGetOptiondImage.equals("");
                }
        }
        if ((dirtyFlags & 0x1000L) != 0) {

                if (questionitemGetOptioncImage != null) {
                    // read questionitem.getOptionc_image().equals("")
                    questionitemGetOptioncImageEqualsJavaLangString = questionitemGetOptioncImage.equals("");
                }
        }
        if ((dirtyFlags & 0x40000L) != 0) {

                if (questionitemGetOptionbImage != null) {
                    // read questionitem.getOptionb_image().equals("")
                    questionitemGetOptionbImageEqualsJavaLangString = questionitemGetOptionbImage.equals("");
                }
        }
        if ((dirtyFlags & 0x10000L) != 0) {

                if (questionitemGetOptionaImage != null) {
                    // read questionitem.getOptiona_image().equals("")
                    questionitemGetOptionaImageEqualsJavaLangString = questionitemGetOptionaImage.equals("");
                }
        }
        if ((dirtyFlags & 0x400L) != 0) {

                if (questionitemGetQuestionImage != null) {
                    // read questionitem.getQuestion_image().equals("")
                    questionitemGetQuestionImageEqualsJavaLangString = questionitemGetQuestionImage.equals("");
                }
        }

        if ((dirtyFlags & 0x3L) != 0) {

                // read questionitem.getOptiond_image() == null ? true : questionitem.getOptiond_image().equals("")
                questionitemGetOptiondImageJavaLangObjectNullBooleanTrueQuestionitemGetOptiondImageEqualsJavaLangString = ((questionitemGetOptiondImageJavaLangObjectNull) ? (true) : (questionitemGetOptiondImageEqualsJavaLangString));
                // read questionitem.getQuestion_image() == null ? true : questionitem.getQuestion_image().equals("")
                questionitemGetQuestionImageJavaLangObjectNullBooleanTrueQuestionitemGetQuestionImageEqualsJavaLangString = ((questionitemGetQuestionImageJavaLangObjectNull) ? (true) : (questionitemGetQuestionImageEqualsJavaLangString));
                // read questionitem.getOptionc_image() == null ? true : questionitem.getOptionc_image().equals("")
                questionitemGetOptioncImageJavaLangObjectNullBooleanTrueQuestionitemGetOptioncImageEqualsJavaLangString = ((questionitemGetOptioncImageJavaLangObjectNull) ? (true) : (questionitemGetOptioncImageEqualsJavaLangString));
                // read questionitem.getOptiona_image() == null ? true : questionitem.getOptiona_image().equals("")
                questionitemGetOptionaImageJavaLangObjectNullBooleanTrueQuestionitemGetOptionaImageEqualsJavaLangString = ((questionitemGetOptionaImageJavaLangObjectNull) ? (true) : (questionitemGetOptionaImageEqualsJavaLangString));
                // read questionitem.getOptionb_image() == null ? true : questionitem.getOptionb_image().equals("")
                questionitemGetOptionbImageJavaLangObjectNullBooleanTrueQuestionitemGetOptionbImageEqualsJavaLangString = ((questionitemGetOptionbImageJavaLangObjectNull) ? (true) : (questionitemGetOptionbImageEqualsJavaLangString));
            if((dirtyFlags & 0x3L) != 0) {
                if(questionitemGetOptiondImageJavaLangObjectNullBooleanTrueQuestionitemGetOptiondImageEqualsJavaLangString) {
                        dirtyFlags |= 0x200000L;
                }
                else {
                        dirtyFlags |= 0x100000L;
                }
            }
            if((dirtyFlags & 0x3L) != 0) {
                if(questionitemGetQuestionImageJavaLangObjectNullBooleanTrueQuestionitemGetQuestionImageEqualsJavaLangString) {
                        dirtyFlags |= 0x8000L;
                }
                else {
                        dirtyFlags |= 0x4000L;
                }
            }
            if((dirtyFlags & 0x3L) != 0) {
                if(questionitemGetOptioncImageJavaLangObjectNullBooleanTrueQuestionitemGetOptioncImageEqualsJavaLangString) {
                        dirtyFlags |= 0x200L;
                }
                else {
                        dirtyFlags |= 0x100L;
                }
            }
            if((dirtyFlags & 0x3L) != 0) {
                if(questionitemGetOptionaImageJavaLangObjectNullBooleanTrueQuestionitemGetOptionaImageEqualsJavaLangString) {
                        dirtyFlags |= 0x80L;
                }
                else {
                        dirtyFlags |= 0x40L;
                }
            }
            if((dirtyFlags & 0x3L) != 0) {
                if(questionitemGetOptionbImageJavaLangObjectNullBooleanTrueQuestionitemGetOptionbImageEqualsJavaLangString) {
                        dirtyFlags |= 0x20L;
                }
                else {
                        dirtyFlags |= 0x10L;
                }
            }


                // read questionitem.getOptiond_image() == null ? true : questionitem.getOptiond_image().equals("") ? View.GONE : View.VISIBLE
                questionitemGetOptiondImageJavaLangObjectNullBooleanTrueQuestionitemGetOptiondImageEqualsJavaLangStringViewGONEViewVISIBLE = ((questionitemGetOptiondImageJavaLangObjectNullBooleanTrueQuestionitemGetOptiondImageEqualsJavaLangString) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
                // read questionitem.getQuestion_image() == null ? true : questionitem.getQuestion_image().equals("") ? View.GONE : View.VISIBLE
                questionitemGetQuestionImageJavaLangObjectNullBooleanTrueQuestionitemGetQuestionImageEqualsJavaLangStringViewGONEViewVISIBLE = ((questionitemGetQuestionImageJavaLangObjectNullBooleanTrueQuestionitemGetQuestionImageEqualsJavaLangString) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
                // read questionitem.getOptionc_image() == null ? true : questionitem.getOptionc_image().equals("") ? View.GONE : View.VISIBLE
                questionitemGetOptioncImageJavaLangObjectNullBooleanTrueQuestionitemGetOptioncImageEqualsJavaLangStringViewGONEViewVISIBLE = ((questionitemGetOptioncImageJavaLangObjectNullBooleanTrueQuestionitemGetOptioncImageEqualsJavaLangString) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
                // read questionitem.getOptiona_image() == null ? true : questionitem.getOptiona_image().equals("") ? View.GONE : View.VISIBLE
                questionitemGetOptionaImageJavaLangObjectNullBooleanTrueQuestionitemGetOptionaImageEqualsJavaLangStringViewGONEViewVISIBLE = ((questionitemGetOptionaImageJavaLangObjectNullBooleanTrueQuestionitemGetOptionaImageEqualsJavaLangString) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
                // read questionitem.getOptionb_image() == null ? true : questionitem.getOptionb_image().equals("") ? View.GONE : View.VISIBLE
                questionitemGetOptionbImageJavaLangObjectNullBooleanTrueQuestionitemGetOptionbImageEqualsJavaLangStringViewGONEViewVISIBLE = ((questionitemGetOptionbImageJavaLangObjectNullBooleanTrueQuestionitemGetOptionbImageEqualsJavaLangString) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            com.iotait.schoolapp.helper.UIHelper.setImage(this.imgAnswerExaplaination, questionitemGetExplainImage);
            this.imgOptiona.setVisibility(questionitemGetOptionaImageJavaLangObjectNullBooleanTrueQuestionitemGetOptionaImageEqualsJavaLangStringViewGONEViewVISIBLE);
            com.iotait.schoolapp.helper.UIHelper.setImage(this.imgOptiona, questionitemGetOptionaImage);
            this.imgOptionb.setVisibility(questionitemGetOptionbImageJavaLangObjectNullBooleanTrueQuestionitemGetOptionbImageEqualsJavaLangStringViewGONEViewVISIBLE);
            com.iotait.schoolapp.helper.UIHelper.setImage(this.imgOptionb, questionitemGetOptionbImage);
            this.imgOptionc.setVisibility(questionitemGetOptioncImageJavaLangObjectNullBooleanTrueQuestionitemGetOptioncImageEqualsJavaLangStringViewGONEViewVISIBLE);
            com.iotait.schoolapp.helper.UIHelper.setImage(this.imgOptionc, questionitemGetOptioncImage);
            this.imgOptiond.setVisibility(questionitemGetOptiondImageJavaLangObjectNullBooleanTrueQuestionitemGetOptiondImageEqualsJavaLangStringViewGONEViewVISIBLE);
            com.iotait.schoolapp.helper.UIHelper.setImage(this.imgOptiond, questionitemGetOptiondImage);
            this.imgQuestion.setVisibility(questionitemGetQuestionImageJavaLangObjectNullBooleanTrueQuestionitemGetQuestionImageEqualsJavaLangStringViewGONEViewVISIBLE);
            com.iotait.schoolapp.helper.UIHelper.setImage(this.imgQuestion, questionitemGetQuestionImage);
            com.iotait.schoolapp.helper.UIHelper.setImage(this.imgScenario, questionitemGetScenarioImage);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView10, questionitemGetOptionC);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView12, questionitemGetOptionD);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView4, questionitemGetQuestion);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView6, questionitemGetOptionA);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView8, questionitemGetOptionB);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.txtAnsExpDetails, questionitemGetExplaination);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.txtScenarioDetails, questionitemGetScenario);
        }
    }
    // Listener Stub Implementations
    // callback impls
    // dirty flag
    private  long mDirtyFlags = 0xffffffffffffffffL;
    /* flag mapping
        flag 0 (0x1L): questionitem
        flag 1 (0x2L): null
        flag 2 (0x3L): questionitem.getOptiond_image() == null ? true : questionitem.getOptiond_image().equals("")
        flag 3 (0x4L): questionitem.getOptiond_image() == null ? true : questionitem.getOptiond_image().equals("")
        flag 4 (0x5L): questionitem.getOptionb_image() == null ? true : questionitem.getOptionb_image().equals("") ? View.GONE : View.VISIBLE
        flag 5 (0x6L): questionitem.getOptionb_image() == null ? true : questionitem.getOptionb_image().equals("") ? View.GONE : View.VISIBLE
        flag 6 (0x7L): questionitem.getOptiona_image() == null ? true : questionitem.getOptiona_image().equals("") ? View.GONE : View.VISIBLE
        flag 7 (0x8L): questionitem.getOptiona_image() == null ? true : questionitem.getOptiona_image().equals("") ? View.GONE : View.VISIBLE
        flag 8 (0x9L): questionitem.getOptionc_image() == null ? true : questionitem.getOptionc_image().equals("") ? View.GONE : View.VISIBLE
        flag 9 (0xaL): questionitem.getOptionc_image() == null ? true : questionitem.getOptionc_image().equals("") ? View.GONE : View.VISIBLE
        flag 10 (0xbL): questionitem.getQuestion_image() == null ? true : questionitem.getQuestion_image().equals("")
        flag 11 (0xcL): questionitem.getQuestion_image() == null ? true : questionitem.getQuestion_image().equals("")
        flag 12 (0xdL): questionitem.getOptionc_image() == null ? true : questionitem.getOptionc_image().equals("")
        flag 13 (0xeL): questionitem.getOptionc_image() == null ? true : questionitem.getOptionc_image().equals("")
        flag 14 (0xfL): questionitem.getQuestion_image() == null ? true : questionitem.getQuestion_image().equals("") ? View.GONE : View.VISIBLE
        flag 15 (0x10L): questionitem.getQuestion_image() == null ? true : questionitem.getQuestion_image().equals("") ? View.GONE : View.VISIBLE
        flag 16 (0x11L): questionitem.getOptiona_image() == null ? true : questionitem.getOptiona_image().equals("")
        flag 17 (0x12L): questionitem.getOptiona_image() == null ? true : questionitem.getOptiona_image().equals("")
        flag 18 (0x13L): questionitem.getOptionb_image() == null ? true : questionitem.getOptionb_image().equals("")
        flag 19 (0x14L): questionitem.getOptionb_image() == null ? true : questionitem.getOptionb_image().equals("")
        flag 20 (0x15L): questionitem.getOptiond_image() == null ? true : questionitem.getOptiond_image().equals("") ? View.GONE : View.VISIBLE
        flag 21 (0x16L): questionitem.getOptiond_image() == null ? true : questionitem.getOptiond_image().equals("") ? View.GONE : View.VISIBLE
    flag mapping end*/
    //end
}