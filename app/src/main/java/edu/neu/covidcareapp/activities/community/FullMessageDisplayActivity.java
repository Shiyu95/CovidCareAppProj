package edu.neu.covidcareapp.activities.community;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import edu.neu.covidcareapp.R;

public class FullMessageDisplayActivity extends AppCompatActivity {
    //Firebase
    FirebaseDatabase database;
    FirebaseUser firebaseUser;
    FirebaseAuth firebaseAuth;

    TextView displayTitle;
    TextView displayDescription;
    TextView displayContent;

    TextView displayDate;

    ImageView imgCurUser;

    final static String COMMENT = "Comment" ;

    EditText editComment;
    Button addCommentBtn;

    RecyclerView  recyclerViewComment;
    CommentAdapter commentAdapter;
    List<Comment> listComment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_message_display);


        //firebase
        database = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        String title = getIntent().getStringExtra("POST_TITLE");
        String desc = getIntent().getStringExtra("POST_DESC");
        String content = getIntent().getStringExtra("POST_CONTENT");

        String postID = getIntent().getStringExtra("POST_ID");

        System.out.println("PostID: " + postID);

        String date = getIntent().getStringExtra("postDate");


        addCommentBtn = findViewById(R.id.add_comment_btn);
        editComment = findViewById(R.id.edit_comment);

        displayTitle = findViewById(R.id.display_post_title);
        displayTitle.setText(title);

        displayDescription = findViewById(R.id.display_description);
        displayDescription.setText(desc);

        displayContent = findViewById(R.id.display_content);
        displayContent.setText(content);

        displayDate = findViewById(R.id.post_date);
        displayDate.setText(date);

        imgCurUser = findViewById(R.id.post_detail_currentuser_img);


        recyclerViewComment = findViewById(R.id.comment_display);


        // set comment user image
        Glide.with(this).load(firebaseUser.getPhotoUrl()).into( imgCurUser);


        //add Comment button click listner
        addCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addCommentBtn.setVisibility(View.INVISIBLE);
                DatabaseReference commentReference = database.getReference(COMMENT).child(postID).push();

                String comment_content = editComment.getText().toString();
                String userUid = firebaseUser.getUid();
                String userName = firebaseUser.getDisplayName();
                String userImg = firebaseUser.getPhotoUrl().toString();
                Comment comment = new Comment(comment_content,userUid,userImg,userName);


                commentReference.setValue(comment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        showMessage("comment added");
                        editComment.setText("");
                        addCommentBtn.setVisibility(View.VISIBLE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showMessage("comment added fail " + e.getMessage());
                    }
                });

            }
        });







        //comment recyclerview start

        recyclerViewComment.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference commentRef = database.getReference(COMMENT).child(postID);
        commentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                listComment = new ArrayList<>();
                for(DataSnapshot ds:snapshot.getChildren()){
                    Comment comment = ds.getValue(Comment.class);
                    listComment.add(comment);
                }


                commentAdapter = new CommentAdapter(getApplicationContext(), listComment);
                recyclerViewComment.setAdapter(commentAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void showMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

}