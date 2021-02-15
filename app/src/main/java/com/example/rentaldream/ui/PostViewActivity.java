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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.rentaldream.adapter.CommentAdapter;
import com.example.rentaldream.model.CommentInfo;
import com.example.rentaldream.model.MemberInfo;
import com.example.rentaldream.model.PostInfo;
import com.example.rentaldream.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.rentaldream.ui.FirstShowActivity.myImgURL;
import static com.example.rentaldream.ui.FirstShowActivity.myName;
import static com.example.rentaldream.ui.FirstShowActivity.myUid;
import static com.example.rentaldream.ui.FirstShowActivity.myprofile;

public class PostViewActivity extends AppCompatActivity {

    private RequestQueue mRquestqueue;
    private String URL = "https://fcm.googleapis.com/fcm/send\n";

    TextView tv_cat, tv_countbook, tv_countview, tv_name, tv_report;
    TextView et_title, et_cat, et_price1, et_price2, et_contents, et_per, et_product, et_comment, tv_seller, tv_postdate, tv_state, tv_tradeway;


    SimpleDraweeView iv_profile;
    SimpleDraweeView mainphoto;
    Button btn_comment, btn_goback, btn_del, btn_sellerinfo, btn_share;
    FloatingActionButton btn_chat;
    ImageView btn_book;
    RadioGroup rgb;
    RadioButton r1, r2, r3;
    DecimalFormat formatter1 = new DecimalFormat("###,###");
    DecimalFormat formatter2 = new DecimalFormat("###,###");
    List<String> chatlist = new ArrayList<>();
    LinearLayout ll_capture;

    Bitmap bitmap;
    String targetToken;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    private static final String TAG = "PostViewActivity";


    CommentAdapter commentAdapter;
    ArrayList<CommentInfo> arrayList;
    ArrayList<MemberInfo> arrayList2;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager, linearLayoutManager2;


    String userName = "";
    String userID = myUid;
    String postID= "";
    String path="";
    String postuserId="";
    String buyer=myUid;
    String chatroomid=null;
    String postImgURL="";
    String buyerImg=null;
    String sellerImg=null;

    long now;
    Date date;
    boolean booked = false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_postview);



        tv_cat = (TextView)findViewById(R.id.tv_cat);
        et_title = (TextView)findViewById(R.id.et_title);
        et_product = (TextView)findViewById(R.id.et_product);
        et_cat = (TextView)findViewById(R.id.spin_postcom);
        et_price1  = (TextView)findViewById(R.id.et_price1);
        et_price2 = (TextView)findViewById(R.id.et_price2);
        et_contents = (TextView)findViewById(R.id.et_contents);
        et_comment = (EditText)findViewById(R.id.et_comment);
        et_per =  (TextView)findViewById(R.id.et_per);
        mainphoto = (SimpleDraweeView) findViewById(R.id.mainphoto);
        btn_comment = (Button)findViewById(R.id.btn_comment);
        btn_book = (ImageView)findViewById(R.id.btn_book);
        iv_profile = (SimpleDraweeView)findViewById(R.id.iv_product);
        tv_name = (TextView)findViewById(R.id.tv_name);
        rgb = (RadioGroup)findViewById(R.id.rgb);
        r1 = (RadioButton)findViewById(R.id.r1);
        r2 = (RadioButton)findViewById(R.id.r2);
        r3 = (RadioButton)findViewById(R.id.r3);
        tv_report = (TextView)findViewById(R.id.tv_report);
        btn_sellerinfo = (Button)findViewById(R.id.btn_sellerinfo);
        btn_share = (Button)findViewById(R.id.btn_share);

        tv_countbook = (TextView)findViewById(R.id.tv_countbook);
        tv_countview = (TextView)findViewById(R.id.tv_countview);
        tv_seller = (TextView)findViewById(R.id.tv_seller);
        tv_postdate = (TextView)findViewById(R.id.tv_postdate);
        tv_state = (TextView)findViewById(R.id.tv_state);
        tv_tradeway = (TextView)findViewById(R.id.tv_tradeway);
        btn_goback = (Button)findViewById(R.id.btn_goback);
        btn_del = (Button)findViewById(R.id.btn_del);
        btn_chat = (FloatingActionButton)findViewById(R.id.btn_chat);
        ll_capture = (LinearLayout)findViewById(R.id.ll_capture);

        mRquestqueue = Volley.newRequestQueue(this);



        //조회할 게시글 확인
        postID = getIntent().getStringExtra("postID");

        recyclerView = (RecyclerView)findViewById(R.id.rv_comment);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList= new ArrayList<>();

        commentAdapter = new CommentAdapter(this, arrayList);
        recyclerView.setAdapter(commentAdapter);

        tv_name.setText(myName);
        //et_comment.setText(myUid);
