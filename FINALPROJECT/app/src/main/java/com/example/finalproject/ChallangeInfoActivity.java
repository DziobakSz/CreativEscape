package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.finalproject.login.RegisterActivity;
import com.example.finalproject.login.SetupActivity;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChallangeInfoActivity extends YouTubeBaseActivity {

    private static final int RECOVERY_REQUEST = 1000;
    public Button Finish;
    private DatabaseReference ChelRef;
    private YouTubePlayerView youTubePlayer;
    private TextView  Desc,Title;
    private String video_adress, tag;
    private Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challange_info);
        String Id = getIntent().getStringExtra("ID");

        youTubePlayer = (YouTubePlayerView) findViewById(R.id.youtube_view);
        playButton = (Button) findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youTubePlayer.initialize("AIzaSyC5QWBrFqxFGpuXBlHr_vO8jfO7xiXzFJA", onInitializedListener);
            }
        });
        ChelRef = FirebaseDatabase.getInstance().getReference().child("Challanges").child(Id);
        Finish = (Button) findViewById(R.id.done_button);
        Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendUserToSetupActivity();

            }
        });
        Desc = (TextView) findViewById(R.id.challenge_description);
        Title = (TextView) findViewById(R.id.challenge_title);
        GetChallange();

    }

    public void GetChallange() {
        ChelRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String description = snapshot.child("description").getValue().toString();
                    String title = snapshot.child("title").getValue().toString();//tu nie wiem pod jakim hasłem to jest w bazie
                     video_adress = snapshot.child("video_address").getValue().toString();
                      tag=snapshot.child("tag").getValue().toString();
                     Desc.setText(description);
                     Title.setText(title);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    YouTubePlayer.OnInitializedListener onInitializedListener = new
            YouTubePlayer.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {

                    youTubePlayer.loadVideo(video_adress);

                    YouTubePlayer.PlayerStateChangeListener stateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
                        @Override
                        public void onLoading() {}

                        @Override
                        public void onLoaded(String s) {}

                        @Override
                        public void onAdStarted() {}

                        @Override
                        public void onVideoStarted() {}

                        @Override
                        public void onVideoEnded() {}

                        @Override
                        public void onError(YouTubePlayer.ErrorReason errorReason) {
                            Toast.makeText(ChallangeInfoActivity.this, "Error Occured: ", Toast.LENGTH_SHORT).show();
                        }
                    };
                    youTubePlayer.setPlayerStateChangeListener(stateChangeListener);
                }

                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                    if (youTubeInitializationResult.isUserRecoverableError()) {
                        youTubeInitializationResult.getErrorDialog(ChallangeInfoActivity.this, RECOVERY_REQUEST)
                                .show(); //enables the user to recover from error e.g by installing or enabling yt
                    } else {
                        Toast.makeText(ChallangeInfoActivity.this, "Error Occured: ", Toast.LENGTH_SHORT).show();
                    }
                }
            };
    private void SendUserToSetupActivity()
    {
        Intent setupIntent = new Intent(ChallangeInfoActivity.this, NewPostActivity.class);

        setupIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        setupIntent.putExtra("TAG", tag);
        startActivity(setupIntent);
        finish();
    }

}