// Generated by data binding compiler. Do not edit!
package com.iotait.schoolapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.iotait.schoolapp.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ItemFaqBinding extends ViewDataBinding {
  @NonNull
  public final TextView answerTv;

  @NonNull
  public final ImageView collapsingImg;

  @NonNull
  public final TextView questionTv;

  @NonNull
  public final RelativeLayout questionView;

  @NonNull
  public final ImageView selectFaq;

  protected ItemFaqBinding(Object _bindingComponent, View _root, int _localFieldCount,
      TextView answerTv, ImageView collapsingImg, TextView questionTv, RelativeLayout questionView,
      ImageView selectFaq) {
    super(_bindingComponent, _root, _localFieldCount);
    this.answerTv = answerTv;
    this.collapsingImg = collapsingImg;
    this.questionTv = questionTv;
    this.questionView = questionView;
    this.selectFaq = selectFaq;
  }

  @NonNull
  public static ItemFaqBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup root,
      boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.item_faq, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ItemFaqBinding inflate(@NonNull LayoutInflater inflater, @Nullable ViewGroup root,
      boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ItemFaqBinding>inflateInternal(inflater, R.layout.item_faq, root, attachToRoot, component);
  }

  @NonNull
  public static ItemFaqBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.item_faq, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ItemFaqBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ItemFaqBinding>inflateInternal(inflater, R.layout.item_faq, null, false, component);
  }

  public static ItemFaqBinding bind(@NonNull View view) {
    return bind(view, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.bind(view, component)
   */
  @Deprecated
  public static ItemFaqBinding bind(@NonNull View view, @Nullable Object component) {
    return (ItemFaqBinding)bind(component, view, R.layout.item_faq);
  }
}
