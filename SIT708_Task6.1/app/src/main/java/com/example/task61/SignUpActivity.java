package com.example.task61;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_MEDIA_IMAGES;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.task61.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;
    FirebaseAuth mAuth;

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("users");
    StorageReference storageRef = FirebaseStorage.getInstance().getReference();
    Uri imageUri;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Creating binding for using items from view
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //when addimage button pressed, open storage manager to select image
                //if permissions are not allowed, prompt user to allow permission
                if (SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        Intent intentImage = new Intent();
                        intentImage.setAction(Intent.ACTION_GET_CONTENT);
                        intentImage.setType("image/*");
                        startActivityForResult(intentImage, 1);
                    } else { //request for the permission
                        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivity(intent);
                    }
                } else {
                    //below android 11 request for permission
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{READ_EXTERNAL_STORAGE}, 100);
                }
            }
        });

        binding.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password, confirmPassword, fullName, phoneNumber;

                //get input email and password
                email = binding.emailEditText.getText().toString().trim();
                password = binding.passwordEditText.getText().toString();
                confirmPassword = binding.confirmPasswordEditText.getText().toString();
                fullName = binding.fullNameEditText.getText().toString().trim();
                phoneNumber = binding.phoneNumberEditText.getText().toString().trim();

                //check all details have been input
                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(SignUpActivity.this, "Please Enter an Email Address", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(password)) {
                    Toast.makeText(SignUpActivity.this, "Please Enter a Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(imageUri == null) {
                    Toast.makeText(SignUpActivity.this, "Please Add an Image", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!password.equals(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this, "Check Passwords Match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(fullName)) {
                    Toast.makeText(SignUpActivity.this, "Please Enter Your Full Name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(SignUpActivity.this, "Please Enter a Phone Number", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //if creation of user is successful
                                if (task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Account Created.",
                                            Toast.LENGTH_SHORT).show();

                                    //add image to storage database
                                    //this will be stored as a folder named the user id
                                    //within this folder is profileImage.(extension)
                                    String id = mAuth.getCurrentUser().getUid();
                                    StorageReference fileRef = storageRef.child(id).child("profileImage." +
                                            MimeTypeMap.getSingleton()
                                                    .getExtensionFromMimeType(getContentResolver().getType(imageUri)));

                                    fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    Toast.makeText(SignUpActivity.this, "Upload Successful.",
                                                            Toast.LENGTH_SHORT).show();
                                                    //if upload of image is successful
                                                    //add extra data (image) to the database
                                                    //this will be stored under 'users>'userID'>(data here eg. imageUrl)'
                                                    UserModel userModel = new UserModel(uri.toString(), fullName, phoneNumber);
                                                    myRef.child(id).setValue(userModel);

                                                }
                                            });
                                        }
                                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(SignUpActivity.this, "Uploading failed.",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
        });

        binding.returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //show the image when user has successfully selected an image from storage
        if(requestCode == 1 && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            binding.addImage.setImageURI(imageUri);

        }

    }
}