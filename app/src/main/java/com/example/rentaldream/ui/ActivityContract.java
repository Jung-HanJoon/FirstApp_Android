package com.example.rentaldream.ui;
import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentaldream.R;
import com.example.rentaldream.adapter.TimeStampAdapter;
import com.example.rentaldream.model.TimeStampInfo;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.rentaldream.ui.FirstShowActivity.myImgURL;
import static com.example.rentaldream.ui.FirstShowActivity.myName;
import static com.example.rentaldream.ui.FirstShowActivity.myUid;

public class ActivityContract extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String songjang_Search = "https://m.search.naver.com/search.naver?sm=mtp_hty.top&where=m&query=";

    LinearLayout ll_checkPrice, ll_songJang, ll_person, ll_product, ll_return, ll_rate;
    Button btn_state, btn_contract, btn_checkPrice, btn_checksongjang, btn_goPost, btn_goReturn, btn_return, btn_senDm;
    CalendarView calendarView2;
    ImageButton btn_send;
    SimpleDraweeView iv_seller, iv_buyer, iv_product;
    TextView state1, state2, state3, state4, state5, pickedDate1, pickedDate2, tv_sellerName, tv_sellerTel, tv_buyerName, tv_buyerTel, tv_product, tv_price1, tv_price2, tv_period, tv_postbox, tv_sBank, tv_bBank,tv_warning,tv_warning2;
    EditText et_price1, et_price2, et_songjang, et_text, et_bank;
    Spinner spin_postcom, spinner_return, spin_bank;

    FloatingActionButton btn_chat2;
    RatingBar ratingBar;

    Bitmap bitmap;
    Date date;
    long now;
    int rate;


    //달력
    Calendar testcal = Calendar.getInstance();
    int syear = 0, smonth = 0, sday = 0, ryear = 0, rmonth = 0, rday = 0;

    boolean b_state = true;
    boolean b_contract = true;
    boolean warn = false;

    String postID;
    long state;
    String sellerID;
    String sellername;
    String sellertel;
    String buyer;
    String buyername;
    String buyertel;
    String postcom;
    String postbox;
    String startDate;
    String returnDate;
    String price1;
    String price2;
    String product;
    long checks = 0;
    long checkb = 0;
    String returntype;
    String chatroomid;
    long extension;
    String contract;
    String sellerImg;
    String buyerImg;
    String ImageURL;
    String sbankcom;
    String sbanknum="";
    String bbankcom;
    String bbanknum="";
    long returntimestamp;

    String sagijohe = "https://cyber.go.kr/callView.do?accessType=n&fieldType=A&keyword=";

    private TimeStampAdapter stampAdapter;
    private ArrayList<TimeStampInfo> arrayList;
    private RecyclerView rv_timestamp;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_contract);


        ll_checkPrice = (LinearLayout) findViewById(R.id.ll_checkprice);
        ll_songJang = (LinearLayout) findViewById(R.id.ll_songjang);
        ll_person = (LinearLayout) findViewById(R.id.ll_person);
        ll_product = (LinearLayout) findViewById(R.id.ll_product);
        ll_return = (LinearLayout) findViewById(R.id.ll_return);
        ll_rate = (LinearLayout) findViewById(R.id.ll_rate);

        btn_state = (Button) findViewById(R.id.btn_state);
        btn_contract = (Button) findViewById(R.id.btn_contract);
        btn_goReturn = (Button) findViewById(R.id.btn_goreturn);
        btn_return = (Button) findViewById(R.id.btn_return);
        btn_senDm = (Button)findViewById(R.id.btn_sendm);

        ll_checkPrice.setVisibility(View.GONE);
        ll_songJang.setVisibility(View.GONE);
        ll_return.setVisibility(View.GONE);
        ll_rate.setVisibility(View.GONE);
        btn_return.setVisibility(View.GONE);

        btn_checkPrice = (Button) findViewById(R.id.btn_checkprice);
        btn_checksongjang = (Button) findViewById(R.id.btn_checksongjang);
        btn_goPost = (Button) findViewById(R.id.btn_gopost);
        btn_send = (ImageButton) findViewById(R.id.btn_send);
        iv_seller = (SimpleDraweeView) findViewById(R.id.iv_seller);
        iv_buyer = (SimpleDraweeView) findViewById(R.id.iv_buyer);
        iv_product = (SimpleDraweeView) findViewById(R.id.iv_product);

        state1 = (TextView) findViewById(R.id.state1);
        state2 = (TextView) findViewById(R.id.state2);
        state3 = (TextView) findViewById(R.id.state3);
        state4 = (TextView) findViewById(R.id.state4);
        state5 = (TextView) findViewById(R.id.state5);
        pickedDate1 = (TextView) findViewById(R.id.pickeddate1);
        pickedDate2 = (TextView) findViewById(R.id.pickeddate2);
        tv_sellerName = (TextView) findViewById(R.id.tv_sellername);
        tv_sellerTel = (TextView) findViewById(R.id.tv_sellertel);
        tv_buyerName = (TextView) findViewById(R.id.tv_buyername);
        tv_buyerTel = (TextView) findViewById(R.id.tv_buyertel);
        tv_product = (TextView) findViewById(R.id.tv_product);
        tv_price1 = (TextView) findViewById(R.id.tv_price1);
        tv_price2 = (TextView) findViewById(R.id.tv_price2);
        tv_period = (TextView) findViewById(R.id.tv_period);
        tv_postbox = (TextView) findViewById(R.id.tv_postbox);
        tv_sBank = (TextView) findViewById(R.id.tv_sbank);
        tv_bBank = (TextView) findViewById(R.id.tv_bbank);
        tv_warning = (TextView)findViewById(R.id.tv_warning);
        tv_warning2 = (TextView)findViewById(R.id.tv_warning2);

        et_price1 = (EditText) findViewById(R.id.et_price1);
        et_price2 = (EditText) findViewById(R.id.et_price2);
        et_songjang = (EditText) findViewById(R.id.et_songjang);
        et_text = (EditText) findViewById(R.id.et_text);
        et_bank = (EditText) findViewById(R.id.et_bank);
        spin_postcom = (Spinner) findViewById(R.id.spin_postcom);
        spinner_return = (Spinner) findViewById(R.id.spinner_return);
        spin_bank = (Spinner)findViewById(R.id.spin_bank);
        rv_timestamp = (RecyclerView) findViewById(R.id.rv_timestamp);
        calendarView2 = (CalendarView) findViewById(R.id.calendarView2);
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        final ClipboardManager clipboard = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);


        btn_chat2 = (FloatingActionButton) findViewById(R.id.btn_chat2);

        calendarView2.setVisibility(View.GONE);

        final Intent intent = this.getIntent();

        intent.getStringExtra("postID");
        chatroomid = intent.getStringExtra("cId");
        intent.getStringExtra("sellerID");
        intent.getStringExtra("sName");
        intent.getStringExtra("buyer");
        intent.getStringExtra("bName");

        linearLayoutManager = new LinearLayoutManager(this);
        rv_timestamp.setLayoutManager(linearLayoutManager);

        arrayList = new ArrayList<>();

        stampAdapter = new TimeStampAdapter(this, arrayList);
        rv_timestamp.setAdapter(stampAdapter);

        tv_sBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sbanknum.equals("")){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sagijohe+sbanknum));
                    //웹페이지 들어가기
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(), "등록된 계좌가 없습니다.", Toast.LENGTH_SHORT);
                }

            }
        });

        tv_bBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!bbanknum.equals("")){
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sagijohe+bbanknum));
                    //웹페이지 들어가기
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(), "등록된 계좌가 없습니다.", Toast.LENGTH_SHORT);
                }

            }
        });

        tv_warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(warn){
                    tv_warning2.setVisibility(View.GONE);
                    warn=false;
                }else {
                    tv_warning2.setVisibility(View.VISIBLE);
                    warn=true;
                }
            }
        });



        if (chatroomid != null) {
            db.collection("contract").whereEqualTo("chatroomid", chatroomid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful() && !task.getResult().isEmpty() && (task.getResult() != null)) {

                        for (QueryDocumentSnapshot querySnapshot : task.getResult()) {
                            contract = querySnapshot.getId();
                            state = querySnapshot.getLong("state");
                            postID = querySnapshot.get("postID").toString();
                            sellerID = querySnapshot.get("sellerID").toString();

                            sellername = querySnapshot.get("sellername").toString();
                            buyer = querySnapshot.get("buyer").toString();
                            buyername = querySnapshot.get("buyername").toString();

                            postcom = querySnapshot.get("postcom").toString();
                            postbox = querySnapshot.get("postbox").toString();
                            startDate = querySnapshot.get("startDate").toString();
                            returnDate = querySnapshot.get("returnDate").toString();
                            price1 = querySnapshot.get("price1").toString();
                            price2 = querySnapshot.get("price2").toString();
                            product = querySnapshot.get("product").toString();
                            checks = querySnapshot.getLong("checks");
                            checkb = querySnapshot.getLong("checkb");
                            extension = querySnapshot.getLong("extension");
                            ImageURL = querySnapshot.get("imageURL").toString();
                            returntype = querySnapshot.get("returntype").toString();

                            try {
                                sbankcom = querySnapshot.get("sbankcom").toString();
                                sbanknum  = querySnapshot.get("sbanknum").toString();
                                bbankcom = querySnapshot.get("bbankcom").toString();
                                bbanknum = querySnapshot.get("bbanknum").toString();
                                if(sbankcom.equals("")){
                                    tv_sBank.setText("미 등록");
                                }else {
                                    tv_sBank.setText(sbankcom+" "+sbanknum);
                                }
                                if(bbankcom.equals("")){
                                    tv_bBank.setText("미 등록");
                                }else {
                                    tv_bBank.setText(bbankcom+" "+bbanknum);
                                }

                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            Uri uri = Uri.parse(ImageURL);
                            iv_product.setImageURI(uri);
                            //setImageSrc(iv_product, ImageURL);
                        }


                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        final DatabaseReference myRef = database.getReference().child("timestamp").child(contract);

                        myRef.addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                //새로운 채팅이 업데이트 될때마다 바로 적용해주는 부분
                                refrashstamp(dataSnapshot);
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

                        db.collection("users").document(sellerID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                sellerImg = task.getResult().getData().get("imageURL").toString();
                                Uri uri = Uri.parse(sellerImg);
                                iv_seller.setImageURI(uri);
                                //setImageSrc(iv_seller, sellerImg);
                                tv_sellerTel.setText(task.getResult().getData().get("phoneNum").toString());
                            }
                        });
                        db.collection("users").document(buyer).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                buyerImg = task.getResult().getData().get("imageURL").toString();
                                Uri uri = Uri.parse(buyerImg);
                                iv_buyer.setImageURI(uri);
                                //setImageSrc(iv_buyer, buyerImg);
                                tv_buyerTel.setText(task.getResult().getData().get("phoneNum").toString());
                            }
                        });

                        //iv_profile.setBackground(new ShapeDrawable(new OvalShape()));
                        iv_buyer.setBackground(new ShapeDrawable(new OvalShape()));
                        iv_seller.setBackground(new ShapeDrawable(new OvalShape()));
                        if (Build.VERSION.SDK_INT >= 21) {
                            //iv_profile.setClipToOutline(true);
                            iv_buyer.setClipToOutline(true);
                            iv_seller.setClipToOutline(true);
                        }


                        iv_seller.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent1 = new Intent(getApplicationContext(), ActivityUserInfo.class);
                                intent1.putExtra("target", sellerID);
                                startActivity(intent1);
                            }
                        });
                        iv_buyer.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent1 = new Intent(getApplicationContext(), ActivityUserInfo.class);
                                intent1.putExtra("target", buyer);
                                startActivity(intent1);
                            }
                        });

                        iv_product.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent1 = new Intent(getApplicationContext(), PostViewActivity.class);
                                intent1.putExtra("postID", postID);
                                startActivity(intent1);
                            }
                        });


                        tv_sellerName.setText(sellername);
                        tv_buyerName.setText(buyername);
                        et_price1.setText(price1);
                        et_price2.setText(price2);
                        pickedDate1.setText(startDate);
                        pickedDate2.setText(returnDate);
                        //spin_postcom.setSelection(1);
                        if (!postcom.equals("")) {
                            spin_postcom.setSelection(getIndex(spin_postcom, postcom));
                        }
                        et_songjang.setText(postbox);

                        btn_senDm.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent launchIntent = getPackageManager().getLaunchIntentForPackage("viva.republica.toss");
                                if (launchIntent != null) {
                                    if(sellerID.equals(myUid)){
                                        if(!bbankcom.equals("")&&!bbankcom.equals("현금")&&!bbanknum.equals("은행선택")){
                                            try {
                                                ClipData clip = ClipData.newPlainText("simple text", tv_bBank.getText().toString());
                                                clipboard.setPrimaryClip(clip);
                                                startActivity(launchIntent);//null pointer check in case package name was not found
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }

                                        }else {
                                            Toast.makeText(getApplicationContext(), "상대방의 계좌가 등록되지 않았습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        if(!sbankcom.equals("")&&!sbankcom.equals("현금")&&!sbanknum.equals("은행선택")){
                                            try {
                                                ClipData clip = ClipData.newPlainText("simple text", tv_sBank.getText().toString());
                                                clipboard.setPrimaryClip(clip);
                                                startActivity(launchIntent);//null pointer check in case package name was not found
                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }
                                        }else {
                                            Toast.makeText(getApplicationContext(), "상대방의 계좌가 등록되지 않았습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }

                            }
                        });


                        btn_send.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                rate = (int) ratingBar.getRating();

                                if (et_text.getText().toString().equals("") || rate == 0) {
                                    Toast.makeText(getApplicationContext(), "빠진 항목이 없는지 확인하세요.", Toast.LENGTH_SHORT).show();
                                } else {
                                    String target = "";
                                    String targetname = "";

                                    if (sellerID.equals(myUid)) {
                                        target = buyer;
                                        targetname = buyername;
                                    } else {
                                        target = sellerID;
                                        targetname = sellername;
                                    }

                                    now = System.currentTimeMillis();
                                    date = new Date(now);
                                    SimpleDateFormat postDates = new SimpleDateFormat("yyyy년 MM월 dd일");
                                    String postDate = postDates.format(date);

                                    Map map = new HashMap();

                                    map.put("target", target);
                                    map.put("targetname", targetname);
                                    map.put("poster", myUid);
                                    map.put("postername", myName);
                                    map.put("rate", rate);
                                    map.put("posterImg", myImgURL);
                                    map.put("content", et_text.getText().toString());
                                    map.put("postID", postID);
                                    map.put("date", postDate);


                                    if (sellerID.equals(myUid)) {
                                        db.collection("rate").document().set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                db.collection("contract").document(contract).update("checks", 1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(getApplicationContext(), "거래 후기가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        });
                                    } else {
                                        db.collection("rate").document().set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                db.collection("contract").document(contract).update("checkb", 1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Toast.makeText(getApplicationContext(), "거래 후기가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                            }
                                        });
                                    }

                                }
                            }
                        });


                        final DocumentReference erer = db.collection("contract").document(contract);

                        erer.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                                @Nullable FirebaseFirestoreException e) {

                                state = snapshot.getLong("state");
                                checks = snapshot.getLong("checks");
                                checkb = snapshot.getLong("checkb");
                                returnDate = snapshot.get("returnDate").toString();
                                postbox = snapshot.get("postbox").toString();
                                postcom = snapshot.get("postcom").toString();

                                try{
                                    sbankcom = snapshot.get("sbankcom").toString();
                                    sbanknum  = snapshot.get("sbanknum").toString();
                                    bbankcom = snapshot.get("bbankcom").toString();
                                    bbanknum = snapshot.get("bbanknum").toString();
                                    if(sbankcom.equals("")){
                                        tv_sBank.setText("미 등록");
                                    }else {
                                        tv_sBank.setText(sbankcom+" "+sbanknum);
                                    }
                                    if(bbankcom.equals("")){
                                        tv_bBank.setText("미 등록");
                                    }else {
                                        tv_bBank.setText(bbankcom+" "+bbanknum);
                                    }
                                }catch (Exception e2){
                                    e2.printStackTrace();
                                }

                                switch ((int) state) {
                                    case 1: {
                                        state1.setTextColor(Color.parseColor("#000000"));
                                        state1.setTypeface(null, Typeface.BOLD);
                                        state1.setBackgroundResource(R.drawable.state1);
                                        ll_checkPrice.setVisibility(View.VISIBLE);
                                        if (buyer.equals(myUid) && checks == 1) {
                                            et_price1.setText(snapshot.get("price1").toString());
                                            et_price2.setText(snapshot.get("price2").toString());
                                            pickedDate1.setText(snapshot.get("startDate").toString());
                                            pickedDate2.setText(snapshot.get("returnDate").toString());
                                            tv_price1.setText(et_price1.getText().toString());
                                            tv_price2.setText(et_price2.getText().toString());
                                            tv_period.setText(pickedDate1.getText().toString() + " ~ " + pickedDate2.getText().toString());
                                            Toast.makeText(getApplicationContext(), "거래 정보가 변경되었습니다. 내용이 맞으면 '확인' 버튼을 눌러주세요.", Toast.LENGTH_LONG).show();
                                        }
                                        break;
                                    }
                                    case 2: {
                                        state2.setTextColor(Color.parseColor("#000000"));
                                        state2.setTypeface(null, Typeface.BOLD);
                                        state2.setBackgroundResource(R.drawable.state1);
                                        ll_checkPrice.setVisibility(View.GONE);
                                        ll_songJang.setVisibility(View.VISIBLE);

                                        state1.setTextColor(Color.DKGRAY);
                                        state1.setBackgroundResource(R.drawable.state3);

                                        if (sellerID.equals(myUid)) {
                                            btn_checksongjang.setText("송장등록");
                                            spin_postcom.setEnabled(true);
                                            et_songjang.setEnabled(true);
                                            btn_goPost.setVisibility(View.GONE);
                                        } else {
                                            btn_checksongjang.setText("수취확인");
                                            spin_postcom.setClickable(false);
                                            et_songjang.setEnabled(false);
                                            btn_goPost.setVisibility(View.VISIBLE);
                                        }

                                        if (buyer.equals(myUid) && checks == 1) {
                                            if (!snapshot.get("postcom").toString().equals("")) {
                                                spin_postcom.setSelection(getIndex(spin_postcom, snapshot.get("postcom").toString()));
                                            }
                                            et_songjang.setText(snapshot.get("postbox").toString());
                                            btn_checksongjang.setText("수취확인");
                                            Toast.makeText(getApplicationContext(), "송장이 등록되었습니다.\n물건을 받으시면 '수취확인'을 눌러주세요.", Toast.LENGTH_LONG).show();
                                        }

                                        break;
                                    }
                                    case 3: {
                                        state3.setTextColor(Color.parseColor("#000000"));
                                        state3.setTypeface(null, Typeface.BOLD);
                                        state3.setBackgroundResource(R.drawable.state1);
                                        ll_checkPrice.setVisibility(View.GONE);
                                        ll_songJang.setVisibility(View.GONE);

                                        state1.setTextColor(Color.DKGRAY);
                                        state2.setTextColor(Color.DKGRAY);
                                        state1.setBackgroundResource(R.drawable.state3);
                                        state2.setBackgroundResource(R.drawable.state3);
                                        if (buyer.equals(myUid)) {
                                            btn_return.setVisibility(View.VISIBLE);
                                        }
                                        break;
                                    }
                                    case 4: {
                                        state4.setTextColor(Color.parseColor("#000000"));
                                        state4.setTypeface(null, Typeface.BOLD);
                                        state4.setBackgroundResource(R.drawable.state1);

                                        state1.setTextColor(Color.DKGRAY);
                                        state2.setTextColor(Color.DKGRAY);
                                        state3.setTextColor(Color.DKGRAY);
                                        state1.setBackgroundResource(R.drawable.state3);
                                        state2.setBackgroundResource(R.drawable.state3);
                                        state3.setBackgroundResource(R.drawable.state3);
                                        btn_goPost.setVisibility(View.GONE);

                                        if (checks == 0) {
                                            if (buyer.equals(myUid)) {
                                                ll_return.setVisibility(View.VISIBLE);
                                                btn_return.setVisibility(View.GONE);
                                            } else {
                                                Toast.makeText(getApplicationContext(), "대여자가 반납신청중입니다.", Toast.LENGTH_SHORT).show();
                                            }

                                        } else {
                                            if (sellerID.equals(myUid)) {
                                                Toast.makeText(getApplicationContext(), "대여자가 반납신청을 하였습니다.", Toast.LENGTH_SHORT).show();
                                            }
                                            ll_return.setVisibility(View.VISIBLE);
                                            if (sellerID.equals(myUid)) {
                                                btn_goReturn.setText("반납 수락");
                                            } else {
                                                btn_goReturn.setText("반납 수락대기중");
                                            }
                                        }

                                        spinner_return.setSelection(getIndex(spinner_return, snapshot.get("returntype").toString()));

                                        break;
                                    }
                                    case 5: {
                                        state4.setTextColor(Color.parseColor("#000000"));
                                        state4.setTypeface(null, Typeface.BOLD);
                                        state4.setBackgroundResource(R.drawable.state1);

                                        state1.setTextColor(Color.DKGRAY);
                                        state2.setTextColor(Color.DKGRAY);
                                        state3.setTextColor(Color.DKGRAY);
                                        state1.setBackgroundResource(R.drawable.state3);
                                        state2.setBackgroundResource(R.drawable.state3);
                                        state3.setBackgroundResource(R.drawable.state3);
                                        ll_songJang.setVisibility(View.VISIBLE);
                                        btn_goPost.setVisibility(View.GONE);
                                        btn_goReturn.setVisibility(View.GONE);
                                        if (sellerID.equals(myUid)) {
                                            btn_checksongjang.setText("수취확인");
                                            spin_postcom.setClickable(false);
                                            et_songjang.setEnabled(false);
                                            btn_goPost.setVisibility(View.VISIBLE);
                                        } else {
                                            btn_checksongjang.setText("송장등록");
                                            spin_postcom.setEnabled(true);
                                            et_songjang.setEnabled(true);
                                            btn_goPost.setVisibility(View.GONE);
                                        }

                                        spin_postcom.setSelection(getIndex(spin_postcom, snapshot.get("postcom").toString()));
                                        et_songjang.setText(snapshot.get("postbox").toString());

                                        if (sellerID.equals(myUid) && checks == 1) {

                                            spin_postcom.setSelection(getIndex(spin_postcom, snapshot.get("postcom").toString()));

                                            et_songjang.setText(snapshot.get("postbox").toString());
                                            btn_checksongjang.setText("수취확인");
                                            Toast.makeText(getApplicationContext(), "송장이 등록되었습니다.\n물건을 받으시면 '수취확인'을 눌러주세요.", Toast.LENGTH_LONG).show();
                                        }

                                        ll_return.setVisibility(View.GONE);
                                        break;
                                    }
                                    case 6: {
                                        state5.setTextColor(Color.parseColor("#000000"));
                                        state5.setTypeface(null, Typeface.BOLD);
                                        state5.setBackgroundResource(R.drawable.state1);

                                        state1.setTextColor(Color.DKGRAY);
                                        state2.setTextColor(Color.DKGRAY);
                                        state3.setTextColor(Color.DKGRAY);
                                        state4.setTextColor(Color.DKGRAY);
                                        state1.setBackgroundResource(R.drawable.state3);
                                        state2.setBackgroundResource(R.drawable.state3);
                                        state3.setBackgroundResource(R.drawable.state3);
                                        state4.setBackgroundResource(R.drawable.state3);
                                        ll_songJang.setVisibility(View.GONE);
                                        if (sellerID.equals(myUid) && checks == 0) {
                                            ll_rate.setVisibility(View.VISIBLE);
                                        } else if (buyer.equals(myUid) && checkb == 0) {
                                            ll_rate.setVisibility(View.VISIBLE);
                                        } else {
                                            ll_rate.setVisibility(View.GONE);
                                        }

                                        break;
                                    }
                                    default:
                                        Toast.makeText(getApplicationContext(), "거래상태 확인 불가", Toast.LENGTH_SHORT).show();
                                }

                                if (sellerID.equals(myUid)) {
                                    if (snapshot.getLong("checks") != 0) {

                                        //holder.notice.setText(String.valueOf(snapshot.get("checks")));
                                        //holder.notice.setVisibility(View.VISIBLE);
                                    } else {
                                        //holder.notice.setVisibility(View.INVISIBLE);
                                    }
                                } else {

                                    if (snapshot.getLong("checkb") != 0) {
                                        //holder.notice.setText(String.valueOf(snapshot.get("checkb")));
                                        //holder.notice.setVisibility(View.VISIBLE);
                                    } else {
                                        //holder.notice.setVisibility(View.INVISIBLE);
                                    }
                                }

                            }
                        });


                        tv_sellerName.setText(sellername);
                        tv_buyerName.setText(buyername);
                        tv_product.setText(product);
                        tv_price1.setText(price1);
                        tv_price2.setText(price2);
                        tv_period.setText(startDate + " ~ " + returnDate);

                        //iv_profile.setImageBitmap(myprofile);
                        //tv_name.setText(myName);

                        et_price1.setEnabled(false);
                        et_price2.setEnabled(false);
                        spin_postcom.setClickable(false);
                        et_songjang.setEnabled(false);
                        btn_goPost.setVisibility(View.GONE);


                        //내가 판매자면 기간 선택 가능
                        if (sellerID.equals(myUid)) {
                            pickedDate1.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    calendarView2.setVisibility(View.VISIBLE);
                                    Toast.makeText(getApplicationContext(), "대여 시작일을 선택하세요.", Toast.LENGTH_SHORT).show();
                                }
                            });
                            pickedDate2.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    calendarView2.setVisibility(View.VISIBLE);
                                    Toast.makeText(getApplicationContext(), "대여 종료일을 선택하세요.", Toast.LENGTH_SHORT).show();
                                }
                            });
                            et_price1.setEnabled(true);
                            et_price2.setEnabled(true);
                            spin_postcom.setEnabled(true);
                            et_songjang.setEnabled(true);


                        } else {
                            if (!et_songjang.getText().toString().equals("")) {
                                btn_goPost.setVisibility(View.VISIBLE);
                            }

                        }

                        //달력 클릭시 이벤트 처리
                        calendarView2.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
                            @Override
                            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {


                                if (pickedDate1.getText().toString().equals("")) {
                                    pickedDate1.setText(String.valueOf(year) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(dayOfMonth));
                                    syear = year;
                                    smonth = month;
                                    sday = dayOfMonth;
                                    Toast.makeText(getApplicationContext(), "대여 종료일을 선택하세요.", Toast.LENGTH_SHORT).show();

                                } else if (!pickedDate1.getText().toString().equals("") && pickedDate2.getText().toString().equals("")) {
                                    pickedDate2.setText(String.valueOf(year) + "/" + String.valueOf(month + 1) + "/" + String.valueOf(dayOfMonth));
                                    ryear = year;
                                    rmonth = month;
                                    rday = dayOfMonth;
                                    //testcal.set(cyear, cmonth, cday, 0, 0, 0);
                                    Date date = new Date(year,month,dayOfMonth);
                                    returntimestamp = date.getTime();
                                } else {
                                    Toast.makeText(getApplicationContext(), "기간을 수정하려면 대여 시작일부터 선택하세요.", Toast.LENGTH_SHORT).show();
                                    pickedDate1.setText("");
                                    pickedDate2.setText("");
                                }

                            }
                        });

                        btn_return.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                db.collection("contract").document(contract).update("state", 4).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getApplicationContext(), "반납유형을 선택하세요.", Toast.LENGTH_SHORT).show();

                                        now = System.currentTimeMillis();
                                        date = new Date(now);
                                        SimpleDateFormat postDates = new SimpleDateFormat("yy.MM.dd\nHH:mm:ss");
                                        String postDate = postDates.format(date);

                                        final TimeStampInfo stamp = new TimeStampInfo();
                                        stamp.setTime(postDate);
                                        stamp.setMessage("대여자가 반납신청을 시작하였습니다.");
                                        stamp.setTimestamp(now);
                                        stamp.setContract(contract);

                                        myRef.push().setValue(stamp);
                                    }
                                });

                            }
                        });

                        //거래내용 확인 버튼 클릭시
                        btn_checkPrice.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (sellerID.equals(myUid)) {//판매자일 경우 거래내용 수정 가능
                                    if (et_price1.getText().toString().equals("") || et_price2.getText().toString().equals("") || pickedDate2.getText().toString().equals("") || pickedDate1.getText().toString().equals("")||spin_bank.getSelectedItem().toString().equals("은행선택")) {
                                        Toast.makeText(getApplicationContext(), "빠진 항목이 없는지 확인하세요.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "거래 내용을 수정하였습니다.", Toast.LENGTH_SHORT).show();
                                        tv_price1.setText(et_price1.getText().toString());
                                        tv_price2.setText(et_price2.getText().toString());
                                        tv_period.setText(pickedDate1.getText().toString() + " ~ " + pickedDate2.getText().toString());

                                        db.collection("contract").document(contract).update("checks", 1, "price1",
                                                tv_price1.getText().toString(), "price2", tv_price2.getText().toString(), "startDate",
                                                pickedDate1.getText().toString(), "returnDate", pickedDate2.getText().toString(), "sbankcom", spin_bank.getSelectedItem().toString(), "sbanknum", et_bank.getText().toString()).
                                                addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        btn_checkPrice.setText("상대방이 변경사항을 확인중입니다.");
                                                        calendarView2.setVisibility(View.GONE);
                                                        now = System.currentTimeMillis();
                                                        date = new Date(now);
                                                        SimpleDateFormat postDates = new SimpleDateFormat("yy.MM.dd\nHH:mm:ss");
                                                        String postDate = postDates.format(date);

                                                        final TimeStampInfo stamp = new TimeStampInfo();
                                                        stamp.setTime(postDate);
                                                        stamp.setMessage("상품 제공자가 거래 내용을 수정하였습니다.");
                                                        stamp.setTimestamp(now);
                                                        stamp.setContract(contract);

                                                        myRef.push().setValue(stamp);
                                                        /*
                                                        SimpleDateFormat abcde= new SimpleDateFormat("yyyy/MM/dd");
                                                        try {
                                                            abcde.parse(returnDate);
                                                        } catch (ParseException e) {
                                                            e.printStackTrace();
                                                        }

                                                         */
                                                        db.collection("posts").document(postID).update("returndate", returntimestamp, "yorn", "거래중").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {

                                                            }
                                                        });
                                                    }
                                                });

                                    }

                                } else {//대여자일경우 거래내용 확인 후 동의 거부 가능
                                    if (et_price1.getText().toString().equals("") || et_price2.getText().toString().equals("") || pickedDate2.getText().toString().equals("") || pickedDate1.getText().toString().equals("")) {
                                        Toast.makeText(getApplicationContext(), "상대방이 거래 내용을 확인중입니다.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        db.collection("contract").document(contract).update("checks", 0, "state", 2, "bbankcom", spin_bank.getSelectedItem().toString(), "bbanknum", et_bank.getText().toString()).
                                                addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        ll_checkPrice.setVisibility(View.GONE);
                                                        ll_songJang.setVisibility(View.VISIBLE);
                                                        //거래 내역에 추가하기 코드 작성
                                                        Toast.makeText(getApplicationContext(), "거래 내용에 동의하였습니다.\n상품 제공자에게 송장 등록을 의뢰하세요.", Toast.LENGTH_LONG).show();
                                                        now = System.currentTimeMillis();
                                                        date = new Date(now);
                                                        SimpleDateFormat postDates = new SimpleDateFormat("yy.MM.dd\nHH:mm:ss");
                                                        String postDate = postDates.format(date);

                                                        final TimeStampInfo stamp = new TimeStampInfo();
                                                        stamp.setTime(postDate);
                                                        stamp.setMessage("대여자가 거래 내용에 동의하였습니다.");
                                                        stamp.setTimestamp(now);
                                                        stamp.setContract(contract);

                                                        myRef.push().setValue(stamp);
                                                    }
                                                });
                                        state++;
                                    }
                                }
                            }
                        });


                        btn_goPost.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(songjang_Search + postcom + "+" + postbox));
                                //웹페이지 들어가기
                                startActivity(intent);
                            }
                        });

                        btn_chat2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        });

                        btn_goReturn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //ll_songjang.setVisibility(View.VISIBLE);
                                //btn_goreturn.setVisibility(View.GONE);
                                if (buyer.equals(myUid)) {
                                    db.collection("contract").document(contract).update("checks", 1, "returntype", spinner_return.getSelectedItem()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getApplicationContext(), "반납신청 하였습니다.", Toast.LENGTH_SHORT).show();
                                            now = System.currentTimeMillis();
                                            date = new Date(now);
                                            SimpleDateFormat postDates = new SimpleDateFormat("yy.MM.dd\nHH:mm:ss");
                                            String postDate = postDates.format(date);

                                            final TimeStampInfo stamp = new TimeStampInfo();
                                            stamp.setTime(postDate);
                                            stamp.setMessage("대여자가 상품 반납을 신청하였습니다.");
                                            stamp.setTimestamp(now);
                                            stamp.setContract(contract);

                                            myRef.push().setValue(stamp);
                                        }
                                    });
                                } else {
                                    db.collection("contract").document(contract).update("checks", 0, "state", 5, "postcom", "직거래", "postbox", "직거래").addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(getApplicationContext(), "반납신청을 수락하였습니다.", Toast.LENGTH_SHORT).show();
                                            now = System.currentTimeMillis();
                                            date = new Date(now);
                                            SimpleDateFormat postDates = new SimpleDateFormat("yy.MM.dd\nHH:mm:ss");
                                            String postDate = postDates.format(date);

                                            final TimeStampInfo stamp = new TimeStampInfo();
                                            stamp.setTime(postDate);
                                            stamp.setMessage("상품 제공자가 반납 신청을 수락하였습니다.");
                                            stamp.setTimestamp(now);
                                            stamp.setContract(contract);

                                            myRef.push().setValue(stamp);
                                        }
                                    });
                                }
                            }
                        });


                        btn_checksongjang.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (state == 2) {
                                    if (sellerID.equals(myUid)) {
                                        if(checks==0){
                                            if(ContextCompat.checkSelfPermission(ActivityContract.this, Manifest.permission.WRITE_CALENDAR)!= PackageManager.PERMISSION_GRANTED){
                                                if(ActivityCompat.shouldShowRequestPermissionRationale(ActivityContract.this, Manifest.permission.WRITE_CALENDAR)) {
                                                    ActivityCompat.requestPermissions(ActivityContract.this, new String[]{Manifest.permission.WRITE_CALENDAR},3);
                                                }else {

                                                }
                                            }else{
                                                long startMillis = 0;
                                                long endMillis = 0;

                                                int year=0;
                                                int month=0;
                                                int day=0;

                                                String[] abc = returnDate.split("/");


                                                year = Integer.parseInt(abc[0]);
                                                month = Integer.parseInt(abc[1])-1;
                                                day = Integer.parseInt(abc[2]);

                                                Log.i("ymd =>", year+"년 "+ month+"월 "+ day+"일\n"+returnDate);


                                                Calendar beginTime = Calendar.getInstance();

                                                beginTime.set(year, month, day, 9, 0);
                                                startMillis = beginTime.getTimeInMillis();

                                                Calendar endTime = Calendar.getInstance();
                                                endTime.set(year, month, day, 21, 0); // 2013년 11월 25일 10시 45분
                                                endMillis = endTime.getTimeInMillis();

                                                ContentResolver cr = getContentResolver();
                                                ContentValues cv = new ContentValues();
                                                cv.put(CalendarContract.Events.TITLE, "빌려드림\n "+product+"반납 일자");
                                                cv.put(CalendarContract.Events.DESCRIPTION, "대여상품("+product+")반납 일자 = "+returnDate);
                                                cv.put(CalendarContract.Events.EVENT_LOCATION, "빌려드림");
                                                cv.put(CalendarContract.Events.DTSTART, startMillis);
                                                cv.put(CalendarContract.Events.DTEND, endMillis);
                                                cv.put(CalendarContract.Events.CALENDAR_ID, 1);
                                                cv.put(CalendarContract.Events.ALL_DAY, 1);
                                                cv.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().getID());
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    if (checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
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
                                                Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, cv);
                                                Toast.makeText(getApplicationContext(), "반납일정이 등록되었습니다.", Toast.LENGTH_SHORT).show();

                                                long eventID = Long.parseLong(uri.getLastPathSegment());

                                                Uri uris = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
                                                Intent intent = new Intent(Intent.ACTION_VIEW)
                                                        .setData(uris);
                                                startActivity(intent);
                                            }

                                        }

                                        if (spin_postcom.getSelectedItem().equals("직거래")) {
                                            db.collection("contract").document(contract).update("checks", 1, "postcom", "직거래", "postbox", "").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(getApplicationContext(), "상품을 건내주셨군요!\n상대방에게 수취확인을 요청하세요.", Toast.LENGTH_LONG).show();

                                                    now = System.currentTimeMillis();
                                                    date = new Date(now);
                                                    SimpleDateFormat postDates = new SimpleDateFormat("yy.MM.dd\nHH:mm:ss");
                                                    String postDate = postDates.format(date);

                                                    final TimeStampInfo stamp = new TimeStampInfo();
                                                    stamp.setTime(postDate);
                                                    stamp.setMessage("상품 제공자가 송장을 등록하였습니다.");
                                                    stamp.setTimestamp(now);
                                                    stamp.setContract(contract);

                                                    myRef.push().setValue(stamp);

                                                }
                                            });
                                        }else {
                                            db.collection("contract").document(contract).update("checks", 1, "postcom",spin_postcom.getSelectedItem().toString(),"postbox",et_songjang.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(getApplicationContext(),"송장을 등록했습니다.", Toast.LENGTH_LONG).show();
                                                    //거래 내역에 추가하기 코드 작성
                                                    now = System.currentTimeMillis();
                                                    date = new Date(now);
                                                    SimpleDateFormat postDates = new SimpleDateFormat( "yy.MM.dd\nHH:mm:ss");
                                                    String postDate = postDates.format (date);

                                                    final TimeStampInfo stamp = new TimeStampInfo();
                                                    stamp.setTime(postDate);
                                                    stamp.setMessage("상품 제공자가 송장을 등록하였습니다.");
                                                    stamp.setTimestamp(now);
                                                    stamp.setContract(contract);

                                                    myRef.push().setValue(stamp);
                                                }
                                            });
                                        }
                                    }else {
                                        if((state==2&&checks==0)||(state==5&&checks==0)){
                                            Toast.makeText(getApplicationContext(),"아직 송장이 등록되지 않았습니다. 상품 제공자에게 문의하세요.", Toast.LENGTH_LONG).show();
                                        }else {
                                            db.collection("contract").document(contract).update("checks", 0, "state", 3).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(getApplicationContext(),"상품이 수취처리 되었습니다.\n 상품에 이상이 확인시 3일내에 반품 신청바랍니다.", Toast.LENGTH_LONG).show();
                                                    ll_songJang.setVisibility(View.GONE);

                                                    now = System.currentTimeMillis();
                                                    date = new Date(now);
                                                    SimpleDateFormat postDates = new SimpleDateFormat( "yy.MM.dd\nHH:mm:ss");
                                                    String postDate = postDates.format (date);

                                                    TimeStampInfo stamp = new TimeStampInfo();
                                                    stamp.setTime(postDate);
                                                    stamp.setMessage("대여자가 수취 확인을 하였습니다.");
                                                    stamp.setTimestamp(now);
                                                    stamp.setContract(contract);

                                                    myRef.push().setValue(stamp);

                                                    if(ContextCompat.checkSelfPermission(ActivityContract.this, Manifest.permission.WRITE_CALENDAR)!= PackageManager.PERMISSION_GRANTED){
                                                        if(ActivityCompat.shouldShowRequestPermissionRationale(ActivityContract.this, Manifest.permission.WRITE_CALENDAR)) {
                                                            ActivityCompat.requestPermissions(ActivityContract.this, new String[]{Manifest.permission.WRITE_CALENDAR},3);
                                                        }else {

                                                        }
                                                    }else {
                                                        long startMillis = 0;
                                                        long endMillis = 0;

                                                        int year=0;
                                                        int month=0;
                                                        int day=0;

                                                        String[] abc = returnDate.split("/");


                                                        year = Integer.parseInt(abc[0]);
                                                        month = Integer.parseInt(abc[1])-1;
                                                        day = Integer.parseInt(abc[2]);


                                                        Calendar beginTime = Calendar.getInstance();

                                                        beginTime.set(year, month, day, 9, 0);
                                                        startMillis = beginTime.getTimeInMillis();

                                                        Calendar endTime = Calendar.getInstance();
                                                        endTime.set(year, month, day, 21, 0); // 2013년 11월 25일 10시 45분
                                                        endMillis = endTime.getTimeInMillis();

                                                        ContentResolver cr = getContentResolver();
                                                        ContentValues cv = new ContentValues();
                                                        cv.put(CalendarContract.Events.TITLE, "빌려드림\n "+product+"반납 일자");
                                                        cv.put(CalendarContract.Events.DESCRIPTION, "대여상품("+product+")반납 일자 = "+returnDate);
                                                        cv.put(CalendarContract.Events.EVENT_LOCATION, "빌려드림");
                                                        cv.put(CalendarContract.Events.DTSTART, startMillis);
                                                        cv.put(CalendarContract.Events.DTEND, endMillis);
                                                        cv.put(CalendarContract.Events.ALL_DAY, 1);
                                                        cv.put(CalendarContract.Events.CALENDAR_ID, 1);
                                                        cv.put(CalendarContract.Events.EVENT_TIMEZONE, Calendar.getInstance().getTimeZone().getID());
                                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                            if (checkSelfPermission(Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
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
                                                        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, cv);
                                                        Toast.makeText(getApplicationContext(), "반납일정이 등록되었습니다.", Toast.LENGTH_SHORT).show();

                                                        long eventID = Long.parseLong(uri.getLastPathSegment());

                                                        Uri uris = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, eventID);
                                                        Intent intent = new Intent(Intent.ACTION_VIEW)
                                                                .setData(uris);
                                                        startActivity(intent);
                                                    }

                                                    db.collection("posts").document(postID).update("yorn", "대여중", "buyer", buyer).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            now = System.currentTimeMillis();
                                                            date = new Date(now);
                                                            SimpleDateFormat postDates = new SimpleDateFormat( "yy.MM.dd\nHH:mm:ss");
                                                            String postDate = postDates.format (date);

                                                            final TimeStampInfo stamp = new TimeStampInfo();
                                                            stamp.setTime(postDate);
                                                            stamp.setMessage("상품의 게시글 상태가 \"대여중\"으로 변경되었습니다.");
                                                            stamp.setTimestamp(now);
                                                            stamp.setContract(contract);

                                                            myRef.push().setValue(stamp);
                                                        }
                                                    });


                                                    state++;
                                                }
                                            });
                                        }
                                    }
                                }else if(state==5){
                                    if(buyer.equals(myUid)){
                                        if(spin_postcom.getSelectedItem().equals("직거래")){
                                            db.collection("contract").document(contract).update("checks", 1, "postcom","직거래","postbox","").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(getApplicationContext(),"상품을 건내주셨군요!\n상대방에게 수취확인을 요청하세요.", Toast.LENGTH_LONG).show();

                                                    now = System.currentTimeMillis();
                                                    date = new Date(now);
                                                    SimpleDateFormat postDates = new SimpleDateFormat( "yy.MM.dd\nHH:mm:ss");
                                                    String postDate = postDates.format (date);

                                                    final TimeStampInfo stamp = new TimeStampInfo();
                                                    stamp.setTime(postDate);
                                                    stamp.setMessage("대여자가 송장을 등록하였습니다.");
                                                    stamp.setTimestamp(now);
                                                    stamp.setContract(contract);

                                                    myRef.push().setValue(stamp);


                                                }
                                            });
                                        }else {
                                            db.collection("contract").document(contract).update("checks", 1, "postcom",spin_postcom.getSelectedItem().toString(),"postbox",et_songjang.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(getApplicationContext(),"송장을 등록했습니다.", Toast.LENGTH_LONG).show();
                                                    //거래 내역에 추가하기 코드 작성
                                                    now = System.currentTimeMillis();
                                                    date = new Date(now);
                                                    SimpleDateFormat postDates = new SimpleDateFormat( "yy.MM.dd\nHH:mm:ss");
                                                    String postDate = postDates.format (date);

                                                    final TimeStampInfo stamp = new TimeStampInfo();
                                                    stamp.setTime(postDate);
                                                    stamp.setMessage("대여자가 송장을 등록하였습니다.");
                                                    stamp.setTimestamp(now);
                                                    stamp.setContract(contract);

                                                    myRef.push().setValue(stamp);
                                                }
                                            });
                                        }
                                    }else {
                                        if((state==2&&checks==0)||(state==5&&checks==0)){
                                            Toast.makeText(getApplicationContext(),"아직 송장이 등록되지 않았습니다. 상품 제공자에게 문의하세요.", Toast.LENGTH_LONG).show();
                                        }else {
                                            db.collection("contract").document(contract).update("checks", 0, "state", 6).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    Toast.makeText(getApplicationContext(),"거래가 종료되었습니다.", Toast.LENGTH_LONG).show();
                                                    ll_songJang.setVisibility(View.GONE);

                                                    now = System.currentTimeMillis();
                                                    date = new Date(now);
                                                    SimpleDateFormat postDates = new SimpleDateFormat( "yy.MM.dd\nHH:mm:ss");
                                                    String postDate = postDates.format (date);

                                                    TimeStampInfo stamp = new TimeStampInfo();
                                                    stamp.setTime(postDate);
                                                    stamp.setMessage("상품 제공자가 수취 확인을 하였습니다.");
                                                    stamp.setTimestamp(now);
                                                    stamp.setContract(contract);

                                                    myRef.push().setValue(stamp);

                                                    db.collection("posts").document(postID).update("yorn", "거래종료").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            now = System.currentTimeMillis();
                                                            date = new Date(now);
                                                            SimpleDateFormat postDates = new SimpleDateFormat( "yy.MM.dd\nHH:mm:ss");
                                                            String postDate = postDates.format (date);

                                                            final TimeStampInfo stamp = new TimeStampInfo();
                                                            stamp.setTime(postDate);
                                                            stamp.setMessage("상품의 게시글 상태가 \"거래종료\"로 변경되었습니다.");
                                                            stamp.setTimestamp(now);
                                                            stamp.setContract(contract);

                                                            myRef.push().setValue(stamp);
                                                        }
                                                    });

                                                    db.collection("chat").document(chatroomid).update("state", "finish").addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                        }
                                                    });

                                                    state++;
                                                }
                                            });
                                        }
                                    }
                                }
                            }
                        });
                        //거래내용 확인 버튼 클릭시
                    }else {
                        Toast.makeText(getApplicationContext(),"찾을 수 없음", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

        tv_sellerTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:/"+ tv_sellerTel.getText().toString()));
                startActivity(intent1);
            }
        });

        tv_buyerTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:/"+ tv_buyerTel.getText().toString()));
                startActivity(intent1);
            }
        });

        btn_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(b_state){
                    rv_timestamp.setVisibility(View.GONE);
                    btn_state.setBackgroundResource(R.drawable.ic_arrow2);
                    b_state=false;
                }else {
                    rv_timestamp.setVisibility(View.VISIBLE);
                    btn_state.setBackgroundResource(R.drawable.ic_arrow3);
                    b_state=true;
                }
            }
        });

        btn_contract.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(b_contract){
                    ll_person.setVisibility(View.GONE);
                    btn_contract.setBackgroundResource(R.drawable.ic_arrow2);
                    b_contract=false;
                }else {
                    ll_person.setVisibility(View.VISIBLE);
                    btn_contract.setBackgroundResource(R.drawable.ic_arrow3);
                    b_contract=true;
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

    private void setStampAdapter(DataSnapshot dataSnapshot) {
        TimeStampInfo timeStampInfo = new TimeStampInfo(dataSnapshot.getValue(TimeStampInfo.class));
        arrayList.add(timeStampInfo);
        stampAdapter.notifyDataSetChanged();
        //rv_timestamp.smoothScrollToPosition(stampAdapter.getItemCount());
    }

    private int getIndex(Spinner spinner, String item){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(item)){
                return i;
            }
        }
        return 0;
    }

    private void refrashstamp(DataSnapshot dataSnapshot) {
        TimeStampInfo timeStampInfo = new TimeStampInfo(dataSnapshot.getValue(TimeStampInfo.class));
        arrayList.add(timeStampInfo);
        stampAdapter.notifyDataSetChanged();
        rv_timestamp.smoothScrollToPosition(stampAdapter.getItemCount());
    }
}