/*
        rv_chatlist = (RecyclerView)findViewById(R.id.rv_chatlist);
        linearLayoutManager2 = new LinearLayoutManager(this);
        rv_chatlist.setLayoutManager(linearLayoutManager2);

 */
/*
        final String[] atr = {""};
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("message").child(postID);
        reference.addChildEventListener(new ChildEventListener() {
            String test="";
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                chatlist.add(dataSnapshot.getKey());
                for(int i =0 ; i<chatlist.size() ; i++){
                    test+=chatlist.get(i);
                }
                et_comment.setText(test);
                if(!atr[0].equals(null)){
                    et_comment.setText(test);
                    buyer=atr[0];
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {


                chatlist.add(dataSnapshot.getKey());
                for(int i =0 ; i<chatlist.size() ; i++){
                    test+=chatlist.get(i);
                }
                et_comment.setText(test);
                if(!atr[0].equals(null)){
                    et_comment.setText(test);
                    buyer=atr[0];
                }

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

 */



        btn_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }

        });

        btn_del.setVisibility(View.GONE);
        //조회할 게시글 db 연결
        final DocumentReference getPost = db.collection("posts").document(postID);

        //로그인한 회원정보 받아오기
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DocumentReference docRef = db.collection("users").document(user.getUid());

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

            }
        });

        btn_sellerinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityUserInfo.class);
                intent.putExtra("target", postuserId);
                startActivity(intent);
            }
        });


        //조회할 게시글 정보 표시하기
        getPost.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                final PostInfo postInfo = documentSnapshot.toObject(PostInfo.class);

                db.collection("users").document(postInfo.getPostuserID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        tv_seller.setText(task.getResult().getData().get("name").toString());
                        postuserId =postInfo.getPostuserID();
                        targetToken=task.getResult().getData().get("pushToken").toString();
                        if(task.getResult().getData().get("imageURL")!=null){
                            sellerImg = task.getResult().getData().get("imageURL").toString();
                        }
                        if(postuserId.equals(myUid)){
                            btn_del.setVisibility(View.VISIBLE);
                            //btn_chat.setVisibility(View.INVISIBLE);
                            btn_chat.hide();
                            btn_chat.setClickable(false);
                        }

                        db.collection("report").whereEqualTo("targetUid", postuserId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.getResult()!=null&&!task.getResult().isEmpty()){
                                    int i =0;
                                    for(QueryDocumentSnapshot doc : task.getResult()){
                                        i++;
                                    }
                                    tv_report.setText("신고 "+ String.valueOf(i)+ "건");
                                }
                            }
                        });

                    }
                });



                tv_postdate.setText(postInfo.getPostDate());
                tv_state.setText(postInfo.getYorn());
                if(postInfo.getYorn().equals("거래종료")){
                    tv_state.setBackgroundResource(R.drawable.state3);
                }else if(postInfo.getYorn().equals("대여중")){
                    tv_state.setBackgroundResource(R.drawable.state4);
                }
                et_title.setText(postInfo.getTitle());
                et_product.setText(postInfo.getProduct());
                et_cat.setText(postInfo.getCategory());
                et_price1.setText(formatter1.format(postInfo.getPrice1())+"원");
                et_price2.setText(formatter2.format(postInfo.getPrice2())+"원");
                et_contents.setText(postInfo.getContents());
                et_per.setText(postInfo.getPeriod());
                tv_tradeway.setText(postInfo.getTradeWay());
                tv_countbook.setText(String.valueOf(postInfo.getBooked()));
                switch (postInfo.getExtension()){
                    case 1:{
                        r1.setChecked(true);
                    }
                    break;
                    case 2:{
                        r2.setChecked(true);
                    }
                    break;
                    case 3:{
                        r3.setChecked(true);
                    }
                    default:
                        r3.setChecked(true);
                        break;
                }

                if(postInfo.getPostuserID().equals(user.getUid())){
                    btn_del.setVisibility(View.VISIBLE);
                }

                if(postInfo.getImageURL()!=null){
                    //setImageSrc(mainphoto, postInfo.getImageURL());
                    Uri uri = Uri.parse(postInfo.getImageURL());
                    mainphoto.setImageURI(uri);
                    postImgURL = postInfo.getImageURL();
                }

                db.collection("users").document(postInfo.getPostuserID()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    }
                });











                //조회수 증가
                final int countView = postInfo.getCountView()+1;

                db.collection("posts").document(postID).update("countView", countView).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        tv_countview.setText(String.valueOf(countView));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_SHORT).show();
                    }
                });


                checkbooked();

                btn_book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btn_book.setEnabled(false);
                        if(booked){
                            db.collection("booked").document(path).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    final int countbook = Integer.parseInt(tv_countbook.getText().toString())-1;
                                    db.collection("posts").document(postID).update("booked", countbook).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            tv_countbook.setText(String.valueOf(countbook));
                                            btn_book.setEnabled(true);
                                        }
                                    });

                                    checkbooked();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("찜 취소 실패1", "찜 취소 실패1");
                                }
                            });
                        }else {
                            Map<String, Object> books = new HashMap<>();
                            books.put("postID", postID);
                            books.put("userID", userID);
                            db.collection("booked").document().set(books).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    final int countbook = Integer.parseInt(tv_countbook.getText().toString())+1;
                                    db.collection("posts").document(postID).update("booked", countbook).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            tv_countbook.setText(String.valueOf(countbook));
                                            btn_book.setEnabled(true);
                                        }
                                    });
                                    checkbooked();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("찜 추가 실패1", "찜 추가 실패1");
                                }
                            });
                        }

                    }

                });

                btn_goback.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });






