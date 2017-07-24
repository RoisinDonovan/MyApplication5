package com.example.katiedennehy.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.katiedennehy.myapplication.R.id.settings_status_btn;

public class SettingsActivity extends AppCompatActivity {
    private DatabaseReference mUserDatabase;
    private FirebaseUser mCurrentUser;

    //android layout
    private CircleImageView mDisplayImage;
    private TextView mName;
    private TextView mStatus;

    private Button mStatusBtn;
    private Button mImageBtn;

   private static final int GALLERY_PICK = 1;

    private ProgressDialog mProgressDialog;
    private StorageReference mImageStorage;
    private ProgressDialog getmProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

      mDisplayImage = (CircleImageView) findViewById(R.id.user_single_image);
      mName = (TextView) findViewById(R.id.settings_display_name);
       mStatus = (TextView) findViewById(R.id.settings_status);

       mStatusBtn = (Button) findViewById(R.id.settings_status_btn);
        mImageBtn = (Button) findViewById(R.id.settings_img_btn);
        mImageStorage = FirebaseStorage.getInstance().getReference();

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        String current_uid = mCurrentUser.getUid();

       mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);
        mUserDatabase.keepSynced(true);

       mUserDatabase.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {

              // String email = dataSnapshot.child("email").getValue().toString();
               String image = dataSnapshot.child("image").getValue().toString();
               String name = dataSnapshot.child("name").getValue().toString();
               //String password = dataSnapshot.child("password").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
              // String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();

               mName.setText(name);
               mStatus.setText(status);

               if(!image.equals("default"));
               //Picasso.with(SettingsActivity.this).load(image).placeholder(R.drawable.avatar).into(mDisplayImage);

               Picasso.with(SettingsActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE)
                       .placeholder(R.drawable.avatar).into(mDisplayImage, new Callback() {
                   @Override
                   public void onSuccess() {

                   }

                   @Override
                   public void onError() {

                   }
               });

           }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

       mStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String status_value = mStatus.getText().toString();

               Intent status_intent = new Intent(SettingsActivity.this, StatusActivity.class);
                status_intent.putExtra("Status_value", status_value);
               startActivity(status_intent);
            }
       });

        mImageBtn.setOnClickListener(new View.OnClickListener() {
          @Override
           public void onClick(View view) {
              Intent galleryIntent = new Intent();
               galleryIntent.setType("image/*");
               galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

               startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);
           }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

            Uri imageURI = data.getData();

            CropImage.activity(imageURI)
                   .start(this);

          Toast.makeText(SettingsActivity.this, (CharSequence) imageURI, Toast.LENGTH_LONG).show();
        }
    }
}