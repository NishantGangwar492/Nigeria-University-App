package com.iotait.schoolapp.databinding;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class QuestionItemBindingImpl extends QuestionItemBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.question_no, 17);
        sViewsWithIds.put(R.id.scenario_text, 18);
        sViewsWithIds.put(R.id.linear_option, 19);
        sViewsWithIds.put(R.id.layout_option_a, 20);
        sViewsWithIds.put(R.id.layout_option_b, 21);
        sViewsWithIds.put(R.id.layout_option_c, 22);
        sViewsWithIds.put(R.id.layout_option_d, 23);
        sViewsWithIds.put(R.id.btn_show_answer, 24);
        sViewsWithIds.put(R.id.answer_layout, 25);
        sViewsWithIds.put(R.id.txt_ans_exp, 26);
    }
    // views
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    @NonNull
    private final android.widget.TextView mboundView11;
    @NonNull
    private final android.widget.TextView mboundView13;
    @NonNull
    private final android.widget.TextView mboundView14;
    @NonNull
    private final android.widget.TextView mboundView5;
    @NonNull
    private final android.widget.TextView mboundView7;
    @NonNull
    private final android.widget.TextView mboundView9;
    // variables
    // values
    // listeners
    // Inverse Binding Event Handlers

    public QuestionItemBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 27, sIncludes, sViewsWithIds));
    }
    private QuestionItemBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.LinearLayout) bindings[25]
            , (android.widget.Button) bindings[24]
            , (android.widget.ImageView) bindings[15]
            , (android.widget.ImageView) bindings[6]
            , (android.widget.ImageView) bindings[8]
            , (android.widget.ImageView) bindings[10]
            , (android.widget.ImageView) bindings[12]
            , (android.widget.ImageView) bindings[4]
            , (android.widget.ImageView) bindings[2]
            , (android.widget.LinearLayout) bindings[20]
            , (android.widget.LinearLayout) bindings[21]
            , (android.widget.LinearLayout) bindings[22]
            , (android.widget.LinearLayout) bindings[23]
            , (android.widget.LinearLayout) bindings[19]
            , (android.widget.TextView) bindings[17]
            , (android.widget.TextView) bindings[1]
            , (android.widget.TextView) bindings[18]
            , (android.widget.TextView) bindings[26]
            , (android.widget.TextView) bindings[16]
            , (android.widget.TextView) bindings[3]
            );
        this.imgAnswerExaplaination.setTag(null);
        this.imgOptionA.setTag(null);
        this.imgOptionB.setTag(null);
        this.imgOptionC.setTag(null);
        this.imgOptionD.setTag(null);
        this.imgQuestion.setTag(null);
        this.imgScenario.setTag(null);
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView11 = (android.widget.TextView) bindings[11];
        this.mboundView11.setTag(null);
        this.mboundView13 = (android.widget.TextView) bindings[13];
        this.mboundView13.setTag(null);
        this.mboundView14 = (android.widget.TextView) bindings[14];
        this.mboundView14.setTag(null);
        this.mboundView5 = (android.widget.TextView) bindings[5];
        this.mboundView5.setTag(null);
        this.mboundView7 = (android.widget.TextView) bindings[7];
        this.mboundView7.setTag(null);
        this.mboundView9 = (android.widget.TextView) bindings[9];
        this.mboundView9.setTag(null);
        this.questionReport.setTag(null);
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
        java.lang.String questionitemGetAnswer = null;
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
        java.lang.String questionitemGetQuestionId = null;
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
                    // read questionitem.getAnswer()
                    questionitemGetAnswer = questionitem.getAnswer();
                    // read questionitem.getOptionC()
                    questionitemGetOptionC = questionitem.getOptionC();
                    // read questionitem.getOptiond_image()
                    questionitemGetOptiondImage = questionitem.getOptiond_image();
                    // read questionitem.getQuestion()
                    questionitemGetQuestion = questionitem.getQuestion();
                    // read questionitem.getQuestionId()
                    questionitemGetQuestionId = questionitem.getQuestionId();
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
            this.imgOptionA.setVisibility(questionitemGetOptionaImageJavaLangObjectNullBooleanTrueQuestionitemGetOptionaImageEqualsJavaLangStringViewGONEViewVISIBLE);
            com.iotait.schoolapp.helper.UIHelper.setImage(this.imgOptionA, questionitemGetOptionaImage);
            this.imgOptionB.setVisibility(questionitemGetOptionbImageJavaLangObjectNullBooleanTrueQuestionitemGetOptionbImageEqualsJavaLangStringViewGONEViewVISIBLE);
            com.iotait.schoolapp.helper.UIHelper.setImage(this.imgOptionB, questionitemGetOptionbImage);
            this.imgOptionC.setVisibility(questionitemGetOptioncImageJavaLangObjectNullBooleanTrueQuestionitemGetOptioncImageEqualsJavaLangStringViewGONEViewVISIBLE);
            com.iotait.schoolapp.helper.UIHelper.setImage(this.imgOptionC, questionitemGetOptioncImage);
            this.imgOptionD.setVisibility(questionitemGetOptiondImageJavaLangObjectNullBooleanTrueQuestionitemGetOptiondImageEqualsJavaLangStringViewGONEViewVISIBLE);
            com.iotait.schoolapp.helper.UIHelper.setImage(this.imgOptionD, questionitemGetOptiondImage);
            this.imgQuestion.setVisibility(questionitemGetQuestionImageJavaLangObjectNullBooleanTrueQuestionitemGetQuestionImageEqualsJavaLangStringViewGONEViewVISIBLE);
            com.iotait.schoolapp.helper.UIHelper.setImage(this.imgQuestion, questionitemGetQuestionImage);
            com.iotait.schoolapp.helper.UIHelper.setImage(this.imgScenario, questionitemGetScenarioImage);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView11, questionitemGetOptionC);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView13, questionitemGetOptionD);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView14, questionitemGetAnswer);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView5, questionitemGetQuestion);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView7, questionitemGetOptionA);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView9, questionitemGetOptionB);
            com.iotait.schoolapp.helper.UIHelper.checkReportedData(this.questionReport, questionitemGetQuestionId);
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