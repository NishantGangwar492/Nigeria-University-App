// Generated by data binding compiler. Do not edit!
package com.iotait.schoolapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.iotait.schoolapp.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class RechargePaymentRequestDialogBinding extends ViewDataBinding {
  @NonNull
  public final Button accept;

  @NonNull
  public final TextView amount;

  @NonNull
  public final TextView cardPins;

  @NonNull
  public final CardView cardRecyclerview;

  @NonNull
  public final Button decline;

  @NonNull
  public final TextView pinNumbers;

  @NonNull
  public final LinearLayout pinlayout;

  @NonNull
  public final TextView rechargecardtype;

  @NonNull
  public final TextView txtTitle;

  @NonNull
  public final TextView userNamereq;

  protected RechargePaymentRequestDialogBinding(Object _bindingComponent, View _root,
      int _localFieldCount, Button accept, TextView amount, TextView cardPins,
      CardView cardRecyclerview, Button decline, TextView pinNumbers, LinearLayout pinlayout,
      TextView rechargecardtype, TextView txtTitle, TextView userNamereq) {
    super(_bindingComponent, _root, _localFieldCount);
    this.accept = accept;
    this.amount = amount;
    this.cardPins = cardPins;
    this.cardRecyclerview = cardRecyclerview;
    this.decline = decline;
    this.pinNumbers = pinNumbers;
    this.pinlayout = pinlayout;
    this.rechargecardtype = rechargecardtype;
    this.txtTitle = txtTitle;
    this.userNamereq = userNamereq;
  }

  @NonNull
  public static RechargePaymentRequestDialogBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.recharge_payment_request_dialog, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static RechargePaymentRequestDialogBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<RechargePaymentRequestDialogBinding>inflateInternal(inflater, R.layout.recharge_payment_request_dialog, root, attachToRoot, component);
  }

  @NonNull
  public static RechargePaymentRequestDialogBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.recharge_payment_request_dialog, null, false, component)
   */
  @NonNull
  @Deprecated
  public static RechargePaymentRequestDialogBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<RechargePaymentRequestDialogBinding>inflateInternal(inflater, R.layout.recharge_payment_request_dialog, null, false, component);
  }

  public static RechargePaymentRequestDialogBinding bind(@NonNull View view) {
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
  public static RechargePaymentRequestDialogBinding bind(@NonNull View view,
      @Nullable Object component) {
    return (RechargePaymentRequestDialogBinding)bind(component, view, R.layout.recharge_payment_request_dialog);
  }
}
