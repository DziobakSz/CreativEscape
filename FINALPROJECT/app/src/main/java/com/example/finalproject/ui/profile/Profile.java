package com.example.finalproject.ui.profile;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.finalproject.R;
import com.example.finalproject.login.MainActivity;
import com.example.finalproject.login.RegisterActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends Fragment {

    private ProfileViewModel mViewModel;
    //private RecyclerView challangeList;
    private DatabaseReference ProfRef;
    private FirebaseAuth mAuth;
    private TextView userName, fullName;
    private CircleImageView userImage;
    private String currentUserID;
    private ImageButton profileSettings;


    public static Profile newInstance() {
        return new Profile();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        ProfRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserID);
        profileSettings = (Button) findViewById(R.id.profileSettingsBtn);
        profileSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent registerIntent = new Intent(this, ProfileSettings.class);//tu nie wiem jak do settingsów go przekierować
                    startActivity(registerIntent);

            }
        });

        userImage = (CircleImageView) findViewById(R.id.my_profile_pic);
        userName = (TextView) findViewById(R.id.my_profile_user_name);
        fullName = (TextView) findViewById(R.id.my_profile_full_name);
        ProfRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              if(snapshot.exists()){
                  String myProfileImage = snapshot.child("").getValue().toString(); //tu nie wiem pod jakim hasłem to jest w bazie
                  String myUserName = snapshot.child("username").getValue().toString();
                  String myFullName = snapshot.child("fullname").getValue().toString();

                  Picasso.get().load(myProfileImage).placeholder(R.drawable.profile).into(userImage);
                  userName.setText(myUserName);
                  fullName.setText(myFullName);
              }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return inflater.inflate(R.layout.profile_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
    }

}