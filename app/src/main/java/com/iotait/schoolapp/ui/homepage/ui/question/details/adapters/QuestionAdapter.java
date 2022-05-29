package com.iotait.schoolapp.ui.homepage.ui.question.details.adapters;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.iotait.schoolapp.R;
import com.iotait.schoolapp.databinding.QuestionItemBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.listener.ImageClick;
import com.iotait.schoolapp.ui.homepage.ui.question.details.models.QuestionItem;
import com.iotait.schoolapp.ui.homepage.ui.question.view.AddQuestionView;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    private Context context;
    private List<QuestionItem> questionItems;
    private AddQuestionView addQuestionView;
    private ImageClick imageClick;

    public QuestionAdapter(Context context, List<QuestionItem> questionItems, AddQuestionView addQuestionView, ImageClick imageClick) {
        this.context = context;
        this.questionItems = questionItems;
        this.addQuestionView = addQuestionView;
        this.imageClick=imageClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        QuestionItemBinding questionItemBinding= DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.question_item,parent,false);
        return new ViewHolder(questionItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final QuestionItem questionItem = questionItems.get(position);
        holder.questionItemBinding.setQuestionitem(questionItem);
        holder.questionItemBinding.questionNo.setText("Question " + String.valueOf(position + 1));

        if (questionItem.getExplaination().equals("") && (questionItem.getExplain_image() == null || questionItem.getExplain_image().equals(""))) {
            holder.questionItemBinding.txtAnsExp.setVisibility(View.GONE);
            holder.questionItemBinding.txtAnsExpDetails.setVisibility(View.GONE);
            holder.questionItemBinding.imgAnswerExaplaination.setVisibility(View.GONE);
        } else {
            holder.questionItemBinding.txtAnsExp.setVisibility(View.VISIBLE);
            if (questionItem.getExplaination().equals("")) {
                holder.questionItemBinding.txtAnsExpDetails.setVisibility(View.GONE);
            } else {
                holder.questionItemBinding.txtAnsExpDetails.setVisibility(View.VISIBLE);
            }
            if (questionItem.getExplain_image() == null || questionItem.getExplain_image().equals("")) {
                holder.questionItemBinding.imgAnswerExaplaination.setVisibility(View.GONE);
            } else {
                holder.questionItemBinding.imgAnswerExaplaination.setVisibility(View.VISIBLE);
            }
        }

        if (questionItem.getScenario().equals("") && (questionItem.getScenario_image() == null || questionItem.getScenario_image().equals(""))) {
            holder.questionItemBinding.scenarioText.setVisibility(View.GONE);
            holder.questionItemBinding.txtScenarioDetails.setVisibility(View.GONE);
            holder.questionItemBinding.imgScenario.setVisibility(View.GONE);
        } else {
            holder.questionItemBinding.scenarioText.setVisibility(View.VISIBLE);
            if (questionItem.getScenario().equals("")) {
                holder.questionItemBinding.txtScenarioDetails.setVisibility(View.GONE);
            } else {
                holder.questionItemBinding.txtScenarioDetails.setVisibility(View.VISIBLE);
            }
            if (questionItem.getScenario_image() == null || questionItem.getScenario_image().equals("")) {
                holder.questionItemBinding.imgScenario.setVisibility(View.GONE);
            } else {
                holder.questionItemBinding.imgScenario.setVisibility(View.VISIBLE);
            }
        }

        holder.questionItemBinding.questionReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.equals(holder.questionItemBinding.questionReport.getText().toString(), "Reported")) {
                    new CustomMessage((Activity) context, "This question already reported");
                } else {
                    addQuestionView.onQuestionReport(questionItem.getQuestionId(), holder.questionItemBinding.questionNo.getText().toString());
                }
            }
        });
        holder.questionItemBinding.btnShowAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.questionItemBinding.answerLayout.setVisibility(View.VISIBLE);
            }
        });
        holder.questionItemBinding.imgAnswerExaplaination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClick.onImageClick(questionItem.getExplain_image());
            }
        });

        holder.questionItemBinding.imgScenario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClick.onImageClick(questionItem.getScenario_image());
            }
        });
        holder.questionItemBinding.imgQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClick.onImageClick(questionItem.getQuestion_image());
            }
        });
        holder.questionItemBinding.imgOptionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClick.onImageClick(questionItem.getOptiona_image());
            }
        });
        holder.questionItemBinding.imgOptionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClick.onImageClick(questionItem.getOptionb_image());
            }
        });
        holder.questionItemBinding.imgOptionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClick.onImageClick(questionItem.getOptionb_image());
            }
        });
        holder.questionItemBinding.imgOptionD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClick.onImageClick(questionItem.getOptiond_image());
            }
        });
//        }else {
//            final QuestionItem questionItem = questionItems.get(position);
//            holder.questionItemBinding.setQuestionitem(questionItem);
//
//            UIHelper.setImage(holder.questionItemBinding.imgQuestion,questionItem.getQuestion_image());
//            UIHelper.setImage(holder.questionItemBinding.imgAnswerExaplaination,questionItem.getExplain_image());
//
//
//            holder.questionItemBinding.btnShowAnswer.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    holder.questionItemBinding.answerLayout.setVisibility(View.VISIBLE);
//                }
//            });
//        }
    }

    @Override
    public int getItemCount() {
        return questionItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        QuestionItemBinding questionItemBinding;
        public ViewHolder(@NonNull QuestionItemBinding itemView) {
            super(itemView.getRoot());
            questionItemBinding=itemView;
        }
    }
}
