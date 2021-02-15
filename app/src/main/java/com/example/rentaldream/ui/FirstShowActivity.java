package com.example.rentaldream.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentaldream.model.MemberInfo;
import com.example.rentaldream.adapter.PostAdapter2;
import com.example.rentaldream.model.PostInfo;
import com.example.rentaldream.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class FirstShowActivity extends AppCompatActivity {

    TextView tv_cat, tv_cat_1, tv_cat_2, tv_cat_3, tv_cat_4, tv_cat_5, tv_cat_6, tv_cat_7, tv_cat_8, tv_cat_9, tv_cat_10, tv_cat_11;
    ImageButton btn_fashion, btn_kids, btn_sports, btn_pet, btn_homes, btn_life, btn_car, btn_digital, btn_book, btn_office, btn_jewelry, btn_health;
    Button btn_myhome;

    EditText et_search;
    LinearLayout startview;
    LinearLayout ll_banner;
    ScrollView sv;

    int msgNum=0;
    static Bitmap myprofile = null;
    static String myUid;
    static String myName;
    static String myImgURL=null;

    RecyclerView rv_best;
    LinearLayoutManager linearLayoutManager;
    ArrayList<PostInfo> arrayList;
    PostAdapter2 postAdapter;

    private FirebaseFirestore db  = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiti_main2);


        btn_myhome = (Button)findViewById(R.id.btn_myhome);


        btn_fashion = (ImageButton) findViewById(R.id.btn_fashion);
        btn_kids = (ImageButton)findViewById(R.id.btn_kids);
        btn_sports = (ImageButton)findViewById(R.id.btn_sports);
        btn_pet = (ImageButton)findViewById(R.id.btn_pet);
        btn_homes = (ImageButton)findViewById(R.id.btn_homes);
        btn_life = (ImageButton)findViewById(R.id.btn_life);
        btn_car = (ImageButton)findViewById(R.id.btn_car);
        btn_digital = (ImageButton)findViewById(R.id.btn_digital);
        btn_book = (ImageButton)findViewById(R.id.btn_book);
        btn_office = (ImageButton)findViewById(R.id.btn_office);
        btn_jewelry = (ImageButton)findViewById(R.id.btn_jewelry);
        btn_health = (ImageButton)findViewById(R.id.btn_health);
        et_search = (EditText)findViewById(R.id.et_search);


        ll_banner = (LinearLayout)findViewById(R.id.ll_banner);
        startview = (LinearLayout)findViewById(R.id.startview);
        sv = (ScrollView)findViewById(R.id.scrollview);

        rv_best = (RecyclerView)findViewById(R.id.rv_best);



        final String data = getIntent().getStringExtra("data");
        final String gogo = getIntent().getStringExtra("gogo");
        final String into = getIntent().getStringExtra("into");
        final String chatroomid = getIntent().getStringExtra("chatroomid");
        final String sellerID = getIntent().getStringExtra("sellerID");
        final String saln = getIntent().getStringExtra("saln");
        final String buyer = getIntent().getStringExtra("buyer");
        final String buyerName = getIntent().getStringExtra("buyerName");







        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        myUid = user.getUid();
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                MemberInfo memberInfo = documentSnapshot.toObject(MemberInfo.class);
                myName = memberInfo.getName();
                if(memberInfo.getImageURL()!=null){
                    myImgURL = memberInfo.getImageURL();
                    setImageSrc(memberInfo.getImageURL());
                }
                for(int i=0; i<200;){
                    try {
                        Thread.sleep(10);
                        i++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                startview.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), myName+"님 환영합니다.", Toast.LENGTH_SHORT).show();

                if(data!=null){
                    if(gogo.equals("comment")){
                        Log.i("pushdata => ", data);
                        Intent intent = new Intent(getApplicationContext(), PostViewActivity.class);
                        intent.putExtra("postID", data);
                        startActivity(intent);
                    }else if(gogo.equals("request")){
                        Intent intent = new Intent(getApplicationContext(), ActivityChat.class);
                        intent.putExtra("postID", data);
                        intent.putExtra("into", into);
                        intent.putExtra("chatroomid", chatroomid);
                        intent.putExtra("sellerID", sellerID);
                        intent.putExtra("sellerName", saln);
                        intent.putExtra("buyer", buyer);
                        intent.putExtra("buyerName", buyerName);
                        startActivity(intent);
                    }
                }
            }
        });

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_best.setLayoutManager(linearLayoutManager);

        arrayList= new ArrayList<>();

        postAdapter = new PostAdapter2(this, arrayList);
        rv_best.setAdapter(postAdapter);







        btn_myhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), MyHomeActivity.class);
                //String a = tv_name.getText().toString();
                //String b = tv_uid.getText().toString();
                //intent.putExtra("name", a);
                //intent.putExtra("Uid", b);
                startActivity(intent);
            }
        });




        //카테고리별 연결

        et_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("category", "전체");
                startActivity(intent);
            }
        });

        btn_fashion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("category", "의류/잡화");
                startActivity(intent);
            }
        });

        btn_kids.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("category", "완구/취미");
                startActivity(intent);
            }
        });

        btn_sports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("category", "스포츠/레져");
                startActivity(intent);
            }
        });

        btn_pet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("category", "반려동물용품");
                startActivity(intent);
            }
        });

        btn_homes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("category", "홈/인테리어");
                startActivity(intent);
            }
        });

        btn_life.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("category", "생활용품");
                startActivity(intent);
            }
        });

        btn_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("category", "자동차용품");
                startActivity(intent);
            }
        });

        btn_digital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("category", "디지털/가전");
                startActivity(intent);
            }
        });

        btn_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("category", "도서/음반/DVD");
                startActivity(intent);
            }
        });

        btn_office.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("category", "문구/오피스");
                startActivity(intent);
            }
        });

        btn_jewelry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("category", "악세사리");
                startActivity(intent);
            }
        });

        btn_health.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("category", "헬스");
                startActivity(intent);
            }
        });

        db.collection("posts").whereEqualTo("yorn", "거래가능").whereGreaterThanOrEqualTo("booked", 1).orderBy("booked", Query.Direction.DESCENDING).limit(10).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                    PostInfo postInfo;
                    postInfo = new PostInfo(documentSnapshot.toObject(PostInfo.class), documentSnapshot.getId());
                    arrayList.add(postInfo);
                }
                postAdapter.notifyDataSetChanged();
            }
        });




    }

    public void setImageSrc(final String imgurl) {
        //ImageView url 설정
        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {

                    URL url = new URL(imgurl);

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();

                    InputStream is = conn.getInputStream();
                    myprofile = BitmapFactory.decodeStream(is);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        mThread.start();
/*
        try {
            mThread.join();
            imageView.setImageBitmap(myprofile);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

 */

    }
}

