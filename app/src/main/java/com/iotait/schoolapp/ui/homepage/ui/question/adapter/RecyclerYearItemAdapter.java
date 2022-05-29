package com.iotait.schoolapp.ui.homepage.ui.question.adapter;

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

public class RecyclerYearItemAdapter extends RecyclerView.Adapter<RecyclerYearItemAdapter.ViewHolder> {

    private Context context;
    private List<String> yearlist;
    private AddQuestionView addQuestionView;

    public RecyclerYearItemAdapter(Context context, List<String> yearlist, AddQuestionView addQuestionView) {
        this.context = context;
        this.yearlist = yearlist;
        this.addQuestionView = addQuestionView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RowItemYearBinding rowItemYearBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.row_item_year, parent, false);
        return new ViewHolder(rowItemYearBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String year = yearlist.get(position);
        holder.rowItemYearBinding.setYearitem(year);

        if (position == yearlist.size() - 1)
            holder.rowItemYearBinding.devider.setVisibility(View.GONE);
        holder.rowItemYearBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addQuestionView.onYearSelected(year);
            }
        });
    }

    @Override
    public int getItemCount() {
        return yearlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RowItemYearBinding rowItemYearBinding;

        public ViewHolder(@NonNull RowItemYearBinding itemView) {
            super(itemView.getRoot());
            rowItemYearBinding = itemView;
        }
    }
}
