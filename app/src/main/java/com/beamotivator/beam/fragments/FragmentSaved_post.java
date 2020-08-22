package com.beamotivator.beam.fragments;


import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
;

import com.beamotivator.beam.R;
import com.beamotivator.beam.adapters.AdapterPosts;
import com.beamotivator.beam.adapters.AdapterPostsview;
import com.beamotivator.beam.models.ModelPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class FragmentSaved_post extends Fragment {
    RecyclerView savedPostsRv;

    List<ModelPost> postList;
    AdapterPostsview adapterPosts;

    FirebaseAuth firebaseAuth;
    String myId ;
    String ig = "";
    int count  = 0;
    public FragmentSaved_post() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         View root = inflater.inflate(R.layout.fragment_my_saved, container, false);
        firebaseAuth = FirebaseAuth.getInstance();
        myId = firebaseAuth.getCurrentUser().getUid();



        ;

        savedPostsRv =root. findViewById(R.id.mysave);

        postList = new ArrayList<>();

        loadSavedPosts();



        return  root;
    }

    private void loadSavedPosts() {

        //linear layout for recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        //show newest posts, load from last
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        GridLayoutManager layoutManager1 = new GridLayoutManager(getActivity(),3);
        savedPostsRv.setLayoutManager(layoutManager1);
        //set this layout to recycler view

        myId = firebaseAuth.getCurrentUser().getUid();


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Extras").child(myId).child("Saved");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postList.clear();
                for(DataSnapshot ds:snapshot.getChildren()){

                    String postId = ""+ds.getKey();

                    //now check for the post details
                    DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Posts");
                    ref1.orderByChild("pId")
                            .equalTo(postId)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                                    for(DataSnapshot ds1:datasnapshot.getChildren()){


                                        ModelPost modelPost = ds1.getValue(ModelPost.class);


                                        //add post
                                        postList.add(modelPost);



                                        //adapter
                                        adapterPosts = new AdapterPostsview(getActivity(),postList);

                                        //set adapter to recycler view
                                        savedPostsRv.setAdapter(adapterPosts);
                                    }




                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }



}

//    public void setLocale(String hi) {
//        Locale locale=new Locale(hi);
//        Locale.setDefault(locale);
//        Configuration configuration=new Configuration();
//        configuration.locale=locale;
//        getResources().updateConfiguration(configuration,getResources().getDisplayMetrics());
//        SharedPreferences sh=getActivity().getSharedPreferences("Settings",MODE_PRIVATE);
//        SharedPreferences.Editor editor=sh.edit();
//        editor.putString("my",hi);
//        editor.apply();
//    }
//    private void loadLocale() {
//
//        SharedPreferences sharedPreferences=getActivity().getSharedPreferences("Settings",MODE_PRIVATE);
//        String language=sharedPreferences.getString("my","");
//        setLocale(language);
//
//
//    }


//}
