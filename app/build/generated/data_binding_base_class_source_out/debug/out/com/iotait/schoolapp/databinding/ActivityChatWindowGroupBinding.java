// Generated by data binding compiler. Do not edit!
package com.iotait.schoolapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;
import com.iotait.schoolapp.R;
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ActivityChatWindowGroupBinding extends ViewDataBinding {
  @NonNull
  public final ProgressBar chatProgressBar;

  @NonNull
  public final ImageView imagePickIv;

  @NonNull
  public final LinearLayout linearLayout4;

  @NonNull
  public final TextView otherUserName;

  @NonNull
  public final CircleImageView otherUserProfile;

  @NonNull
  public final RecyclerView rvchats;

  @NonNull
  public final ImageView sendBtn;

  @NonNull
  public final EditText smsWriteET;

  @NonNull
  public final ToolbarLayoutBinding toolbar;

  protected ActivityChatWindowGroupBinding(Object _bindingComponent, View _root,
      int _localFieldCount, ProgressBar chatProgressBar, ImageView imagePickIv,
      LinearLayout linearLayout4, TextView otherUserName, CircleImageView otherUserProfile,
      RecyclerView rvchats, ImageView sendBtn, EditText smsWriteET, ToolbarLayoutBinding toolbar) {
    super(_bindingComponent, _root, _localFieldCount);
    this.chatProgressBar = chatProgressBar;
    this.imagePickIv = imagePickIv;
    this.linearLayout4 = linearLayout4;
    this.otherUserName = otherUserName;
    this.otherUserProfile = otherUserProfile;
    this.rvchats = rvchats;
    this.sendBtn = sendBtn;
    this.smsWriteET = smsWriteET;
    this.toolbar = toolbar;
  }

  @NonNull
  public static ActivityChatWindowGroupBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_chat_window_group, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ActivityChatWindowGroupBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ActivityChatWindowGroupBinding>inflateInternal(inflater, R.layout.activity_chat_window_group, root, attachToRoot, component);
  }

  @NonNull
  public static ActivityChatWindowGroupBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.activity_chat_window_group, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ActivityChatWindowGroupBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ActivityChatWindowGroupBinding>inflateInternal(inflater, R.layout.activity_chat_window_group, null, false, component);
  }

  public static ActivityChatWindowGroupBinding bind(@NonNull View view) {
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
  public static ActivityChatWindowGroupBinding bind(@NonNull View view,
      @Nullable Object component) {
    return (ActivityChatWindowGroupBinding)bind(component, view, R.layout.activity_chat_window_group);
  }
}
