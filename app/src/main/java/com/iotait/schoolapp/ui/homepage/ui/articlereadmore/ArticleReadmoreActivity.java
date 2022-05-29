package com.iotait.schoolapp.ui.homepage.ui.articlereadmore;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.iotait.schoolapp.BuildConfig;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.databinding.ActivityArticleReadmoreBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.Customlog;
import com.iotait.schoolapp.helper.FirebaseHelper;
import com.iotait.schoolapp.helper.UIHelper;
import com.iotait.schoolapp.ui.homepage.ui.articals.articlemodel.ArticleModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ArticleReadmoreActivity extends AppCompatActivity implements View.OnClickListener {
    ActivityArticleReadmoreBinding binding;

    FirebaseHelper firebaseHelper;

    List<ArticleModel> models;
    List<String> likeUid;
    List<String> DislikeUid;

    ArticleModel articleModel;

    String ArticleId;
    ArrayList<String> peoplesDislike;
    ArrayList<String> peoplesLike;
    String writerImage = "";
    String article = "";
    String articlePublishedDate = "";
    String writerDesignation = "";
    String writerName = "";
    String articleTitle = "";
    int like = 0;
    int dislike = 0;

    private AdRequest adRequest;
    private String premium_type="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_article_readmore);
        adRequest = new AdRequest.Builder().build();
        likeUid = new ArrayList<>();
        DislikeUid = new ArrayList<>();
        models = new ArrayList<>();
        firebaseHelper = new FirebaseHelper();


        if (getIntent().getStringExtra("articleId") != null) {
            ArticleId = getIntent().getStringExtra("articleId");
        }
        if (getIntent().getStringArrayListExtra("peoplesDislike") != null) {
            peoplesDislike = getIntent().getStringArrayListExtra("peoplesDislike");
        }
        if (getIntent().getStringArrayListExtra("peoplesLike") != null) {
            peoplesLike = getIntent().getStringArrayListExtra("peoplesLike");
        }
        if (getIntent().getStringExtra("writerImage") != null) {
            writerImage = getIntent().getStringExtra("writerImage");
        }
        if (getIntent().getStringExtra("article") != null) {
            article = getIntent().getStringExtra("article");
        }
        if (getIntent().getStringExtra("articlePublishedDate") != null) {
            articlePublishedDate = getIntent().getStringExtra("articlePublishedDate");
        }
        if (getIntent().getStringExtra("writerDesignation") != null) {
            writerDesignation = getIntent().getStringExtra("writerDesignation");
        }
        if (getIntent().getStringExtra("writerName") != null) {
            writerName = getIntent().getStringExtra("writerName");
        }
        if (getIntent().getStringExtra("articleTitle") != null) {
            articleTitle = getIntent().getStringExtra("articleTitle");
        }

        dislike = getIntent().getIntExtra("dislike", 0);
        like = getIntent().getIntExtra("like", 0);

        binding.title.setText(articleTitle);
        binding.writername.setText(writerName);
        binding.writerDesignation.setText(writerDesignation);
        binding.pubDate.setText(articlePublishedDate);
        UIHelper.setArticleImage(this, binding.articleprofile, writerImage);
        binding.articlemain.setText(article);
        binding.imagedislike.setOnClickListener(this);
        binding.imagelike.setOnClickListener(this);
        binding.backbutton.setOnClickListener(this);
        binding.loshare.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLikeDislike();
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        premium_type = pref.getString("premiumType", "");
        Customlog.showlogD("CURRENT_STATE",premium_type);
        if (TextUtils.equals(premium_type,"p0")){
            loadAd();
        }


    }

    private void loadAd() {
        binding.admobBanner.adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                binding.admobBanner.adView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

            }

            @Override
            public void onAdOpened() {

            }

            @Override
            public void onAdClicked() {

            }

            @Override
            public void onAdLeftApplication() {

            }

            @Override
            public void onAdClosed() {

            }
        });
        binding.admobBanner.adView.loadAd(adRequest);
    }

    private void getuserLikeOrDislikeitem(List<String> peoplesDislike, List<String> peoplesLike) {
        for (String uid : peoplesLike) {
            if (uid.equals(AppController.getFirebaseHelper().getFirebaseAuth().getUid())) {
                binding.imagelike.setImageResource(R.drawable.likeclick);
                break;
            } else {
                binding.imagelike.setImageResource(R.drawable.likewhite);
            }
        }
        if (peoplesLike.size() == 0) {
            binding.imagelike.setImageResource(R.drawable.likewhite);
        }

        for (String uid : peoplesDislike) {
            if (uid.equals(AppController.getFirebaseHelper().getFirebaseAuth().getUid())) {
                binding.imagedislike.setImageResource(R.drawable.dislikeclick);
                break;
            } else {
                binding.imagedislike.setImageResource(R.drawable.dislikewhite);
            }
        }

        if (peoplesDislike.size() == 0) {
            binding.imagedislike.setImageResource(R.drawable.dislikewhite);
        }
    }

    List<String> personLikedId = new ArrayList<>();


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imagelike:
                imagelikeclick();
                break;
            case R.id.imagedislike:
                imageDislikeClick();
                break;
            case R.id.backbutton:
                finish();
            case R.id.loshare:
                share();

                break;
        }
    }

    private void share() {
        try {
            String shareArticle100 = "";
            String shareArticleTitle = "";

            if(binding.articlemain.getText().length() < 100) {
                shareArticle100 = binding.articlemain.getText().toString() + " ...";
            }
            else {
                shareArticle100 = binding.articlemain.getText().subSequence(0,100).toString() + " ...";
            }

            shareArticleTitle = binding.title.getText().toString();


            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
            String shareMessage= "Article Title: " + shareArticleTitle + "\n\n" + shareArticle100 + "\n\n" + " To read up the Complete Article, open or download " + getString(R.string.app_name) + " App from \n\n" ;
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n And then check the Articles section.";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));



        } catch(Exception e) {
            //e.toString();
        }
    }

    private void imageDislikeClick() {
        if (!writerImage.equals("") && !article.equals("") && !article.equals("") && !articlePublishedDate.equals("") && !writerDesignation.equals("")
                && !writerName.equals("") && !articleTitle.equals("") && !ArticleId.equals("")) {
            AppController.getFirebaseHelper().getArticles().child(ArticleId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    DislikeUid.clear();
                    if (dataSnapshot.exists()) {
                        if (dataSnapshot.child("peoplesDislike").getChildrenCount() > 0) {
                            for (DataSnapshot people : dataSnapshot.child("peoplesDislike").getChildren()) {
                                final String personid = people.child("personid").getValue(String.class);
                                DislikeUid.add(personid);
                            }

                            if (DislikeUid.contains(AppController.getFirebaseHelper().getFirebaseAuth().getUid())) {
                                // Toast.makeText(getContext(), "You Already Dislike this Article", Toast.LENGTH_SHORT).show();
                                new CustomMessage(ArticleReadmoreActivity.this, "You Already Dislike this Article");
                            } else {
                                String newkey = AppController.getFirebaseHelper().getArticles().child(ArticleId).getRef().child("peoplesDislike").push().getKey();
                                onLikeRemove(newkey);
                                new CustomMessage(ArticleReadmoreActivity.this, "You Dislike this Article");
                            }
                        } else {
                            String newkey = AppController.getFirebaseHelper().getArticles().child(ArticleId).getRef().child("peoplesDislike").push().getKey();

                            onLikeRemove(newkey);
                            new CustomMessage(ArticleReadmoreActivity.this, "You Dislike this Article");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            new CustomMessage(ArticleReadmoreActivity.this, "Something Wrong with this articles.we will take action soon.Thank you.");
        }
    }

    List<String> personDislikeId = new ArrayList<>();

    private void getLikeDislike() {
        AppController.getFirebaseHelper().getArticles().child(ArticleId).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            models.clear();
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
                            if (dataSnapshot.child("Article").getValue() != null)
                                Article = dataSnapshot.child("Article").getValue(String.class);
                            if (dataSnapshot.child("ArticleId").getValue() != null)
                                ArticleId = dataSnapshot.child("ArticleId").getValue(String.class);
                            if (dataSnapshot.child("ArticlePublishedDate").getValue() != null)
                                ArticlePublishedDate = dataSnapshot.child("ArticlePublishedDate").getValue(String.class);
                            if (dataSnapshot.child("ArticleTitle").getValue() != null)
                                ArticleTitle = dataSnapshot.child("ArticleTitle").getValue(String.class);
                            if (dataSnapshot.child("Like").getValue() != null)
                                Like = dataSnapshot.child("Like").getValue(Integer.class);
                            if (dataSnapshot.child("Dislike").getValue() != null)
                                Dislike = dataSnapshot.child("Dislike").getValue(Integer.class);
                            if (dataSnapshot.child("WriterDesignation").getValue() != null)
                                WriterDesignation = dataSnapshot.child("WriterDesignation").getValue(String.class);

                            if (dataSnapshot.child("WriterImage").getValue() != null)
                                WriterImage = dataSnapshot.child("WriterImage").getValue(String.class);
                            if (dataSnapshot.child("WriterName").getValue() != null)
                                WriterName = dataSnapshot.child("WriterName").getValue(String.class);


                            if (dataSnapshot.child("peoplesLike").getChildrenCount() > 0) {
                                for (DataSnapshot people : dataSnapshot.child("peoplesLike").getChildren()) {
                                    String personid = people.child("personid").getValue(String.class);
                                    peoplesLike.add(personid);
                                }
                            }

                            if (dataSnapshot.child("peoplesDislike").getChildrenCount() > 0) {
                                for (DataSnapshot people : dataSnapshot.child("peoplesDislike").getChildren()) {
                                    String personid = people.child("personid").getValue(String.class);
                                    peoplesDislike.add(personid);
                                }
                            }


                            like = Like;
                            dislike = Dislike;
                            articleModel = new ArticleModel();
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

                            models.add(articleModel);

                            getuserLikeOrDislikeitem(peoplesDislike, peoplesLike);
                            binding.like.setText(String.valueOf(articleModel.getLike()));
                            binding.dislike.setText(String.valueOf(articleModel.getDislike()));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }

    private void imagelikeclick() {
        if (!writerImage.equals("") && !article.equals("") && !article.equals("") && !articlePublishedDate.equals("") && !writerDesignation.equals("")
                && !writerName.equals("") && !articleTitle.equals("") && !ArticleId.equals("")) {
            AppController.getFirebaseHelper().getArticles().child(ArticleId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    likeUid.clear();
                    if (dataSnapshot.exists()) {
                        if (dataSnapshot.child("peoplesLike").getChildrenCount() > 0) {
                            for (DataSnapshot people : dataSnapshot.child("peoplesLike").getChildren()) {
                                final String personid = people.child("personid").getValue(String.class);
                                likeUid.add(personid);
                            }

                            if (likeUid.contains(AppController.getFirebaseHelper().getFirebaseAuth().getUid())) {
                                new CustomMessage(ArticleReadmoreActivity.this, "You Already Like this Article");
                            } else {
                                String newkey = AppController.getFirebaseHelper().getArticles().child(ArticleId).getRef().child("peoplesLike").push().getKey();
                                onDislikeRemove(newkey);
                                new CustomMessage(ArticleReadmoreActivity.this, "You Liked this Article");
                            }
                        } else {
                            String newkey = AppController.getFirebaseHelper().getArticles().child(ArticleId).getRef().child("peoplesLike").push().getKey();

                            onDislikeRemove(newkey);
                            new CustomMessage(ArticleReadmoreActivity.this, "You Liked this Article");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            new CustomMessage(ArticleReadmoreActivity.this, "Something Wrong with this articles.we will take action soon.Thank you.");
        }
    }

    private void onLikeRemove(final String newKey) {
        AppController.getFirebaseHelper().getArticles().child(ArticleId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                personLikedId.clear();
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("peoplesLike").getChildrenCount() > 0) {

                        for (DataSnapshot people : dataSnapshot.child("peoplesLike").getChildren()) {
                            final String personid = people.child("personid").getValue(String.class);

                            personLikedId.add(personid);
                            if (personid.equals(AppController.getFirebaseHelper().getFirebaseAuth().getUid())) {
                                AppController.getFirebaseHelper().getArticles().child(ArticleId).child("peoplesLike").child(people.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            int likecount = like;

                                            int dislikecount = dislike;

                                            HashMap<String, Object> user = new HashMap<>();

                                            user.put("ArticleTitle", articleTitle);
                                            user.put("Article", article);
                                            user.put("WriterName", writerName);
                                            user.put("WriterDesignation", writerDesignation);
                                            user.put("ArticlePublishedDate", articlePublishedDate);
                                            user.put("WriterImage", writerImage);
                                            user.put("Like", likecount - 1);
                                            user.put("Dislike", dislikecount + 1);
                                            user.put("ArticleId", ArticleId);
                                            user.put("peoplesDislike" + "/" + newKey + "/" + "personid" + "/", AppController.getFirebaseHelper().getFirebaseAuth().getCurrentUser().getUid());

                                            AppController.getFirebaseHelper().getArticles().child(articleModel.getArticleId())
                                                    .updateChildren(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    getLikeDislike();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    new CustomMessage(ArticleReadmoreActivity.this, "" + e.getMessage());
                                                }
                                            });

                                        } else {
                                            Customlog.showlogD(TAG, task.getException().getMessage());
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Customlog.showlogD(TAG, e.getMessage());
                                    }
                                });

                            }
                        }

                        if (!personLikedId.contains(AppController.getFirebaseHelper().getFirebaseAuth().getUid())) {
                            int likecount = articleModel.getLike();
                            int dislikecount = articleModel.getDislike();

                            HashMap<String, Object> user = new HashMap<>();
                            user.put("ArticleTitle", articleModel.getArticleTitle());
                            user.put("Article", articleModel.getArticle());
                            user.put("WriterName", articleModel.getWriterName());
                            user.put("WriterDesignation", articleModel.getWriterDesignation());
                            user.put("ArticlePublishedDate", articleModel.getArticlePublishedDate());
                            user.put("WriterImage", articleModel.getWriterImage());
                            user.put("Like", likecount);
                            user.put("Dislike", dislikecount + 1);
                            user.put("ArticleId", articleModel.getArticleId());
                            user.put("peoplesDislike" + "/" + newKey + "/" + "personid" + "/", AppController.getFirebaseHelper().getFirebaseAuth().getCurrentUser().getUid());

                            AppController.getFirebaseHelper().getArticles().child(articleModel.getArticleId())
                                    .updateChildren(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    getLikeDislike();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    new CustomMessage(ArticleReadmoreActivity.this, "" + e.getMessage());
                                }
                            });
                        }


                    } else {

                        int likecount = like;
                        int dislikecount = dislike;
                        HashMap<String, Object> user = new HashMap<>();
                        user.put("ArticleTitle", articleTitle);
                        user.put("Article", article);
                        user.put("WriterName", writerName);
                        user.put("WriterDesignation", writerDesignation);
                        user.put("ArticlePublishedDate", articlePublishedDate);
                        user.put("WriterImage", writerImage);
                        user.put("Like", likecount);
                        user.put("Dislike", dislikecount + 1);
                        user.put("ArticleId", ArticleId);
                        user.put("peoplesDislike" + "/" + newKey + "/" + "personid" + "/", AppController.getFirebaseHelper().getFirebaseAuth().getCurrentUser().getUid());

                        AppController.getFirebaseHelper().getArticles().child(articleModel.getArticleId())
                                .updateChildren(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                getLikeDislike();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                new CustomMessage(ArticleReadmoreActivity.this, "" + e.getMessage());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void onDislikeRemove(final String newKey) {
        AppController.getFirebaseHelper().getArticles().child(ArticleId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                personDislikeId.clear();
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("peoplesDislike").getChildrenCount() > 0) {

                        for (DataSnapshot people : dataSnapshot.child("peoplesDislike").getChildren()) {
                            final String personid = people.child("personid").getValue(String.class);

                            personDislikeId.add(personid);
                            if (personid.equals(AppController.getFirebaseHelper().getFirebaseAuth().getUid())) {
                                AppController.getFirebaseHelper().getArticles().child(ArticleId).child("peoplesDislike").child(people.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            //   reduceDislikeCount(articleId);
                                            int likecount = like;

                                            int dislikecount = dislike;

                                            HashMap<String, Object> user = new HashMap<>();

                                            user.put("ArticleTitle", articleTitle);
                                            user.put("Article", article);
                                            user.put("WriterName", writerName);
                                            user.put("WriterDesignation", writerDesignation);
                                            user.put("ArticlePublishedDate", articlePublishedDate);
                                            user.put("WriterImage", writerImage);
                                            user.put("Like", likecount + 1);
                                            user.put("Dislike", dislikecount - 1);
                                            user.put("ArticleId", ArticleId);
                                            user.put("peoplesLike" + "/" + newKey + "/" + "personid" + "/", AppController.getFirebaseHelper().getFirebaseAuth().getCurrentUser().getUid());

                                            AppController.getFirebaseHelper().getArticles().child(articleModel.getArticleId())
                                                    .updateChildren(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    getLikeDislike();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    new CustomMessage(ArticleReadmoreActivity.this, "" + e.getMessage());
                                                }
                                            });


                                        } else {
                                            Customlog.showlogD(TAG, task.getException().getMessage());
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Customlog.showlogD(TAG, e.getMessage());
                                    }
                                });

                            }
                        }
                        if (!personDislikeId.contains(AppController.getFirebaseHelper().getFirebaseAuth().getUid())) {
                            int likecount = articleModel.getLike();
                            int dislikecount = articleModel.getDislike();

                            HashMap<String, Object> user = new HashMap<>();
                            user.put("ArticleTitle", articleModel.getArticleTitle());
                            user.put("Article", articleModel.getArticle());
                            user.put("WriterName", articleModel.getWriterName());
                            user.put("WriterDesignation", articleModel.getWriterDesignation());
                            user.put("ArticlePublishedDate", articleModel.getArticlePublishedDate());
                            user.put("WriterImage", articleModel.getWriterImage());
                            user.put("Like", likecount + 1);
                            user.put("Dislike", dislikecount);
                            user.put("ArticleId", articleModel.getArticleId());
                            user.put("peoplesLike" + "/" + newKey + "/" + "personid" + "/", AppController.getFirebaseHelper().getFirebaseAuth().getCurrentUser().getUid());

                            AppController.getFirebaseHelper().getArticles().child(articleModel.getArticleId())
                                    .updateChildren(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    getLikeDislike();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    new CustomMessage(ArticleReadmoreActivity.this, "" + e.getMessage());
                                }
                            });
                        }


                    } else {
                        int likecount = like;

                        int dislikecount = dislike;

                        HashMap<String, Object> user = new HashMap<>();

                        user.put("ArticleTitle", articleTitle);
                        user.put("Article", article);
                        user.put("WriterName", writerName);
                        user.put("WriterDesignation", writerDesignation);
                        user.put("ArticlePublishedDate", articlePublishedDate);
                        user.put("WriterImage", writerImage);
                        user.put("Like", likecount + 1);
                        user.put("Dislike", dislikecount);
                        user.put("ArticleId", ArticleId);
                        user.put("peoplesLike" + "/" + newKey + "/" + "personid" + "/", AppController.getFirebaseHelper().getFirebaseAuth().getCurrentUser().getUid());

                        AppController.getFirebaseHelper().getArticles().child(articleModel.getArticleId())
                                .updateChildren(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                getLikeDislike();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                new CustomMessage(ArticleReadmoreActivity.this, "" + e.getMessage());
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
