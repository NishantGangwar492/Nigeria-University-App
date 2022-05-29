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
import de.hdodenhof.circleimageview.CircleImageView;
import java.lang.Deprecated;
import java.lang.Object;

public abstract class ItemArticalsBinding extends ViewDataBinding {
  @NonNull
  public final TextView articlemain;

  @NonNull
  public final CircleImageView articleprofile;

  @NonNull
  public final TextView dislike;

  @NonNull
  public final ImageView imagedislike;

  @NonNull
  public final ImageView imagelike;

  @NonNull
  public final TextView like;

  @NonNull
  public final TextView pubDate;

  @NonNull
  public final TextView readmore;

  @NonNull
  public final TextView title;

  @NonNull
  public final TextView writerDesignation;

  @NonNull
  public final TextView writername;

  protected ItemArticalsBinding(Object _bindingComponent, View _root, int _localFieldCount,
      TextView articlemain, CircleImageView articleprofile, TextView dislike,
      ImageView imagedislike, ImageView imagelike, TextView like, TextView pubDate,
      TextView readmore, TextView title, TextView writerDesignation, TextView writername) {
    super(_bindingComponent, _root, _localFieldCount);
    this.articlemain = articlemain;
    this.articleprofile = articleprofile;
    this.dislike = dislike;
    this.imagedislike = imagedislike;
    this.imagelike = imagelike;
    this.like = like;
    this.pubDate = pubDate;
    this.readmore = readmore;
    this.title = title;
    this.writerDesignation = writerDesignation;
    this.writername = writername;
  }

  @NonNull
  public static ItemArticalsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot) {
    return inflate(inflater, root, attachToRoot, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.item_articals, root, attachToRoot, component)
   */
  @NonNull
  @Deprecated
  public static ItemArticalsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup root, boolean attachToRoot, @Nullable Object component) {
    return ViewDataBinding.<ItemArticalsBinding>inflateInternal(inflater, R.layout.item_articals, root, attachToRoot, component);
  }

  @NonNull
  public static ItemArticalsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, DataBindingUtil.getDefaultComponent());
  }

  /**
   * This method receives DataBindingComponent instance as type Object instead of
   * type DataBindingComponent to avoid causing too many compilation errors if
   * compilation fails for another reason.
   * https://issuetracker.google.com/issues/116541301
   * @Deprecated Use DataBindingUtil.inflate(inflater, R.layout.item_articals, null, false, component)
   */
  @NonNull
  @Deprecated
  public static ItemArticalsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable Object component) {
    return ViewDataBinding.<ItemArticalsBinding>inflateInternal(inflater, R.layout.item_articals, null, false, component);
  }

  public static ItemArticalsBinding bind(@NonNull View view) {
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
  public static ItemArticalsBinding bind(@NonNull View view, @Nullable Object component) {
    return (ItemArticalsBinding)bind(component, view, R.layout.item_articals);
  }
}
