package com.example.finalproject.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.friends.FindFriends;
import com.example.finalproject.friends.PersonProfileActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardFragment extends Fragment {

    private RecyclerView postList;
    private ImageButton SearchButton;
    private DatabaseReference PostsRef,allUsersDatabaseRef,LikesRef;
    private EditText SearchInputText;
    private RecyclerView SearchResultList;
    private  FirebaseRecyclerAdapter<Posts, DashboardFragment.PostsViewHolder> mFirebaseAdapter;
    private FirebaseAuth mAuth;
    Boolean LikeChecker = false;
    boolean listOpened = false;
    String currentUserID;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        postList = (RecyclerView) root.findViewById(R.id.all_users_post_list);
        postList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postList.setLayoutManager(linearLayoutManager);
        LikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        PostsRef = FirebaseDatabase.getInstance().getReference();
        allUsersDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");
        SearchResultList = (RecyclerView) root.findViewById(R.id.search_result_list);
        SearchResultList.setLayoutManager(new LinearLayoutManager(root.getContext()));

        SearchButton = (ImageButton) root.findViewById(R.id.search_people_friends_button);
        SearchInputText = (EditText) root.findViewById(R.id.search_box_input);

        DisplayAllUsersPosts();

        SearchButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(listOpened){
                SearchResultList.setVisibility(View.VISIBLE);
                    postList.setVisibility(View.INVISIBLE);
                String searchBoxInput = SearchInputText.getText().toString();
                SearchPeopleAndFriends(searchBoxInput);}
                else{
                    postList.setVisibility(View.VISIBLE);
                    SearchResultList.setVisibility(View.INVISIBLE);
                }
                listOpened=!listOpened;


            }
        });


        return root;
    }

    private void SearchPeopleAndFriends(String searchBoxInput) {
        //Toast.makeText(this, "Searching....", Toast.LENGTH_LONG).show();

        Query searchPeopleAndFriendsQuery = allUsersDatabaseRef.orderByChild("fullname")
                .startAt(searchBoxInput).endAt(searchBoxInput + "\uf8ff");

        FirebaseRecyclerOptions<FindFriends> options=new FirebaseRecyclerOptions.Builder<FindFriends>().
                setQuery(searchPeopleAndFriendsQuery, FindFriends.class).build(); //query build past the query to FirebaseRecyclerAdapter
        FirebaseRecyclerAdapter<FindFriends, DashboardFragment.FindFriendViewHolder> adapter=new FirebaseRecyclerAdapter<FindFriends,  DashboardFragment.FindFriendViewHolder>(options)
        {
            @Override
            protected void onBindViewHolder(@NonNull  DashboardFragment.FindFriendViewHolder holder, final int position, @NonNull FindFriends model)
            {
                final String PostKey = getRef(position).getKey();
                holder.username.setText(model.getFullname());

                Picasso.get().load(model.getProfileimage()).into(holder.profileimage);

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        Intent findOthersIntent = new Intent(v.getContext(), DashboardFragment.class);
                        findOthersIntent.putExtra("PostKey", PostKey);
                        startActivity(findOthersIntent);
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener()
                {

                    @Override
                    public void onClick(View v)
                    {
                        String visit_user_id = getRef(position).getKey();
Log.d("user_id",visit_user_id);
                        Intent profileIntent = new Intent(v.getContext(), PersonProfileActivity.class);
                        profileIntent.putExtra("visit_user_id", visit_user_id);
                        startActivity(profileIntent);
                    }
                });
            }
            @NonNull
            @Override
            public  DashboardFragment.FindFriendViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
            {
                View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.all_users_display_layout,viewGroup,false);

                DashboardFragment.FindFriendViewHolder viewHolder=new  DashboardFragment.FindFriendViewHolder(view);
                return viewHolder;
            }
        };

        SearchResultList.setAdapter(adapter);
        adapter.startListening();
    }


    public class FindFriendViewHolder extends RecyclerView.ViewHolder
    {
        TextView username, status;
        CircleImageView profileimage;
        View mView;

        public FindFriendViewHolder(@NonNull View itemView)
        {
            super(itemView);
            username = itemView.findViewById(R.id.all_users_profile_full_name);

            profileimage = itemView.findViewById(R.id.all_users_profile_image);
        }
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
                        final String PostKey = getRef(position).getKey();
                        viewHolder.setFullname(model.getUsername());
                        viewHolder.setTime(model.getTime());
                        viewHolder.setDate(model.getDate());
                        viewHolder.setDescription(model.getDescription());
                        viewHolder.setPostimage( model.getPost_image());
                        viewHolder.setProfileImage(model.getProfile_image());
                        viewHolder.setLikeButtonStatus(PostKey);
                        viewHolder.LikepostButton.setOnClickListener(new View.OnClickListener()
                        {
                            @Override
                            public void onClick(View v)
                            {
                                LikeChecker = true;

                                LikesRef.addValueEventListener(new ValueEventListener()
                                {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                    {
                                        if(LikeChecker.equals(true))
                                        {
                                            if(dataSnapshot.child(PostKey).hasChild(currentUserID))
                                            {
                                                LikesRef.child(PostKey).child(currentUserID).removeValue();
                                                LikeChecker = false;
                                            }
                                            else
                                            {
                                                LikesRef.child(PostKey).child(currentUserID).setValue(true);
                                                LikeChecker = false;
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError)
                                    {

                                    }
                                });
                            }
                        });
                    }

                };
        postList.setAdapter( mFirebaseAdapter);
    }

    public static class PostsViewHolder extends RecyclerView.ViewHolder {
        ImageButton LikepostButton;
        View mView;
        int countLikes;
        String currentUserId;
        DatabaseReference LikesRef;
        TextView DisplayNoOfLikes;

        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            LikepostButton = (ImageButton) mView.findViewById(R.id.like_button);
            LikesRef = FirebaseDatabase.getInstance().getReference().child("Likes");
            currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DisplayNoOfLikes = (TextView) mView.findViewById(R.id.display_no_of_likes);
        }

        public void setFullname(String fullname) {
            TextView username = (TextView) mView.findViewById(R.id.my_profile_user_name);
            username.setText(fullname);
        }


        public void setTime(String time) {
            TextView PostTime = (TextView) mView.findViewById(R.id.post_time);
            PostTime.setText("    " + time);
        }

        public void setDate(String date) {
            TextView PostDate = (TextView) mView.findViewById(R.id.post_date);
            PostDate.setText("    " + date);
        }

        public void setDescription(String description) {
            TextView PostDescription = (TextView) mView.findViewById(R.id.post_content);
            PostDescription.setText(description);
        }

        public void setPostimage(String postimage) {
            ImageView PostImage = (ImageView) mView.findViewById(R.id.post_image);
            Picasso.get().load(postimage).fit().into(PostImage);
        }

        public void setProfileImage(String profileImage) {
            ImageView ProfileImage = (ImageView) mView.findViewById(R.id.prof_image);
            Picasso.get().load(profileImage).fit().into(ProfileImage);
        }

        public void setLikeButtonStatus(final String PostKey) {
            LikesRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.child(PostKey).hasChild(currentUserId)) {
                        countLikes = (int) dataSnapshot.child(PostKey).getChildrenCount();
                        LikepostButton.setImageResource(R.drawable.heart);
                        DisplayNoOfLikes.setText((Integer.toString(countLikes) + (" Likes")));
                    } else {
                        countLikes = (int) dataSnapshot.child(PostKey).getChildrenCount();
                        LikepostButton.setImageResource(R.drawable.heartb);
                        DisplayNoOfLikes.setText((Integer.toString(countLikes) + (" Likes")));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
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