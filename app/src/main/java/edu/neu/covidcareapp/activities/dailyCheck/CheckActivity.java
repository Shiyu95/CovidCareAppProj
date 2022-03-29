package edu.neu.covidcareapp.activities.dailyCheck;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import edu.neu.covidcareapp.R;
import edu.neu.covidcareapp.activities.test.HeartRateActivity;

public class CheckActivity extends AppCompatActivity {

    private EditText etTemp;
    private RadioButton rb_h_y;
    private RadioButton rb_h_n;
    private RadioButton rb_c_y;
    private RadioButton rb_c_n;
    private RadioButton rb_b_y;
    private RadioButton rb_b_n;
    private RadioButton rb_l_y;
    private RadioButton rb_l_n;
    private RadioButton rb_fever_y;
    private RadioButton rb_fever_n;
    private RadioButton rb_cv_y;
    private RadioButton rb_cv_n;
    private Check check;
    private EditText tvHeart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);
        etTemp = findViewById(R.id.et_temp);
        rb_h_y = findViewById(R.id.rb_h_y);
        rb_h_n = findViewById(R.id.rb_h_n);
        tvHeart = findViewById(R.id.tvHeart);
        rb_fever_y = findViewById(R.id.rb_fever_y);
        rb_fever_n = findViewById(R.id.rb_fever_n);

        rb_c_y = findViewById(R.id.rb_c_y);
        rb_c_n = findViewById(R.id.rb_c_n);

        rb_b_y = findViewById(R.id.rb_b_y);
        rb_b_n = findViewById(R.id.rb_b_n);

        rb_l_y = findViewById(R.id.rb_l_y);
        rb_l_n = findViewById(R.id.rb_l_n);


        rb_cv_y = findViewById(R.id.rb_cv_y);
        rb_cv_n = findViewById(R.id.rb_cv_n);
        findViewById(R.id.bt_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (check!=null){
                    return;
                }
                check();
            }
        });
        findViewById(R.id.bt_heart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CheckActivity.this, HeartRateActivity.class));
            }
        });
        initData();
    }

    private void initData() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("dailyCheck").child(mAuth.getUid()).child(getToday()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Log.e("TAG",snapshot.toString());
                String key = snapshot.getKey();
                check = snapshot.getValue(Check.class);
                if (check!=null){
                    rb_h_y.setClickable(false);
                    rb_h_n.setClickable(false);

                    rb_c_y.setClickable(false);
                    rb_c_n.setClickable(false);

                    rb_b_y.setClickable(false);
                    rb_b_n.setClickable(false);

                    rb_l_y.setClickable(false);
                    rb_l_n.setClickable(false);
                    etTemp.setEnabled(false);
                    tvHeart.setEnabled(false);
                    etTemp.setText(check.getTemp());
                    tvHeart.setText(check.getHeart());
                    if (check.getThroat().equals("yes")){
                        rb_h_y.setChecked(true);
                    }else{
                        rb_h_n.setChecked(true);
                    }

                    if (check.getFeverish().equals("yes")){
                        rb_fever_y.setChecked(true);
                    }else{
                        rb_fever_n.setChecked(true);
                    }
                    if (check.getCough().equals("yes")){
                        rb_c_y.setChecked(true);
                    }else{
                        rb_c_n.setChecked(true);
                    }
                    if (check.getDiarrhea().equals("yes")){
                        rb_b_y.setChecked(true);
                    }else{
                        rb_b_n.setChecked(true);
                    }
                    if (check.getCloseContact().equals("yes")){
                        rb_cv_y.setChecked(true);
                    }else{
                        rb_cv_n.setChecked(true);
                    }
                    if (check.getLossOfTaste()!=null&&check.getLossOfTaste().equals("yes")){
                        rb_l_y.setChecked(true);
                    }else{
                        rb_l_n.setChecked(true);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void check() {
        int number = 100;
        HashMap hashMap = new HashMap<>();
        String temp = etTemp.getText().toString();
        hashMap.put("temp", temp);
        if (rb_h_y.isChecked()) {
            hashMap.put("throat", "yes");
            number-=2;
        } else {

            hashMap.put("throat", "no");
        }
        if (rb_cv_y.isChecked()) {
            number-=5;
            hashMap.put("closeContact", "yes");
        } else {

            hashMap.put("closeContact", "no");
        }
        if (rb_fever_y.isChecked()) {
            number-=2;
            hashMap.put("feverish", "yes");
        } else {

            hashMap.put("feverish", "no");
        }
        if (rb_c_y.isChecked()) {
            number-=2;
            hashMap.put("cough", "yes");
        } else {

            hashMap.put("cough", "no");
        }
        if (rb_b_y.isChecked()) {
            number-=2;
            hashMap.put("diarrhea", "yes");
        } else {

            hashMap.put("diarrhea", "no");
        }
        if (rb_l_y.isChecked()) {
            number-=2;
            hashMap.put("lossOfTaste", "yes");
        } else {

            hashMap.put("lossOfTaste", "no");
        }
        double value = Double.parseDouble(temp);
        if (value>37.3){
            number-=8;
        }
        String heart = tvHeart .getText().toString();
        if (!TextUtils.isEmpty(heart)){
            hashMap.put("heart", heart);
        }
        if(Double.parseDouble(heart)>130){
            number-=2;
        }
        if (number<90){
            new AlertDialog.Builder(this)
                    .setTitle("Warn!")
                    .setMessage("your health score is too low!")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create().show();
        }else{
            Toast.makeText(this,"your health score is good!",Toast.LENGTH_SHORT).show();
        }

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("dailyCheck").child(mAuth.getUid()).child(getToday()).setValue(hashMap);
    }
    private String getToday(){
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
        String dateNowStr = sdf.format(new Date());
        return dateNowStr;
    }
}