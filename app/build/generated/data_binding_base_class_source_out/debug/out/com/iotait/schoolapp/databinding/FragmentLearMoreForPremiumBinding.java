// Generated by data binding compiler. Do not edit!
package com.iotait.schoolapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.google.android.material.appbar.AppBarLayout;
import com.iotait.schoolapp.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class FragmentLearMoreForPremiumBinding extends ViewDataBinding {
  @NonNull
  public final AppBarLayout appBarLayout;

  @NonNull
  public final ImageButton back;

  @NonNull
  public final Toolbar toolbar;

  protected FragmentLearMoreForPremiumBinding(Object _bindingComponent, View _root,
      int _localFieldCount, AppBarLayout appBarLayout, ImageButton back, Toolbar toolbar) {
    super(_bindingComponent, _root, _localFieldCount);
    this.appBarLayout = appBarLayout;
    this.back = back;
    this.toolbar = toolbar;
  }

  @NonNull
  public static FragmentLearMoreForPremiumBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_lear_more_for_premium, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static FragmentLearMoreForPremiumBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<FragmentLearMoreForPremiumBinding>inflateInternal(inflater, R.layout.fragment_lear_more_for_premium, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentLearMoreForPremiumBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_lear_more_for_premium, null, false, component)
   */
  @NonNull
  @Deprecated
  public static FragmentLearMoreForPremiumBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<FragmentLearMoreForPremiumBinding>inflateInternal(inflater, R.layout.fragment_lear_more_for_premium, null, false, component);
  }

  public static FragmentLearMoreForPremiumBinding bind(@NonNull View view) {
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
  public static FragmentLearMoreForPremiumBinding bind(@NonNull View view,
      @Nullable Object component) {
    return (FragmentLearMoreForPremiumBinding)bind(component, view, R.layout.fragment_lear_more_for_premium);
  }
}
