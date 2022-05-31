// Generated by data binding compiler. Do not edit!
package com.iotait.schoolapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.iotait.schoolapp.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ItemPersonalBinding extends ViewDataBinding {
  @NonNull
  public final TextView lastMMessageTime;

  @NonNull
  public final TextView lastMessage;

  @NonNull
  public final LinearLayout linearlayoutinroom;

  @NonNull
  public final TextView name;

  @NonNull
  public final CircleImageView profileImage;

  @NonNull
  public final TextView unseenMessageCount;

  protected ItemPersonalBinding(Object _bindingComponent, View _root, int _localFieldCount,
      TextView lastMMessageTime, TextView lastMessage, LinearLayout linearlayoutinroom,
      TextView name, CircleImageView profileImage, TextView unseenMessageCount) {
    super(_bindingComponent, _root, _localFieldCount);
    this.lastMMessageTime = lastMMessageTime;
    this.lastMessage = lastMessage;
    this.linearlayoutinroom = linearlayoutinroom;
    this.name = name;
    this.profileImage = profileImage;
    this.unseenMessageCount = unseenMessageCount;
  }

  @NonNull
  public static ItemPersonalBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.item_personal, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ItemPersonalBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ItemPersonalBinding>inflateInternal(inflater, R.layout.item_personal, root, attachToRoot, component);
  }

  @NonNull
  public static ItemPersonalBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.item_personal, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ItemPersonalBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ItemPersonalBinding>inflateInternal(inflater, R.layout.item_personal, null, false, component);
  }

  public static ItemPersonalBinding bind(@NonNull View view) {
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
  public static ItemPersonalBinding bind(@NonNull View view, @Nullable Object component) {
    return (ItemPersonalBinding)bind(component, view, R.layout.item_personal);
  }
}
