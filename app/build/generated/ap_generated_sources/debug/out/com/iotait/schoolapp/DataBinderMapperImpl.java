package com.iotait.schoolapp;

import android.util.SparseArray;
import android.util.SparseIntArray;
import android.view.View;
import androidx.databinding.DataBinderMapper;
import androidx.databinding.DataBindingComponent;
import androidx.databinding.ViewDataBinding;
import com.iotait.schoolapp.databinding.ActivityArticleReadmoreBindingImpl;
import com.iotait.schoolapp.databinding.ActivityAuthenticationBindingImpl;
import com.iotait.schoolapp.databinding.ActivityChatListBindingImpl;
import com.iotait.schoolapp.databinding.ActivityChatWindowBindingImpl;
import com.iotait.schoolapp.databinding.ActivityChatWindowGroupBindingImpl;
import com.iotait.schoolapp.databinding.ActivityDetailsNewsWebViewBindingImpl;
import com.iotait.schoolapp.databinding.ActivityFullPhotoViewBindingImpl;
import com.iotait.schoolapp.databinding.ActivityGroupAdminBindingImpl;
import com.iotait.schoolapp.databinding.ActivityHomeNavigationBindingImpl;
import com.iotait.schoolapp.databinding.ActivityLeaderboardinfoBindingImpl;
import com.iotait.schoolapp.databinding.ActivityLoginBindingImpl;
import com.iotait.schoolapp.databinding.ActivitySignUpBindingImpl;
import com.iotait.schoolapp.databinding.ActivitySplashBindingImpl;
import com.iotait.schoolapp.databinding.BannerLayoutBindingImpl;
import com.iotait.schoolapp.databinding.ConformationDialogeBindingImpl;
import com.iotait.schoolapp.databinding.CustomImageViwerBindingImpl;
import com.iotait.schoolapp.databinding.DialogPointBindingImpl;
import com.iotait.schoolapp.databinding.DialogResultBindingImpl;
import com.iotait.schoolapp.databinding.FaqImageDialogBindingImpl;
import com.iotait.schoolapp.databinding.FragmentAboutAppBindingImpl;
import com.iotait.schoolapp.databinding.FragmentAllTimeBindingImpl;
import com.iotait.schoolapp.databinding.FragmentArticalesBindingImpl;
import com.iotait.schoolapp.databinding.FragmentBankingSystemBindingImpl;
import com.iotait.schoolapp.databinding.FragmentContactusBindingImpl;
import com.iotait.schoolapp.databinding.FragmentContestResultBindingImpl;
import com.iotait.schoolapp.databinding.FragmentCustomExamBindingImpl;
import com.iotait.schoolapp.databinding.FragmentCustomExamDetailsBindingImpl;
import com.iotait.schoolapp.databinding.FragmentExamPreviewBindingImpl;
import com.iotait.schoolapp.databinding.FragmentFullExamBindingImpl;
import com.iotait.schoolapp.databinding.FragmentGetRewardBindingImpl;
import com.iotait.schoolapp.databinding.FragmentHomeBindingImpl;
import com.iotait.schoolapp.databinding.FragmentLeaderBoardBindingImpl;
import com.iotait.schoolapp.databinding.FragmentLearMoreForPremiumBindingImpl;
import com.iotait.schoolapp.databinding.FragmentLearnMoreForBesicBindingImpl;
import com.iotait.schoolapp.databinding.FragmentMobileRechargeBindingImpl;
import com.iotait.schoolapp.databinding.FragmentMonthlyTopBindingImpl;
import com.iotait.schoolapp.databinding.FragmentNavigationBindingImpl;
import com.iotait.schoolapp.databinding.FragmentNotificationBindingImpl;
import com.iotait.schoolapp.databinding.FragmentOnlinePaymentBindingImpl;
import com.iotait.schoolapp.databinding.FragmentPremiumProcessBindingImpl;
import com.iotait.schoolapp.databinding.FragmentQuestionBindingImpl;
import com.iotait.schoolapp.databinding.FragmentQuestionDetailsBindingImpl;
import com.iotait.schoolapp.databinding.FragmentSchoolAnthemBindingImpl;
import com.iotait.schoolapp.databinding.FragmentSyllabusBindingImpl;
import com.iotait.schoolapp.databinding.FragmentSyllabusItemClickBindingImpl;
import com.iotait.schoolapp.databinding.FragmentTakeContestExamBindingImpl;
import com.iotait.schoolapp.databinding.FragmentTakeExamBindingImpl;
import com.iotait.schoolapp.databinding.FragmentUnnFaqBindingImpl;
import com.iotait.schoolapp.databinding.FragmentWeeklyContestBindingImpl;
import com.iotait.schoolapp.databinding.FragmentWeeklyTopBindingImpl;
import com.iotait.schoolapp.databinding.ImageSliderLayoutBindingImpl;
import com.iotait.schoolapp.databinding.InSmsMediaBindingImpl;
import com.iotait.schoolapp.databinding.InSmsTextBindingImpl;
import com.iotait.schoolapp.databinding.ItemActivenowBindingImpl;
import com.iotait.schoolapp.databinding.ItemArticalsBindingImpl;
import com.iotait.schoolapp.databinding.ItemArticalsNotificationBindingImpl;
import com.iotait.schoolapp.databinding.ItemFaqBindingImpl;
import com.iotait.schoolapp.databinding.ItemLatestNewsBindingImpl;
import com.iotait.schoolapp.databinding.ItemLeaderBoardBindingImpl;
import com.iotait.schoolapp.databinding.ItemMakeadminBindingImpl;
import com.iotait.schoolapp.databinding.ItemMessageLeftBindingImpl;
import com.iotait.schoolapp.databinding.ItemMessageLeftImageBindingImpl;
import com.iotait.schoolapp.databinding.ItemMessageRightBindingImpl;
import com.iotait.schoolapp.databinding.ItemMessageRightImageBindingImpl;
import com.iotait.schoolapp.databinding.ItemOverviewBindingImpl;
import com.iotait.schoolapp.databinding.ItemPersonalBindingImpl;
import com.iotait.schoolapp.databinding.ItemPublicroomBindingImpl;
import com.iotait.schoolapp.databinding.ItemQuestionBindingImpl;
import com.iotait.schoolapp.databinding.ItemQuestionSubjectBindingImpl;
import com.iotait.schoolapp.databinding.ItemSyllabusSubjectBindingImpl;
import com.iotait.schoolapp.databinding.ItemUnnNewsBindingImpl;
import com.iotait.schoolapp.databinding.OutSmsMediaBindingImpl;
import com.iotait.schoolapp.databinding.OutSmsTextBindingImpl;
import com.iotait.schoolapp.databinding.PaymentRequestDialogBindingImpl;
import com.iotait.schoolapp.databinding.PhoneNumberRequestDialogBindingImpl;
import com.iotait.schoolapp.databinding.PreviewQuestionItemBindingImpl;
import com.iotait.schoolapp.databinding.QuestionItemBindingImpl;
import com.iotait.schoolapp.databinding.RechargePaymentRequestDialogBindingImpl;
import com.iotait.schoolapp.databinding.RowItemYearBindingImpl;
import com.iotait.schoolapp.databinding.SelectExamSubjectViewBindingImpl;
import com.iotait.schoolapp.databinding.SelectYearViewBindingImpl;
import com.iotait.schoolapp.databinding.ToolbarLayoutBindingImpl;
import com.iotait.schoolapp.databinding.UnnNewsFragmentBindingImpl;
import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.Override;
import java.lang.RuntimeException;
import java.lang.String;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataBinderMapperImpl extends DataBinderMapper {
  private static final int LAYOUT_ACTIVITYARTICLEREADMORE = 1;

  private static final int LAYOUT_ACTIVITYAUTHENTICATION = 2;

  private static final int LAYOUT_ACTIVITYCHATLIST = 3;

  private static final int LAYOUT_ACTIVITYCHATWINDOW = 4;

  private static final int LAYOUT_ACTIVITYCHATWINDOWGROUP = 5;

  private static final int LAYOUT_ACTIVITYDETAILSNEWSWEBVIEW = 6;

  private static final int LAYOUT_ACTIVITYFULLPHOTOVIEW = 7;

  private static final int LAYOUT_ACTIVITYGROUPADMIN = 8;

  private static final int LAYOUT_ACTIVITYHOMENAVIGATION = 9;

  private static final int LAYOUT_ACTIVITYLEADERBOARDINFO = 10;

  private static final int LAYOUT_ACTIVITYLOGIN = 11;

  private static final int LAYOUT_ACTIVITYSIGNUP = 12;

  private static final int LAYOUT_ACTIVITYSPLASH = 13;

  private static final int LAYOUT_BANNERLAYOUT = 14;

  private static final int LAYOUT_CONFORMATIONDIALOGE = 15;

  private static final int LAYOUT_CUSTOMIMAGEVIWER = 16;

  private static final int LAYOUT_DIALOGPOINT = 17;

  private static final int LAYOUT_DIALOGRESULT = 18;

  private static final int LAYOUT_FAQIMAGEDIALOG = 19;

  private static final int LAYOUT_FRAGMENTABOUTAPP = 20;

  private static final int LAYOUT_FRAGMENTALLTIME = 21;

  private static final int LAYOUT_FRAGMENTARTICALES = 22;

  private static final int LAYOUT_FRAGMENTBANKINGSYSTEM = 23;

  private static final int LAYOUT_FRAGMENTCONTACTUS = 24;

  private static final int LAYOUT_FRAGMENTCONTESTRESULT = 25;

  private static final int LAYOUT_FRAGMENTCUSTOMEXAM = 26;

  private static final int LAYOUT_FRAGMENTCUSTOMEXAMDETAILS = 27;

  private static final int LAYOUT_FRAGMENTEXAMPREVIEW = 28;

  private static final int LAYOUT_FRAGMENTFULLEXAM = 29;

  private static final int LAYOUT_FRAGMENTGETREWARD = 30;

  private static final int LAYOUT_FRAGMENTHOME = 31;

  private static final int LAYOUT_FRAGMENTLEADERBOARD = 32;

  private static final int LAYOUT_FRAGMENTLEARMOREFORPREMIUM = 33;

  private static final int LAYOUT_FRAGMENTLEARNMOREFORBESIC = 34;

  private static final int LAYOUT_FRAGMENTMOBILERECHARGE = 35;

  private static final int LAYOUT_FRAGMENTMONTHLYTOP = 36;

  private static final int LAYOUT_FRAGMENTNAVIGATION = 37;

  private static final int LAYOUT_FRAGMENTNOTIFICATION = 38;

  private static final int LAYOUT_FRAGMENTONLINEPAYMENT = 39;

  private static final int LAYOUT_FRAGMENTPREMIUMPROCESS = 40;

  private static final int LAYOUT_FRAGMENTQUESTION = 41;

  private static final int LAYOUT_FRAGMENTQUESTIONDETAILS = 42;

  private static final int LAYOUT_FRAGMENTSCHOOLANTHEM = 43;

  private static final int LAYOUT_FRAGMENTSYLLABUS = 44;

  private static final int LAYOUT_FRAGMENTSYLLABUSITEMCLICK = 45;

  private static final int LAYOUT_FRAGMENTTAKECONTESTEXAM = 46;

  private static final int LAYOUT_FRAGMENTTAKEEXAM = 47;

  private static final int LAYOUT_FRAGMENTUNNFAQ = 48;

  private static final int LAYOUT_FRAGMENTWEEKLYCONTEST = 49;

  private static final int LAYOUT_FRAGMENTWEEKLYTOP = 50;

  private static final int LAYOUT_IMAGESLIDERLAYOUT = 51;

  private static final int LAYOUT_INSMSMEDIA = 52;

  private static final int LAYOUT_INSMSTEXT = 53;

  private static final int LAYOUT_ITEMACTIVENOW = 54;

  private static final int LAYOUT_ITEMARTICALS = 55;

  private static final int LAYOUT_ITEMARTICALSNOTIFICATION = 56;

  private static final int LAYOUT_ITEMFAQ = 57;

  private static final int LAYOUT_ITEMLATESTNEWS = 58;

  private static final int LAYOUT_ITEMLEADERBOARD = 59;

  private static final int LAYOUT_ITEMMAKEADMIN = 60;

  private static final int LAYOUT_ITEMMESSAGELEFT = 61;

  private static final int LAYOUT_ITEMMESSAGELEFTIMAGE = 62;

  private static final int LAYOUT_ITEMMESSAGERIGHT = 63;

  private static final int LAYOUT_ITEMMESSAGERIGHTIMAGE = 64;

  private static final int LAYOUT_ITEMOVERVIEW = 65;

  private static final int LAYOUT_ITEMPERSONAL = 66;

  private static final int LAYOUT_ITEMPUBLICROOM = 67;

  private static final int LAYOUT_ITEMQUESTION = 68;

  private static final int LAYOUT_ITEMQUESTIONSUBJECT = 69;

  private static final int LAYOUT_ITEMSYLLABUSSUBJECT = 70;

  private static final int LAYOUT_ITEMUNNNEWS = 71;

  private static final int LAYOUT_OUTSMSMEDIA = 72;

  private static final int LAYOUT_OUTSMSTEXT = 73;

  private static final int LAYOUT_PAYMENTREQUESTDIALOG = 74;

  private static final int LAYOUT_PHONENUMBERREQUESTDIALOG = 75;

  private static final int LAYOUT_PREVIEWQUESTIONITEM = 76;

  private static final int LAYOUT_QUESTIONITEM = 77;

  private static final int LAYOUT_RECHARGEPAYMENTREQUESTDIALOG = 78;

  private static final int LAYOUT_ROWITEMYEAR = 79;

  private static final int LAYOUT_SELECTEXAMSUBJECTVIEW = 80;

  private static final int LAYOUT_SELECTYEARVIEW = 81;

  private static final int LAYOUT_TOOLBARLAYOUT = 82;

  private static final int LAYOUT_UNNNEWSFRAGMENT = 83;

  private static final SparseIntArray INTERNAL_LAYOUT_ID_LOOKUP = new SparseIntArray(83);

  static {
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.activity_article_readmore, LAYOUT_ACTIVITYARTICLEREADMORE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.activity_authentication, LAYOUT_ACTIVITYAUTHENTICATION);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.activity_chat_list, LAYOUT_ACTIVITYCHATLIST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.activity_chat_window, LAYOUT_ACTIVITYCHATWINDOW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.activity_chat_window_group, LAYOUT_ACTIVITYCHATWINDOWGROUP);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.activity_details_news_web_view, LAYOUT_ACTIVITYDETAILSNEWSWEBVIEW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.activity_full_photo_view, LAYOUT_ACTIVITYFULLPHOTOVIEW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.activity_group_admin, LAYOUT_ACTIVITYGROUPADMIN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.activity_home_navigation, LAYOUT_ACTIVITYHOMENAVIGATION);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.activity_leaderboardinfo, LAYOUT_ACTIVITYLEADERBOARDINFO);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.activity_login, LAYOUT_ACTIVITYLOGIN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.activity_sign_up, LAYOUT_ACTIVITYSIGNUP);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.activity_splash, LAYOUT_ACTIVITYSPLASH);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.banner_layout, LAYOUT_BANNERLAYOUT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.conformation_dialoge, LAYOUT_CONFORMATIONDIALOGE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.custom_image_viwer, LAYOUT_CUSTOMIMAGEVIWER);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.dialog_point, LAYOUT_DIALOGPOINT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.dialog_result, LAYOUT_DIALOGRESULT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.faq_image_dialog, LAYOUT_FAQIMAGEDIALOG);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_about_app, LAYOUT_FRAGMENTABOUTAPP);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_all_time, LAYOUT_FRAGMENTALLTIME);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_articales, LAYOUT_FRAGMENTARTICALES);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_banking_system, LAYOUT_FRAGMENTBANKINGSYSTEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_contactus, LAYOUT_FRAGMENTCONTACTUS);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_contest_result, LAYOUT_FRAGMENTCONTESTRESULT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_custom_exam, LAYOUT_FRAGMENTCUSTOMEXAM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_custom_exam_details, LAYOUT_FRAGMENTCUSTOMEXAMDETAILS);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_exam_preview, LAYOUT_FRAGMENTEXAMPREVIEW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_full_exam, LAYOUT_FRAGMENTFULLEXAM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_get_reward, LAYOUT_FRAGMENTGETREWARD);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_home, LAYOUT_FRAGMENTHOME);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_leader_board, LAYOUT_FRAGMENTLEADERBOARD);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_lear_more_for_premium, LAYOUT_FRAGMENTLEARMOREFORPREMIUM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_learn_more_for_besic, LAYOUT_FRAGMENTLEARNMOREFORBESIC);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_mobile_recharge, LAYOUT_FRAGMENTMOBILERECHARGE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_monthly_top, LAYOUT_FRAGMENTMONTHLYTOP);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_navigation, LAYOUT_FRAGMENTNAVIGATION);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_notification, LAYOUT_FRAGMENTNOTIFICATION);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_online_payment, LAYOUT_FRAGMENTONLINEPAYMENT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_premium_process, LAYOUT_FRAGMENTPREMIUMPROCESS);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_question, LAYOUT_FRAGMENTQUESTION);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_question_details, LAYOUT_FRAGMENTQUESTIONDETAILS);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_school_anthem, LAYOUT_FRAGMENTSCHOOLANTHEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_syllabus, LAYOUT_FRAGMENTSYLLABUS);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_syllabus_item_click, LAYOUT_FRAGMENTSYLLABUSITEMCLICK);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_take_contest_exam, LAYOUT_FRAGMENTTAKECONTESTEXAM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_take_exam, LAYOUT_FRAGMENTTAKEEXAM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_unn_faq, LAYOUT_FRAGMENTUNNFAQ);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_weekly_contest, LAYOUT_FRAGMENTWEEKLYCONTEST);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.fragment_weekly_top, LAYOUT_FRAGMENTWEEKLYTOP);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.image_slider_layout, LAYOUT_IMAGESLIDERLAYOUT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.in_sms_media, LAYOUT_INSMSMEDIA);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.in_sms_text, LAYOUT_INSMSTEXT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.item_activenow, LAYOUT_ITEMACTIVENOW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.item_articals, LAYOUT_ITEMARTICALS);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.item_articals_notification, LAYOUT_ITEMARTICALSNOTIFICATION);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.item_faq, LAYOUT_ITEMFAQ);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.item_latest_news, LAYOUT_ITEMLATESTNEWS);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.item_leader_board, LAYOUT_ITEMLEADERBOARD);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.item_makeadmin, LAYOUT_ITEMMAKEADMIN);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.item_message_left, LAYOUT_ITEMMESSAGELEFT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.item_message_left_image, LAYOUT_ITEMMESSAGELEFTIMAGE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.item_message_right, LAYOUT_ITEMMESSAGERIGHT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.item_message_right_image, LAYOUT_ITEMMESSAGERIGHTIMAGE);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.item_overview, LAYOUT_ITEMOVERVIEW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.item_personal, LAYOUT_ITEMPERSONAL);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.item_publicroom, LAYOUT_ITEMPUBLICROOM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.item_question, LAYOUT_ITEMQUESTION);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.item_question_subject, LAYOUT_ITEMQUESTIONSUBJECT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.item_syllabus_subject, LAYOUT_ITEMSYLLABUSSUBJECT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.item_unn_news, LAYOUT_ITEMUNNNEWS);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.out_sms_media, LAYOUT_OUTSMSMEDIA);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.out_sms_text, LAYOUT_OUTSMSTEXT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.payment_request_dialog, LAYOUT_PAYMENTREQUESTDIALOG);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.phone_number_request_dialog, LAYOUT_PHONENUMBERREQUESTDIALOG);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.preview_question_item, LAYOUT_PREVIEWQUESTIONITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.question_item, LAYOUT_QUESTIONITEM);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.recharge_payment_request_dialog, LAYOUT_RECHARGEPAYMENTREQUESTDIALOG);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.row_item_year, LAYOUT_ROWITEMYEAR);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.select_exam_subject_view, LAYOUT_SELECTEXAMSUBJECTVIEW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.select_year_view, LAYOUT_SELECTYEARVIEW);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.toolbar_layout, LAYOUT_TOOLBARLAYOUT);
    INTERNAL_LAYOUT_ID_LOOKUP.put(com.iotait.schoolapp.R.layout.unn_news_fragment, LAYOUT_UNNNEWSFRAGMENT);
  }

  private final ViewDataBinding internalGetViewDataBinding0(DataBindingComponent component,
      View view, int internalId, Object tag) {
    switch(internalId) {
      case  LAYOUT_ACTIVITYARTICLEREADMORE: {
        if ("layout/activity_article_readmore_0".equals(tag)) {
          return new ActivityArticleReadmoreBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_article_readmore is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYAUTHENTICATION: {
        if ("layout/activity_authentication_0".equals(tag)) {
          return new ActivityAuthenticationBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_authentication is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYCHATLIST: {
        if ("layout/activity_chat_list_0".equals(tag)) {
          return new ActivityChatListBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_chat_list is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYCHATWINDOW: {
        if ("layout/activity_chat_window_0".equals(tag)) {
          return new ActivityChatWindowBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_chat_window is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYCHATWINDOWGROUP: {
        if ("layout/activity_chat_window_group_0".equals(tag)) {
          return new ActivityChatWindowGroupBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_chat_window_group is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYDETAILSNEWSWEBVIEW: {
        if ("layout/activity_details_news_web_view_0".equals(tag)) {
          return new ActivityDetailsNewsWebViewBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_details_news_web_view is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYFULLPHOTOVIEW: {
        if ("layout/activity_full_photo_view_0".equals(tag)) {
          return new ActivityFullPhotoViewBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_full_photo_view is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYGROUPADMIN: {
        if ("layout/activity_group_admin_0".equals(tag)) {
          return new ActivityGroupAdminBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_group_admin is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYHOMENAVIGATION: {
        if ("layout/activity_home_navigation_0".equals(tag)) {
          return new ActivityHomeNavigationBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_home_navigation is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYLEADERBOARDINFO: {
        if ("layout/activity_leaderboardinfo_0".equals(tag)) {
          return new ActivityLeaderboardinfoBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_leaderboardinfo is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYLOGIN: {
        if ("layout/activity_login_0".equals(tag)) {
          return new ActivityLoginBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_login is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYSIGNUP: {
        if ("layout/activity_sign_up_0".equals(tag)) {
          return new ActivitySignUpBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_sign_up is invalid. Received: " + tag);
      }
      case  LAYOUT_ACTIVITYSPLASH: {
        if ("layout/activity_splash_0".equals(tag)) {
          return new ActivitySplashBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for activity_splash is invalid. Received: " + tag);
      }
      case  LAYOUT_BANNERLAYOUT: {
        if ("layout/banner_layout_0".equals(tag)) {
          return new BannerLayoutBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for banner_layout is invalid. Received: " + tag);
      }
      case  LAYOUT_CONFORMATIONDIALOGE: {
        if ("layout/conformation_dialoge_0".equals(tag)) {
          return new ConformationDialogeBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for conformation_dialoge is invalid. Received: " + tag);
      }
      case  LAYOUT_CUSTOMIMAGEVIWER: {
        if ("layout/custom_image_viwer_0".equals(tag)) {
          return new CustomImageViwerBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for custom_image_viwer is invalid. Received: " + tag);
      }
      case  LAYOUT_DIALOGPOINT: {
        if ("layout/dialog_point_0".equals(tag)) {
          return new DialogPointBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for dialog_point is invalid. Received: " + tag);
      }
      case  LAYOUT_DIALOGRESULT: {
        if ("layout/dialog_result_0".equals(tag)) {
          return new DialogResultBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for dialog_result is invalid. Received: " + tag);
      }
      case  LAYOUT_FAQIMAGEDIALOG: {
        if ("layout/faq_image_dialog_0".equals(tag)) {
          return new FaqImageDialogBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for faq_image_dialog is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTABOUTAPP: {
        if ("layout/fragment_about_app_0".equals(tag)) {
          return new FragmentAboutAppBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_about_app is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTALLTIME: {
        if ("layout/fragment_all_time_0".equals(tag)) {
          return new FragmentAllTimeBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_all_time is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTARTICALES: {
        if ("layout/fragment_articales_0".equals(tag)) {
          return new FragmentArticalesBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_articales is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTBANKINGSYSTEM: {
        if ("layout/fragment_banking_system_0".equals(tag)) {
          return new FragmentBankingSystemBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_banking_system is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTCONTACTUS: {
        if ("layout/fragment_contactus_0".equals(tag)) {
          return new FragmentContactusBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_contactus is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTCONTESTRESULT: {
        if ("layout/fragment_contest_result_0".equals(tag)) {
          return new FragmentContestResultBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_contest_result is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTCUSTOMEXAM: {
        if ("layout/fragment_custom_exam_0".equals(tag)) {
          return new FragmentCustomExamBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_custom_exam is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTCUSTOMEXAMDETAILS: {
        if ("layout/fragment_custom_exam_details_0".equals(tag)) {
          return new FragmentCustomExamDetailsBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_custom_exam_details is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTEXAMPREVIEW: {
        if ("layout/fragment_exam_preview_0".equals(tag)) {
          return new FragmentExamPreviewBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_exam_preview is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTFULLEXAM: {
        if ("layout/fragment_full_exam_0".equals(tag)) {
          return new FragmentFullExamBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_full_exam is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTGETREWARD: {
        if ("layout/fragment_get_reward_0".equals(tag)) {
          return new FragmentGetRewardBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_get_reward is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTHOME: {
        if ("layout/fragment_home_0".equals(tag)) {
          return new FragmentHomeBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_home is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTLEADERBOARD: {
        if ("layout/fragment_leader_board_0".equals(tag)) {
          return new FragmentLeaderBoardBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_leader_board is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTLEARMOREFORPREMIUM: {
        if ("layout/fragment_lear_more_for_premium_0".equals(tag)) {
          return new FragmentLearMoreForPremiumBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_lear_more_for_premium is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTLEARNMOREFORBESIC: {
        if ("layout/fragment_learn_more_for_besic_0".equals(tag)) {
          return new FragmentLearnMoreForBesicBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_learn_more_for_besic is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTMOBILERECHARGE: {
        if ("layout/fragment_mobile_recharge_0".equals(tag)) {
          return new FragmentMobileRechargeBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_mobile_recharge is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTMONTHLYTOP: {
        if ("layout/fragment_monthly_top_0".equals(tag)) {
          return new FragmentMonthlyTopBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_monthly_top is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTNAVIGATION: {
        if ("layout/fragment_navigation_0".equals(tag)) {
          return new FragmentNavigationBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_navigation is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTNOTIFICATION: {
        if ("layout/fragment_notification_0".equals(tag)) {
          return new FragmentNotificationBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_notification is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTONLINEPAYMENT: {
        if ("layout/fragment_online_payment_0".equals(tag)) {
          return new FragmentOnlinePaymentBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_online_payment is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTPREMIUMPROCESS: {
        if ("layout/fragment_premium_process_0".equals(tag)) {
          return new FragmentPremiumProcessBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_premium_process is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTQUESTION: {
        if ("layout/fragment_question_0".equals(tag)) {
          return new FragmentQuestionBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_question is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTQUESTIONDETAILS: {
        if ("layout/fragment_question_details_0".equals(tag)) {
          return new FragmentQuestionDetailsBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_question_details is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTSCHOOLANTHEM: {
        if ("layout/fragment_school_anthem_0".equals(tag)) {
          return new FragmentSchoolAnthemBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_school_anthem is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTSYLLABUS: {
        if ("layout/fragment_syllabus_0".equals(tag)) {
          return new FragmentSyllabusBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_syllabus is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTSYLLABUSITEMCLICK: {
        if ("layout/fragment_syllabus_item_click_0".equals(tag)) {
          return new FragmentSyllabusItemClickBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_syllabus_item_click is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTTAKECONTESTEXAM: {
        if ("layout/fragment_take_contest_exam_0".equals(tag)) {
          return new FragmentTakeContestExamBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_take_contest_exam is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTTAKEEXAM: {
        if ("layout/fragment_take_exam_0".equals(tag)) {
          return new FragmentTakeExamBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_take_exam is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTUNNFAQ: {
        if ("layout/fragment_unn_faq_0".equals(tag)) {
          return new FragmentUnnFaqBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_unn_faq is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTWEEKLYCONTEST: {
        if ("layout/fragment_weekly_contest_0".equals(tag)) {
          return new FragmentWeeklyContestBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_weekly_contest is invalid. Received: " + tag);
      }
      case  LAYOUT_FRAGMENTWEEKLYTOP: {
        if ("layout/fragment_weekly_top_0".equals(tag)) {
          return new FragmentWeeklyTopBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for fragment_weekly_top is invalid. Received: " + tag);
      }
    }
    return null;
  }

  private final ViewDataBinding internalGetViewDataBinding1(DataBindingComponent component,
      View view, int internalId, Object tag) {
    switch(internalId) {
      case  LAYOUT_IMAGESLIDERLAYOUT: {
        if ("layout/image_slider_layout_0".equals(tag)) {
          return new ImageSliderLayoutBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for image_slider_layout is invalid. Received: " + tag);
      }
      case  LAYOUT_INSMSMEDIA: {
        if ("layout/in_sms_media_0".equals(tag)) {
          return new InSmsMediaBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for in_sms_media is invalid. Received: " + tag);
      }
      case  LAYOUT_INSMSTEXT: {
        if ("layout/in_sms_text_0".equals(tag)) {
          return new InSmsTextBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for in_sms_text is invalid. Received: " + tag);
      }
      case  LAYOUT_ITEMACTIVENOW: {
        if ("layout/item_activenow_0".equals(tag)) {
          return new ItemActivenowBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for item_activenow is invalid. Received: " + tag);
      }
      case  LAYOUT_ITEMARTICALS: {
        if ("layout/item_articals_0".equals(tag)) {
          return new ItemArticalsBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for item_articals is invalid. Received: " + tag);
      }
      case  LAYOUT_ITEMARTICALSNOTIFICATION: {
        if ("layout/item_articals_notification_0".equals(tag)) {
          return new ItemArticalsNotificationBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for item_articals_notification is invalid. Received: " + tag);
      }
      case  LAYOUT_ITEMFAQ: {
        if ("layout/item_faq_0".equals(tag)) {
          return new ItemFaqBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for item_faq is invalid. Received: " + tag);
      }
      case  LAYOUT_ITEMLATESTNEWS: {
        if ("layout/item_latest_news_0".equals(tag)) {
          return new ItemLatestNewsBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for item_latest_news is invalid. Received: " + tag);
      }
      case  LAYOUT_ITEMLEADERBOARD: {
        if ("layout/item_leader_board_0".equals(tag)) {
          return new ItemLeaderBoardBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for item_leader_board is invalid. Received: " + tag);
      }
      case  LAYOUT_ITEMMAKEADMIN: {
        if ("layout/item_makeadmin_0".equals(tag)) {
          return new ItemMakeadminBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for item_makeadmin is invalid. Received: " + tag);
      }
      case  LAYOUT_ITEMMESSAGELEFT: {
        if ("layout/item_message_left_0".equals(tag)) {
          return new ItemMessageLeftBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for item_message_left is invalid. Received: " + tag);
      }
      case  LAYOUT_ITEMMESSAGELEFTIMAGE: {
        if ("layout/item_message_left_image_0".equals(tag)) {
          return new ItemMessageLeftImageBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for item_message_left_image is invalid. Received: " + tag);
      }
      case  LAYOUT_ITEMMESSAGERIGHT: {
        if ("layout/item_message_right_0".equals(tag)) {
          return new ItemMessageRightBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for item_message_right is invalid. Received: " + tag);
      }
      case  LAYOUT_ITEMMESSAGERIGHTIMAGE: {
        if ("layout/item_message_right_image_0".equals(tag)) {
          return new ItemMessageRightImageBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for item_message_right_image is invalid. Received: " + tag);
      }
      case  LAYOUT_ITEMOVERVIEW: {
        if ("layout/item_overview_0".equals(tag)) {
          return new ItemOverviewBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for item_overview is invalid. Received: " + tag);
      }
      case  LAYOUT_ITEMPERSONAL: {
        if ("layout/item_personal_0".equals(tag)) {
          return new ItemPersonalBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for item_personal is invalid. Received: " + tag);
      }
      case  LAYOUT_ITEMPUBLICROOM: {
        if ("layout/item_publicroom_0".equals(tag)) {
          return new ItemPublicroomBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for item_publicroom is invalid. Received: " + tag);
      }
      case  LAYOUT_ITEMQUESTION: {
        if ("layout/item_question_0".equals(tag)) {
          return new ItemQuestionBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for item_question is invalid. Received: " + tag);
      }
      case  LAYOUT_ITEMQUESTIONSUBJECT: {
        if ("layout/item_question_subject_0".equals(tag)) {
          return new ItemQuestionSubjectBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for item_question_subject is invalid. Received: " + tag);
      }
      case  LAYOUT_ITEMSYLLABUSSUBJECT: {
        if ("layout/item_syllabus_subject_0".equals(tag)) {
          return new ItemSyllabusSubjectBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for item_syllabus_subject is invalid. Received: " + tag);
      }
      case  LAYOUT_ITEMUNNNEWS: {
        if ("layout/item_unn_news_0".equals(tag)) {
          return new ItemUnnNewsBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for item_unn_news is invalid. Received: " + tag);
      }
      case  LAYOUT_OUTSMSMEDIA: {
        if ("layout/out_sms_media_0".equals(tag)) {
          return new OutSmsMediaBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for out_sms_media is invalid. Received: " + tag);
      }
      case  LAYOUT_OUTSMSTEXT: {
        if ("layout/out_sms_text_0".equals(tag)) {
          return new OutSmsTextBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for out_sms_text is invalid. Received: " + tag);
      }
      case  LAYOUT_PAYMENTREQUESTDIALOG: {
        if ("layout/payment_request_dialog_0".equals(tag)) {
          return new PaymentRequestDialogBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for payment_request_dialog is invalid. Received: " + tag);
      }
      case  LAYOUT_PHONENUMBERREQUESTDIALOG: {
        if ("layout/phone_number_request_dialog_0".equals(tag)) {
          return new PhoneNumberRequestDialogBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for phone_number_request_dialog is invalid. Received: " + tag);
      }
      case  LAYOUT_PREVIEWQUESTIONITEM: {
        if ("layout/preview_question_item_0".equals(tag)) {
          return new PreviewQuestionItemBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for preview_question_item is invalid. Received: " + tag);
      }
      case  LAYOUT_QUESTIONITEM: {
        if ("layout/question_item_0".equals(tag)) {
          return new QuestionItemBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for question_item is invalid. Received: " + tag);
      }
      case  LAYOUT_RECHARGEPAYMENTREQUESTDIALOG: {
        if ("layout/recharge_payment_request_dialog_0".equals(tag)) {
          return new RechargePaymentRequestDialogBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for recharge_payment_request_dialog is invalid. Received: " + tag);
      }
      case  LAYOUT_ROWITEMYEAR: {
        if ("layout/row_item_year_0".equals(tag)) {
          return new RowItemYearBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for row_item_year is invalid. Received: " + tag);
      }
      case  LAYOUT_SELECTEXAMSUBJECTVIEW: {
        if ("layout/select_exam_subject_view_0".equals(tag)) {
          return new SelectExamSubjectViewBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for select_exam_subject_view is invalid. Received: " + tag);
      }
      case  LAYOUT_SELECTYEARVIEW: {
        if ("layout/select_year_view_0".equals(tag)) {
          return new SelectYearViewBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for select_year_view is invalid. Received: " + tag);
      }
      case  LAYOUT_TOOLBARLAYOUT: {
        if ("layout/toolbar_layout_0".equals(tag)) {
          return new ToolbarLayoutBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for toolbar_layout is invalid. Received: " + tag);
      }
      case  LAYOUT_UNNNEWSFRAGMENT: {
        if ("layout/unn_news_fragment_0".equals(tag)) {
          return new UnnNewsFragmentBindingImpl(component, view);
        }
        throw new IllegalArgumentException("The tag for unn_news_fragment is invalid. Received: " + tag);
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View view, int layoutId) {
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = view.getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      // find which method will have it. -1 is necessary becausefirst id starts with 1;
      int methodIndex = (localizedLayoutId - 1) / 50;
      switch(methodIndex) {
        case 0: {
          return internalGetViewDataBinding0(component, view, localizedLayoutId, tag);
        }
        case 1: {
          return internalGetViewDataBinding1(component, view, localizedLayoutId, tag);
        }
      }
    }
    return null;
  }

  @Override
  public ViewDataBinding getDataBinder(DataBindingComponent component, View[] views, int layoutId) {
    if(views == null || views.length == 0) {
      return null;
    }
    int localizedLayoutId = INTERNAL_LAYOUT_ID_LOOKUP.get(layoutId);
    if(localizedLayoutId > 0) {
      final Object tag = views[0].getTag();
      if(tag == null) {
        throw new RuntimeException("view must have a tag");
      }
      switch(localizedLayoutId) {
      }
    }
    return null;
  }

  @Override
  public int getLayoutId(String tag) {
    if (tag == null) {
      return 0;
    }
    Integer tmpVal = InnerLayoutIdLookup.sKeys.get(tag);
    return tmpVal == null ? 0 : tmpVal;
  }

  @Override
  public String convertBrIdToString(int localId) {
    String tmpVal = InnerBrLookup.sKeys.get(localId);
    return tmpVal;
  }

  @Override
  public List<DataBinderMapper> collectDependencies() {
    ArrayList<DataBinderMapper> result = new ArrayList<DataBinderMapper>(1);
    result.add(new androidx.databinding.library.baseAdapters.DataBinderMapperImpl());
    return result;
  }

  private static class InnerBrLookup {
    static final SparseArray<String> sKeys = new SparseArray<String>(6);

    static {
      sKeys.put(0, "_all");
      sKeys.put(1, "article");
      sKeys.put(2, "newsitem");
      sKeys.put(3, "questionitem");
      sKeys.put(4, "subjectname");
      sKeys.put(5, "yearitem");
    }
  }

  private static class InnerLayoutIdLookup {
    static final HashMap<String, Integer> sKeys = new HashMap<String, Integer>(83);

    static {
      sKeys.put("layout/activity_article_readmore_0", com.iotait.schoolapp.R.layout.activity_article_readmore);
      sKeys.put("layout/activity_authentication_0", com.iotait.schoolapp.R.layout.activity_authentication);
      sKeys.put("layout/activity_chat_list_0", com.iotait.schoolapp.R.layout.activity_chat_list);
      sKeys.put("layout/activity_chat_window_0", com.iotait.schoolapp.R.layout.activity_chat_window);
      sKeys.put("layout/activity_chat_window_group_0", com.iotait.schoolapp.R.layout.activity_chat_window_group);
      sKeys.put("layout/activity_details_news_web_view_0", com.iotait.schoolapp.R.layout.activity_details_news_web_view);
      sKeys.put("layout/activity_full_photo_view_0", com.iotait.schoolapp.R.layout.activity_full_photo_view);
      sKeys.put("layout/activity_group_admin_0", com.iotait.schoolapp.R.layout.activity_group_admin);
      sKeys.put("layout/activity_home_navigation_0", com.iotait.schoolapp.R.layout.activity_home_navigation);
      sKeys.put("layout/activity_leaderboardinfo_0", com.iotait.schoolapp.R.layout.activity_leaderboardinfo);
      sKeys.put("layout/activity_login_0", com.iotait.schoolapp.R.layout.activity_login);
      sKeys.put("layout/activity_sign_up_0", com.iotait.schoolapp.R.layout.activity_sign_up);
      sKeys.put("layout/activity_splash_0", com.iotait.schoolapp.R.layout.activity_splash);
      sKeys.put("layout/banner_layout_0", com.iotait.schoolapp.R.layout.banner_layout);
      sKeys.put("layout/conformation_dialoge_0", com.iotait.schoolapp.R.layout.conformation_dialoge);
      sKeys.put("layout/custom_image_viwer_0", com.iotait.schoolapp.R.layout.custom_image_viwer);
      sKeys.put("layout/dialog_point_0", com.iotait.schoolapp.R.layout.dialog_point);
      sKeys.put("layout/dialog_result_0", com.iotait.schoolapp.R.layout.dialog_result);
      sKeys.put("layout/faq_image_dialog_0", com.iotait.schoolapp.R.layout.faq_image_dialog);
      sKeys.put("layout/fragment_about_app_0", com.iotait.schoolapp.R.layout.fragment_about_app);
      sKeys.put("layout/fragment_all_time_0", com.iotait.schoolapp.R.layout.fragment_all_time);
      sKeys.put("layout/fragment_articales_0", com.iotait.schoolapp.R.layout.fragment_articales);
      sKeys.put("layout/fragment_banking_system_0", com.iotait.schoolapp.R.layout.fragment_banking_system);
      sKeys.put("layout/fragment_contactus_0", com.iotait.schoolapp.R.layout.fragment_contactus);
      sKeys.put("layout/fragment_contest_result_0", com.iotait.schoolapp.R.layout.fragment_contest_result);
      sKeys.put("layout/fragment_custom_exam_0", com.iotait.schoolapp.R.layout.fragment_custom_exam);
      sKeys.put("layout/fragment_custom_exam_details_0", com.iotait.schoolapp.R.layout.fragment_custom_exam_details);
      sKeys.put("layout/fragment_exam_preview_0", com.iotait.schoolapp.R.layout.fragment_exam_preview);
      sKeys.put("layout/fragment_full_exam_0", com.iotait.schoolapp.R.layout.fragment_full_exam);
      sKeys.put("layout/fragment_get_reward_0", com.iotait.schoolapp.R.layout.fragment_get_reward);
      sKeys.put("layout/fragment_home_0", com.iotait.schoolapp.R.layout.fragment_home);
      sKeys.put("layout/fragment_leader_board_0", com.iotait.schoolapp.R.layout.fragment_leader_board);
      sKeys.put("layout/fragment_lear_more_for_premium_0", com.iotait.schoolapp.R.layout.fragment_lear_more_for_premium);
      sKeys.put("layout/fragment_learn_more_for_besic_0", com.iotait.schoolapp.R.layout.fragment_learn_more_for_besic);
      sKeys.put("layout/fragment_mobile_recharge_0", com.iotait.schoolapp.R.layout.fragment_mobile_recharge);
      sKeys.put("layout/fragment_monthly_top_0", com.iotait.schoolapp.R.layout.fragment_monthly_top);
      sKeys.put("layout/fragment_navigation_0", com.iotait.schoolapp.R.layout.fragment_navigation);
      sKeys.put("layout/fragment_notification_0", com.iotait.schoolapp.R.layout.fragment_notification);
      sKeys.put("layout/fragment_online_payment_0", com.iotait.schoolapp.R.layout.fragment_online_payment);
      sKeys.put("layout/fragment_premium_process_0", com.iotait.schoolapp.R.layout.fragment_premium_process);
      sKeys.put("layout/fragment_question_0", com.iotait.schoolapp.R.layout.fragment_question);
      sKeys.put("layout/fragment_question_details_0", com.iotait.schoolapp.R.layout.fragment_question_details);
      sKeys.put("layout/fragment_school_anthem_0", com.iotait.schoolapp.R.layout.fragment_school_anthem);
      sKeys.put("layout/fragment_syllabus_0", com.iotait.schoolapp.R.layout.fragment_syllabus);
      sKeys.put("layout/fragment_syllabus_item_click_0", com.iotait.schoolapp.R.layout.fragment_syllabus_item_click);
      sKeys.put("layout/fragment_take_contest_exam_0", com.iotait.schoolapp.R.layout.fragment_take_contest_exam);
      sKeys.put("layout/fragment_take_exam_0", com.iotait.schoolapp.R.layout.fragment_take_exam);
      sKeys.put("layout/fragment_unn_faq_0", com.iotait.schoolapp.R.layout.fragment_unn_faq);
      sKeys.put("layout/fragment_weekly_contest_0", com.iotait.schoolapp.R.layout.fragment_weekly_contest);
      sKeys.put("layout/fragment_weekly_top_0", com.iotait.schoolapp.R.layout.fragment_weekly_top);
      sKeys.put("layout/image_slider_layout_0", com.iotait.schoolapp.R.layout.image_slider_layout);
      sKeys.put("layout/in_sms_media_0", com.iotait.schoolapp.R.layout.in_sms_media);
      sKeys.put("layout/in_sms_text_0", com.iotait.schoolapp.R.layout.in_sms_text);
      sKeys.put("layout/item_activenow_0", com.iotait.schoolapp.R.layout.item_activenow);
      sKeys.put("layout/item_articals_0", com.iotait.schoolapp.R.layout.item_articals);
      sKeys.put("layout/item_articals_notification_0", com.iotait.schoolapp.R.layout.item_articals_notification);
      sKeys.put("layout/item_faq_0", com.iotait.schoolapp.R.layout.item_faq);
      sKeys.put("layout/item_latest_news_0", com.iotait.schoolapp.R.layout.item_latest_news);
      sKeys.put("layout/item_leader_board_0", com.iotait.schoolapp.R.layout.item_leader_board);
      sKeys.put("layout/item_makeadmin_0", com.iotait.schoolapp.R.layout.item_makeadmin);
      sKeys.put("layout/item_message_left_0", com.iotait.schoolapp.R.layout.item_message_left);
      sKeys.put("layout/item_message_left_image_0", com.iotait.schoolapp.R.layout.item_message_left_image);
      sKeys.put("layout/item_message_right_0", com.iotait.schoolapp.R.layout.item_message_right);
      sKeys.put("layout/item_message_right_image_0", com.iotait.schoolapp.R.layout.item_message_right_image);
      sKeys.put("layout/item_overview_0", com.iotait.schoolapp.R.layout.item_overview);
      sKeys.put("layout/item_personal_0", com.iotait.schoolapp.R.layout.item_personal);
      sKeys.put("layout/item_publicroom_0", com.iotait.schoolapp.R.layout.item_publicroom);
      sKeys.put("layout/item_question_0", com.iotait.schoolapp.R.layout.item_question);
      sKeys.put("layout/item_question_subject_0", com.iotait.schoolapp.R.layout.item_question_subject);
      sKeys.put("layout/item_syllabus_subject_0", com.iotait.schoolapp.R.layout.item_syllabus_subject);
      sKeys.put("layout/item_unn_news_0", com.iotait.schoolapp.R.layout.item_unn_news);
      sKeys.put("layout/out_sms_media_0", com.iotait.schoolapp.R.layout.out_sms_media);
      sKeys.put("layout/out_sms_text_0", com.iotait.schoolapp.R.layout.out_sms_text);
      sKeys.put("layout/payment_request_dialog_0", com.iotait.schoolapp.R.layout.payment_request_dialog);
      sKeys.put("layout/phone_number_request_dialog_0", com.iotait.schoolapp.R.layout.phone_number_request_dialog);
      sKeys.put("layout/preview_question_item_0", com.iotait.schoolapp.R.layout.preview_question_item);
      sKeys.put("layout/question_item_0", com.iotait.schoolapp.R.layout.question_item);
      sKeys.put("layout/recharge_payment_request_dialog_0", com.iotait.schoolapp.R.layout.recharge_payment_request_dialog);
      sKeys.put("layout/row_item_year_0", com.iotait.schoolapp.R.layout.row_item_year);
      sKeys.put("layout/select_exam_subject_view_0", com.iotait.schoolapp.R.layout.select_exam_subject_view);
      sKeys.put("layout/select_year_view_0", com.iotait.schoolapp.R.layout.select_year_view);
      sKeys.put("layout/toolbar_layout_0", com.iotait.schoolapp.R.layout.toolbar_layout);
      sKeys.put("layout/unn_news_fragment_0", com.iotait.schoolapp.R.layout.unn_news_fragment);
    }
  }
}
