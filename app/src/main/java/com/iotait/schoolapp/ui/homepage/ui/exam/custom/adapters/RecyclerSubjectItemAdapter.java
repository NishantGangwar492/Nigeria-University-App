package com.iotait.schoolapp.ui.homepage.ui.exam.custom.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.iotait.schoolapp.R;
import com.iotait.schoolapp.databinding.RowItemYearBinding;
import com.iotait.schoolapp.ui.homepage.ui.question.view.AddQuestionView;

import java.util.List;

public class RecyclerSubjectItemAdapter extends RecyclerView.Adapter<RecyclerSubjectItemAdapter.ViewHolder> {

    private Context context;
    private List<String> subjectList;
    private AddQuestionView addQuestionView;

    public RecyclerSubjectItemAdapter(Context context, List<String> subjectList, AddQuestionView addQuestionView) {
        this.context = context;
        this.subjectList = subjectList;
        this.addQuestionView=addQuestionView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowItemYearBinding rowItemYearBinding= DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_item_year,parent,false);
        return new ViewHolder(rowItemYearBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String subject=subjectList.get(position);
        holder.rowItemYearBinding.setYearitem(subject);
        if (position==subjectList.size()-1)
            holder.rowItemYearBinding.devider.setVisibility(View.GONE);
        holder.rowItemYearBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addQuestionView.onSubjectSelected(subject);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RowItemYearBinding rowItemYearBinding;
        public ViewHolder(@NonNull RowItemYearBinding itemView) {
            super(itemView.getRoot());
            rowItemYearBinding=itemView;
        }
    }
}
