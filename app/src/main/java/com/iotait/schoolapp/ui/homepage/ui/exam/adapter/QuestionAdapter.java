package com.iotait.schoolapp.ui.homepage.ui.exam.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.iotait.schoolapp.R;
import com.iotait.schoolapp.databinding.ItemQuestionBinding;
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.listener.ImageClick;
import com.iotait.schoolapp.ui.homepage.ui.exam.fullexam.models.AnswerModel;
import com.iotait.schoolapp.ui.homepage.ui.question.details.models.QuestionItem;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    Context context;
    private List<QuestionItem> questionItems;
    private ImageClick imageClick;

    public QuestionAdapter(Context context, List<QuestionItem> questionItems, ImageClick imageClick) {
        this.context = context;
        this.questionItems = questionItems;
        this.imageClick = imageClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQuestionBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_question,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final QuestionItem questionItem=questionItems.get(position);
        holder.binding.setQuestionitem(questionItem);
        holder.binding.questionNo.setText("Question "+String.valueOf(position+1));


        holder.binding.layoutOptionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.binding.layoutOptionA.setBackgroundColor(context.getResources().getColor(R.color.gray));
                holder.binding.layoutOptionB.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.binding.layoutOptionC.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.binding.layoutOptionD.setBackgroundColor(context.getResources().getColor(R.color.white));
                UIHelper.checkAnswerData(questionItem.getQuestionId(),"A",questionItem.getAnswer(), position);
            }
        });

        holder.binding.layoutOptionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.binding.layoutOptionB.setBackgroundColor(context.getResources().getColor(R.color.gray));
                holder.binding.layoutOptionA.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.binding.layoutOptionC.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.binding.layoutOptionD.setBackgroundColor(context.getResources().getColor(R.color.white));
                UIHelper.checkAnswerData(questionItem.getQuestionId(),"B",questionItem.getAnswer(), position);
            }
        });

        holder.binding.layoutOptionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.binding.layoutOptionC.setBackgroundColor(context.getResources().getColor(R.color.gray));
                holder.binding.layoutOptionB.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.binding.layoutOptionA.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.binding.layoutOptionD.setBackgroundColor(context.getResources().getColor(R.color.white));
                UIHelper.checkAnswerData(questionItem.getQuestionId(),"C",questionItem.getAnswer(), position);
            }
        });

        holder.binding.layoutOptionD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.binding.layoutOptionD.setBackgroundColor(context.getResources().getColor(R.color.gray));
                holder.binding.layoutOptionB.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.binding.layoutOptionC.setBackgroundColor(context.getResources().getColor(R.color.white));
                holder.binding.layoutOptionA.setBackgroundColor(context.getResources().getColor(R.color.white));
                UIHelper.checkAnswerData(questionItem.getQuestionId(),"D",questionItem.getAnswer(), position);
            }
        });

        holder.binding.imgSchenario.setOnClickListener(new View.OnClickListener() {
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
        ItemQuestionBinding binding;
        public ViewHolder(@NonNull ItemQuestionBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
