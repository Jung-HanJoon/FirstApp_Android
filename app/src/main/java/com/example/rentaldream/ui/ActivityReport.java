package com.example.rentaldream.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rentaldream.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ActivityReport extends AppCompatActivity {

    Button btn_post;
    EditText et_title, et_target, et_content;
    Spinner spinner;

    private FirebaseFirestore db  = FirebaseFirestore.getInstance();

    String targetUid;
    String targetName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Intent intents = this.getIntent();
        targetUid = intents.getStringExtra("tUid");
        targetName = intents.getStringExtra("tName");

        btn_post = (Button)findViewById(R.id.btn_checkprice);
        et_title = (EditText) findViewById(R.id.et_title);
        et_target = (EditText)findViewById(R.id.et_target);
        et_content = (EditText)findViewById(R.id.et_content);
        spinner = (Spinner) findViewById(R.id.spinner);


        //Toast.makeText(getApplicationContext(),"범인은 "+targetName,Toast.LENGTH_SHORT).show();

        et_target.setText(targetName);



        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_title.getText().equals("")||et_target.getText().equals("")||et_content.getText().equals("")){
                    Toast.makeText(getApplicationContext(), "빈 항목이 없는지 확인하세요.", Toast.LENGTH_SHORT).show();
                }else {
                    Map map = new HashMap();
                    map.put("title", et_title.getText().toString());
                    map.put("target", targetName);
                    map.put("content", et_content.getText().toString());
                    map.put("category", spinner.getSelectedItem().toString());
                    map.put("targetUid", targetUid);

                    db.collection("report").document().set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(), "신고가 접수되었습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }
            }
        });
    }
}
