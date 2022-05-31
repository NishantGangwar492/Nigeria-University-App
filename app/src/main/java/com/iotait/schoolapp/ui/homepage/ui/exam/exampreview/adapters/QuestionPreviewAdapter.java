package com.iotait.schoolapp.ui.homepage.ui.exam.exampreview.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.iotait.schoolapp.R;
import com.iotait.schoolapp.common.Common;
import com.iotait.schoolapp.databinding.ItemQuestionBinding;
import com.iotait.schoolapp.databinding.PreviewQuestionItemBinding;
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.listener.ImageClick;
import com.iotait.schoolapp.ui.homepage.ui.exam.fullexam.models.AnswerModel;
import com.iotait.schoolapp.ui.homepage.ui.question.details.models.QuestionItem;

import java.util.List;

public class QuestionPreviewAdapter extends RecyclerView.Adapter<QuestionPreviewAdapter.ViewHolder> {
    Context context;
    private List<QuestionItem> questionItems;
    private ImageClick imageClick;

    public QuestionPreviewAdapter(Context context, List<QuestionItem> questionItems, ImageClick imageClick) {
        this.context = context;
        this.questionItems = questionItems;
        this.imageClick = imageClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PreviewQuestionItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.preview_question_item,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final QuestionItem questionItem=questionItems.get(position);
        holder.binding.setQuestionitem(questionItem);
        holder.binding.questionNo.setText("Question "+String.valueOf(position+1));
        for(AnswerModel answerModel : Common.answerList) {
            if(answerModel.getQuestioid().equals(questionItem.getQuestionId())){
                if(answerModel.getQuestioid().equals(questionItem.getQuestionId())){
                    if (answerModel.getYour_answer().equals(answerModel.getCurrect_answer()) && answerModel.getCurrect_answer().equals("A")){
                        holder.binding.layoutOptionA.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                    }
                    else if (answerModel.getYour_answer().equals(answerModel.getCurrect_answer()) && answerModel.getCurrect_answer().equals("B")){
                        holder.binding.layoutOptionB.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                    }
                    else if (answerModel.getYour_answer().equals(answerModel.getCurrect_answer()) && answerModel.getCurrect_answer().equals("C")){
                        holder.binding.layoutOptionC.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                    }
                    else if (answerModel.getYour_answer().equals(answerModel.getCurrect_answer()) && answerModel.getCurrect_answer().equals("D")){
                        holder.binding.layoutOptionD.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
                    }else {
                        if (answerModel.getYour_answer().equals("A")){
                            holder.binding.layoutOptionA.setBackgroundColor(context.getResources().getColor(R.color.wrong_ans_bg));
                        }   
                        else if (answerModel.getYour_answer().equals("B")){
                            holder.binding.layoutOptionB.setBackgroundColor(context.getResources().getColor(R.color.wrong_ans_bg));
                        } else if (answerModel.getYour_answer().equals("C")){
                            holder.binding.layoutOptionC.setBackgroundColor(context.getResources().getColor(R.color.wrong_ans_bg));
                        }
                        else if (answerModel.getYour_answer().equals("D")){
                            holder.binding.layoutOptionD.setBackgroundColor(context.getResources().getColor(R.color.wrong_ans_bg));
                        }
                    }
                }
            }
        }
        if (questionItem.getAnswer().equals("A")){
            holder.binding.layoutOptionA.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        }
        else if (questionItem.getAnswer().equals("B")){
            holder.binding.layoutOptionB.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        } else if (questionItem.getAnswer().equals("C")){
            holder.binding.layoutOptionC.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        }
        else if (questionItem.getAnswer().equals("D")){
            holder.binding.layoutOptionD.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));
        }

        if (questionItem.getExplaination().equals("") && (questionItem.getExplain_image()==null || questionItem.getExplain_image().equals(""))){
            holder.binding.txtAnsExp.setVisibility(View.GONE);
            holder.binding.txtAnsExpDetails.setVisibility(View.GONE);
            holder.binding.imgAnswerExaplaination.setVisibility(View.GONE);
        }
        else {
            holder.binding.txtAnsExp.setVisibility(View.VISIBLE);
            if (questionItem.getExplaination().equals("")){
                holder.binding.txtAnsExpDetails.setVisibility(View.GONE);
            }
            else {
                holder.binding.txtAnsExpDetails.setVisibility(View.VISIBLE);
            }
            if (questionItem.getExplain_image()==null || questionItem.getExplain_image().equals("")){
                holder.binding.imgAnswerExaplaination.setVisibility(View.GONE);
            }
            else {
                holder.binding.imgAnswerExaplaination.setVisibility(View.VISIBLE);
            }
        }

        if (questionItem.getScenario().equals("") && (questionItem.getScenario_image()==null || questionItem.getScenario_image().equals(""))){
            holder.binding.scenarioText.setVisibility(View.GONE);
            holder.binding.txtScenarioDetails.setVisibility(View.GONE);
            holder.binding.imgScenario.setVisibility(View.GONE);
        }
        else {
            holder.binding.scenarioText.setVisibility(View.VISIBLE);
            if (questionItem.getScenario().equals("")){
                holder.binding.txtScenarioDetails.setVisibility(View.GONE);
            }
            else {
                holder.binding.txtScenarioDetails.setVisibility(View.VISIBLE);
            }
            if (questionItem.getScenario_image()==null || questionItem.getScenario_image().equals("")){
                holder.binding.imgScenario.setVisibility(View.GONE);
            }
            else {
                holder.binding.imgScenario.setVisibility(View.VISIBLE);
            }
        }
        holder.binding.imgScenario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClick.onImageClick(questionItem.getScenario_image());
            }
        });
        holder.binding.imgQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClick.onImageClick(questionItem.getQuestion_image());
            }
        });
        holder.binding.imgAnswerExaplaination.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClick.onImageClick(questionItem.getQuestion_image());
            }
        });
        holder.binding.imgOptiona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClick.onImageClick(questionItem.getOptiona_image());
            }
        });
        holder.binding.imgOptionb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClick.onImageClick(questionItem.getOptionb_image());
            }
        });
        holder.binding.imgOptionc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClick.onImageClick(questionItem.getOptionb_image());
            }
        });
        holder.binding.imgOptiond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageClick.onImageClick(questionItem.getOptiond_image());
            }
        });
    }


    @Override
    public int getItemCount() {
        return questionItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PreviewQuestionItemBinding binding;
        public ViewHolder(@NonNull PreviewQuestionItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    public static AnswerModel findCorrectAnswer(String questionId, ViewHolder viewHolder, Context context) {

        return null;
    }
}
