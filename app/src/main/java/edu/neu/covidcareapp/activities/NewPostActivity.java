package edu.neu.covidcareapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import edu.neu.covidcareapp.R;

public class NewPostActivity extends AppCompatActivity {
    private EditText title,description,context;
    private Button sub;


    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_post);

        title = findViewById(R.id.post_title);
        description = findViewById(R.id.post_description);
        context = findViewById(R.id.input_context);
        sub = findViewById(R.id.submit_but);



        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inputTitle = title.getText().toString();
                String inputDesc = description.getText().toString();
                String inputContext = context.getText().toString();

                Intent intent = new Intent();

                intent.putExtra("TITLE",inputTitle);
                intent.putExtra("DESC",inputDesc);
                intent.putExtra("CONTEXT",inputContext);

                setResult(2,intent);
                finish();//finishing activity

            }
        });



    }






}
