package com.example.finalproject.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.finalproject.FriendsActivity;
import com.example.finalproject.MyPostsActivity;
import com.example.finalproject.NewPostActivity;
import com.example.finalproject.R;
import com.example.finalproject.RewardActivity;
import com.example.finalproject.SettingsActivity;
import com.example.finalproject.login.MainActivity;
import com.example.finalproject.login.RegisterActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import sun.bob.mcalendarview.MCalendarView;
import sun.bob.mcalendarview.vo.DateData;

public class Profile extends Fragment {

    private ProfileViewModel mViewModel;
    //private RecyclerView challangeList;
    private DatabaseReference ProfRef,PostRef;
    private FirebaseAuth mAuth;
    private TextView userName, fullName;
    private CircleImageView userImage;
    private String currentUserID;
    private ImageButton profileSettings;
    private Button Friends ,Posts, Rewards;
    int counter = 0;
MCalendarView calendarView;

    public static Profile newInstance() {
        return new Profile();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

/*
        ArrayList<DateData> dates=new ArrayList<>();
        dates.add(new DateData(2018,04,26));
        dates.add(new DateData(2018,04,27));

        for(int i=0;i<dates.size();i++) {
            calendarView.markDate(dates.get(i).getYear(),dates.get(i).getMonth(),dates.get(i).getDay());//mark multiple dates with this code.
        }*/



        View root = inflater.inflate(R.layout.profile_fragment, container, false);
        calendarView = ((MCalendarView) root.findViewById(R.id.calendarView));

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        ProfRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        PostRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        Friends = (Button) root.findViewById(R.id.friends_button);
        Friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rIntent = new Intent(getActivity(), FriendsActivity.class);
                startActivity(rIntent);
            }
        });
        Posts = (Button) root.findViewById(R.id.my_posts);
        Posts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rIntent = new Intent(getActivity(), MyPostsActivity.class);
                startActivity(rIntent);
            }
        });

        Rewards = (Button) root.findViewById(R.id.reward_button);
        Rewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent rIntent = new Intent(getActivity(), RewardActivity.class);
                startActivity(rIntent);
            }
        });



        profileSettings = (ImageButton) root.findViewById(R.id.profileSettingsBtn);
        profileSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent registerIntent = new Intent(getActivity(), SettingsActivity.class);
                    startActivity(registerIntent);

            }
        });

        userImage = (CircleImageView) root.findViewById(R.id.my_profile_pic);
        userName = (TextView) root.findViewById(R.id.my_profile_user_name);
        fullName = (TextView) root.findViewById(R.id.my_profile_full_name);
        ProfRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              if(snapshot.exists()){
                  if(snapshot.child("profileimage" ).exists()){
                  String myProfileImage = snapshot.child("profileimage").getValue().toString();
                  Picasso.get().load(myProfileImage).placeholder(R.drawable.profile).into(userImage);
                  }
                  String myUserName = snapshot.child("username").getValue().toString();
                  String myFullName = snapshot.child("fullname").getValue().toString();

                  //counter = Integer.parseInt(snapshot.child("counter").getValue().toString());

                  userName.setText(myUserName);
                  fullName.setText(myFullName);
              }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        ArrayList<String> Dates = new ArrayList<>();
        ArrayList<DateData> dates=new ArrayList<>();
        SimpleDateFormat formater = new SimpleDateFormat("dd-MMMM-yyyy");

        Query myPostsQuery = PostRef.orderByChild("uid").startAt(currentUserID).endAt(currentUserID + "\uf8ff");
        myPostsQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Dates.add(postSnapshot.child("date").getValue().toString());
                }
                for(int i=0;i<Dates.size();i++){
                    int year = 2021;
                    int month=01;
                    int day =0;
                    try {
                        Date date=formater.parse(Dates.get(i));
                         year = date.getYear()+1900;
                         month = date.getMonth()+1;
                         day = date.getDate();
                         Log.d("tag",String.valueOf(month));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    dates.add(new DateData(year,month,day));
                }
                Mark(dates);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return root;
    }
void Mark( ArrayList<DateData> dates) {
    for (int i = 0; i < dates.size(); i++) {
        Log.d("tag", dates.get(i).toString());
        calendarView.markDate(dates.get(i).getYear(), dates.get(i).getMonth(), dates.get(i).getDay());

    }
}

}