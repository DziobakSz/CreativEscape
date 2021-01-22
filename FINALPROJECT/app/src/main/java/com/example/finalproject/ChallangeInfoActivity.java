package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ChallangeInfoActivity extends AppCompatActivity {

    public Button Finish;
    private DatabaseReference ChelRef;
    private TextView  Desc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challange_info);
        String Id = getIntent().getStringExtra("ID");
        Log.d("tag",Id);
        ChelRef = FirebaseDatabase.getInstance().getReference().child("Challanges").child(Id);
        Finish = (Button) findViewById(R.id.button2);
        Finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
        Desc = (TextView) findViewById(R.id.textView3);
        GetChallange();
    }

    public void GetChallange() {
        ChelRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String description = snapshot.child("description").getValue().toString(); //tu nie wiem pod jakim hasłem to jest w bazie
                 Desc.setText(description);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}