package edu.neu.covidcareapp.activities.community;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import edu.neu.covidcareapp.R;

public class communityActivity extends AppCompatActivity {


    private ArrayList<PostCard> PostList = new ArrayList<>();
    private FloatingActionButton addButton;
    private RecyclerView recyclerView;
    private ReviewAdapter reviewAdapter;
    private RecyclerView.LayoutManager rLayoutManger;

    //Firebase
    private DatabaseReference ref;
    FirebaseDatabase database;
    FirebaseUser currentUser;
    FirebaseAuth mAuth;

    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        //create recycleReview
        init(savedInstanceState);
        addButton =  findViewById(R.id.floatingActionButton);

        //instance for firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        ref = database.getReference("postDatabase");//set up post database



        String uid = currentUser.getUid();
        String userName = currentUser.getDisplayName();
        String userPhoto = currentUser.getPhotoUrl().toString();

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference postRef = rootRef.child("covidcareapp-e9687-default-rtdb").child("postDatabase");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String description = ds.child("description").getValue(String.class);
                    String title = ds.child("title").getValue(String.class);
                    String content = ds.child("postContext").getValue(String.class);
                    String userName = ds.child("name").getValue(String.class);
                    String postKey = ds.child("postKey").getValue(String.class);
                    System.out.println(title);
                    System.out.println(userName);
                    PostCard itemCard = new PostCard(postKey,title,description, content);
                    PostList.add(itemCard);
                    Log.d("TAG", description);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        postRef.addListenerForSingleValueEvent(eventListener);

        //set actions to floatAddButton
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(communityActivity.this, NewPostActivity.class);

                // start the input activity
                startActivityForResult(intent, 2);

            }
        });


    }



    //initialize RecyclerView
    private void init(Bundle savedInstanceState) {
        initialItemData(savedInstanceState);
        //createRecyclerView();
    }

    private void initialItemData(Bundle savedInstanceState) {
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("postDatabase");
        Query checkUser = rootRef;
        //DatabaseReference postRef = rootRef.child("covidcareapp-e9687-default-rtdb").child("postDatabase");
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    String description = ds.child("description").getValue(String.class);

                    String title = ds.child("title").getValue(String.class);
                    System.out.println(title);
                    String userName = ds.child("name").getValue(String.class);

                    String content = ds.child("postContext").getValue(String.class);
                    String postKey = ds.child("postKey").getValue(String.class);
                    PostCard itemCard = new PostCard(postKey,title,description, content);
                    PostList.add(itemCard);
                    Log.d("TAG", description);
                }
                createRecyclerView();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

//        if (savedInstanceState != null && savedInstanceState.containsKey(NUMBER_OF_ITEMS)) {
//            if (PostList == null || PostList.size() == 0) {
//
//                int size = savedInstanceState.getInt(NUMBER_OF_ITEMS);
//                // Retrieve keys we stored in the instance
//                for (int i = 0; i < size; i++) {
//                    String title = savedInstanceState.getString(KEY_OF_INSTANCE + i + "0");
//                    String desc = savedInstanceState.getString(KEY_OF_INSTANCE + i + "1");
//
//                    PostCard itemCard = new PostCard(title,desc);
//                    PostList.add(itemCard);
//                }
//            }
//        }



    //Get result from input activity
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        //DatabaseReference postsRef = ref.child("posts");//saving posts to real time database
        DatabaseReference postsRef = ref;
        //Map<String,Post> posts = new HashMap<>();//Saving posts to map
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode==2)
        {
            String inputTitle = data.getStringExtra("TITLE");//get post title from user
            String inputDesc = data.getStringExtra("DESC");//get post description from user
            String inputContext = data.getStringExtra("CONTEXT");//get post context from user

            //posts.put("Molly", new Post("Molly", inputTitle,inputDesc,inputContext));



            DatabaseReference newPostRef = postsRef.push();//generate unique ID


            // Get the unique ID generated by a push()
            //TODO:Get current username
            String userName = currentUser.getDisplayName();
            Post post = new Post(userName, inputTitle,inputDesc,inputContext);
            String key = newPostRef.getKey();
            post.setPostKey(key);

            newPostRef.setValue(post);



            View v = findViewById(R.id.activity_community);

            int pos = 0;
            addItem(pos,key,inputTitle,inputDesc, inputContext); //add a new card when clicking fab
            Snackbar.make(v, "Your new post has been posted out!", Snackbar.LENGTH_LONG).setAction("Action", null).show();

        }
    }



    //createRecyclerView method
    private void createRecyclerView() {
        rLayoutManger = new LinearLayoutManager(this);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        reviewAdapter = new ReviewAdapter(PostList);
        ItemClickListener itemClickListener = new ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //attributions bond to the item has been changed
                PostList.get(position).onItemClick(position);
//                String URL = PostList.get(position).getItemURL();
//                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
//                startActivity(browserIntent);
                //reviewAdapter.notifyItemChanged(position);
                String postTitle = PostList.get(position).getPostTitle();
                String postDesc= PostList.get(position).getPostDesc();
                String postContent = PostList.get(position).getPostContent();

                String postID = PostList.get(position).getPostKey();

                Intent intent = new Intent(getBaseContext(), FullMessageDisplayActivity.class);
                intent.putExtra("POST_TITLE", postTitle);
                intent.putExtra("POST_DESC", postDesc);
                intent.putExtra("POST_CONTENT", postContent);
                intent.putExtra("POST_ID",postID);
                startActivity(intent);

            }

        };
        reviewAdapter.setOnItemClickListener(itemClickListener);
        recyclerView.setAdapter(reviewAdapter);
        recyclerView.setLayoutManager(rLayoutManger);
    }

    //addItem method
    private void addItem(int position,String postKey,String title,String description, String content) {
        //add a new card
        PostList.add(position, new PostCard(postKey,title,description, content));
        //Toast.makeText(lcActivity.this, "A new link has been added", Toast.LENGTH_SHORT).show();
        reviewAdapter.notifyItemInserted(position);
    }





}