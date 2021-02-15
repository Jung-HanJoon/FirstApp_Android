package com.example.rentaldream.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rentaldream.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    EditText et_id;
    EditText et_pass;
    Button btn_login;
    Button btn_signup, btn_findpass;
    CheckBox auto_login;
    String gogo;
    String data;
    String into;
    String chatroomid;
    String sellerID;
    String saln;
    String buyer;
    String buyerName;

    FirebaseFirestore db = FirebaseFirestore.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        et_id = (EditText) findViewById(R.id.et_id);
        et_pass = (EditText) findViewById(R.id.et_pass);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        auto_login = (CheckBox) findViewById(R.id.auto_login);
        btn_findpass = (Button)findViewById(R.id.btn_findpass);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = et_id.getText().toString();
                String pass = et_pass.getText().toString();
                if (id.equals("")) {
                    Toast.makeText(getApplicationContext(), "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if (pass.equals("")) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    login(id, pass);

                }
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


        btn_findpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.sendPasswordResetEmail(et_id.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "해당 이메일로 비밀번호 변경 메일을 전송했습니다.", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "이메일을 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        gogo = getIntent().getStringExtra("gogo");
        data = getIntent().getStringExtra("data");
        into = getIntent().getStringExtra("into");
        chatroomid = getIntent().getStringExtra("chatroomid");
        sellerID = getIntent().getStringExtra("sellerID");
        saln = getIntent().getStringExtra("saln");
        buyer = getIntent().getStringExtra("buyer");
        buyerName = getIntent().getStringExtra("buyerName");

        if(getIntent()!=null && getIntent().hasExtra("data")){
            data = getIntent().getExtras().getString("data");
            gogo = getIntent().getExtras().getString("gogo");
            if(gogo.equals("request")){
                into = getIntent().getExtras().getString("into");
                chatroomid = getIntent().getExtras().getString("chatroomid");
                sellerID = getIntent().getExtras().getString("sellerID");
                saln = getIntent().getExtras().getString("saln");
                buyer = getIntent().getExtras().getString("buyer");
                buyerName = getIntent().getExtras().getString("buyerName");
            }
        }
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){

            final String[] lastlog = new String[1];
            db.collection("users").document(currentUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.get("lastlogin")!=null){
                        lastlog[0] = documentSnapshot.get("lastlogin").toString();
                    }else {
                        lastlog[0] = "접속기록 없음";
                    }

                    long now;

                    Date date;

                    now = System.currentTimeMillis();
                    date = new Date(now);
                    SimpleDateFormat sdfnow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    String strnow = sdfnow.format(date);
                    db.collection("users").document(currentUser.getUid()).update("lastlogin", strnow).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(getApplicationContext(),"마지막 접속일 : "+lastlog[0],Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, FirstShowActivity.class);
                            intent.putExtra("data",data);
                            intent.putExtra("gogo",gogo);
                            intent.putExtra("lastlog", lastlog[0]);
                            intent.putExtra("into", into);
                            intent.putExtra("chatroomid", chatroomid);
                            intent.putExtra("sellerID", sellerID);
                            intent.putExtra("saln", saln);
                            intent.putExtra("buyer", buyer);
                            intent.putExtra("buyerName", buyerName);

                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });

        }
        //updateUI(currentUser);
    }

    private void login(String id, String pass){

        mAuth.signInWithEmailAndPassword(id, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "loginWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Intent intent = new Intent(LoginActivity.this, FirstShowActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "logInWithEmail:failure", task.getException());
                            //Toast.makeText(LoginActivity.this, "Authentication failed.",Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "아이디 혹은 비밀번호를 확인하세요.", Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });

}


}


