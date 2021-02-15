package com.example.rentaldream.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.rentaldream.adapter.ChatAdapter;
import com.example.rentaldream.model.ChatInfo;
import com.example.rentaldream.model.ChatListInfo;
import com.example.rentaldream.model.MemberInfo;
import com.example.rentaldream.model.PostInfo;
import com.example.rentaldream.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


import static com.example.rentaldream.ui.FirstShowActivity.myImgURL;
import static com.example.rentaldream.ui.FirstShowActivity.myName;
import static com.example.rentaldream.ui.FirstShowActivity.myUid;

public class ActivityTalk extends AppCompatActivity {

    private RequestQueue mRquestqueue;
    private String URL = "https://fcm.googleapis.com/fcm/send\n";

    Bitmap bitmap;

    Button btn_agree, btn_deny, btn_request, btn_report, btn_gocontract;
    ImageButton btn_send;
    EditText et_text;
    TextView tv_name, tv_buyer1, tv_buyer2, tv_title, tv_title2, tv_price1, tv_price2, tv_price11, tv_price22, tv_period, tv_period2;
    SimpleDraweeView iv_profile, iv_buyer1, iv_buyer2, iv_post, iv_post2 ;
    LinearLayout ll1, ll2;

    String postID=null;
    String sellerID=null;
    String sName=null;
    String buyer=null;
    String buyerName=null;
    String chatingroomid=null;
    String product;
    String pushToken;
    String targetImg;

    long price1 =0;
    long price2 =0;
    long extension =0;
    PostInfo postInfo;


    private DatabaseReference myRef;

    private ChatAdapter chatAdapter;
    private ArrayList<ChatInfo> arrayList;
    private RecyclerView rv_chat;
    private LinearLayoutManager linearLayoutManager;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    ListenerRegistration registration;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Fresco.initialize(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_talk);



        Intent intents = getIntent();

        postID = intents.getExtras().getString("postID");
        chatingroomid = intents.getExtras().getString("cId");
        sellerID = intents.getExtras().getString("sellerID");
        sName = intents.getExtras().getString("saln");
        if(sName==null){
            ChatListInfo chatListInfo = (ChatListInfo)intents.getSerializableExtra("test");
            sName = chatListInfo.getSeller_name();
        }
        Log.i("sName =>", sName+"");
        buyer = intents.getExtras().getString("buyer");
        buyerName = intents.getExtras().getString("bName");
        //String title = intents.getStringExtra("title");

        db.collection("chat").document(chatingroomid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                sellerID = documentSnapshot.get("seller").toString();
                buyer = documentSnapshot.get("buyer").toString();
            }
        });

        //테스트용
        mRquestqueue = Volley.newRequestQueue(this);



        btn_send = (ImageButton) findViewById(R.id.btn_send);
        btn_request = (Button) findViewById(R.id.btn_request);
        btn_agree = (Button) findViewById(R.id.btn_agree);
        btn_deny = (Button) findViewById(R.id.btn_deny);
        et_text = (EditText) findViewById(R.id.et_text);
        tv_name = (TextView) findViewById(R.id.tv_name);
        iv_profile = (SimpleDraweeView) findViewById(R.id.iv_product);
        btn_report = (Button)findViewById(R.id.btn_report);
        btn_gocontract = (Button)findViewById(R.id.btn_gocontract);

        tv_buyer1 = (TextView) findViewById(R.id.tv_buyer1);
        tv_buyer2 = (TextView) findViewById(R.id.tv_buyer2);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title2 = (TextView) findViewById(R.id.tv_title2);
        tv_price1 = (TextView) findViewById(R.id.tv_price2);
        tv_price2 = (TextView) findViewById(R.id.tv_price1);
        tv_price11 = (TextView) findViewById(R.id.tv_price22);
        tv_price22 = (TextView) findViewById(R.id.tv_price11);
        tv_period = (TextView) findViewById(R.id.tv_period);
        tv_period2 = (TextView) findViewById(R.id.tv_period2);


        iv_buyer1 = (SimpleDraweeView)findViewById(R.id.iv_buyer1);
        iv_buyer2 = (SimpleDraweeView)findViewById(R.id.iv_buyer2);
        iv_post = (SimpleDraweeView)findViewById(R.id.iv_post);
        iv_post2 = (SimpleDraweeView)findViewById(R.id.iv_post2);
        ll1 = (LinearLayout)findViewById(R.id.ll1);
        ll2 = (LinearLayout)findViewById(R.id.ll2);

