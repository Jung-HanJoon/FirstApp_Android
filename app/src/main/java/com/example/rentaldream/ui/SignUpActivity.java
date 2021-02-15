package com.example.rentaldream.ui;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rentaldream.model.MemberInfo;
import com.example.rentaldream.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";
    private FirebaseAuth mAuth;

    private EditText et_id, et_pass, et_pass2, et_name, et_jumin, et_jumin2, et_tel;
    private Button bt_sign, btn_check;
    private ScrollView sv_signup, sv_check;

    long now;
    Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        et_id = (EditText) findViewById(R.id.et_id);
        et_pass = (EditText) findViewById(R.id.et_pass);
        et_pass2 = (EditText) findViewById(R.id.et_pass2);
        et_name = (EditText) findViewById(R.id.et_name);
        et_jumin = (EditText) findViewById(R.id.et_jumin);
        et_jumin2 = (EditText) findViewById(R.id.et_jumin2);
        et_tel = (EditText) findViewById(R.id.et_tel);
        bt_sign = (Button) findViewById(R.id.bt_sign);
        btn_check = (Button) findViewById(R.id.btn_check);
        sv_signup = (ScrollView) findViewById(R.id.sv_signup);
        sv_check = (ScrollView) findViewById(R.id.sv_check);

        bt_sign.setVisibility(View.INVISIBLE);
        sv_signup.setVisibility(View.INVISIBLE);
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sv_check.setVisibility(View.INVISIBLE);
                sv_signup.setVisibility(View.VISIBLE);
                bt_sign.setVisibility(View.VISIBLE);
                now = System.currentTimeMillis();
                date = new Date(now);
                SimpleDateFormat postDates = new SimpleDateFormat("yyyy / MM / dd HH:mm:ss");
                String postDate = postDates.format(date);
                Toast.makeText(getApplicationContext(), postDate + "\n약관에 동의하였습니다.", Toast.LENGTH_LONG).show();

            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    Activity#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for Activity#requestPermissions for more details.
                return;
            }
        }

        TelephonyManager telManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

        String phoneNum = telManager.getLine1Number();
        if(phoneNum.startsWith("+82")){
            phoneNum = phoneNum.replace("+82", "0");
        }
        et_tel.setText(PhoneNumberUtils.formatNumber(phoneNum, Locale.getDefault().getCountry()));
        et_tel.setFocusable(false);


        //phoneNum = phoneNum.substring(phoneNum.length()-10,phoneNum.length());
        //phoneNum = PhoneNumberUtils.formatNumber(phoneNum);
        //et_tel.setText(phoneNum);
        //DecimalFormat formatter1 = new DecimalFormat("###-####-####");






        bt_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = et_id.getText().toString();
                String pass = et_pass.getText().toString();
                String pass2 = et_pass2.getText().toString();
                String name = et_name.getText().toString();
                String jumin = et_jumin.getText().toString();
                String jumin2 = et_jumin2.getText().toString();
                String tel = et_tel.getText().toString();


                if(id.equals("")||pass.equals("")||pass2.equals("")||name.equals("")||jumin.equals("")||jumin2.equals("")||tel.equals("")){
                    Toast.makeText(getApplicationContext(),"모든 항목을 입력해주세요.",Toast.LENGTH_SHORT).show();
                }else if(pass.equals(pass2)==false){
                    Toast.makeText(getApplicationContext(),"비밀번호를 확인해주세요.",Toast.LENGTH_SHORT).show();
                }else if(jumin.length()!=6||jumin2.length()!=7){
                    Toast.makeText(getApplicationContext(),"주민번호를 다시 확인해주세요.",Toast.LENGTH_SHORT).show();
                }else if(et_tel.getText().toString().equals("")){
                    Toast.makeText(getApplicationContext(),"휴대폰 번호를 다시 확인해주세요.",Toast.LENGTH_SHORT).show();
                }else if(pass.length()<6){
                    Toast.makeText(getApplicationContext(),"비밀번호는 6글자 이상으로 작성해주세요.",Toast.LENGTH_SHORT).show();
                }else{
                    signup();

                }
            }
        });


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
    }
    private  void signup(){
        String id = et_id.getText().toString();
        String pass = et_pass.getText().toString();
        final String name = et_name.getText().toString();
        final String jumin = et_jumin.getText().toString();
        final String tel = et_tel.getText().toString();

        mAuth.createUserWithEmailAndPassword(id, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            FirebaseUser uuser = FirebaseAuth.getInstance().getCurrentUser();

                            String token = FirebaseInstanceId.getInstance().getToken();

                            MemberInfo memberInfo = new MemberInfo(name, tel, jumin, token, null, null);
                            db.collection("users").document(uuser.getUid()).set(memberInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_SHORT).show();
                                }
                            });
                            Toast.makeText(getApplicationContext(),"가입을 환영합니다.",Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(),"가입에 실패했습니다.",Toast.LENGTH_SHORT).show();
                        }
                        // ...
                    }
                });
    }
}