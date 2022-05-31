package com.iotait.schoolapp.ui.homepage.ui.syllabus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.iotait.schoolapp.R;
import com.iotait.schoolapp.databinding.ItemSyllabusSubjectBinding;
import com.iotait.schoolapp.ui.homepage.ui.syllabus.view.SyllabusClickView;

import java.util.List;

public class SyllabusAdapter extends RecyclerView.Adapter<SyllabusAdapter.ViewHolder> {

    Context context;
    List<String> subjectname;
    SyllabusClickView syllabusClickView;

    public SyllabusAdapter(Context context, List<String> subjectname, SyllabusClickView syllabusClickView) {
        this.context = context;
        this.subjectname = subjectname;
        this.syllabusClickView = syllabusClickView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSyllabusSubjectBinding itemSyllabusSubjectBinding = DataBindingUtil.inflate(LayoutInflater.from(context),
                R.layout.item_syllabus_subject, parent, false);
        return new ViewHolder(itemSyllabusSubjectBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        holder.binding.textsubject.setText(subjectname.get(position));
        holder.binding.textsubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                syllabusClickView.onsubjectClick(subjectname.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return subjectname.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ItemSyllabusSubjectBinding binding;

        public ViewHolder(@NonNull ItemSyllabusSubjectBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
