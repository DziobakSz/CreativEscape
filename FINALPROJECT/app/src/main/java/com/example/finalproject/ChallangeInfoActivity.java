package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

    public Button Finish;
    private DatabaseReference ChelRef;
    private YouTubePlayerView youTubePlayer;
    private TextView  Desc;
    private String video_adress;
    private Button playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challange_info);
        String Id = getIntent().getStringExtra("ID");
        Log.d("tag",Id);
        youTubePlayer = (YouTubePlayerView) findViewById(R.id.youtube_view);
        playButton = (Button) findViewById(R.id.play_button);
        ChelRef = FirebaseDatabase.getInstance().getReference().child("Challanges").child(Id);
        Finish = (Button) findViewById(R.id.done_button);
        Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        Desc = (TextView) findViewById(R.id.challenge_description);
        GetChallange();

    }

    public void GetChallange() {
        ChelRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String description = snapshot.child("description").getValue().toString(); //tu nie wiem pod jakim hasłem to jest w bazie
                     video_adress = snapshot.child("video_address").getValue().toString();
                 Desc.setText(description);
                 playButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            youTubePlayer.initialize("AIzaSyC5QWBrFqxFGpuXBlHr_vO8jfO7xiXzFJA", onInitializedListener);
                        }
                 });
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
                public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                    YouTubePlayer youTubePlayer, boolean b) {

                    youTubePlayer.loadVideo(video_adress);


                }
                @Override
                public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                    YouTubeInitializationResult youTubeInitializationResult) {

                }


            };

}