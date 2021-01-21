package com.example.finalproject.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private RecyclerView postList;
    private DatabaseReference PostsRef;
    private  FirebaseRecyclerAdapter<Posts, DashboardFragment.PostsViewHolder> mFirebaseAdapter;

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

        PostsRef = FirebaseDatabase.getInstance().getReference();

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

    FirebaseRecyclerOptions<Posts> options =
            new FirebaseRecyclerOptions.Builder<Posts>().setQuery(query, Posts.class)
                    .build();

    //koniec rzeczy z internetu

    private void DisplayAllUsersPosts() {
        mFirebaseAdapter = new FirebaseRecyclerAdapter<Posts, DashboardFragment.PostsViewHolder>(options){
                    @NonNull
                    @Override
                    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.all_posts_layout, parent, false);
                        return new PostsViewHolder(view);
                    }

                    @Override
                    protected void onBindViewHolder(@NonNull PostsViewHolder  viewHolder, int position, @NonNull Posts model) {
                        viewHolder.setFullname(model.getUsername());
                        viewHolder.setTime(model.getTime());
                        viewHolder.setDate(model.getDate());
                        viewHolder.setDescription(model.getDescription());
                        viewHolder.setPostimage( model.getPost_image());
                        viewHolder.setProfileImage(model.getProfile_image());
                    }

                };
        postList.setAdapter( mFirebaseAdapter);
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setFullname(String fullname)
        {
            TextView username = (TextView) mView.findViewById(R.id.my_profile_user_name);
            username.setText(fullname);
        }


        public void setTime(String time)
        {
            TextView PostTime = (TextView) mView.findViewById(R.id.post_time);
            PostTime.setText("    " + time);
        }

        public void setDate(String date)
        {
            TextView PostDate = (TextView) mView.findViewById(R.id.post_date);
            PostDate.setText("    " + date);
        }

        public void setDescription(String description)
        {
            TextView PostDescription = (TextView) mView.findViewById(R.id.post_content);
            PostDescription.setText(description);
        }

        public void setPostimage(  String postimage)
        {
            ImageView PostImage = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.get().load(postimage).fit().into(PostImage);
        }
        public void setProfileImage (String profileImage){
            ImageView ProfileImage = (ImageView) mView.findViewById(R.id.prof_image);
            Picasso.get().load(profileImage).fit().into(ProfileImage);
        }
    }

    @Override
    public void onStart() { // Ph4 Reading chat
        super.onStart();
        mFirebaseAdapter.startListening();
    }

    @Override
    public void onStop() { // Ph4 Reading chat
        super.onStop();
        mFirebaseAdapter.stopListening();
    }
}