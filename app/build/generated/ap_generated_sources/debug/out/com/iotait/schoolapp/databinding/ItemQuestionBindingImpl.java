package com.iotait.schoolapp.databinding;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.BR;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.View;
@SuppressWarnings("unchecked")
public class ItemQuestionBindingImpl extends ItemQuestionBinding  {

    @Nullable
    private static final androidx.databinding.ViewDataBinding.IncludedLayouts sIncludes;
    @Nullable
    private static final android.util.SparseIntArray sViewsWithIds;
    static {
        sIncludes = null;
        sViewsWithIds = new android.util.SparseIntArray();
        sViewsWithIds.put(R.id.question_no, 14);
        sViewsWithIds.put(R.id.linear_option, 15);
        sViewsWithIds.put(R.id.layout_option_a, 16);
        sViewsWithIds.put(R.id.layout_option_b, 17);
        sViewsWithIds.put(R.id.layout_option_c, 18);
        sViewsWithIds.put(R.id.layout_option_d, 19);
    }
    // views
    @NonNull
    private final android.widget.LinearLayout mboundView0;
    @NonNull
    private final android.widget.TextView mboundView11;
    @NonNull
    private final android.widget.TextView mboundView13;
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

    public ItemQuestionBindingImpl(@Nullable androidx.databinding.DataBindingComponent bindingComponent, @NonNull View root) {
        this(bindingComponent, root, mapBindings(bindingComponent, root, 20, sIncludes, sViewsWithIds));
    }
    private ItemQuestionBindingImpl(androidx.databinding.DataBindingComponent bindingComponent, View root, Object[] bindings) {
        super(bindingComponent, root, 0
            , (android.widget.TextView) bindings[3]
            , (android.widget.ImageView) bindings[6]
            , (android.widget.ImageView) bindings[8]
            , (android.widget.ImageView) bindings[10]
            , (android.widget.ImageView) bindings[12]
            , (android.widget.ImageView) bindings[4]
            , (android.widget.ImageView) bindings[2]
            , (android.widget.LinearLayout) bindings[16]
            , (android.widget.LinearLayout) bindings[17]
            , (android.widget.LinearLayout) bindings[18]
            , (android.widget.LinearLayout) bindings[19]
            , (android.widget.LinearLayout) bindings[15]
            , (android.widget.TextView) bindings[14]
            , (android.widget.TextView) bindings[1]
            );
        this.equationviewScenario.setTag(null);
        this.imgOptiona.setTag(null);
        this.imgOptionb.setTag(null);
        this.imgOptionc.setTag(null);
        this.imgOptiond.setTag(null);
        this.imgQuestion.setTag(null);
        this.imgSchenario.setTag(null);
        this.mboundView0 = (android.widget.LinearLayout) bindings[0];
        this.mboundView0.setTag(null);
        this.mboundView11 = (android.widget.TextView) bindings[11];
        this.mboundView11.setTag(null);
        this.mboundView13 = (android.widget.TextView) bindings[13];
        this.mboundView13.setTag(null);
        this.mboundView5 = (android.widget.TextView) bindings[5];
        this.mboundView5.setTag(null);
        this.mboundView7 = (android.widget.TextView) bindings[7];
        this.mboundView7.setTag(null);
        this.mboundView9 = (android.widget.TextView) bindings[9];
        this.mboundView9.setTag(null);
        this.scenarioText.setTag(null);
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
        boolean questionitemGetScenarioImageJavaLangObjectNull = false;
        boolean questionitemGetOptionbImageJavaLangObjectNull = false;
        boolean questionitemGetOptiondImageJavaLangObjectNullBooleanTrueQuestionitemGetOptiondImageEqualsJavaLangString = false;
        boolean questionitemGetOptiondImageJavaLangObjectNull = false;
        boolean questionitemGetScenarioEqualsJavaLangString = false;
        int questionitemGetScenarioImageJavaLangObjectNullBooleanTrueQuestionitemGetScenarioImageEqualsJavaLangStringViewGONEViewVISIBLE = 0;
        boolean questionitemGetOptiondImageEqualsJavaLangString = false;
        com.iotait.schoolapp.ui.homepage.ui.question.details.models.QuestionItem questionitem = mQuestionitem;
        java.lang.String questionitemGetOptionC = null;
        int questionitemGetScenarioEqualsJavaLangStringViewGONEViewVISIBLE = 0;
        int questionitemGetOptionbImageJavaLangObjectNullBooleanTrueQuestionitemGetOptionbImageEqualsJavaLangStringViewGONEViewVISIBLE = 0;
        boolean questionitemGetOptioncImageEqualsJavaLangString = false;
        boolean questionitemGetScenarioEqualsJavaLangStringQuestionitemGetScenarioImageJavaLangObjectNullBooleanFalse = false;
        boolean questionitemGetOptionaImageJavaLangObjectNull = false;
        java.lang.String questionitemGetOptiondImage = null;
        java.lang.String questionitemGetQuestion = null;
        boolean questionitemGetQuestionImageJavaLangObjectNull = false;
        int questionitemGetOptionaImageJavaLangObjectNullBooleanTrueQuestionitemGetOptionaImageEqualsJavaLangStringViewGONEViewVISIBLE = 0;
        java.lang.String questionitemGetOptionB = null;
        java.lang.String questionitemGetOptioncImage = null;
        boolean questionitemGetOptionbImageEqualsJavaLangString = false;
        boolean questionitemGetScenarioEqualsJavaLangStringQuestionitemGetScenarioImageJavaLangObjectNullBooleanFalseBooleanTrueQuestionitemGetScenarioImageEqualsJavaLangString = false;
        boolean questionitemGetOptioncImageJavaLangObjectNull = false;
        int questionitemGetOptioncImageJavaLangObjectNullBooleanTrueQuestionitemGetOptioncImageEqualsJavaLangStringViewGONEViewVISIBLE = 0;
        java.lang.String questionitemGetScenarioImage = null;
        java.lang.String questionitemGetQuestionImage = null;
        int questionitemGetScenarioEqualsJavaLangStringQuestionitemGetScenarioImageJavaLangObjectNullBooleanFalseBooleanTrueQuestionitemGetScenarioImageEqualsJavaLangStringViewGONEViewVISIBLE = 0;
        boolean questionitemGetQuestionImageJavaLangObjectNullBooleanTrueQuestionitemGetQuestionImageEqualsJavaLangString = false;
        java.lang.String questionitemGetOptionA = null;
        boolean questionitemGetOptioncImageJavaLangObjectNullBooleanTrueQuestionitemGetOptioncImageEqualsJavaLangString = false;
        int questionitemGetQuestionImageJavaLangObjectNullBooleanTrueQuestionitemGetQuestionImageEqualsJavaLangStringViewGONEViewVISIBLE = 0;
        boolean questionitemGetOptionaImageJavaLangObjectNullBooleanTrueQuestionitemGetOptionaImageEqualsJavaLangString = false;
        boolean questionitemGetOptionaImageEqualsJavaLangString = false;
        java.lang.String questionitemGetOptionbImage = null;
        boolean questionitemGetScenarioImageJavaLangObjectNullBooleanTrueQuestionitemGetScenarioImageEqualsJavaLangString = false;
        boolean questionitemGetOptionbImageJavaLangObjectNullBooleanTrueQuestionitemGetOptionbImageEqualsJavaLangString = false;
        int questionitemGetOptiondImageJavaLangObjectNullBooleanTrueQuestionitemGetOptiondImageEqualsJavaLangStringViewGONEViewVISIBLE = 0;
        java.lang.String questionitemGetScenario = null;
        boolean questionitemGetScenarioImageEqualsJavaLangString = false;
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
                    // read questionitem.getScenario_image()
                    questionitemGetScenarioImage = questionitem.getScenario_image();
                    // read questionitem.getQuestion_image()
                    questionitemGetQuestionImage = questionitem.getQuestion_image();
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
                // read questionitem.getScenario_image() == null
                questionitemGetScenarioImageJavaLangObjectNull = (questionitemGetScenarioImage) == (null);
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
                        dirtyFlags |= 0x800000L;
                }
                else {
                        dirtyFlags |= 0x400000L;
                }
            }
            if((dirtyFlags & 0x3L) != 0) {
                if(questionitemGetScenarioImageJavaLangObjectNull) {
                        dirtyFlags |= 0x20000000L;
                }
                else {
                        dirtyFlags |= 0x10000000L;
                }
            }
            if((dirtyFlags & 0x3L) != 0) {
                if(questionitemGetQuestionImageJavaLangObjectNull) {
                        dirtyFlags |= 0x200000L;
                }
                else {
                        dirtyFlags |= 0x100000L;
                }
            }
            if((dirtyFlags & 0x3L) != 0) {
                if(questionitemGetOptionbImageJavaLangObjectNull) {
                        dirtyFlags |= 0x80000000L;
                }
                else {
                        dirtyFlags |= 0x40000000L;
                }
            }
            if((dirtyFlags & 0x3L) != 0) {
                if(questionitemGetOptionaImageJavaLangObjectNull) {
                        dirtyFlags |= 0x8000000L;
                }
                else {
                        dirtyFlags |= 0x4000000L;
                }
            }
                if (questionitemGetScenario != null) {
                    // read questionitem.getScenario().equals("")
                    questionitemGetScenarioEqualsJavaLangString = questionitemGetScenario.equals("");
                }
            if((dirtyFlags & 0x3L) != 0) {
                if(questionitemGetScenarioEqualsJavaLangString) {
                        dirtyFlags |= 0x80L;
                        dirtyFlags |= 0x800L;
                }
                else {
                        dirtyFlags |= 0x40L;
                        dirtyFlags |= 0x400L;
                }
            }


                // read questionitem.getScenario().equals("") ? View.GONE : View.VISIBLE
                questionitemGetScenarioEqualsJavaLangStringViewGONEViewVISIBLE = ((questionitemGetScenarioEqualsJavaLangString) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
        }
        // batch finished

        if ((dirtyFlags & 0x4L) != 0) {

                if (questionitemGetOptiondImage != null) {
                    // read questionitem.getOptiond_image().equals("")
                    questionitemGetOptiondImageEqualsJavaLangString = questionitemGetOptiondImage.equals("");
                }
        }
        if ((dirtyFlags & 0x400000L) != 0) {

                if (questionitemGetOptioncImage != null) {
                    // read questionitem.getOptionc_image().equals("")
                    questionitemGetOptioncImageEqualsJavaLangString = questionitemGetOptioncImage.equals("");
                }
        }
        if ((dirtyFlags & 0x3L) != 0) {

                // read questionitem.getScenario().equals("") ? questionitem.getScenario_image() == null : false
                questionitemGetScenarioEqualsJavaLangStringQuestionitemGetScenarioImageJavaLangObjectNullBooleanFalse = ((questionitemGetScenarioEqualsJavaLangString) ? (questionitemGetScenarioImageJavaLangObjectNull) : (false));
            if((dirtyFlags & 0x3L) != 0) {
                if(questionitemGetScenarioEqualsJavaLangStringQuestionitemGetScenarioImageJavaLangObjectNullBooleanFalse) {
                        dirtyFlags |= 0x8000L;
                }
                else {
                        dirtyFlags |= 0x4000L;
                }
            }
        }
        if ((dirtyFlags & 0x40000000L) != 0) {

                if (questionitemGetOptionbImage != null) {
                    // read questionitem.getOptionb_image().equals("")
                    questionitemGetOptionbImageEqualsJavaLangString = questionitemGetOptionbImage.equals("");
                }
        }
        if ((dirtyFlags & 0x4000000L) != 0) {

                if (questionitemGetOptionaImage != null) {
                    // read questionitem.getOptiona_image().equals("")
                    questionitemGetOptionaImageEqualsJavaLangString = questionitemGetOptionaImage.equals("");
                }
        }
        if ((dirtyFlags & 0x10000000L) != 0) {

                if (questionitemGetScenarioImage != null) {
                    // read questionitem.getScenario_image().equals("")
                    questionitemGetScenarioImageEqualsJavaLangString = questionitemGetScenarioImage.equals("");
                }
        }
        if ((dirtyFlags & 0x100000L) != 0) {

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
                // read questionitem.getScenario_image() == null ? true : questionitem.getScenario_image().equals("")
                questionitemGetScenarioImageJavaLangObjectNullBooleanTrueQuestionitemGetScenarioImageEqualsJavaLangString = ((questionitemGetScenarioImageJavaLangObjectNull) ? (true) : (questionitemGetScenarioImageEqualsJavaLangString));
                // read questionitem.getOptionb_image() == null ? true : questionitem.getOptionb_image().equals("")
                questionitemGetOptionbImageJavaLangObjectNullBooleanTrueQuestionitemGetOptionbImageEqualsJavaLangString = ((questionitemGetOptionbImageJavaLangObjectNull) ? (true) : (questionitemGetOptionbImageEqualsJavaLangString));
            if((dirtyFlags & 0x3L) != 0) {
                if(questionitemGetOptiondImageJavaLangObjectNullBooleanTrueQuestionitemGetOptiondImageEqualsJavaLangString) {
                        dirtyFlags |= 0x200000000L;
                }
                else {
                        dirtyFlags |= 0x100000000L;
                }
            }
            if((dirtyFlags & 0x3L) != 0) {
                if(questionitemGetQuestionImageJavaLangObjectNullBooleanTrueQuestionitemGetQuestionImageEqualsJavaLangString) {
                        dirtyFlags |= 0x2000000L;
                }
                else {
                        dirtyFlags |= 0x1000000L;
                }
            }
            if((dirtyFlags & 0x3L) != 0) {
                if(questionitemGetOptioncImageJavaLangObjectNullBooleanTrueQuestionitemGetOptioncImageEqualsJavaLangString) {
                        dirtyFlags |= 0x20000L;
                }
                else {
                        dirtyFlags |= 0x10000L;
                }
            }
            if((dirtyFlags & 0x3L) != 0) {
                if(questionitemGetOptionaImageJavaLangObjectNullBooleanTrueQuestionitemGetOptionaImageEqualsJavaLangString) {
                        dirtyFlags |= 0x2000L;
                }
                else {
                        dirtyFlags |= 0x1000L;
                }
            }
            if((dirtyFlags & 0x3L) != 0) {
                if(questionitemGetScenarioImageJavaLangObjectNullBooleanTrueQuestionitemGetScenarioImageEqualsJavaLangString) {
                        dirtyFlags |= 0x20L;
                }
                else {
                        dirtyFlags |= 0x10L;
                }
            }
            if((dirtyFlags & 0x3L) != 0) {
                if(questionitemGetOptionbImageJavaLangObjectNullBooleanTrueQuestionitemGetOptionbImageEqualsJavaLangString) {
                        dirtyFlags |= 0x200L;
                }
                else {
                        dirtyFlags |= 0x100L;
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
                // read questionitem.getScenario_image() == null ? true : questionitem.getScenario_image().equals("") ? View.GONE : View.VISIBLE
                questionitemGetScenarioImageJavaLangObjectNullBooleanTrueQuestionitemGetScenarioImageEqualsJavaLangStringViewGONEViewVISIBLE = ((questionitemGetScenarioImageJavaLangObjectNullBooleanTrueQuestionitemGetScenarioImageEqualsJavaLangString) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
                // read questionitem.getOptionb_image() == null ? true : questionitem.getOptionb_image().equals("") ? View.GONE : View.VISIBLE
                questionitemGetOptionbImageJavaLangObjectNullBooleanTrueQuestionitemGetOptionbImageEqualsJavaLangStringViewGONEViewVISIBLE = ((questionitemGetOptionbImageJavaLangObjectNullBooleanTrueQuestionitemGetOptionbImageEqualsJavaLangString) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
        }
        // batch finished

        if ((dirtyFlags & 0x4000L) != 0) {

                if (questionitemGetScenarioImage != null) {
                    // read questionitem.getScenario_image().equals("")
                    questionitemGetScenarioImageEqualsJavaLangString = questionitemGetScenarioImage.equals("");
                }
        }

        if ((dirtyFlags & 0x3L) != 0) {

                // read questionitem.getScenario().equals("") ? questionitem.getScenario_image() == null : false ? true : questionitem.getScenario_image().equals("")
                questionitemGetScenarioEqualsJavaLangStringQuestionitemGetScenarioImageJavaLangObjectNullBooleanFalseBooleanTrueQuestionitemGetScenarioImageEqualsJavaLangString = ((questionitemGetScenarioEqualsJavaLangStringQuestionitemGetScenarioImageJavaLangObjectNullBooleanFalse) ? (true) : (questionitemGetScenarioImageEqualsJavaLangString));
            if((dirtyFlags & 0x3L) != 0) {
                if(questionitemGetScenarioEqualsJavaLangStringQuestionitemGetScenarioImageJavaLangObjectNullBooleanFalseBooleanTrueQuestionitemGetScenarioImageEqualsJavaLangString) {
                        dirtyFlags |= 0x80000L;
                }
                else {
                        dirtyFlags |= 0x40000L;
                }
            }


                // read questionitem.getScenario().equals("") ? questionitem.getScenario_image() == null : false ? true : questionitem.getScenario_image().equals("") ? View.GONE : View.VISIBLE
                questionitemGetScenarioEqualsJavaLangStringQuestionitemGetScenarioImageJavaLangObjectNullBooleanFalseBooleanTrueQuestionitemGetScenarioImageEqualsJavaLangStringViewGONEViewVISIBLE = ((questionitemGetScenarioEqualsJavaLangStringQuestionitemGetScenarioImageJavaLangObjectNullBooleanFalseBooleanTrueQuestionitemGetScenarioImageEqualsJavaLangString) ? (android.view.View.GONE) : (android.view.View.VISIBLE));
        }
        // batch finished
        if ((dirtyFlags & 0x3L) != 0) {
            // api target 1

            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.equationviewScenario, questionitemGetScenario);
            this.equationviewScenario.setVisibility(questionitemGetScenarioEqualsJavaLangStringViewGONEViewVISIBLE);
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
            this.imgSchenario.setVisibility(questionitemGetScenarioImageJavaLangObjectNullBooleanTrueQuestionitemGetScenarioImageEqualsJavaLangStringViewGONEViewVISIBLE);
            com.iotait.schoolapp.helper.UIHelper.setImage(this.imgSchenario, questionitemGetScenarioImage);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView11, questionitemGetOptionC);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView13, questionitemGetOptionD);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView5, questionitemGetQuestion);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView7, questionitemGetOptionA);
            androidx.databinding.adapters.TextViewBindingAdapter.setText(this.mboundView9, questionitemGetOptionB);
            this.scenarioText.setVisibility(questionitemGetScenarioEqualsJavaLangStringQuestionitemGetScenarioImageJavaLangObjectNullBooleanFalseBooleanTrueQuestionitemGetScenarioImageEqualsJavaLangStringViewGONEViewVISIBLE);
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
        flag 4 (0x5L): questionitem.getScenario_image() == null ? true : questionitem.getScenario_image().equals("") ? View.GONE : View.VISIBLE
        flag 5 (0x6L): questionitem.getScenario_image() == null ? true : questionitem.getScenario_image().equals("") ? View.GONE : View.VISIBLE
        flag 6 (0x7L): questionitem.getScenario().equals("") ? View.GONE : View.VISIBLE
        flag 7 (0x8L): questionitem.getScenario().equals("") ? View.GONE : View.VISIBLE
        flag 8 (0x9L): questionitem.getOptionb_image() == null ? true : questionitem.getOptionb_image().equals("") ? View.GONE : View.VISIBLE
        flag 9 (0xaL): questionitem.getOptionb_image() == null ? true : questionitem.getOptionb_image().equals("") ? View.GONE : View.VISIBLE
        flag 10 (0xbL): questionitem.getScenario().equals("") ? questionitem.getScenario_image() == null : false
        flag 11 (0xcL): questionitem.getScenario().equals("") ? questionitem.getScenario_image() == null : false
        flag 12 (0xdL): questionitem.getOptiona_image() == null ? true : questionitem.getOptiona_image().equals("") ? View.GONE : View.VISIBLE
        flag 13 (0xeL): questionitem.getOptiona_image() == null ? true : questionitem.getOptiona_image().equals("") ? View.GONE : View.VISIBLE
        flag 14 (0xfL): questionitem.getScenario().equals("") ? questionitem.getScenario_image() == null : false ? true : questionitem.getScenario_image().equals("")
        flag 15 (0x10L): questionitem.getScenario().equals("") ? questionitem.getScenario_image() == null : false ? true : questionitem.getScenario_image().equals("")
        flag 16 (0x11L): questionitem.getOptionc_image() == null ? true : questionitem.getOptionc_image().equals("") ? View.GONE : View.VISIBLE
        flag 17 (0x12L): questionitem.getOptionc_image() == null ? true : questionitem.getOptionc_image().equals("") ? View.GONE : View.VISIBLE
        flag 18 (0x13L): questionitem.getScenario().equals("") ? questionitem.getScenario_image() == null : false ? true : questionitem.getScenario_image().equals("") ? View.GONE : View.VISIBLE
        flag 19 (0x14L): questionitem.getScenario().equals("") ? questionitem.getScenario_image() == null : false ? true : questionitem.getScenario_image().equals("") ? View.GONE : View.VISIBLE
        flag 20 (0x15L): questionitem.getQuestion_image() == null ? true : questionitem.getQuestion_image().equals("")
        flag 21 (0x16L): questionitem.getQuestion_image() == null ? true : questionitem.getQuestion_image().equals("")
        flag 22 (0x17L): questionitem.getOptionc_image() == null ? true : questionitem.getOptionc_image().equals("")
        flag 23 (0x18L): questionitem.getOptionc_image() == null ? true : questionitem.getOptionc_image().equals("")
        flag 24 (0x19L): questionitem.getQuestion_image() == null ? true : questionitem.getQuestion_image().equals("") ? View.GONE : View.VISIBLE
        flag 25 (0x1aL): questionitem.getQuestion_image() == null ? true : questionitem.getQuestion_image().equals("") ? View.GONE : View.VISIBLE
        flag 26 (0x1bL): questionitem.getOptiona_image() == null ? true : questionitem.getOptiona_image().equals("")
        flag 27 (0x1cL): questionitem.getOptiona_image() == null ? true : questionitem.getOptiona_image().equals("")
        flag 28 (0x1dL): questionitem.getScenario_image() == null ? true : questionitem.getScenario_image().equals("")
        flag 29 (0x1eL): questionitem.getScenario_image() == null ? true : questionitem.getScenario_image().equals("")
        flag 30 (0x1fL): questionitem.getOptionb_image() == null ? true : questionitem.getOptionb_image().equals("")
        flag 31 (0x20L): questionitem.getOptionb_image() == null ? true : questionitem.getOptionb_image().equals("")
        flag 32 (0x21L): questionitem.getOptiond_image() == null ? true : questionitem.getOptiond_image().equals("") ? View.GONE : View.VISIBLE
        flag 33 (0x22L): questionitem.getOptiond_image() == null ? true : questionitem.getOptiond_image().equals("") ? View.GONE : View.VISIBLE
    flag mapping end*/
    //end
}