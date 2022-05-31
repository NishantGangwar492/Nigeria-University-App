package com.iotait.schoolapp.ui.homepage.ui.question.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.iotait.schoolapp.R;
import com.iotait.schoolapp.databinding.ItemQuestionSubjectBinding;
import com.iotait.schoolapp.ui.homepage.ui.question.view.AddQuestionView;

import java.util.List;

public class RecyclerSubjectAdapter extends RecyclerView.Adapter<RecyclerSubjectAdapter.ViewHolder> {
    private Context context;
    private List<String> subjectList;
    private AddQuestionView questionView;
    private int id = -1;

    public RecyclerSubjectAdapter(Context context, List<String> subjectList, AddQuestionView questionView) {
        this.context = context;
        this.subjectList = subjectList;
        this.questionView = questionView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQuestionSubjectBinding questionSubjectBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_question_subject, parent, false);
        return new ViewHolder(questionSubjectBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final String subject = subjectList.get(position);
        holder.subjectBinding.setSubjectname(subject);
        holder.subjectBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = position;
                questionView.onSubjectSelected(subject);
                notifyDataSetChanged();
            }
        });
        if (id == position) {
            holder.subjectBinding.root.setBackgroundColor(context.getResources().getColor(R.color.grey50));
        } else {
            holder.subjectBinding.root.setBackgroundColor(context.getResources().getColor(R.color.color_white));

        }
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemQuestionSubjectBinding subjectBinding;

        public ViewHolder(@NonNull ItemQuestionSubjectBinding itemView) {
            super(itemView.getRoot());
            subjectBinding = itemView;
        }
    }
}
