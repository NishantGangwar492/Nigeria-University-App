package com.iotait.schoolapp.ui.homepage.ui.unnfaq.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.iotait.schoolapp.R;
import com.iotait.schoolapp.databinding.ItemFaqBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.ui.homepage.ui.unnfaq.interfaceClass.FaqInterface;
import com.iotait.schoolapp.ui.homepage.ui.unnfaq.model.FaqData;
import com.iotait.schoolapp.ui.homepage.ui.unnfaq.model.SelectionItemFaq;

import java.util.ArrayList;
import java.util.List;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ViewHolder> {

    Context context;
    private List<FaqData> faqDataList;
    FaqInterface faqInterface;

    private int current_position = -1;
    private int selectItemSize = 0;
    private List<SelectionItemFaq> selectionItems;


    public FaqAdapter(Context context, List<FaqData> faqDataList, FaqInterface faqInterface) {
        this.context = context;
        this.faqDataList = faqDataList;
        this.faqInterface = faqInterface;
        selectionItems = new ArrayList<>();

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemFaqBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_faq, parent, false);
        return new FaqAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final String question = faqDataList.get(position).getQuestion();
        final String answer = faqDataList.get(position).getAnswer();

        holder.binding.answerTv.setText(faqDataList.get(position).getAnswer());
        holder.binding.questionTv.setText(faqDataList.get(position).getQuestion());

        final SelectionItemFaq selectionItemFaq = new SelectionItemFaq();
        selectionItemFaq.setId(position);
        selectionItemFaq.setSelect(false);
        selectionItems.add(selectionItemFaq);

        holder.itemView.setOnClickListener(v -> {
            if (holder.binding.answerTv.getVisibility() == View.GONE) {
                holder.binding.answerTv.setVisibility(View.VISIBLE);
                holder.binding.collapsingImg.setRotation(180);
            } else if (holder.binding.answerTv.getVisibility() == View.VISIBLE) {
                holder.binding.answerTv.setVisibility(View.GONE);
                holder.binding.collapsingImg.setRotation(0);
            }


        });

        holder.binding.selectFaq.setOnClickListener(v -> {
            if (!selectionItems.get(position).isSelect()) {
                if (selectItemSize < 4) {
                    selectItemSize++;
                    faqInterface.onFaqSelect(position, question, answer);
                    //holder.binding.selectFaq.setBackgroundColor(context.getResources().getColor(R.color.add_slider_text_color));
                    holder.binding.selectFaq.setColorFilter(ContextCompat.getColor(context, R.color.add_slider_text_color), android.graphics.PorterDuff.Mode.SRC_IN);
                    SelectionItemFaq update = new SelectionItemFaq();
                    update.setId(position);
                    update.setSelect(true);
                    selectionItems.set(position, update);
                } else {
                    new CustomMessage((Activity) context, "You can't select more than [4] FAQ for Share!");
                }

            } else {
                if (selectItemSize < 4) {
                    selectItemSize--;
                    faqInterface.onSubjectDeselect(position, question, answer);
                    SelectionItemFaq update = new SelectionItemFaq();
                    update.setId(position);
                    update.setSelect(false);
                    selectionItems.set(position, update);
                    holder.binding.selectFaq.setColorFilter(ContextCompat.getColor(context, R.color.check_color), android.graphics.PorterDuff.Mode.SRC_IN);


                } else {
                    selectItemSize--;
                    SelectionItemFaq update = new SelectionItemFaq();
                    update.setId(position);
                    update.setSelect(false);
                    selectionItems.set(position, update);
                    faqInterface.onSubjectDeselect(position, question, answer);
                    holder.binding.selectFaq.setColorFilter(ContextCompat.getColor(context, R.color.check_color), android.graphics.PorterDuff.Mode.SRC_IN);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return faqDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemFaqBinding binding;
        public ViewHolder(@NonNull ItemFaqBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
