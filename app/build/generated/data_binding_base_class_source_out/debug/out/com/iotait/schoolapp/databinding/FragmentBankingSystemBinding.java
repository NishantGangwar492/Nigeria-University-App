// Generated by data binding compiler. Do not edit!
package com.iotait.schoolapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.github.ybq.android.spinkit.SpinKitView;
import com.google.android.material.appbar.AppBarLayout;
import com.iotait.schoolapp.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class FragmentBankingSystemBinding extends ViewDataBinding {
  @NonNull
  public final AppBarLayout appBarLayout;

  @NonNull
  public final ImageButton backbutton;

  @NonNull
  public final LinearLayout banner;

  @NonNull
  public final TextView close;

  @NonNull
  public final TextView email;

  @NonNull
  public final EditText fullName;

  @NonNull
  public final RelativeLayout relativeLayout;

  @NonNull
  public final SpinKitView spinKit;

  @NonNull
  public final Button submit;

  @NonNull
  public final Toolbar toolbar;

  @NonNull
  public final TextView totalAmount;

  protected FragmentBankingSystemBinding(Object _bindingComponent, View _root, int _localFieldCount,
      AppBarLayout appBarLayout, ImageButton backbutton, LinearLayout banner, TextView close,
      TextView email, EditText fullName, RelativeLayout relativeLayout, SpinKitView spinKit,
      Button submit, Toolbar toolbar, TextView totalAmount) {
    super(_bindingComponent, _root, _localFieldCount);
    this.appBarLayout = appBarLayout;
    this.backbutton = backbutton;
    this.banner = banner;
    this.close = close;
    this.email = email;
    this.fullName = fullName;
    this.relativeLayout = relativeLayout;
    this.spinKit = spinKit;
    this.submit = submit;
    this.toolbar = toolbar;
    this.totalAmount = totalAmount;
  }

  @NonNull
  public static FragmentBankingSystemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_banking_system, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static FragmentBankingSystemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<FragmentBankingSystemBinding>inflateInternal(inflater, R.layout.fragment_banking_system, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentBankingSystemBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_banking_system, null, false, component)
   */
  @NonNull
  @Deprecated
  public static FragmentBankingSystemBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<FragmentBankingSystemBinding>inflateInternal(inflater, R.layout.fragment_banking_system, null, false, component);
  }

  public static FragmentBankingSystemBinding bind(@NonNull View view) {
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
  public static FragmentBankingSystemBinding bind(@NonNull View view, @Nullable Object component) {
    return (FragmentBankingSystemBinding)bind(component, view, R.layout.fragment_banking_system);
  }
}
