package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.finalproject.login.MainActivity;
import com.example.finalproject.login.SetupActivity;
import com.example.finalproject.ui.profile.Profile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference UsersRef;


    private EditText UserName, FullName;
    private Button SaveButton;
    private CircleImageView ProfileImage;
    private ProgressDialog loadingBar;
    private ImageButton changeProfilePic;
    private CheckBox DIY,Sport,Book,Music,Food,Photo;
    private StorageReference UserProfileImageRef;
    final static int Gallery_Pick = 1;

    private Button Logout;
    String currentUserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_settings_layout);
        mFirebaseAuth = FirebaseAuth.getInstance();
        currentUserID = mFirebaseAuth.getCurrentUser().getUid();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        UserProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");


        UserName = (EditText) findViewById(R.id.username_profile_edit);
        FullName = (EditText) findViewById(R.id.name_profile_edit);
        SaveButton = (Button) findViewById(R.id.save_button2) ;
        ProfileImage = (CircleImageView) findViewById(R.id.my_profile_pic);
        DIY = (CheckBox) findViewById(R.id.DIY_check2);
        Sport = (CheckBox) findViewById(R.id.sport_check2);
        Music = (CheckBox) findViewById(R.id.music_check2);
        Photo = (CheckBox) findViewById(R.id.photo_check2);
        Food = (CheckBox) findViewById(R.id.food_check2);
        Book = (CheckBox) findViewById(R.id.food_check2);
        changeProfilePic = (ImageButton) findViewById(R.id.changeProfilePictureBtn);

        loadingBar = new ProgressDialog(this);

        SaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                SaveAccountSetupInformation();
            }
        });







        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    String DIYState = snapshot.child(currentUserID).child("iDIY").getValue().toString();
                    String BookState = snapshot.child(currentUserID).child("iBook").getValue().toString();
                    String FoodState = snapshot.child(currentUserID).child("iFood").getValue().toString();
                    String MusicState = snapshot.child(currentUserID).child("iMusic").getValue().toString();
                    String PhotoState = snapshot.child(currentUserID).child("iPhoto").getValue().toString();
                    String SportState = snapshot.child(currentUserID).child("iSport").getValue().toString();

                    String username = snapshot.child(currentUserID).child("username").getValue().toString();
                    String fullname = snapshot.child(currentUserID).child("fullname").getValue().toString();

                    if(DIYState=="true") DIY.setChecked(true);
                    else DIY.setChecked(false);
                    if(BookState=="true") Book.setChecked(true);
                    else Book.setChecked(false);
                    if(FoodState=="true") Food.setChecked(true);
                    else Food.setChecked(false);
                    if(MusicState=="true") Music.setChecked(true);
                    else Music.setChecked(false);
                    if(PhotoState=="true") Photo.setChecked(true);
                    else Photo.setChecked(false);
                    if(SportState=="true") Sport.setChecked(true);
                    else Sport.setChecked(false);
                    UserName.setText(username);
                    FullName.setText(fullname);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        changeProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, Gallery_Pick);
            }
        });


        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    if (dataSnapshot.hasChild("profileimage"))
                    {
                        String image = dataSnapshot.child("profileimage").getValue().toString();
                        Log.d("tag",image);
                        Picasso.get().load(image).placeholder(R.drawable.profile).into(ProfileImage);
                    }
                    else
                    {
                        Toast.makeText(SettingsActivity.this, "Please select profile image first.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        Logout = (Button) findViewById(R.id.button3);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFirebaseAuth.signOut();
                SendUserToLoginActivity();
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Gallery_Pick && resultCode==RESULT_OK && data!=null)
        {
            Uri ImageUri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            loadingBar.setTitle("Profile Image");
            loadingBar.setMessage("Please wait, while we updating your profile image...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            Uri resultUri = result.getUri();

            StorageReference filePath = UserProfileImageRef.child(currentUserID + ".jpg");

            filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                    Toast.makeText(SettingsActivity.this, "Profile Image stored successfully to Firebase storage...", Toast.LENGTH_SHORT).show();
                    final Task<Uri> firebaseUri = taskSnapshot.getStorage().getDownloadUrl();
                    firebaseUri.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            final String downloadUrl = uri.toString();

                            UsersRef.child("profileimage").setValue(downloadUrl)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Intent selfIntent = new Intent(SettingsActivity.this, SetupActivity.class);
                                                startActivity(selfIntent);

                                                Toast.makeText(SettingsActivity.this, "Profile Image stored to Firebase Database Successfully...", Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            } else {
                                                String message = task.getException().getMessage();
                                                Toast.makeText(SettingsActivity.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                                                loadingBar.dismiss();
                                            }
                                        }
                                    });
                        }
                    });
                    firebaseUri.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            Toast.makeText(SettingsActivity.this, "Error Occured: Image can not be cropped. Try Again.", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    });
                }
            });
        }

    }
    private void SendUserToLoginActivity()
    {
        Intent loginIntent = new Intent(SettingsActivity.this, MainActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }

    private void SaveAccountSetupInformation()
    {
        String username = UserName.getText().toString();
        String fullname = FullName.getText().toString();

        boolean bDiy = DIY.isChecked();
        boolean bSport = Sport.isChecked();
        boolean bBook = Book.isChecked();
        boolean bMusic = Music.isChecked();
        boolean bFood = Food.isChecked();
        boolean bPhoto = Photo.isChecked();

        if(TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "Please write your username...", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(fullname))
        {
            Toast.makeText(this, "Please write your name...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Saving Information");
            loadingBar.setMessage("Please wait, while we are creating your new Account...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            HashMap userMap = new HashMap();
            userMap.put("username", username);
            userMap.put("fullname", fullname);
            userMap.put("gender", "none");
            userMap.put("iDIY", bDiy);
            userMap.put("iSport", bSport);
            userMap.put("iBook", bBook);
            userMap.put("iMusic", bMusic);
            userMap.put("iFood", bFood);
            userMap.put("iPhoto", bPhoto);

            UsersRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task)
                {
                    if(task.isSuccessful())
                    {

                        SendUserToProfileActivity();
                        Toast.makeText(SettingsActivity.this, "your Account was updated Successfully.", Toast.LENGTH_LONG).show();
                        loadingBar.dismiss();
                    }
                    else
                    {
                        String message =  task.getException().getMessage();
                        Toast.makeText(SettingsActivity.this, "Error Occured: " + message, Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                    }
                }
            });
        }
    }



    private void SendUserToProfileActivity() {
        Intent loginIntent = new Intent(SettingsActivity.this, Profile.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
        finish();
    }
}