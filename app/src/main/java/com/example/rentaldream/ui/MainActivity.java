package com.example.rentaldream.ui;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentaldream.model.MemberInfo;
import com.example.rentaldream.adapter.PostAdapter;
import com.example.rentaldream.model.PostInfo;
import com.example.rentaldream.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.tasks.OnCompleteListener;
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
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    Button btn_myhome, btn_sort1, btn_sort2, btn_sort3, btn_sort4, btn_research, btn_goback;
    FloatingActionButton btn_post;
    TextView tv_cat;
    EditText et_search;
    ListView lv_cat;
    String userName="";
    static String userUid="";
    String field="product";
    String ordercase="postDate";
    String ordertype="date";
    final int REQUEST_CODE = 7;
    String category;





    static final String[] CAT_MENU = {"전체", "의류/잡화", "완구/취미", "스포츠/레져" ,"반려동물용품", "홈/인테리어", "생활용품", "자동차용품", "디지털/가전", "도서/음반/DVD", "문구/오피스", "뷰티/악세사리", "헬스"};

    public static MainActivity mainActivity;

    private FirebaseFirestore db  = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();




    private PostAdapter postAdapter;
    private ArrayList<PostInfo> arrayList;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;

    private static final String TAG = "MainActivity";


    @Override
        protected void onCreate(Bundle savedInstanceState) {
        Fresco.initialize(this);
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
        mainActivity = MainActivity.this;




        btn_myhome = (Button)findViewById(R.id.btn_myhome);
        btn_post = (FloatingActionButton)findViewById(R.id.btn_post2);
        et_search = (EditText)findViewById(R.id.et_search);
        tv_cat = (TextView)findViewById(R.id.tv_cat);
        lv_cat = (ListView)findViewById(R.id.lv_cat);
        btn_sort1 =(Button)findViewById(R.id.btn_sort1);
        btn_sort2 =(Button)findViewById(R.id.btn_sort2);
        btn_sort3 =(Button)findViewById(R.id.btn_sort3);
        btn_sort4 =(Button)findViewById(R.id.btn_sort4);
        btn_research = (Button)findViewById(R.id.btn_home);
        btn_goback = (Button)findViewById(R.id.btn_goback);

        category = getIntent().getStringExtra("category");
        tv_cat.setText(category);






        lv_cat.setVisibility(View.GONE);

        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, CAT_MENU) ;
        lv_cat.setAdapter(adapter) ;


        recyclerView = (RecyclerView)findViewById(R.id.rv_comment);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        arrayList= new ArrayList<>();

        postAdapter = new PostAdapter(this, arrayList);
        recyclerView.setAdapter(postAdapter);

        //passPushTokentToServer();









        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        final DocumentReference docRef = db.collection("users").document(user.getUid());
/*
        DocumentReference getPost = db.collection("posts").document();
        getPost.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                PostInfo postInfo = documentSnapshot.toObject(PostInfo.class);
                testview.setText(postInfo.getTitle());
            }
        });
*/


        //tv_uid.setText(docRef.getId());


        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                MemberInfo memberInfo = documentSnapshot.toObject(MemberInfo.class);
                userName = memberInfo.getName();
                userUid = docRef.getId();
                //tv_tel.setText(memberInfo.getphoneNum());
            }
        });




        tv_cat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lv_cat.setVisibility(View.VISIBLE);
            }
        });



        //getPostList();

        if(tv_cat.getText().toString().equals("전체")||tv_cat.getText().toString().equals("카테고리")){
            field="product";
            getPostList();
        }else {
            field="category";
            findByQuery(tv_cat.getText().toString());
            //getPostListwithCategory2();
        }



        lv_cat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                tv_cat.setText((String) parent.getItemAtPosition(position));
                lv_cat.setVisibility(View.GONE);
                if(tv_cat.getText().toString().equals("전체")||tv_cat.getText().toString().equals("카테고리")){
                    field="product";
                    getPostList();
                }else {
                    field="category";
                    findByQuery(tv_cat.getText().toString());

                }
            }
        }) ;


        btn_sort1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordercase="postDate";
                ordertype="date";
                if(tv_cat.getText().toString().equals("카테고리")||tv_cat.getText().toString().equals("전체"))
                {
                    getPostList();
                }else {
                    getPostListwithCategory();
                }

            }
        });
        btn_sort2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordercase="booked";
                ordertype="book";
                if(tv_cat.getText().toString().equals("카테고리")||tv_cat.getText().toString().equals("전체"))
                {
                    getPostList();
                }else {
                    getPostListwithCategory();
                }
            }
        });
        btn_sort3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordercase="price2";
                ordertype="price2";
                if(tv_cat.getText().toString().equals("카테고리")||tv_cat.getText().toString().equals("전체"))
                {
                    getPostList();
                }else {
                    getPostListwithCategory();
                }
            }
        });
        btn_sort4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordercase="price1";
                ordertype="price2";
                if(tv_cat.getText().toString().equals("카테고리")||tv_cat.getText().toString().equals("전체"))
                {
                    getPostList();
                }else {
                    getPostListwithCategory();
                }
            }
        });






        btn_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(getApplicationContext(), a + "\n" + b, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, PostActivity.class);
                intent.putExtra("name", userName);
                intent.putExtra("Uid", userUid);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        btn_research.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPostList();
                /*
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA);

                 */
            }
        });

        btn_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
