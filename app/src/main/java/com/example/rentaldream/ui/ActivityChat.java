package com.example.rentaldream.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentaldream.adapter.ChatListAdapter;
import com.example.rentaldream.model.ChatListInfo;
import com.example.rentaldream.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.rentaldream.ui.FirstShowActivity.myName;
import static com.example.rentaldream.ui.FirstShowActivity.myUid;

public class ActivityChat extends AppCompatActivity {


    TextView tv_checkSell, tv_checkBuy, tv_send, tv_get;
    ImageView iv_profile;
    LinearLayout chatListLayout;
    ScrollView sv_get, sv_send;


    String postID, sellerID, sellerName, buyer, chatRoomId, buyerName;
    String into=null;

    private DatabaseReference myRef;

    private ChatListAdapter chatListAdapter1, chatListAdapter2;
    private ArrayList<ChatListInfo> arrayList2, arrayList3;
    private RecyclerView rv_chatList1, rv_chatList2;
    private LinearLayoutManager linearLayoutManager2, linearLayoutManager3;

    private FirebaseFirestore db  = FirebaseFirestore.getInstance();

    boolean shows=false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activtiy_chat);

        rv_chatList1 = (RecyclerView)findViewById(R.id.rv_chatlist);
        rv_chatList2 = (RecyclerView)findViewById(R.id.rv_chatlist2);

        tv_checkSell = (TextView)findViewById(R.id.tv_checksell);
        tv_checkBuy = (TextView)findViewById(R.id.tv_checkbuy);
        tv_send = (TextView)findViewById(R.id.tv_send);
        tv_get = (TextView)findViewById(R.id.tv_get);

        sv_get = (ScrollView)findViewById(R.id.sv_get);
        sv_send = (ScrollView)findViewById(R.id.sv_send);


        //채팅리스트 뷰(판매자)
        linearLayoutManager2 = new LinearLayoutManager(this);
        rv_chatList1.setLayoutManager(linearLayoutManager2);
        arrayList2= new ArrayList<>();
        chatListAdapter1 = new ChatListAdapter(getApplicationContext(), arrayList2);
        chatListAdapter1.setHasStableIds(true);
        rv_chatList1.setAdapter(chatListAdapter1);


        //채팅리스트 뷰(구매자)
        rv_chatList2 = (RecyclerView)findViewById(R.id.rv_chatlist2);
        linearLayoutManager3 = new LinearLayoutManager(this);
        rv_chatList2.setLayoutManager(linearLayoutManager3);

        arrayList3= new ArrayList<>();

        chatListAdapter2 = new ChatListAdapter(getApplicationContext(), arrayList3);
        chatListAdapter2.setHasStableIds(true);
        rv_chatList2.setAdapter(chatListAdapter2);


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        //message를 참조(getReference)해서 가져옴.
        myRef = database.getReference();
        into = getIntent().getStringExtra("into");

        sv_send.setVisibility(View.GONE);

        tv_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sv_send.setVisibility(View.GONE);
                sv_get.setVisibility(View.VISIBLE);

                tv_get.setBackgroundColor(Color.parseColor("#FAF9FD"));
                tv_send.setBackgroundColor(Color.parseColor("#D2D0D6"));
                shows=false;
            }
        });

        tv_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sv_send.setVisibility(View.VISIBLE);
                sv_get.setVisibility(View.GONE);

                tv_send.setBackgroundColor(Color.parseColor("#FAF9FD"));
                tv_get.setBackgroundColor(Color.parseColor("#D2D0D6"));
                shows=true;
            }
        });



        if(into.equals("post")){

            postID = getIntent().getStringExtra("postID");
            chatRoomId = getIntent().getStringExtra("chatroomid");
            sellerID = getIntent().getStringExtra("sellerID");
            sellerName = getIntent().getStringExtra("sellerName");
            buyer = getIntent().getStringExtra("buyer");

            Intent intentq = new Intent(this, ActivityTalk.class);
            intentq.putExtra("postID", postID);
            intentq.putExtra("cId", chatRoomId);
            intentq.putExtra("sellerID", sellerID);
            intentq.putExtra("saln", sellerName);
            intentq.putExtra("buyer", myUid);
            intentq.putExtra("bName", myName);
            startActivity(intentq);

        }else if (into.equals("pushAlarm")){

            postID = getIntent().getStringExtra("postID");
            chatRoomId = getIntent().getStringExtra("chatroomid");
            sellerID = getIntent().getStringExtra("sellerID");
            sellerName = getIntent().getStringExtra("sellerName");
            buyer = getIntent().getStringExtra("buyer");
            buyerName = getIntent().getStringExtra("buyerName");


            Intent intent_talk = new Intent(this, ActivityTalk.class);
            intent_talk.putExtra("postID", postID);
            intent_talk.putExtra("cId", chatRoomId);
            intent_talk.putExtra("sellerID", sellerID);
            intent_talk.putExtra("saln", sellerName);
            intent_talk.putExtra("buyer", myUid);
            intent_talk.putExtra("bName", buyerName);
            startActivity(intent_talk);

        }else if(into.equals("myhome")){
            db.collection("chat").whereEqualTo("seller",myUid).orderBy("postID").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.getResult().isEmpty()||task.getResult().equals(null)){

                    }else {
                        tv_checkSell.setVisibility(View.GONE);
                        //채팅 리스트 리사이클러뷰 적용
                        for(QueryDocumentSnapshot document : task.getResult()) {
                            ChatListInfo chatListInfo;
                            chatListInfo = new ChatListInfo(document.toObject(ChatListInfo.class), document.getId().toString());
                            arrayList2.add(chatListInfo);
                        }
                        chatListAdapter1.notifyDataSetChanged();
                        rv_chatList1.smoothScrollToPosition(chatListAdapter1.getItemCount());
                    }
                }
            });

            db.collection("chat").whereEqualTo("buyer",myUid).orderBy("postID").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.getResult().isEmpty()||task.getResult().equals(null)){

                    }else {
                        tv_checkBuy.setVisibility(View.GONE);
                        //채팅 리스트 리사이클러뷰 적용
                        for(QueryDocumentSnapshot document2 : task.getResult()){
                            ChatListInfo chatListInfo2;
                            chatListInfo2 = new ChatListInfo(document2.toObject(ChatListInfo.class), document2.getId().toString());
                            arrayList3.add(chatListInfo2);
                        }
                        chatListAdapter2.notifyDataSetChanged();
                        rv_chatList2.smoothScrollToPosition(chatListAdapter2.getItemCount());
                    }
                }
            });

        }

        if(!myUid.equals(sellerID)){
            buyer = myUid;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(into.equals("post")){
            finish();
        }else if(into.equals("pushAlarm")){
            finish();
        }
    }
}



