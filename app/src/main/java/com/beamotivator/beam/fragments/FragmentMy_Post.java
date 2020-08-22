package com.beamotivator.beam.fragments;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.beamotivator.beam.R;
import com.beamotivator.beam.SavedPost;
import com.beamotivator.beam.adapters.AdapterPosts;
import com.beamotivator.beam.models.ModelPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentMy_Post extends Fragment {

    RecyclerView savedPostsRv;

    List<ModelPost> postList;
    AdapterPosts adapterPosts;

    FirebaseAuth firebaseAuth;
    String myId ;
    String ig = "";
    int count  = 0;
    public FragmentMy_Post() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_my_post, container, false);

        //set firebase
        firebaseAuth = FirebaseAuth.getInstance();
        myId = firebaseAuth.getCurrentUser().getUid();



        ;

        savedPostsRv =root. findViewById(R.id.myposts);

        postList = new ArrayList<>();

        loadSavedPosts();
//


return  root;
    }

    private void loadSavedPosts() {

        //linear layout for recyclerview
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        //show newest posts, load from last
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);

        //set this layout to recycler view
        savedPostsRv.setLayoutManager(layoutManager);

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
                                        adapterPosts = new AdapterPosts(getActivity(),postList);

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