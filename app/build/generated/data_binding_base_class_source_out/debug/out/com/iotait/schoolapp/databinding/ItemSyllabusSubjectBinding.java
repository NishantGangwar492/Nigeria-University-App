// Generated by data binding compiler. Do not edit!
package com.iotait.schoolapp.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import com.iotait.schoolapp.R;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ItemSyllabusSubjectBinding extends ViewDataBinding {
  @NonNull
  public final TextView textsubject;

  protected ItemSyllabusSubjectBinding(Object _bindingComponent, View _root, int _localFieldCount,
      TextView textsubject) {
    super(_bindingComponent, _root, _localFieldCount);
    this.textsubject = textsubject;
  }

  @NonNull
  public static ItemSyllabusSubjectBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.item_syllabus_subject, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ItemSyllabusSubjectBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ItemSyllabusSubjectBinding>inflateInternal(inflater, R.layout.item_syllabus_subject, root, attachToRoot, component);
  }

  @NonNull
  public static ItemSyllabusSubjectBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.item_syllabus_subject, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ItemSyllabusSubjectBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ItemSyllabusSubjectBinding>inflateInternal(inflater, R.layout.item_syllabus_subject, null, false, component);
  }

  public static ItemSyllabusSubjectBinding bind(@NonNull View view) {
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
  public static ItemSyllabusSubjectBinding bind(@NonNull View view, @Nullable Object component) {
    return (ItemSyllabusSubjectBinding)bind(component, view, R.layout.item_syllabus_subject);
  }
}
