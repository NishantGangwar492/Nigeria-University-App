package com.iotait.schoolapp.ui.homepage.ui.home.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.iotait.schoolapp.R;
import com.iotait.schoolapp.databinding.ItemLatestNewsBinding;
import com.iotait.schoolapp.ui.homepage.ui.home.model.Datum;
import com.iotait.schoolapp.ui.homepage.ui.home.view.HomeView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.google.android.gms.common.internal.Preconditions.checkArgument;

public class LatestNewsAdapter extends RecyclerView.Adapter<LatestNewsAdapter.ViewHolder> {

    Context context;
    List<Datum> data;
    HomeView homeView;

    public LatestNewsAdapter(Context context, List<Datum> data, HomeView homeView) {
        this.context = context;
        this.data = data;
        this.homeView=homeView;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLatestNewsBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.item_latest_news, parent, false);
        return new ViewHolder(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Datum datum = data.get(position);
        holder.binding.setNewsitem(datum);
        holder.binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeView.onNewsClick(datum);
            }
        });



    }

    @Override
    public int getItemCount() {
        return data.size();
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
        ItemLatestNewsBinding binding;

        public ViewHolder(@NonNull ItemLatestNewsBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }

    //pagination
    public void addData(List<Datum> datumList){
        for (Datum datum:datumList){
            data.add(datum);
        }
        notifyDataSetChanged();
    }

//    static String[] suffixes =
//            //    0     1     2     3     4     5     6     7     8     9
//            { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
//                    //    10    11    12    13    14    15    16    17    18    19
//                    "th", "th", "th", "th", "th", "th", "th", "th", "th", "th",
//                    //    20    21    22    23    24    25    26    27    28    29
//                    "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th",
//                    //    30    31
//                    "th", "st" };
//
//    String getDayOfMonthSuffix(final int n) {
//        checkArgument(n >= 1 && n <= 31, "illegal day of month: " + n);
//        if (n >= 11 && n <= 13) {
//            return "th";
//        }
//        switch (n % 10) {
//            case 1:  return "st";
//            case 2:  return "nd";
//            case 3:  return "rd";
//            default: return "th";
//        }
//    }
}
