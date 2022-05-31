package com.iotait.schoolapp.ui.homepage.ui.home.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;


import com.iotait.schoolapp.R;
import com.iotait.schoolapp.databinding.ImageSliderLayoutBinding;
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.ui.homepage.ui.home.model.SliderItem;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.List;

public class SliderAdapter extends
        SliderViewAdapter<SliderAdapter.SliderAdapterVH> {

    private Context context;
    private List<SliderItem> mSliderItems;

    public SliderAdapter(Context context, List<SliderItem> mSliderItems) {
        this.context = context;
        this.mSliderItems = mSliderItems;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        ImageSliderLayoutBinding imageSliderLayoutItemBinding= DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.image_slider_layout, parent,false);
        return new SliderAdapterVH(imageSliderLayoutItemBinding);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, final int position) {

        final SliderItem sliderItem = mSliderItems.get(position);
        UIHelper.setImageInView(viewHolder.imageSliderLayoutItemBinding.ivAutoImageSlider,sliderItem.getSliderimage());
    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        ImageSliderLayoutBinding imageSliderLayoutItemBinding;

        public SliderAdapterVH(ImageSliderLayoutBinding itemView) {
            super(itemView.getRoot());
            imageSliderLayoutItemBinding=itemView;
        }
    }

}
