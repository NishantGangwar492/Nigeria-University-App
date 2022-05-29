package com.iotait.schoolapp.ui.homepage.ui.articals;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.iotait.schoolapp.R;
import com.iotait.schoolapp.application.AppController;
import com.iotait.schoolapp.databinding.FragmentArticalesBinding;
import com.iotait.schoolapp.helper.CustomMessage;
import com.iotait.schoolapp.helper.Customlog;
import com.iotait.schoolapp.helper.FirebaseHelper;
import com.iotait.schoolapp.helper.FragmentHelper;
import com.iotait.schoolapp.helper.NetworkHelper;
import com.iotait.schoolapp.ui.homepage.ui.articals.View.ArticleView;
import com.iotait.schoolapp.ui.homepage.ui.articals.articalsadapter.ArticalsAdapter;
import com.iotait.schoolapp.ui.homepage.ui.articals.articlemodel.ArticleModel;
import com.iotait.schoolapp.ui.homepage.ui.articlereadmore.ArticleReadmoreActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class Articales extends Fragment implements ArticleView, View.OnClickListener {

    FragmentArticalesBinding fragmentArticalesBinding;
    ArticalsAdapter articalsAdapter;
    FirebaseHelper firebaseHelper;
    List<ArticleModel> articleModelList;
    List<ArticleModel> articleModelListSearched;
    List<ArticleModel> articleModelListTemp;
    List<String> likeUid;
    List<String> DislikeUid;


    public Articales() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        fragmentArticalesBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_articales, container, false);

        View root = fragmentArticalesBinding.getRoot();

        firebaseHelper = new FirebaseHelper();
        articleModelList = new ArrayList<>();
        articleModelListTemp = new ArrayList<>();
        likeUid = new ArrayList<>();
        DislikeUid = new ArrayList<>();
        fragmentArticalesBinding.backbtn.setOnClickListener(this);
       // fragmentArticalesBinding.btnSearch.setOnClickListener(this);
       // fragmentArticalesBinding.btnShowAll.setOnClickListener(this);


        return root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        articalsAdapter = new ArticalsAdapter(getContext(), articleModelList, this);
        fragmentArticalesBinding.articalsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentArticalesBinding.articalsRecyclerView.setAdapter(articalsAdapter);

        setUpClickListener();
    }

    private void setUpClickListener() {
        fragmentArticalesBinding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String query) {
        ArrayList<ArticleModel> filterdNames = new ArrayList<>();
        for (ArticleModel userItem : articleModelList) {
            if (userItem.getArticleTitle().toLowerCase().contains(query.toLowerCase())) {
                filterdNames.add(userItem);
            }
        }
        articalsAdapter.filterList(filterdNames);
    }

    @Override
    public void onResume() {
        super.onResume();
        getArticle();
    }

    private void getArticle(List<ArticleModel> x,String s) {

        articleModelListSearched = new ArrayList<>();

        if (NetworkHelper.hasNetworAccess(getContext())) {
            fragmentArticalesBinding.spinKit.setVisibility(View.VISIBLE);

            for (int i=0; i<x.size(); i++){

                if (x.get(i).getArticleTitle().toLowerCase().contains(s.toLowerCase())){
                    articleModelListSearched.add(x.get(i));
                }
            }

            fragmentArticalesBinding.spinKit.setVisibility(View.GONE);

        } else {
            fragmentArticalesBinding.spinKit.setVisibility(View.GONE);
            new CustomMessage(getActivity(), "No internet is available");
        }
    }
    private void getArticle() {

        if (NetworkHelper.hasNetworAccess(getContext())) {
            fragmentArticalesBinding.spinKit.setVisibility(View.VISIBLE);
            firebaseHelper.getArticles().addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        fragmentArticalesBinding.spinKit.setVisibility(View.GONE);
                        articleModelListTemp.clear();
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

                            articleModelListTemp.add(articleModel);
                        }
                        Collections.reverse(articleModelListTemp);
                        articleModelList.clear();
                        articleModelList.addAll(articleModelListTemp);
                        articalsAdapter.notifyDataSetChanged();
                    } else {
                        fragmentArticalesBinding.spinKit.setVisibility(View.GONE);
                        new CustomMessage(getActivity(), "No Articles are available");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    fragmentArticalesBinding.spinKit.setVisibility(View.GONE);
                }
            });
        } else {
            fragmentArticalesBinding.spinKit.setVisibility(View.GONE);
            new CustomMessage(getActivity(), "No internet is available");
        }
    }

    List<String> personDislikeId = new ArrayList<>();
    List<String> personLikedId = new ArrayList<>();

    @Override
    public void onArticleLike(final ArticleModel articleModels) {
        if (!articleModels.getArticleId().equals("")
                && !articleModels.getArticle().equals("") && !articleModels.getArticleTitle().equals("")
                && !articleModels.getArticlePublishedDate().equals("") && !articleModels.getWriterName().equals("")
                && !articleModels.getWriterDesignation().equals("")) {
            AppController.getFirebaseHelper().getArticles().child(articleModels.getArticleId()).addListenerForSingleValueEvent(new ValueEventListener() {
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

                                // Toast.makeText(getContext(), "You Already Like this Article", Toast.LENGTH_SHORT).show();
                                new CustomMessage(getActivity(), "You Already Like this Article");
                            } else {
                                String newkey = AppController.getFirebaseHelper().getArticles().child(articleModels.getArticleId()).getRef().child("peoplesLike").push().getKey();
//                                HashMap<String, Object> user = new HashMap<>();
                                // user.put()

//                                user.put("ArticleTitle", binding.articleTitle.getText().toString());
//                                user.put("Article", binding.article.getText().toString());
//                                user.put("WriterName", binding.addWriterName.getText().toString());
//                                user.put("WriterDesignation", binding.WriterDesignation.getText().toString());
//                                user.put("ArticlePublishedDate", binding.articlePublishingDate.getText().toString());
//                                user.put("WriterImage", "none");
//                                user.put("Like", 0);
//                                user.put("Dislike", 0);
//                                user.put("ArticleId", itemId.toString());

//                                user.put("personid", AppController.getFirebaseHelper().getFirebaseAuth().getUid());
//                                AppController.getFirebaseHelper().getArticles().child(articleModels.getArticleId())
//                                        .child("peoplesLike").child(newkey).updateChildren(user);

                                //  updateCountForLike(articleModels.getArticleId());
                                onDislikeRemove(articleModels, newkey);

                                new CustomMessage(getActivity(), "You Liked this Article");

                            }
                        } else {
                            String newkey = AppController.getFirebaseHelper().getArticles().child(articleModels.getArticleId()).getRef().child("peoplesLike").push().getKey();
//                            HashMap<String, Object> user = new HashMap<>();
//                            user.put("personid", AppController.getFirebaseHelper().getFirebaseAuth().getUid());
//                            AppController.getFirebaseHelper().getArticles().child(articleModels.getArticleId())
//                                    .child("peoplesLike").child(newkey).updateChildren(user);
//
//                            updateCountForLike(articleModels.getArticleId());
//                            onDislikeRemove(articleModels, newkey);
                            onDislikeRemove(articleModels, newkey);
                            new CustomMessage(getActivity(), "You Liked this Article");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            new CustomMessage(getActivity(), "Something Wrong with this articles.we will take action soon.Thank you.");
        }

    }

    private void reduceDislikeCount(String articleId) {
        AppController.getFirebaseHelper().getArticles().child(articleId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int dislikecount = 0;
                    if (dataSnapshot.child("Dislike").getValue() != null)
                        dislikecount = dataSnapshot.child("Dislike").getValue(Integer.class);
                    dislikecount = dislikecount - 1;
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("Dislike", dislikecount);
                    dataSnapshot.getRef().updateChildren(hashMap);
                    getArticle();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void updateCountForLike(String articleId) {
        AppController.getFirebaseHelper().getArticles().child(articleId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int likecunt = 0;
                    if (dataSnapshot.child("Like").getValue() != null)
                        likecunt = dataSnapshot.child("Like").getValue(Integer.class);
                    likecunt = likecunt + 1;
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("Like", likecunt);
                    dataSnapshot.getRef().updateChildren(hashMap);
                    getArticle();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void onDislikeRemove(final ArticleModel articleModel, String newKey) {


        AppController.getFirebaseHelper().getArticles().child(articleModel.getArticleId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                personDislikeId.clear();
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("peoplesDislike").getChildrenCount() > 0) {

                        for (DataSnapshot people : dataSnapshot.child("peoplesDislike").getChildren()) {
                            final String personid = people.child("personid").getValue(String.class);

                            personDislikeId.add(personid);
                            if (personid.equals(AppController.getFirebaseHelper().getFirebaseAuth().getUid())) {
                                AppController.getFirebaseHelper().getArticles().child(articleModel.getArticleId()).child("peoplesDislike").child(people.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
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
                                            user.put("Dislike", dislikecount - 1);
                                            user.put("ArticleId", articleModel.getArticleId());
                                            user.put("peoplesLike" + "/" + newKey + "/" + "personid" + "/", AppController.getFirebaseHelper().getFirebaseAuth().getCurrentUser().getUid());

                                            AppController.getFirebaseHelper().getArticles().child(articleModel.getArticleId())
                                                    .updateChildren(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    getArticle();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    new CustomMessage(getActivity(), "" + e.getMessage());
                                                }
                                            });


                                        } else {
                                            Customlog.showlogD(TAG, task.getException().getMessage());
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        new CustomMessage(getActivity(), "" + e.getMessage());
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
                                    getArticle();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    new CustomMessage(getActivity(), "" + e.getMessage());
                                }
                            });
                        }

                    } else {
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

                                getArticle();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                new CustomMessage(getActivity(), "" + e.getMessage());
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
    public void onArticleReadmoreClick(String articleId, List<String> peoplesDislike,
                                       List<String> peoplesLike, String writerImage, String article,
                                       String articlePublishedDate, String writerDesignation, String writerName,
                                       String articleTitle, int like, int dislike) {

        if (!articleId.equals("") && articleId != null) {


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
            i.putExtra("like", like);
            i.putExtra("dislike", dislike);


            startActivity(i);
        } else {
            new CustomMessage(getActivity(), "Something Wrong with this articles.we will take action soon.Thank you.");

        }

    }

    @Override
    public void onArticleDislike(final ArticleModel articleModels) {

        if (!articleModels.getArticleId().equals("")
                && !articleModels.getArticle().equals("") && !articleModels.getArticleTitle().equals("")
                && !articleModels.getArticlePublishedDate().equals("") && !articleModels.getWriterName().equals("")
                && !articleModels.getWriterDesignation().equals("")) {
            AppController.getFirebaseHelper().getArticles().child(articleModels.getArticleId()).addListenerForSingleValueEvent(new ValueEventListener() {
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
                                new CustomMessage(getActivity(), "You Already Dislike this Article");
                            } else {
                                String newkey = AppController.getFirebaseHelper().getArticles().child(articleModels.getArticleId()).getRef().child("peoplesDislike").push().getKey();
//                                HashMap<String, Object> user = new HashMap<>();
//                                user.put("personid", AppController.getFirebaseHelper().getFirebaseAuth().getUid());
//                                AppController.getFirebaseHelper().getArticles().child(articleId)
//                                        .child("peoplesDislike").child(newkey).updateChildren(user);

                                // updateCountForDislike(articleId);
                                // onDislikeRemove(articleModels, newkey);

                                onLikeRemove(articleModels, newkey);
                                new CustomMessage(getActivity(), "You Dislike this Article");
                            }
                        } else {
                            String newkey = AppController.getFirebaseHelper().getArticles().child(articleModels.getArticleId()).getRef().child("peoplesDislike").push().getKey();
//                            HashMap<String, Object> user = new HashMap<>();
//                            user.put("personid", AppController.getFirebaseHelper().getFirebaseAuth().getUid());
//                            AppController.getFirebaseHelper().getArticles().child(articleId)
//                                    .child("peoplesDislike").child(newkey).updateChildren(user);
//
//                            updateCountForDislike(articleId);
                            onLikeRemove(articleModels, newkey);
                            new CustomMessage(getActivity(), "You Dislike this Article");
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            new CustomMessage(getActivity(), "Something Wrong with this articles.we will take action soon.Thank you.");
        }

    }

    private void onLikeRemove(ArticleModel articleModel, final String newKey) {

        AppController.getFirebaseHelper().getArticles().child(articleModel.getArticleId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                personLikedId.clear();
                if (dataSnapshot.exists()) {
                    if (dataSnapshot.child("peoplesLike").getChildrenCount() > 0) {

                        for (DataSnapshot people : dataSnapshot.child("peoplesLike").getChildren()) {

                            final String personid = people.child("personid").getValue(String.class);

                            personLikedId.add(personid);
                            if (personid.equals(AppController.getFirebaseHelper().getFirebaseAuth().getUid())) {

                                AppController.getFirebaseHelper().getArticles().child(articleModel.getArticleId()).child("peoplesLike").child(people.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {

                                            //  reduceLikeCount(articleId);

                                            int likecount = articleModel.getLike();

                                            int dislikecount = articleModel.getDislike();

                                            HashMap<String, Object> user = new HashMap<>();

                                            user.put("ArticleTitle", articleModel.getArticleTitle());
                                            user.put("Article", articleModel.getArticle());
                                            user.put("WriterName", articleModel.getWriterName());
                                            user.put("WriterDesignation", articleModel.getWriterDesignation());
                                            user.put("ArticlePublishedDate", articleModel.getArticlePublishedDate());
                                            user.put("WriterImage", articleModel.getWriterImage());
                                            user.put("Like", likecount - 1);
                                            user.put("Dislike", dislikecount + 1);
                                            user.put("ArticleId", articleModel.getArticleId());
                                            user.put("peoplesDislike" + "/" + newKey + "/" + "personid" + "/", AppController.getFirebaseHelper().getFirebaseAuth().getCurrentUser().getUid());

                                            AppController.getFirebaseHelper().getArticles().child(articleModel.getArticleId())
                                                    .updateChildren(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {

                                                    getArticle();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    new CustomMessage(getActivity(), "" + e.getMessage());
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

                                    getArticle();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    new CustomMessage(getActivity(), "" + e.getMessage());
                                }
                            });
                        }


                    } else {
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

                                getArticle();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                new CustomMessage(getActivity(), "" + e.getMessage());
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

    private void reduceLikeCount(String articleId) {
        AppController.getFirebaseHelper().getArticles().child(articleId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int likecunt = 0;
                    if (dataSnapshot.child("Like").getValue() != null)
                        likecunt = dataSnapshot.child("Like").getValue(Integer.class);
                    likecunt = likecunt - 1;
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("Like", likecunt);
                    dataSnapshot.getRef().updateChildren(hashMap);
                    getArticle();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void updateCountForDislike(String articleId) {
        AppController.getFirebaseHelper().getArticles().child(articleId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    int dislikecount = 0;
                    if (dataSnapshot.child("Dislike").getValue() != null)
                        dislikecount = dataSnapshot.child("Dislike").getValue(Integer.class);
                    dislikecount = dislikecount + 1;
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("Dislike", dislikecount);
                    dataSnapshot.getRef().updateChildren(hashMap);
                    getArticle();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onClick(View v) {

        Bundle bundle = new Bundle();
        switch (v.getId()) {

            case R.id.backbtn:
                FragmentHelper.changeFragmet(fragmentArticalesBinding.getRoot(), R.id.action_articals_to_nav_home, bundle);
                break;

//            case R.id.btnSearch:
//                if (fragmentArticalesBinding.etSearch.getText().toString().length() > 0 ) {
//                    getArticle(articleModelList,fragmentArticalesBinding.etSearch.getText().toString());
//                    articalsAdapter = new ArticalsAdapter(getContext(), articleModelListSearched, this);
//                    fragmentArticalesBinding.articalsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                    fragmentArticalesBinding.articalsRecyclerView.setAdapter(articalsAdapter);
//                }
//                else {
//                    fragmentArticalesBinding.etSearch.setError("Please Enter a Title");
//                }
//                break;

//            case R.id.btnShowAll:
//                articalsAdapter = new ArticalsAdapter(getContext(), articleModelList, this);
//                fragmentArticalesBinding.articalsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//                fragmentArticalesBinding.articalsRecyclerView.setAdapter(articalsAdapter);

        }
    }
}
