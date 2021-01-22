package com.example.finalproject.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.ChallangeInfoActivity;
import com.example.finalproject.NewPostActivity;
import com.example.finalproject.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView challangeList;
    private DatabaseReference UsersRef, ChalRef;
    private FirebaseRecyclerAdapter<Challange, ChallangeViewHolder> mFirebaseAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);


        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        ChalRef = FirebaseDatabase.getInstance().getReference().child("Challanges");
        challangeList = (RecyclerView) root.findViewById(R.id.challange_list);
        challangeList.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        challangeList.setLayoutManager(linearLayoutManager);





        Query query = ChalRef; // Ph4 Reading chat
        FirebaseRecyclerOptions<Challange> options = new FirebaseRecyclerOptions.Builder<Challange>() //ph4
                .setQuery(query, Challange.class)
                .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Challange, ChallangeViewHolder>(options) { // Ph4 Reading chat

            @Override
            protected void onBindViewHolder(ChallangeViewHolder holder, int position, Challange model) {
                holder.setFullname(model.getTitle());
                holder.setPostimage( model.getPhoto());
                holder.setID(model.getUid());


            }

            @Override
            public  ChallangeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.challange_view_layout, parent, false);
                return new  ChallangeViewHolder(view);
            }


        };
        challangeList.setAdapter( mFirebaseAdapter);
        return root;
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

    public static class ChallangeViewHolder extends RecyclerView.ViewHolder {
        View mView;
        private Context context;
        String tag;

        public ChallangeViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            context = itemView.getContext();
            ImageButton More = (ImageButton) mView.findViewById(R.id.imageButton2);


            More.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent  intent =  new Intent(context, ChallangeInfoActivity.class);
                    intent.putExtra("ID", tag);
                    context.startActivity(intent);
                }
            });
        }

        public void setFullname(String title) {
            TextView username = (TextView) mView.findViewById(R.id.title_text);
            username.setText(title);
        }

        public void setPostimage( String postimage) {
            ImageView PostImage = (ImageView) mView.findViewById(R.id.image_challange);
            Picasso.get().load(postimage).fit().into(PostImage);

        }
        public void setID( String tag) {
          this.tag= tag;
        }
    }
    public void GoToINFO() {
        Intent registerIntent = new Intent(getActivity(), NewPostActivity.class);//tu nie wiem jak do settingsów go przekierować
        startActivity(registerIntent);
    }
}
