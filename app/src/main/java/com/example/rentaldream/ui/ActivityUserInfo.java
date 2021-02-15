package com.example.rentaldream.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
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
import com.example.rentaldream.adapter.RateAdapter;
import com.example.rentaldream.model.RateInfo;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import static java.lang.Float.parseFloat;

public class ActivityUserInfo extends AppCompatActivity {

    Bitmap bitmap;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    SimpleDraweeView iv_profile;
    TextView tv_name, tv_tel, tv_sell, tv_done, tv_postlist, tv_tdone, tv_norate, tv_countrate, tv_rate, tv_countrate2;
    RatingBar ratingBar, ratingBar2;
    Button btn_sagi;
    RecyclerView rv_postlist, rv_done, rv_rate;
    ProgressBar pb1, pb2, pb3, pb4, pb5;

    int p1, p2, p3, p4,p5 =0;

    private PostAdapter2 postAdapter1;
    private ArrayList<PostInfo> arrayList_postlist;
    private LinearLayoutManager linearLayoutManager1;

    private PostAdapter2 postAdapter2;
    private ArrayList<PostInfo> arrayList_done;
    private LinearLayoutManager linearLayoutManager2;

    private RateAdapter rateAdapter;
    private ArrayList<RateInfo> arrayList_rate;
    private LinearLayoutManager linearLayoutManager3;