/*
                btn_book.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(booked==true){
                            final String[] path = {""};
                            db.collection("users").document(userID).collection("booked").whereEqualTo("postID", postID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for(QueryDocumentSnapshot document : task.getResult()){
                                        path[0] = document.getId();
                                    }
                                    db.collection("users").document().collection("booked").document(path[0]).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            final int countbook = Integer.parseInt(tv_countbook.getText().toString())-1;
                                            db.collection("posts").document(postID).update("booked", countbook).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    tv_countbook.setText(String.valueOf(countbook));

                                                }
                                            });
                                        }
                                    });
                                }
                            });
                            checkbooked();
                            //booked=false;
                        }else{
                            Map<String, Object> books = new HashMap<>();
                            books.put("postID", postID);
                            db.collection("users").document(userID).collection("booked").document().set(books).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    final int countbook = Integer.parseInt(tv_countbook.getText().toString())+1;
                                    db.collection("posts").document(postID).update("booked", countbook).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            tv_countbook.setText(String.valueOf(countbook));
                                        }
                                    });
                                }
                            });
                            checkbooked();
                            //booked=true;
                        }
                    }
                });
                */
            }
        });
        loadcomment();


        //초기 레이아웃 감추기
        /*
        for(int i=1 ;i<8 ;i++){
            photo[i].setVisibility(View.GONE);
        }

         */
        //
        btn_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_state.getText().toString().equals("거래가능")){
                    db.collection("posts").document(postID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(getApplicationContext(),"게시글이 삭제되었습니다.",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(),"거래중인 게시글은 삭제할 수 없습니다.",Toast.LENGTH_SHORT).show();
                }


            }
        });

        iv_profile.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT >= 21) {
            iv_profile.setClipToOutline(true);
        }

        if(myprofile!=null){
            //iv_profile.setImageBitmap(myprofile);
            Uri uri = Uri.parse(myImgURL);
            iv_profile.setImageURI(uri);
        }



        //댓글 DB에 저장하기
        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String contents = et_comment.getText().toString();
                if(contents.equals("")){
                    Toast.makeText(getApplicationContext(),"댓글 내용을 입력해주세요.",Toast.LENGTH_SHORT).show();
                }else {
                    now = System.currentTimeMillis();
                    date = new Date(now);
                    SimpleDateFormat postDates = new SimpleDateFormat( "yyyy.MM.dd  HH:mm:ss");
                    String postDate = postDates.format (date);

                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    //게시글정보, 로그인한 계정과 이름....
                    CommentInfo commentInfo = new CommentInfo(postID, myUid, myName, myImgURL, contents, postDate);

                    db.collection("comments").document().set(commentInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            loadcomment();
                            et_comment.setText("");
                            sendNotification(contents, targetToken);
                            //sendGcm(targetToken, "상품에 댓글이 달렸습니다", contents);

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"fail",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });





        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    final Map map = new HashMap();
                    map.put("postID", postID);
                    map.put("title", et_title.getText());
                    map.put("seller", postuserId);
                    map.put("buyer", myUid);
                    map.put("seller_name",tv_seller.getText().toString());
                    map.put("buyer_name",myName);
                    map.put("postID", postID);
                    map.put("postImg", postImgURL);
                    map.put("checks", 0);
                    map.put("checkb", 0);
                    map.put("sellerImg",sellerImg);
                    map.put("buyerImg",myImgURL);
                    map.put("state" , "0");

                    //해당 게시물에 문의했던 적이 없으면 채팅방 만들기
                    db.collection("chat").whereEqualTo("postID", postID).whereEqualTo("buyer", myUid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                            for(QueryDocumentSnapshot querySnapshot : task.getResult()){
                                if(!querySnapshot.getData().isEmpty()){
                                    chatroomid = querySnapshot.getId();
                                }
                            }

                            if(chatroomid==null){
                                db.collection("chat").document().set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        db.collection("chat").whereEqualTo("postID", postID).whereEqualTo("buyer", myUid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                                                    chatroomid = queryDocumentSnapshot.getId();
                                                }
                                                Intent intent = new Intent(getApplicationContext(), ActivityChat.class);
                                                intent.putExtra("postID", postID);
                                                intent.putExtra("sellerID", postuserId);
                                                intent.putExtra("sellerName", tv_seller.getText().toString());
                                                intent.putExtra("buyer", buyer);
                                                intent.putExtra("into", "post");
                                                intent.putExtra("chatroomid", chatroomid);
                                                startActivity(intent);
                                            }
                                        });
                                    }
                                });
                            }else {

                                Intent intent = new Intent(getApplicationContext(), ActivityChat.class);
                                intent.putExtra("postID", postID);
                                intent.putExtra("sellerID", postuserId);
                                intent.putExtra("sellerName", tv_seller.getText().toString());
                                intent.putExtra("buyer", buyer);
                                intent.putExtra("into", "post");
                                intent.putExtra("chatroomid", chatroomid);
                                startActivity(intent);
                            }

                        }
                    });


            }
        });
    }




    private void loadcomment(){
        db.collection("comments")
                .whereEqualTo("postUid", postID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            arrayList.clear();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //Log.d(TAG, "findByQuery => "+document.getId() + " => " + document.getData());
                                    CommentInfo commentInfo = new CommentInfo(document.toObject(CommentInfo.class));
                                    arrayList.add(commentInfo);
                                    commentAdapter.notifyDataSetChanged();
                                }
                        } else {
                            Toast.makeText(getApplicationContext(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                        //Toast.makeText(getApplicationContext(), String.valueOf(commentAdapter.getItemCount()), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void checkbooked(){
        btn_book.setEnabled(false);
        db.collection("booked").whereEqualTo("postID", postID).whereEqualTo("userID",userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(!task.getResult().isEmpty() ){
                        for(QueryDocumentSnapshot document : task.getResult()){
                            btn_book.setImageResource(R.drawable.ic_book);
                            path = document.getId();
                            booked=true;
                            btn_book.setEnabled(true);

                        }
                    }else {
                        btn_book.setImageResource(R.drawable.ic_booked);
                        booked=false;
                        btn_book.setEnabled(true);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

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


    void sendNotification(String text, String targetToken){

        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to",targetToken);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", "상품에 댓글이 등록되었습니다.");
            notificationObj.put("body", "'"+et_product.getText().toString()+"' "+text);
            JSONObject extraData = new JSONObject();
            extraData.put("data", postID);
            extraData.put("gogo", "comment");
            mainObj.put("notification", notificationObj);
            mainObj.put("data", extraData);

            JsonObjectRequest request = new JsonObjectRequest(com.android.volley.Request.Method.POST, URL, mainObj, new com.android.volley.Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new com.android.volley.Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=AAAArS4dvyk:APA91bElETDmLiNMtqcyAVPTnU8o-_DMtMRGzd298vpL9QWO8Kfu1nqbJsCYS0H3Q80Q8s7-G8oavVQptdTu_ojzI0tsKo4Z-iXRtidFmBEGammCcaKrOr6GBz-XWJPtLsKIs86OMbAX");
                    return header;
                }
            };

            mRquestqueue.add(request);
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

/*
    void sendGcm(String token, String title, String content) {
        final Gson gson = new Gson();

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.notification.title =  title;
        notificationModel.notification.body = content;
        notificationModel.data.title = title;
        notificationModel.data.body = content;
        notificationModel.to = token;

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf8"), gson.toJson(notificationModel));

        Request request = new Request.Builder()
                .header("Content-Type", "application/json")
                .addHeader("Authorization", "key=743803043625")
                .url("https://fcm.googleapis.com/fcm/send")
                .post(requestBody)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@org.jetbrains.annotations.NotNull okhttp3.Call call, @org.jetbrains.annotations.NotNull okhttp3.Response response) throws IOException {
                Log.i("push_error","success");

            }

            @Override
            public void onFailure(@org.jetbrains.annotations.NotNull okhttp3.Call call, @org.jetbrains.annotations.NotNull IOException e) {
                Log.i("push_error",e.getMessage());
            }

        });
    }
*/
}


