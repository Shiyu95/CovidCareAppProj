package edu.neu.covidcareapp.activities.Registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import edu.neu.covidcareapp.R;
import edu.neu.covidcareapp.activities.Home;

public class Register extends AppCompatActivity {

    ImageView regUserPhoto;
    int PRegCode =1;
    int PICK_IMAGE = 1;
    Uri imageUri;


    private EditText userName, userEmail, userPassword1, userPassword2;
    private Button registerBtn;

    private TextView signin;

    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageRef;


    //reference document : https://www.tutorialspoint.com/how-to-pick-an-image-from-image-gallery-in-android
    //https://stackoverflow.com/questions/5309190/android-pick-images-from-gallery
    //https://stackoverflow.com/questions/23074845/android-choose-image-from-gallery-and-store-it-in-a-file-type-variable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //connect with register.xml
        userName = findViewById(R.id.regUsername);
        userEmail = findViewById(R.id.regEmail);
        userPassword1 = findViewById(R.id.regPassword1);
        userPassword2 = findViewById(R.id.regPassword2);
        registerBtn = findViewById(R.id.registerBtn);

        signin = findViewById(R.id.signIn);

        mAuth = FirebaseAuth.getInstance();


        //Already have an account, just sign in;
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
                finish();
            }
        });



        //get all information for register
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get user register info
                final String name = userName.getText().toString();
                final String email = userEmail.getText().toString();
                final String password1 = userPassword1.getText().toString();
                final String password2 = userPassword2.getText().toString();

                if(name.isEmpty() || email.isEmpty() || password1.isEmpty()
                        ||!password1.equals(password2)){
                    showMessage("Please Verify all field");
                }else{
                    createUserAccount(name, email, password1);
                }


            }
        });


        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        //user choose image from storage
        regUserPhoto = findViewById(R.id.regUserPhoto);
        regUserPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(Build.VERSION.SDK_INT >= 22){
//                    checkAndRequestForPermission();
//
//                } else{
//                    openGallery();
//                }
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Title"), PICK_IMAGE);

            }
        });

    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data!= null && data.getData()!=null){
            imageUri = data.getData();
            regUserPhoto.setImageURI(imageUri);
           // uploadPicture();
        }
    }


    //Upload from a local file
    //https://firebase.google.com/docs/storage/android/upload-files
    private void uploadPicture() {
        final String key = UUID.randomUUID().toString();
        StorageReference photoRef = storageRef.child("images/" + key);


       // Register observers to listen for when the download is done or if it fails
        photoRef.putFile(imageUri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(),"Failed To Upload", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Snackbar.make(findViewById(android.R.id.content),"Image Upload.",Snackbar.LENGTH_LONG).show();
            }
        });



    }

    //create user account by using email and password
    //reference document :https://firebase.google.com/docs/auth/android/password-auth
    private void createUserAccount(String name, String email, String password1) {
        mAuth.createUserWithEmailAndPassword(email,password1).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update the signed-in user's information
                    showMessage("createUserWithEmail:success");
                    FirebaseUser user = mAuth.getCurrentUser();
                    updateUserInfo(name, imageUri,user);
                    //updateUI();

                } else {
                    // If sign in fails, display a message to the user.
                    showMessage("createUserWithEmail:failure" +task.getException().getMessage());

                }

            }
        });

    }

    private void updateUserInfo(String name, Uri imageUri, FirebaseUser currentUser) {
        StorageReference photoRef = storageRef.child("images/" + name);


        // Register observers to listen for when the download is done or if it fails
        photoRef.putFile(imageUri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(),"Failed To Upload", Toast.LENGTH_LONG).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // image uploaded successfully
                // now we can get our image url
                photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .setPhotoUri(uri).build();
                        currentUser.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // user info updated successfully
                                    showMessage("Register Complete");
                                    updateUI();
                                }
                            }
                        });

                    }
                });
            }
        });



    }



    private void updateUI() {
        Intent home = new Intent(getApplicationContext(), Home.class);
        startActivity(home);
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

//    private void openGallery() {
//        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        getIntent.setType("image/*");
//        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//        pickIntent.setType("image/*");
//        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
//        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});
//        startActivityForResult(chooserIntent, PICK_IMAGE);
//    }

//    private void checkAndRequestForPermission() {
//        if(ContextCompat.checkSelfPermission(Register.this, Manifest.permission.READ_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED){
//            if(ActivityCompat.shouldShowRequestPermissionRationale(Register.this,android.Manifest.permission.READ_EXTERNAL_STORAGE)){
//                Toast.makeText(Register.this,"Please accept for required permission for access gallery",
//                        Toast.LENGTH_SHORT).show();
//            }
//            else{
//                ActivityCompat.requestPermissions(Register.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PRegCode);
//            }
//        }
//
//    }


}