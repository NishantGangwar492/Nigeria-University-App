// Generated by data binding compiler. Do not edit!
package com.iotait.schoolapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.widget.NestedScrollView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.github.ybq.android.spinkit.SpinKitView;
import com.iotait.schoolapp.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class FragmentMobileRechargeBinding extends ViewDataBinding {
  @NonNull
  public final Button btnSubmit;

  @NonNull
  public final CardView cardView;

  @NonNull
  public final EditText editFullName;

  @NonNull
  public final TextView editMtn;

  @NonNull
  public final EditText editPin1;

  @NonNull
  public final EditText editPin10;

  @NonNull
  public final EditText editPin2;

  @NonNull
  public final EditText editPin3;

  @NonNull
  public final EditText editPin4;

  @NonNull
  public final EditText editPin5;

  @NonNull
  public final EditText editPin6;

  @NonNull
  public final EditText editPin7;

  @NonNull
  public final EditText editPin8;

  @NonNull
  public final EditText editPin9;

  @NonNull
  public final TextView editRechargeCardType;

  @NonNull
  public final TextView editTotalAmount;

  @NonNull
  public final ToolbarLayoutBinding includeToolbar;

  @NonNull
  public final NestedScrollView nestedScrollView2;

  @NonNull
  public final SpinKitView spinKit;

  @NonNull
  public final TextView txtBanner;

  @NonNull
  public final TextView txtClose;

  @NonNull
  public final View view1;

  @NonNull
  public final View view10;

  @NonNull
  public final View view2;

  @NonNull
  public final View view3;

  @NonNull
  public final View view4;

  @NonNull
  public final View view5;

  @NonNull
  public final View view6;

  @NonNull
  public final View view7;

  @NonNull
  public final View view8;

  @NonNull
  public final View view9;

  protected FragmentMobileRechargeBinding(Object _bindingComponent, View _root,
      int _localFieldCount, Button btnSubmit, CardView cardView, EditText editFullName,
      TextView editMtn, EditText editPin1, EditText editPin10, EditText editPin2, EditText editPin3,
      EditText editPin4, EditText editPin5, EditText editPin6, EditText editPin7, EditText editPin8,
      EditText editPin9, TextView editRechargeCardType, TextView editTotalAmount,
      ToolbarLayoutBinding includeToolbar, NestedScrollView nestedScrollView2, SpinKitView spinKit,
      TextView txtBanner, TextView txtClose, View view1, View view10, View view2, View view3,
      View view4, View view5, View view6, View view7, View view8, View view9) {
    super(_bindingComponent, _root, _localFieldCount);
    this.btnSubmit = btnSubmit;
    this.cardView = cardView;
    this.editFullName = editFullName;
    this.editMtn = editMtn;
    this.editPin1 = editPin1;
    this.editPin10 = editPin10;
    this.editPin2 = editPin2;
    this.editPin3 = editPin3;
    this.editPin4 = editPin4;
    this.editPin5 = editPin5;
    this.editPin6 = editPin6;
    this.editPin7 = editPin7;
    this.editPin8 = editPin8;
    this.editPin9 = editPin9;
    this.editRechargeCardType = editRechargeCardType;
    this.editTotalAmount = editTotalAmount;
    this.includeToolbar = includeToolbar;
    this.nestedScrollView2 = nestedScrollView2;
    this.spinKit = spinKit;
    this.txtBanner = txtBanner;
    this.txtClose = txtClose;
    this.view1 = view1;
    this.view10 = view10;
    this.view2 = view2;
    this.view3 = view3;
    this.view4 = view4;
    this.view5 = view5;
    this.view6 = view6;
    this.view7 = view7;
    this.view8 = view8;
    this.view9 = view9;
  }

  @NonNull
  public static FragmentMobileRechargeBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_mobile_recharge, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static FragmentMobileRechargeBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<FragmentMobileRechargeBinding>inflateInternal(inflater, R.layout.fragment_mobile_recharge, root, attachToRoot, component);
  }

  @NonNull
  public static FragmentMobileRechargeBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.fragment_mobile_recharge, null, false, component)
   */
  @NonNull
  @Deprecated
  public static FragmentMobileRechargeBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<FragmentMobileRechargeBinding>inflateInternal(inflater, R.layout.fragment_mobile_recharge, null, false, component);
  }

  public static FragmentMobileRechargeBinding bind(@NonNull View view) {
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
  public static FragmentMobileRechargeBinding bind(@NonNull View view, @Nullable Object component) {
    return (FragmentMobileRechargeBinding)bind(component, view, R.layout.fragment_mobile_recharge);
  }
}
