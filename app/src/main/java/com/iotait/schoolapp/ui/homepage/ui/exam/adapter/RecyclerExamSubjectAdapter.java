package com.iotait.schoolapp.ui.homepage.ui.exam.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.iotait.schoolapp.R;
import com.iotait.schoolapp.common.Common;
import com.iotait.schoolapp.databinding.ItemQuestionSubjectBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.ui.homepage.ui.exam.models.SelectItem;
import com.iotait.schoolapp.ui.homepage.ui.exam.models.SelectionItem;
import com.iotait.schoolapp.ui.homepage.ui.exam.view.ExamSelectionView;
import com.iotait.schoolapp.ui.homepage.ui.question.view.AddQuestionView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerExamSubjectAdapter extends RecyclerView.Adapter<RecyclerExamSubjectAdapter.ViewHolder> {
    private Context context;
    private List<String> subjectList;
    private ExamSelectionView examSelectionView;
    private int current_position = -1;
    private int selectItemSize = 0;
    private List<SelectionItem> selectionItems;

    public RecyclerExamSubjectAdapter(Context context, List<String> subjectList, ExamSelectionView examSelectionView) {
        this.context = context;
        this.subjectList = subjectList;
        this.examSelectionView = examSelectionView;
        selectionItems=new ArrayList<>();
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
        final SelectionItem selectionItem=new SelectionItem();
        selectionItem.setId(position);
        selectionItem.setSelect(false);
        selectionItems.add(selectionItem);
        holder.subjectBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!selectionItems.get(position).isSelect()){
                    if (selectItemSize < 4) {
                        selectItemSize++;
                        examSelectionView.onSubjectSelect(position,subject);
                        holder.subjectBinding.root.setBackgroundColor(context.getResources().getColor(R.color.grey50));
                        SelectionItem update=new SelectionItem();
                        update.setId(position);
                        update.setSelect(true);
                        selectionItems.set(position,update);
                    } else {
                        new CustomMessage((Activity) context, "You can't select more than [4] subjects");
                    }

                }
                else {
                    if (selectItemSize < 4) {
                        selectItemSize--;
                        examSelectionView.onSubjectDeselect(position,subject);
                        SelectionItem update=new SelectionItem();
                        update.setId(position);
                        update.setSelect(false);
                        selectionItems.set(position,update);
                        holder.subjectBinding.root.setBackgroundColor(context.getResources().getColor(R.color.white));
                    } else {
                        selectItemSize--;
                        SelectionItem update=new SelectionItem();
                        update.setId(position);
                        update.setSelect(false);
                        selectionItems.set(position,update);
                        examSelectionView.onSubjectDeselect(position,subject);
                        holder.subjectBinding.root.setBackgroundColor(context.getResources().getColor(R.color.white));
                    }
                }
            }
        });
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