/*
        testview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a = tv_name.getText().toString();
                String b = tv_uid.getText().toString();
                //Toast.makeText(getApplicationContext(), a + "\n" + b, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, PostViewActivity.class);
                intent.putExtra("name", a);
                intent.putExtra("Uid", b);
                intent.putExtra("postid" postID);
                startActivity(intent);
            }
        });

 */
/*
        db.collection("posts").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    List postInfoList = queryDocumentSnapshots.toObjects(PostInfo.class);
                tesxt.setText(postInfoList.get(1).toString());




            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

 */




        et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if((event.getAction()==KeyEvent.ACTION_DOWN)&&(keyCode==KeyEvent.KEYCODE_ENTER)){
                    String search = et_search.getText().toString();
                    findByQuery(search);
                    return true;
                }
                return false;
            }
        });




    }


    private void getPostList(){
        db.collection("posts").orderBy(ordercase).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            arrayList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PostInfo postInfo;
                                postInfo = new PostInfo(document.toObject(PostInfo.class), document.getId(), ordertype);
                                arrayList.add(postInfo);
                                //postlist.add(String.format(document.getData().get("title").toString(),i)) ;
                            }

                            Collections.sort(arrayList);
                            postAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }


    private void getPostListwithCategory(){
        db.collection("posts").whereEqualTo("category",tv_cat.getText().toString()).whereEqualTo("yorn", "거래중").orderBy(ordercase).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            arrayList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PostInfo postInfo;
                                postInfo = new PostInfo(document.toObject(PostInfo.class), document.getId(), ordertype);
                                arrayList.add(postInfo);
                            }
                            Collections.sort(arrayList);
                            postAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    private void getPostListwithCategory2(){
        db.collection("posts").whereEqualTo("category",category).whereEqualTo("yorn", "거래중").orderBy(ordercase).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            arrayList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                PostInfo postInfo;
                                postInfo = new PostInfo(document.toObject(PostInfo.class), document.getId(), ordertype);
                                arrayList.add(postInfo);
                            }
                            Collections.sort(arrayList);
                            postAdapter.notifyDataSetChanged();
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }







    private void findByQuery(String search){
        db.collection("posts")
                .whereEqualTo(field, search).whereEqualTo("yorn", "거래가능")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            PostInfo postInfo;
                            if(task.getResult().isEmpty()){
                                Toast.makeText(getApplicationContext(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                                arrayList.clear();
                                postAdapter.notifyDataSetChanged();
                            }else {
                                arrayList.clear();
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    //Log.d(TAG, "findByQuery => "+document.getId() + " => " + document.getData());
                                    postInfo = new PostInfo(document.toObject(PostInfo.class), document.getId());

                                    arrayList.add(postInfo);
                                }
                                postAdapter.notifyDataSetChanged();
                            }

                        } else {
                                Toast.makeText(getApplicationContext(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
/*
    void passPushTokentToServer(){
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String token = FirebaseInstanceId.getInstance().getToken();

        Map<String, Object> map= new HashMap<>();
        map.put("pushToken", token);


        db.collection("users").document(uid).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }
*/
/*
    @Override
    protected void onResume() {
        super.onResume();
        //getPostList();
    }
    */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            getPostList();
        }

    }
}
