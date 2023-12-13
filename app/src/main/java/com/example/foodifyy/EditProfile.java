package com.example.foodifyy;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.bumptech.glide.Glide;

public class EditProfile extends AppCompatActivity {

    FirebaseAuth auth;
    ImageView profile;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    Uri imageUri;
    EditText emailEdit, unameEdit, contactEdit, fnameEdit, mnameEdit, lnameEdit, houseEdit, barangayEdit, regionEdit, ageEdit, weightEdit, heightEdit;
    TextView save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users").child(currentUser.getUid());
        storageReference = FirebaseStorage.getInstance().getReference("users").child(currentUser.getUid());

        profile = findViewById(R.id.profile_pfp);
        emailEdit = findViewById(R.id.edit_email);
        unameEdit = findViewById(R.id.edit_uname);
        contactEdit = findViewById(R.id.edit_number);
        fnameEdit = findViewById(R.id.edit_fname);
        mnameEdit = findViewById(R.id.edit_mname);
        lnameEdit = findViewById(R.id.edit_lname);
        houseEdit = findViewById(R.id.edit_house);
        barangayEdit = findViewById(R.id.edit_barangay);
        regionEdit = findViewById(R.id.edit_region);
        ageEdit = findViewById(R.id.edit_age);
        weightEdit = findViewById(R.id.edit_weight);
        heightEdit = findViewById(R.id.edit_height);

        save = findViewById(R.id.save_profile);

        retrieveProfileInformation();


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ETemail = emailEdit.getText().toString();
                String ETuname = unameEdit.getText().toString();
                String ETcontact = contactEdit.getText().toString();
                String ETfname = fnameEdit.getText().toString();
                String ETmname = mnameEdit.getText().toString();
                String ETlname = lnameEdit.getText().toString();
                String EThouse = houseEdit.getText().toString();
                String ETbarangay = barangayEdit.getText().toString();
                String ETregion = regionEdit.getText().toString();
                String ETage = ageEdit.getText().toString();
                String ETweight = weightEdit.getText().toString();
                String ETheight = heightEdit.getText().toString();

                saveUserDataToFirebase(ETemail, ETuname, ETcontact, ETfname, ETmname, ETlname, EThouse, ETbarangay, ETregion, ETage, ETweight, ETheight);
                Toast.makeText(EditProfile.this, "Updated Successfuly", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), mainscreen.class));

                if (imageUri != null) {
                    String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    String imageName = "profile_images/" + currentUserId + ".jpg";

                    StorageReference imageRef = storageReference.child(imageName);

                    imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                       imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                           String imageUrl = uri.toString();

                           databaseReference.child("profileImage").setValue(imageUrl);

                           Toast.makeText(EditProfile.this, "Image uploaded and saved successfully", Toast.LENGTH_SHORT).show();
                       });
                    }).addOnFailureListener(e -> {
                        Toast.makeText(EditProfile.this, "Image upload Failed", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });




        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            profile.setImageURI(imageUri);

            // Upload the image to Firebase Storage and save the URL to the user's database
            uploadImageToFirebaseStorage();

        }
    }

    private void saveUserDataToFirebase(String ETemail, String ETuname, String ETcontact, String ETfname, String ETmname, String ETlname, String EThouse, String ETbarangay, String ETregion, String ETage, String ETweight, String ETheight) {
       FirebaseUser firebaseUser = auth.getCurrentUser();
       String userID = firebaseUser.getUid();

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("users");
        databaseReference1.child(userID).child("emailaddress").setValue(ETemail);
        databaseReference1.child(userID).child("username").setValue(ETuname);
        databaseReference1.child(userID).child("phonenumber").setValue(ETcontact);
        databaseReference1.child(userID).child("firstname").setValue(ETfname);
        databaseReference1.child(userID).child("middlename").setValue(ETmname);
        databaseReference1.child(userID).child("lastname").setValue(ETlname);
        databaseReference1.child(userID).child("housenumber").setValue(EThouse);
        databaseReference1.child(userID).child("barangay,city").setValue(ETbarangay);
        databaseReference1.child(userID).child("region,province").setValue(ETregion);
        databaseReference1.child(userID).child("age").setValue(ETage);
        databaseReference1.child(userID).child("weight").setValue(ETweight);
        databaseReference1.child(userID).child("height").setValue(ETheight);
    }


    private void retrieveProfileInformation() {
        databaseReference.get().addOnSuccessListener(documentSnapshot -> {
            if  (documentSnapshot.exists()) {
                String email = documentSnapshot.child("emailaddress").getValue(String.class);
                String firstname = documentSnapshot.child("firstname").getValue(String.class);
                String middlename = documentSnapshot.child("middlename").getValue(String.class);
                String lastname = documentSnapshot.child("lastname").getValue(String.class);
                String username = documentSnapshot.child("username").getValue(String.class);
                String phone = documentSnapshot.child("phonenumber").getValue(String.class);
                String house_no = documentSnapshot.child("housenumber").getValue(String.class);
                String barangay_city = documentSnapshot.child("barangay,city").getValue(String.class);
                String region_province = documentSnapshot.child("region,province").getValue(String.class);
                String Age = documentSnapshot.child("age").getValue(String.class);
                String Weight = documentSnapshot.child("weight").getValue(String.class);
                String Height = documentSnapshot.child("height").getValue(String.class);

                emailEdit.setText(email);
                fnameEdit.setText(firstname);
                lnameEdit.setText(lastname);
                unameEdit.setText(username);
                mnameEdit.setText(middlename);
                contactEdit.setText(phone);
                houseEdit.setText(house_no);
                barangayEdit.setText(barangay_city);
                regionEdit.setText(region_province);
                ageEdit.setText(Age);
                weightEdit.setText(Weight);
                heightEdit.setText(Height);

                String imageUrl = documentSnapshot.child("profileImage").getValue(String.class);

                if (imageUrl != null){
                    Glide.with(EditProfile.this).load(imageUrl).into(profile);
                }
            }
        });
    }
    private void uploadImageToFirebaseStorage() {
        if (imageUri != null) {
            String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String imageName = "profile_images/" + currentUserId + ".jpg";


            StorageReference imageRef = storageReference.child(imageName);

            imageRef.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imageUrl = uri.toString();

                    databaseReference.child("profileImage").setValue(imageUrl);

                    Toast.makeText(EditProfile.this, "Image uploaded and saved successfully", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                });

            }).addOnFailureListener(e -> {
                Toast.makeText(EditProfile.this, "Image upload Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        }
    }
}