//        ll1.setVisibility(View.GONE);
  //      ll2.setVisibility(View.GONE);






        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        myUid = user.getUid();
        final DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                MemberInfo memberInfo = documentSnapshot.toObject(MemberInfo.class);
                myName = memberInfo.getName();
            }
        });

        db.collection("users").document(sellerID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                pushToken = documentSnapshot.get("pushToken").toString();
            }
        });


        if (myImgURL != null) {
            //iv_profile.setImageBitmap(myprofile);
            Uri uri = Uri.parse(myImgURL);
            iv_profile.setImageURI(uri);
        }
        tv_name.setText(myName);



        ll1.setVisibility(View.GONE);
        ll2.setVisibility(View.GONE);

        db.collection("chat").document(chatingroomid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if(myUid.equals(sellerID)){
                    targetImg = documentSnapshot.get("buyerImg").toString();
                }else {
                    targetImg = documentSnapshot.get("sellerImg").toString();
                }
                db.collection("posts").document(postID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        postInfo = documentSnapshot.toObject(PostInfo.class);
                        DecimalFormat formatter = new DecimalFormat("###,###");

                        if(sellerID.equals(myUid)){
                            ll1.setVisibility(View.GONE);
                            ll2.setVisibility(View.VISIBLE);
                            Uri uri2 = Uri.parse(targetImg);
                            iv_buyer2.setImageURI(uri2);
                            tv_buyer2.setText(buyerName);
                            tv_title2.setText(postInfo.getTitle());
                            Uri uri = Uri.parse(postInfo.getImageURL());
                            iv_post2.setImageURI(uri);
                            //setImageSrc(iv_post2, postInfo.getImageURL());
                            iv_buyer2.setBackground(new ShapeDrawable(new OvalShape()));
                            if(Build.VERSION.SDK_INT >= 21) {
                                iv_buyer2.setClipToOutline(true);
                            }

                            tv_price11.setText(String.valueOf(formatter.format(postInfo.getPrice1()))+"원");
                            tv_price22.setText(String.valueOf(formatter.format(postInfo.getPrice2()))+"원");
                            tv_period2.setText(postInfo.getPeriod());
                            //tv_title.setText(documentSnapshot.getData().get("title").toString());
                        }else {
                            ll1.setVisibility(View.VISIBLE);
                            ll2.setVisibility(View.GONE);
                            //iv_buyer1.setImageBitmap(myprofile);
                            Uri uri = Uri.parse(myImgURL);
                            iv_buyer1.setImageURI(uri);
                            tv_buyer1.setText(myName);
                            //setImageSrc(iv_post, postInfo.getImageURL());
                            Uri uri2 = Uri.parse(postInfo.getImageURL());
                            iv_post.setImageURI(uri2);
                            iv_buyer1.setBackground(new ShapeDrawable(new OvalShape()));
                            if(Build.VERSION.SDK_INT >= 21) {
                                iv_buyer1.setClipToOutline(true);
                            }
                            tv_title.setText(postInfo.getTitle());
                            tv_price1.setText(String.valueOf(formatter.format(postInfo.getPrice1()))+"원");
                            tv_price2.setText(String.valueOf(formatter.format(postInfo.getPrice2()))+"원");
                            tv_period.setText(postInfo.getPeriod());
                            //tv_title2.setText(documentSnapshot.getData().get("title").toString());
                        }

                        price1 = postInfo.getPrice1();
                        price2 = postInfo.getPrice2();
                        product = postInfo.getProduct();
                        postInfo.getExtension();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        });



        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PostViewActivity.class);
                intent.putExtra("postID", postID);
                startActivity(intent);
            }
        });
        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PostViewActivity.class);
                intent.putExtra("postID", postID);
                startActivity(intent);
            }
        });



                        //채팅 뷰
                        rv_chat = (RecyclerView) findViewById(R.id.rv_chat);
        linearLayoutManager = new LinearLayoutManager(this);
        rv_chat.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();


        chatAdapter = new ChatAdapter(this, arrayList);
        chatAdapter.setHasStableIds(true);
        rv_chat.setAdapter(chatAdapter);


        btn_request.setVisibility(View.INVISIBLE);
        btn_agree.setVisibility(View.INVISIBLE);
        btn_deny.setVisibility(View.INVISIBLE);
        btn_gocontract.setVisibility(View.INVISIBLE);

        final DocumentReference erer = db.collection("chat").document(chatingroomid);



        btn_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("posts").document(postID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if(documentSnapshot.get("yorn").toString().equals("거래가능")){
                            erer.update("state", "request").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Toast.makeText(getApplicationContext(), "거래 요청을 보냈습니다.", Toast.LENGTH_SHORT).show();
                                }
                            });
                            sendNotification(pushToken);
                        }else if(documentSnapshot.get("yorn").toString().equals("대여중")){
                            Toast.makeText(getApplicationContext(), "해당 상품은 타인이 이미 대여중인 상품입니다.",Toast.LENGTH_SHORT).show();
                        }else if(documentSnapshot.get("yorn").toString().equals("거래종료")){
                            Toast.makeText(getApplicationContext(), "해당 상품은 거래종료된 상품입니다.",Toast.LENGTH_SHORT).show();
                        }else if(documentSnapshot.get("yorn").toString().equals("거래중")){
                            Toast.makeText(getApplicationContext(), "해당 상품은 이미 타인과 거래중인 상품입니다.",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "해당 상품의 상태를 확인할 수 없습니다.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        btn_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("posts").document(postID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if(documentSnapshot.get("yorn").toString().equals("거래가능")){
                            erer.update("state", "contract").addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Map map = new HashMap();
                                    map.put("postID",postID);
                                    map.put("sellerID",sellerID);
                                    map.put("sellername",sName);
                                    map.put("buyer",buyer);
                                    map.put("buyername",buyerName);
                                    map.put("state",1);
                                    map.put("postcom","");
                                    map.put("postbox","");
                                    map.put("startDate","");
                                    map.put("returnDate","");
                                    map.put("price1",price1);
                                    map.put("price2",price2);
                                    map.put("product",product);
                                    map.put("checks",0);
                                    map.put("checkb",0);
                                    map.put("chatroomid",chatingroomid);
                                    map.put("extension",extension);
                                    map.put("imageURL", postInfo.getImageURL());
                                    map.put("returntype", "반납유형선택");
                                    map.put("sbankcom","");
                                    map.put("sbanknum", "");
                                    map.put("bbankcom","");
                                    map.put("bbanknum", "");

                                    db.collection("contract").document().set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
//                                Toast.makeText(getApplicationContext(),"테스트중",Toast.LENGTH_SHORT).show();
                                            btn_agree.setVisibility(View.INVISIBLE);
                                            btn_deny.setVisibility(View.INVISIBLE);
                                        }
                                    });
                                }
                            });
                        }else if(documentSnapshot.get("yorn").toString().equals("대여중")){
                            Toast.makeText(getApplicationContext(), "해당 상품은 타인이 이미 대여중인 상품입니다.",Toast.LENGTH_SHORT).show();
                        }else if(documentSnapshot.get("yorn").toString().equals("거래종료")){
                            Toast.makeText(getApplicationContext(), "해당 상품은 거래종료된 상품입니다.",Toast.LENGTH_SHORT).show();
                        }else if(documentSnapshot.get("yorn").toString().equals("거래중")){
                            Toast.makeText(getApplicationContext(), "해당 상품은 이미 타인과 거래중인 상품입니다.",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "해당 상품의 상태를 확인할 수 없습니다.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });

        btn_deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                erer.update("state", "deny").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        btn_agree.setVisibility(View.INVISIBLE);
                        btn_deny.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

        btn_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tUid="";
                String tName="";
                if(sellerID.equals(myUid)){
                    tUid=buyer;
                    tName=buyerName;
                }else {
                    tUid=sellerID;
                    tName=sName;
                }
                Intent intent = new Intent(getApplicationContext(), ActivityReport.class);
                intent.putExtra("tUid", tUid);
                intent.putExtra("tName", tName);
                startActivity(intent);
            }
        });


        btn_gocontract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityContract.class);
                intent.putExtra("postID", postID);
                intent.putExtra("cId", chatingroomid);
                intent.putExtra("sellerID", sellerID);
                intent.putExtra("sName", sName);
                intent.putExtra("buyer", buyer);
                intent.putExtra("bName", buyerName);
                startActivity(intent);
            }
        });



        if (!myUid.equals(sellerID)) {
            btn_request.setVisibility(View.VISIBLE);
            buyer = myUid;
        }

        iv_profile.setBackground(new ShapeDrawable(new OvalShape()));
        if (Build.VERSION.SDK_INT >= 21) {
            iv_profile.setClipToOutline(true);
        }




        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!et_text.getText().toString().equals(""))) {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference().child("message").child(chatingroomid);

                    String targetuid;
                    String targetname;
                    if(myUid.equals(sellerID)){
                        targetuid=buyer;
                        targetname=buyerName;
                    }else {
                        targetuid=sellerID;
                        targetname=sName;
                    }

                    Map<String, Object> map = new HashMap<>();
                    map.put("postID", postID);
                    map.put("sendUid", myUid);
                    map.put("sendName", myName);
                    map.put("sendImg", myImgURL);
                    map.put("targetUid", targetuid);
                    map.put("targetName", targetname);
                    map.put("targetImg", targetImg);

                    map.put("msg", et_text.getText().toString());
                    long time = System.currentTimeMillis();
                    map.put("timestamp", time);

                    myRef.push().setValue(map);
                    et_text.setText("");
                } else {
                    Toast.makeText(getApplicationContext(), "내용을 입력하세요", Toast.LENGTH_SHORT).show();
                }
                if(myUid.equals(sellerID)) {
                    db.collection("chat").document(chatingroomid).update("checkb", FieldValue.increment(1));
                }else {
                    db.collection("chat").document(chatingroomid).update("checks", FieldValue.increment(1));
                        }


            }
        });


        FirebaseDatabase database = FirebaseDatabase.getInstance();


        //message를 참조(getReference)해서 가져옴.
        myRef = database.getReference();

        if (chatingroomid != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("message").child(chatingroomid);
            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    //새로운 채팅이 업데이트 될때마다 바로 적용해주는 부분
                    chatConversation(dataSnapshot);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                    //새로운 채팅이 업데이트 될때마다 바로 적용해주는 부분
                    //chatConversation(dataSnapshot);
                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }



        registration = erer.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if(sellerID.equals(myUid)){
                        if(snapshot.get("state").toString().equals("request")){
                            Toast.makeText(getApplicationContext(),"상대방이 상품 대여를 신청했습니다.\n수락하시겠습니까?",Toast.LENGTH_SHORT).show();
                            btn_agree.setVisibility(View.VISIBLE);
                            btn_deny.setVisibility(View.VISIBLE);
                        }
                    }else {
                        if(snapshot.get("state").toString().equals("deny")){
                            Toast.makeText(getApplicationContext(),"대여 신청이 거부되었습니다.",Toast.LENGTH_SHORT).show();
                            btn_request.setText("대여 신청");
                            btn_request.setEnabled(true);
                        }else if(snapshot.get("state").toString().equals("request")){
                            Toast.makeText(getApplicationContext(), "거래 요청을 보냈습니다.", Toast.LENGTH_SHORT).show();
                            btn_request.setText("응답 대기중");
                            btn_request.setEnabled(false);
                        }
                    }
                    if(snapshot.get("state").toString().equals("contract")){
                        //registration.remove();
                        btn_gocontract.setVisibility(View.VISIBLE);
                        btn_request.setVisibility(View.INVISIBLE);
                        Toast.makeText(getApplicationContext(),"거래중인 내용을 확인하려면 거래하기 버튼을 클릭하세요.", Toast.LENGTH_SHORT).show();
                        /*
                        Intent intentw = new Intent(getApplicationContext(), ActivityContract.class);
                        intentw.putExtra("postID", postID);
                        intentw.putExtra("cId", chatingroomid);
                        intentw.putExtra("sellerID", sellerID);
                        intentw.putExtra("sName", sName);
                        intentw.putExtra("buyer", buyer);
                        intentw.putExtra("bName", buyerName);


                        startActivity(intentw);

                         */
                    }else if (snapshot.get("state").toString().equals("finish")){
                        btn_gocontract.setVisibility(View.VISIBLE);
                        btn_gocontract.setText("거래내역확인");
                    }
                }
            });






    }

    private void chatConversation(DataSnapshot dataSnapshot) {
        ChatInfo chatInfo = new ChatInfo(dataSnapshot.getValue(ChatInfo.class));
        arrayList.add(chatInfo);
        chatAdapter.notifyDataSetChanged();

        rv_chat.smoothScrollToPosition(chatAdapter.getItemCount());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registration.remove();
        if (sellerID.equals(myUid)){
            db.collection("chat").document(chatingroomid).update("checks",0).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });
        }else{
            db.collection("chat").document(chatingroomid).update("checkb",0).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                }
            });
        }

    }
    /*
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
*/
    void sendNotification(String targetToken){

        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to",targetToken);
            JSONObject notificationObj = new JSONObject();
            notificationObj.put("title", "거래신청 알림");
            notificationObj.put("body", "'"+product+"'"+"상품에 거래신청이 들어왔습니다.");
            JSONObject extraData = new JSONObject();
            extraData.put("data", postID);
            extraData.put("into", "pushAlarm");
            extraData.put("chatroomid", chatingroomid);
            extraData.put("sellerID", sellerID);
            extraData.put("saln", sName);
            extraData.put("buyer", buyer);
            extraData.put("buyerName", buyerName);
            extraData.put("gogo", "request");
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
}
