// Generated by data binding compiler. Do not edit!
package com.iotait.schoolapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.iotait.schoolapp.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class InSmsMediaBinding extends ViewDataBinding {
  @NonNull
  public final ImageView mediaImg;

  @NonNull
  public final TextView messageTime;

  @NonNull
  public final ImageView playBtn;

  protected InSmsMediaBinding(Object _bindingComponent, View _root, int _localFieldCount,
      ImageView mediaImg, TextView messageTime, ImageView playBtn) {
    super(_bindingComponent, _root, _localFieldCount);
    this.mediaImg = mediaImg;
    this.messageTime = messageTime;
    this.playBtn = playBtn;
  }

  @NonNull
  public static InSmsMediaBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.in_sms_media, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static InSmsMediaBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<InSmsMediaBinding>inflateInternal(inflater, R.layout.in_sms_media, root, attachToRoot, component);
  }

  @NonNull
  public static InSmsMediaBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.in_sms_media, null, false, component)
   */
  @NonNull
  @Deprecated
  public static InSmsMediaBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<InSmsMediaBinding>inflateInternal(inflater, R.layout.in_sms_media, null, false, component);
  }

  public static InSmsMediaBinding bind(@NonNull View view) {
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
  public static InSmsMediaBinding bind(@NonNull View view, @Nullable Object component) {
    return (InSmsMediaBinding)bind(component, view, R.layout.in_sms_media);
  }
}
