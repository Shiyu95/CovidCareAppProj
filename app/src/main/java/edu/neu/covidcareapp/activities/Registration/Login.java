package edu.neu.covidcareapp.activities.Registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import edu.neu.covidcareapp.R;
import edu.neu.covidcareapp.activities.Home;

public class Login extends AppCompatActivity {

    private EditText userMail, userPassword;
    private Button loginBtn;
    private TextView needRegister, forgotPassword;


    private FirebaseAuth mAuth;
    private Intent HomeActivity;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userMail = findViewById(R.id.loginUserMail);
        userPassword = findViewById(R.id.loginPassword);
        loginBtn = findViewById(R.id.loginBtn);
        needRegister = findViewById(R.id.signUp);
        forgotPassword = findViewById(R.id.forgot_password);

        mAuth = FirebaseAuth.getInstance();
        HomeActivity = new Intent(Login.this, Home.class);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mail = userMail.getText().toString();
                final String password = userPassword.getText().toString();

                if(mail.isEmpty() || password.isEmpty()){
                    showMessage("Please Verify all field");
                }else{
                    signIn(mail, password);
                }
            }
        });

        needRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
                finish();
            }
        });


        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, ForgotPassword.class);
                startActivity(i);
                finish();
            }
        });



    }

    // reference document:https://firebase.google.com/docs/auth/android/password-auth?hl=el&authuser=0
     public void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            showMessage("UserWithEmail:failure" +task.getException().getMessage());

                        }
                    }
                });
    }


    public String validate(String userName, String password)
    {
        if(userName.equals("user") && password.equals("user"))
            return "Login was successful";
        else
            return "Invalid login!";
    }

    private void updateUI() {
        startActivity(HomeActivity);
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null) {
            //user is already connected
            // so we need to redirect him to home page
            updateUI();

        }

    }
}