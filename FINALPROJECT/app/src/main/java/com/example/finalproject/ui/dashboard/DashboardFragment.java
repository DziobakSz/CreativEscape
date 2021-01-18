package com.example.finalproject.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.facebook.appevents.ml.Model;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private RecyclerView postList;
    private DatabaseReference PostsRef;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        postList = (RecyclerView) root.findViewById(R.id.all_users_post_list);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);

        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        DisplayAllUsersPosts();

       /*final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        return root;
    }

    //tu jest z tutorialu z internetu więc jak się będzie rozwalać to przez to

    Query query = FirebaseDatabase.getInstance()
            .getReference()
            .child("Posts");

    FirebaseRecyclerOptions<Model> options =
            new FirebaseRecyclerOptions.Builder<Model>()
                    .setQuery(query, new SnapshotParser<Model>() {
                        @NonNull
                        @Override
                        public Model parseSnapshot(@NonNull DataSnapshot snapshot) {
                            return new Posts(snapshot.child("uid").getValue().toString(),
                                    snapshot.child("date").getValue().toString(),
                                    snapshot.child("time").getValue().toString(),
                                    snapshot.child("username").getValue().toString(),
                                    snapshot.child("description").getValue().toString(),
                                    snapshot.child("post_image").getValue().toString()
                                    snapshot.child("profile_image").getValue().toString());
                        }
                    })
                    .build();


    //koniec rzeczy z internetu

    private void DisplayAllUsersPosts() {
        FirebaseRecyclerAdapter<Posts, PostsViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Posts, PostsViewHolder>
                        (options){
                    @NonNull
                    @Override
                    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        return null;
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull PostsViewHolder holder, int position, @NonNull Posts model) {

                    }

                };
        postList.setAdapter(firebaseRecyclerAdapter);
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
    }
}