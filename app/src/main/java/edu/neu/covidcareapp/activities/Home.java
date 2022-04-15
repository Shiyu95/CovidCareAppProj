package edu.neu.covidcareapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;


import edu.neu.covidcareapp.R;
import edu.neu.covidcareapp.activities.Registration.Login;
import edu.neu.covidcareapp.databinding.ActivityHome2Binding;


public class Home extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHome2Binding binding;


    //firebase
    FirebaseAuth mAuth;
    FirebaseUser currentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHome2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);


        //instance for firebase
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();



//        binding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_profile, R.id.nav_logout)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
       NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        updateNaveHeader();

      //  navigationView.bringToFront();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_home:
                        Intent home = new Intent(Home.this, Home.class);
                        startActivity(home);
                        break;
                    case R.id.nav_profile:
                        Intent profile = new Intent(Home.this, Profile.class);
                        startActivity(profile);
                        break;
                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        Intent loginActivity  = new Intent(Home.this, Login.class);
                        startActivity(loginActivity);
                        finish();
                        break;
                }



                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }





    //update information for the naveHeader
    public void updateNaveHeader(){
        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        ImageView navUserPhoto = headerView.findViewById(R.id.nav_userPhoto);
        TextView navUserName = headerView.findViewById(R.id.nav_userName);
        TextView navUserEmail = headerView.findViewById(R.id.nav_userEmail);

        navUserEmail.setText(currentUser.getEmail());
        navUserName.setText(currentUser.getDisplayName());
        //navUserPhoto.setImageURI(currentUser.getPhotoUrl());

        //Use glide to load image
        //import library

        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhoto);




    }

}