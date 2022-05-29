package com.iotait.schoolapp.ui.homepage.ui.notification;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.database.tables.ArticleNotification;
import com.iotait.schoolapp.databinding.FragmentNotificationBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.FragmentHelper;
import com.iotait.schoolapp.helper.NetworkHelper;
import com.iotait.schoolapp.ui.homepage.ui.articals.View.ArticleView;
import com.iotait.schoolapp.ui.homepage.ui.articals.articlemodel.ArticleModel;
import com.iotait.schoolapp.ui.homepage.ui.articlereadmore.ArticleReadmoreActivity;
import com.iotait.schoolapp.ui.homepage.ui.notification.adapter.NotificationArticalsAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment implements ArticleView {

    private FragmentNotificationBinding notificationBinding;
    private NotificationArticalsAdapter articalsAdapter;
    private List<ArticleModel> articleModels;

    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        notificationBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_notification,container,false);
        return notificationBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        notificationBinding.includeToolbar.toolbarTitle.setText("UNREAD ARTICLES");
        ((AppCompatActivity) getActivity()).setSupportActionBar(notificationBinding.includeToolbar.toolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        notificationBinding.includeToolbar.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                FragmentHelper.changeFragmet(notificationBinding.getRoot(), R.id.action_notificationFragment_to_nav_home, bundle);
            }
        });
        articleModels=new ArrayList<>();
        articalsAdapter=new NotificationArticalsAdapter(getContext(),articleModels, this);
        articalsAdapter.setHasStableIds(true);
        notificationBinding.recyclerNotification.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationBinding.recyclerNotification.setHasFixedSize(true);
        notificationBinding.recyclerNotification.setNestedScrollingEnabled(false);
        notificationBinding.recyclerNotification.setAdapter(articalsAdapter);

        if (AppController.getDatabase().myDao().getArticles().size()==0){
            new CustomMessage(getActivity(),"No new article available");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getArticle();
    }

    private void getArticle() {
        if (NetworkHelper.hasNetworAccess(getContext())){
            notificationBinding.spinKit.setVisibility(View.VISIBLE);
            AppController.getFirebaseHelper().getArticles().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    notificationBinding.spinKit.setVisibility(View.GONE);
                    if (dataSnapshot.exists()) {
                        articleModels.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                            String Article = "";
                            String ArticleId = "";
                            String ArticlePublishedDate = "";
                            String ArticleTitle = "";
                            int Dislike = 0;
                            int Like = 0;
                            String WriterDesignation = "";
                            String WriterImage = "";
                            String WriterName = "";
                            List<String> peoplesLike = new ArrayList<>();
                            List<String> peoplesDislike = new ArrayList<>();
                            if (snapshot.child("Article").getValue() != null)
                                Article = snapshot.child("Article").getValue(String.class);
                            if (snapshot.child("ArticleId").getValue() != null)
                                ArticleId = snapshot.child("ArticleId").getValue(String.class);
                            if (snapshot.child("ArticlePublishedDate").getValue() != null)
                                ArticlePublishedDate = snapshot.child("ArticlePublishedDate").getValue(String.class);
                            if (snapshot.child("ArticleTitle").getValue() != null)
                                ArticleTitle = snapshot.child("ArticleTitle").getValue(String.class);
                            if (snapshot.child("Like").getValue() != null)
                                Like = snapshot.child("Like").getValue(Integer.class);
                            if (snapshot.child("Dislike").getValue() != null)
                                Dislike = snapshot.child("Dislike").getValue(Integer.class);
                            if (snapshot.child("WriterDesignation").getValue() != null)
                                WriterDesignation = snapshot.child("WriterDesignation").getValue(String.class);

                            if (snapshot.child("WriterImage").getValue() != null)
                                WriterImage = snapshot.child("WriterImage").getValue(String.class);
                            if (snapshot.child("WriterName").getValue() != null)
                                WriterName = snapshot.child("WriterName").getValue(String.class);


                            if (snapshot.child("peoplesLike").getChildrenCount() > 0) {
                                for (DataSnapshot people : snapshot.child("peoplesLike").getChildren()) {
                                    String personid = people.child("personid").getValue(String.class);
                                    peoplesLike.add(personid);
                                }
                            }

                            if (snapshot.child("peoplesDislike").getChildrenCount() > 0) {
                                for (DataSnapshot people : snapshot.child("peoplesDislike").getChildren()) {
                                    String personid = people.child("personid").getValue(String.class);
                                    peoplesDislike.add(personid);
                                }
                            }


                            ArticleModel articleModel = new ArticleModel();
                            articleModel.setArticle(Article);
                            articleModel.setArticleId(ArticleId);
                            articleModel.setArticleTitle(ArticleTitle);
                            articleModel.setArticlePublishedDate(ArticlePublishedDate);
                            articleModel.setLike(Like);
                            articleModel.setDislike(Dislike);
                            articleModel.setWriterDesignation(WriterDesignation);
                            articleModel.setWriterName(WriterName);
                            articleModel.setWriterImage(WriterImage);
                            articleModel.setPeoplesLike(peoplesLike);
                            articleModel.setPeoplesDislike(peoplesDislike);

                            for (ArticleNotification articleNotification:AppController.getDatabase().myDao().getArticles()){
                                if (articleNotification.getArticleId().equals(articleModel.getArticleId())){
                                    articleModels.add(articleModel);
                                }
                            }
                        }
                        Collections.reverse(articleModels);
                        if (articleModels.size()==0){
                            new CustomMessage(getActivity(),"No new article available");
                            for (ArticleNotification articleNotification:AppController.getDatabase().myDao().getArticles()){
                                AppController.getDatabase().myDao().delteArticle(articleNotification.getArticleId());
                            }
                        }
                        articalsAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    notificationBinding.spinKit.setVisibility(View.GONE);
                }
            });
        }
        else {
            new CustomMessage(getActivity(), "No internet connection");
        }

    }

    @Override
    public void onArticleLike(ArticleModel articleId) {

    }

    @Override
    public void onArticleDislike(ArticleModel articleId) {

    }

    @Override
    public void onArticleReadmoreClick(String articleId, List<String> peoplesDislike, List<String> peoplesLike, String writerImage, String article, String articlePublishedDate, String writerDesignation, String writerName, String articleTitle, int like, int dislike) {
        Intent i = new Intent(getContext(), ArticleReadmoreActivity.class);
        i.putExtra("articleId", articleId);
        i.putStringArrayListExtra("peoplesDislike", (ArrayList<String>) peoplesDislike);
        i.putStringArrayListExtra("peoplesLike", (ArrayList<String>) peoplesLike);
        i.putExtra("writerImage", writerImage);
        i.putExtra("article", article);
        i.putExtra("articlePublishedDate", articlePublishedDate);
        i.putExtra("writerDesignation", writerDesignation);
        i.putExtra("writerName", writerName);
        i.putExtra("articleTitle", articleTitle);
        startActivity(i);
        deleteBdRow(articleId);
    }

    private void deleteBdRow(String articleId) {
        AppController.getDatabase().myDao().delteArticle(articleId);
    }
}
