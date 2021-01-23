package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class NewPostActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ProgressDialog loadingBar;

    private ImageButton SelectPostImage;
    private Button UpdatePostButton;
    private EditText PostDescription;

    private static final int Gallery_Pick = 1;
    private Uri ImageUri;
    private String Description;
    private DatabaseReference UsersRef, PostsRef;
    private FirebaseAuth mAuth;

    private StorageReference PostsImagesReference;

    private String saveCurrentDate, saveCurrentTime, postRandomName, downloadUrl, current_user_id,tag;
    private long countPosts = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);
        tag = getIntent().getStringExtra("TAG");

        mAuth = FirebaseAuth.getInstance();
        current_user_id = mAuth.getCurrentUser().getUid();

        PostsImagesReference = FirebaseStorage.getInstance().getReference();
        UsersRef = FirebaseDatabase.getInstance().getReference().child("Users");
        PostsRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        SelectPostImage = (ImageButton) findViewById(R.id.select_post_image);
        UpdatePostButton = (Button) findViewById(R.id.update_post_button);
        PostDescription = (EditText) findViewById(R.id.post_description);
        loadingBar = new ProgressDialog(this);




        SelectPostImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                OpenGallery();
            }
        });

        UpdatePostButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ValidatePostInfo();
            }
        });
    }

    private void ValidatePostInfo()
    {
        Description = PostDescription.getText().toString();

        if(ImageUri == null)
        {
            Toast.makeText(this, "Please select post image... ", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this, "Please write something about your image... ", Toast.LENGTH_SHORT).show();
        }
        else
        {
            loadingBar.setTitle("Add New Post");
            loadingBar.setMessage("Please wait, while we are updating your new post...");
            loadingBar.show();
            loadingBar.setCanceledOnTouchOutside(true);

            StoringImageToFirebaseStorage();
        }
    }

    private void StoringImageToFirebaseStorage()
    {
        Calendar calFordDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMMM-yyyy");
        saveCurrentDate = currentDate.format(calFordDate.getTime());

        Calendar calFordTime = Calendar.getInstance();
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calFordDate.getTime());

        postRandomName = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = PostsImagesReference.child("Post Images").child(ImageUri.getLastPathSegment() + postRandomName + ".jpg");
        filePath.putFile(ImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
        {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
            {
                if (!task.isSuccessful())
                {
                    throw task.getException();
                }
                return filePath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>()
        {
            @Override
            public void onComplete(@NonNull Task<Uri> task)
            {
                if(task.isSuccessful())
                {
                    Uri downUri = task.getResult();
                    Toast.makeText(NewPostActivity.this, "Image uploaded successfully to Storage... ", Toast.LENGTH_SHORT).show();

                    downloadUrl = downUri.toString();
                    SavingPostInformationToDatabase();
                }
                else
                {
                    String message = task.getException().getMessage();
                    Toast.makeText(NewPostActivity.this, "Error occured" + message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void SavingPostInformationToDatabase()
    {
        PostsRef.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    countPosts = dataSnapshot.getChildrenCount();
                }
                else
                {
                    countPosts = 0;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });

        UsersRef.child(current_user_id).addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                if(dataSnapshot.exists())
                {
                    final String userFullName = dataSnapshot.child("username").getValue().toString();
                    final String userProfileImage = dataSnapshot.child("profileimage").getValue().toString();

                    HashMap postsMap = new HashMap();
                    postsMap.put("uid", current_user_id);
                    postsMap.put("date", saveCurrentDate);
                    postsMap.put("time", saveCurrentTime);
                    postsMap.put("description", Description);
                    postsMap.put("post_image", downloadUrl);
                    postsMap.put("profile_image", userProfileImage);
                    postsMap.put("username", userFullName);
                    postsMap.put("tag", tag);
                    postsMap.put("counter", countPosts);
                    PostsRef.child(current_user_id + postRandomName).updateChildren(postsMap)
                            .addOnCompleteListener(new OnCompleteListener()
                            {
                                @Override
                                public void onComplete(@NonNull Task task)
                                {
                                    if(task.isSuccessful())
                                    {
                                        SendUserToMainActivity();
                                        Toast.makeText(NewPostActivity.this, "New Post is updated successfully", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                    else
                                    {
                                        Toast.makeText(NewPostActivity.this, "Error Occured while updating your post.", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                    }
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void OpenGallery()
    {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, Gallery_Pick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == Gallery_Pick && resultCode == RESULT_OK && data != null)
        {
            ImageUri = data.getData();
            SelectPostImage.setImageURI(ImageUri);
        }
        else
        {
            Toast.makeText(NewPostActivity.this, "Error occured: " + requestCode + resultCode + data, Toast.LENGTH_SHORT).show();
        }
    }



    private void SendUserToMainActivity()
    {
        Intent mainIntent = new Intent(NewPostActivity.this, MenuActivity.class);
        startActivity(mainIntent);
    }


}