    String tel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.sell_information);


        final String target  = getIntent().getStringExtra("target");

        iv_profile = (SimpleDraweeView)findViewById(R.id.iv_product);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_tel = (TextView)findViewById(R.id.tv_tel);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);
        ratingBar2 = (RatingBar)findViewById(R.id.ratingbar2);
        tv_sell = (TextView)findViewById(R.id.tv_send);
        tv_done = (TextView)findViewById(R.id.tv_done);
        rv_postlist = (RecyclerView)findViewById(R.id.rv_postlist);
        rv_done = (RecyclerView)findViewById(R.id.rv_done);
        tv_postlist = (TextView)findViewById(R.id.tv_postlist);
        tv_tdone = (TextView)findViewById(R.id.tv_tdone);
        tv_norate = (TextView)findViewById(R.id.tv_norate);
        tv_countrate = (TextView)findViewById(R.id.tv_countrate);
        rv_rate = (RecyclerView)findViewById(R.id.rv_rate);
        tv_rate = (TextView)findViewById(R.id.tv_rate);
        tv_countrate2 = (TextView)findViewById(R.id.tv_countrate2);
        btn_sagi = (Button) findViewById(R.id.btn_sagi);
        pb1 = (ProgressBar)findViewById(R.id.pb1);
        pb2 = (ProgressBar)findViewById(R.id.pb2);
        pb3 = (ProgressBar)findViewById(R.id.pb3);
        pb4 = (ProgressBar)findViewById(R.id.pb4);
        pb5 = (ProgressBar)findViewById(R.id.pb5);



        iv_profile.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT >= 21) {
            iv_profile.setClipToOutline(true);
        }

        //게시글 목록
        linearLayoutManager1 = new LinearLayoutManager(this);
        linearLayoutManager1.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_postlist.setLayoutManager(linearLayoutManager1);

        arrayList_postlist= new ArrayList<>();

        postAdapter1 = new PostAdapter2(this, arrayList_postlist);
        rv_postlist.setAdapter(postAdapter1);

        //대여 종료 목록
        linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_done.setLayoutManager(linearLayoutManager2);

        arrayList_done= new ArrayList<>();

        postAdapter2 = new PostAdapter2(this, arrayList_done);
        rv_done.setAdapter(postAdapter2);

        //리뷰 목록
        linearLayoutManager3 = new LinearLayoutManager(this);
        linearLayoutManager3.setOrientation(LinearLayoutManager.VERTICAL);
        rv_rate.setLayoutManager(linearLayoutManager3);

        arrayList_rate= new ArrayList<>();

        rateAdapter = new RateAdapter(arrayList_rate, this);
        rv_rate.setAdapter(rateAdapter);

        tv_norate.setVisibility(View.GONE);
        tv_postlist.setVisibility(View.GONE);
        tv_tdone.setVisibility(View.GONE);


        db.collection("users").document(target).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                MemberInfo memberInfo = documentSnapshot.toObject(MemberInfo.class);
                tv_name.setText(memberInfo.getName());
                tel = memberInfo.getphoneNum();
                tv_tel.setText(tel);
                //setImageSrc(iv_profile, memberInfo.getImageURL());
                Uri uri = Uri.parse(memberInfo.getImageURL());
                iv_profile.setImageURI(uri);

            }
        });

        db.collection("rate").whereEqualTo("target",target).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                long i=0;
                long rates = 0;
                int b=0;
                if(!task.getResult().isEmpty()){
                    for(QueryDocumentSnapshot document :task.getResult()){
                        rates += document.getLong("rate");
                        i++;

                        RateInfo rateInfo = new RateInfo(document.toObject(RateInfo.class));
                        arrayList_rate.add(rateInfo);

                        switch (document.getLong("rate").intValue()){
                            case 1:{
                                p1+=1;
                                break;
                            }
                            case 2:{
                                p2+=1;
                                break;
                            }
                            case 3:{
                                p3+=1;
                                break;
                            }
                            case 4:{
                                p4+=1;
                                break;
                            }
                            case 5:{
                                p5+=1;
                                break;
                            }
                        }
                    }
                    rateAdapter.notifyDataSetChanged();
                    if(i==0){
                        i=1;
                    }
                    float a = (float)rates;

                    //Log.i("ratenum =>", String.valueOf(a/i));


                    출처: https://altongmon.tistory.com/794 [IOS를 Java]
                    ratingBar.setRating(a/i);
                    ratingBar2.setRating(a/i);
                    tv_countrate.setText(parseFloat(String.format(Locale.KOREAN, "%.1f", a/i))+"/"+i+"건");
                    tv_rate.setText(String.valueOf(parseFloat(String.format(Locale.KOREAN, "%.1f", a/i))));
                    tv_countrate2.setText("총 "+i+"건");

                    int maximum = p1+p2+p3+p4+p5;
                    if(maximum==0){
                        maximum=1;
                    }

                    pb1.setMax(maximum);
                    pb2.setMax(maximum);
                    pb3.setMax(maximum);
                    pb4.setMax(maximum);
                    pb5.setMax(maximum);

                    pb1.setProgress(p1);
                    pb2.setProgress(p2);
                    pb3.setProgress(p3);
                    pb4.setProgress(p4);
                    pb5.setProgress(p5);
                }else {
                    tv_norate.setVisibility(View.VISIBLE);
                    tv_countrate.setText("0.0/0건");
                    tv_countrate2.setText("총 0건");
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"거래 내역이 없습니다.",Toast.LENGTH_SHORT).show();
            }
        });


        db.collection("posts").whereEqualTo("postuserID", target).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    int i=0;
                    if((!task.getResult().isEmpty())&&(task.getResult()!=null)){
                        for(QueryDocumentSnapshot document : task.getResult()){
                            PostInfo postInfo = new PostInfo(document.toObject(PostInfo.class), document.getId().toString());
                            arrayList_postlist.add(postInfo);
                            i++;
                        }
                    }else {
                        tv_postlist.setVisibility(View.VISIBLE);
                    }
                    postAdapter1.notifyDataSetChanged();
                    tv_sell.setText(i+"건");
                }
            }
        });
        btn_sagi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://cyber.go.kr/callView.do?accessType=n&fieldType=H&keyword="+tel));
                //웹페이지 들어가기
                startActivity(intent);
            }
        });


                db.collection("contract").whereEqualTo("buyer", target).whereEqualTo("state",6).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if((!task.getResult().isEmpty())&&(task.getResult()!=null)){
                            int i=0;
                            for(QueryDocumentSnapshot documen : task.getResult()){
                                i++;
                                Log.i("종료물품",documen.get("postID").toString());
                                db.collection("posts").document(documen.get("postID").toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot documentSnapshot = task.getResult();
                                        PostInfo postInfo2 = new PostInfo(documentSnapshot.toObject(PostInfo.class), documentSnapshot.getId().toString());
                                        arrayList_done.add(postInfo2);
                                        postAdapter2.notifyDataSetChanged();
                                    }
                                });
                            }
                            tv_done.setText(i+"건");
                        }else {
                            tv_tdone.setVisibility(View.VISIBLE);
                        }
                    }
                });
            }


    public void setImageSrc(ImageView imageView, final String imgurl) {
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
                    bitmap = BitmapFactory.decodeStream(is);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        mThread.start();

        try {
            mThread.join();
            imageView.setImageBitmap(bitmap);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
