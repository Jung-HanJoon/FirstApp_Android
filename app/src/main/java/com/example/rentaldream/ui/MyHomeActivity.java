package com.example.rentaldream.ui;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentaldream.model.MemberInfo;
import com.example.rentaldream.adapter.PostAdapter2;
import com.example.rentaldream.model.PostInfo;
import com.example.rentaldream.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.rentaldream.ui.FirstShowActivity.myImgURL;
import static com.example.rentaldream.ui.FirstShowActivity.myUid;
import static com.example.rentaldream.ui.FirstShowActivity.myprofile;

public class MyHomeActivity extends AppCompatActivity {

    Button btn_logout, btn_changeprofile, btn_goback, btn_chatlist, btn_rentallist, btn_changepass;
    RecyclerView rv_book, rv_mypost, rv_rented, rv_rented2;
    TextView tv_name, tv_test, tv_test2, tv_test3, tv_test4;
    ImageView profile;
    TextView tv_sprices, tv_bprices, tv_rprice, tv_countsell;

    DecimalFormat formatter = new DecimalFormat("###,###");

    int sumsell=0;
    int sumbuy=0;
    int sumrsell=0;
    int sumrbuy=0;
    int countbuy=0;
    int countsell=0;


    private FirebaseFirestore db  = FirebaseFirestore.getInstance();
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private static final int GALLERY_CODE = 10;
    private String imagePath="";
    String downloadURL="";
    Bitmap bitmap;


    private PostAdapter2 postAdapter;
    private ArrayList<PostInfo> arrayList;
    private LinearLayoutManager linearLayoutManager;

    private PostAdapter2 postAdapter2;
    private ArrayList<PostInfo> arrayList2;
    private LinearLayoutManager linearLayoutManager2;

    private PostAdapter2 postAdapter3;
    private ArrayList<PostInfo> arrayList3;
    private LinearLayoutManager linearLayoutManager3;

    private PostAdapter2 postAdapter4;
    private ArrayList<PostInfo> arrayList4;
    private LinearLayoutManager linearLayoutManager4;

    String userName="";
    String userID="";
    String userTel="";

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_myhome);

        firebaseAuth = FirebaseAuth.getInstance();

        btn_logout = (Button)findViewById(R.id.btn_logout);
        rv_book = (RecyclerView)findViewById(R.id.rv_book);
        rv_mypost = (RecyclerView)findViewById(R.id.rv_mypost);
        rv_rented = (RecyclerView)findViewById(R.id.rv_rented);
        rv_rented2 = (RecyclerView)findViewById(R.id.rv_rented2);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_test = (TextView)findViewById(R.id.tv_test);
        tv_test2 = (TextView)findViewById(R.id.tv_test2);
        tv_test3 = (TextView)findViewById(R.id.tv_test3);
        tv_test4 = (TextView)findViewById(R.id.tv_test4);
        profile = (ImageView)findViewById(R.id.profile);
        btn_goback = (Button)findViewById(R.id.btn_goback);
        btn_changeprofile = (Button)findViewById(R.id.btn_changeprofile);
        btn_chatlist =(Button)findViewById(R.id.btn_chatlist);
        btn_rentallist = (Button)findViewById(R.id.btn_rentallist);
        btn_changepass = (Button)findViewById(R.id.btn_changepass);

        tv_sprices = (TextView)findViewById(R.id.tv_sprices);
        tv_bprices = (TextView)findViewById(R.id.tv_bprices);
        tv_rprice = (TextView)findViewById(R.id.tv_rprice);
        tv_countsell = (TextView)findViewById(R.id.tv_countsell);



        profile.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT >= 21) {
            profile.setClipToOutline(true);
        }


        //찜목록
        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_book.setLayoutManager(linearLayoutManager);

        arrayList= new ArrayList<>();

        postAdapter = new PostAdapter2(this, arrayList);
        rv_book.setAdapter(postAdapter);





        //내 게시글 목록
        linearLayoutManager2 = new LinearLayoutManager(this);
        linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_mypost.setLayoutManager(linearLayoutManager2);

        arrayList2= new ArrayList<>();

        postAdapter2 = new PostAdapter2(this, arrayList2);
        rv_mypost.setAdapter(postAdapter2);


        //대여중인것 목록
        linearLayoutManager3 = new LinearLayoutManager(this);
        linearLayoutManager3.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_rented.setLayoutManager(linearLayoutManager3);

        arrayList3= new ArrayList<>();

        postAdapter3 = new PostAdapter2(this, arrayList3);
        rv_rented.setAdapter(postAdapter3);

        //제공중인것 목록
        linearLayoutManager4 = new LinearLayoutManager(this);
        linearLayoutManager4.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv_rented2.setLayoutManager(linearLayoutManager4);

        arrayList4= new ArrayList<>();

        postAdapter4 = new PostAdapter2(this, arrayList4);
        rv_rented2.setAdapter(postAdapter4);





        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userID = user.getUid();
        DocumentReference docRef = db.collection("users").document(user.getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                MemberInfo memberInfo = documentSnapshot.toObject(MemberInfo.class);
                userName = memberInfo.getName();

                userTel = memberInfo.getphoneNum();
                tv_name.setText(userName);


                if(myprofile!=null){
                    profile.setImageBitmap(myprofile);
                }else if(memberInfo.getImageURL()!=null){
                    setImageSrc(profile, memberInfo.getImageURL());
                }
            }
        });



        //getBookedlist();
        /*
        Query testQuery = db.collection("booked").whereEqualTo("postID", true);
        db.collection("posts").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                PostInfo postInfo;
                                postInfo = new PostInfo(document.toObject(PostInfo.class), document.getId());

                                arrayList.add(postInfo);
                                postAdapter.notifyDataSetChanged();
                                //postlist.add(String.format(document.getData().get("title").toString(),i)) ;
                            }
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
*/

        btn_rentallist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityRentalList.class);
                startActivity(intent);
            }
        });



        //대여중인목록
        db.collection("posts").whereEqualTo("buyer", myUid).whereEqualTo("yorn","대여중").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(!task.getResult().isEmpty()){

                                tv_test2.setVisibility(View.GONE);
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("대여중", "findByQuery => "+document.getId() + " => " + document.getData());

                                    PostInfo postInfo;
                                    postInfo = new PostInfo(document.toObject(PostInfo.class), document.getId());

                                    arrayList3.add(postInfo);
                                    postAdapter3.notifyDataSetChanged();
                                }
                            }else {
                                //tv_test.setText("대여중인 상품이 없습니다.");
                            }

                        } else {
                            Log.d("실행실패", "Error getting documents: ", task.getException());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("gggg", "findByQuery => ");

            }
        });

        //제공중인목록
        db.collection("posts").whereEqualTo("postuserID", myUid).whereEqualTo("yorn","대여중").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(!task.getResult().isEmpty()){

                                tv_test4.setVisibility(View.GONE);
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("대여중", "findByQuery => "+document.getId() + " => " + document.getData());

                                    PostInfo postInfo;
                                    postInfo = new PostInfo(document.toObject(PostInfo.class), document.getId());

                                    arrayList4.add(postInfo);
                                    postAdapter4.notifyDataSetChanged();
                                }
                            }else {
                                //tv_test.setText("대여중인 상품이 없습니다.");
                            }

                        } else {
                            Log.d("실행실패", "Error getting documents: ", task.getException());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("gggg", "findByQuery => ");

            }
        });


        //대여수익
        db.collection("contract").whereEqualTo("sellerID", myUid).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(!task.getResult().isEmpty()){
                    for(QueryDocumentSnapshot document : task.getResult()){
                        sumsell += Integer.parseInt(document.get("price2").toString());
                        if(document.getLong("state")<6){
                            countsell++;
                            sumrsell +=Integer.parseInt(document.get("price1").toString());
                        }

                    }
                    tv_sprices.setText(formatter.format(sumsell)+"원");

                }
                //대여지출 및 건수
                db.collection("contract").whereEqualTo("buyer", myUid).whereLessThan("state", 6).whereGreaterThan("state",2).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(!task.getResult().isEmpty()){
                            for(QueryDocumentSnapshot document : task.getResult()){
                                sumbuy += Integer.parseInt(document.get("price2").toString());
                                sumrbuy +=Integer.parseInt(document.get("price1").toString());
                                countbuy++;
                            }
                        }
                        tv_bprices.setText(formatter.format(sumbuy)+"원");
                        tv_rprice.setText(formatter.format(sumrsell)+"원"+"\n"+formatter.format(sumrbuy)+"원");
                        tv_countsell.setText("제공  "+String.valueOf(countsell)+"건"+"\n"+"대여  "+String.valueOf(countbuy)+"건");
                    }
                });
            }
        });





        //찜목록
        db.collection("booked").whereEqualTo("userID", userID).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(!task.getResult().isEmpty()){

                                tv_test.setVisibility(View.GONE);
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("whynotnn", "findByQuery => "+document.getId() + " => " + document.getData());
                                    String ll = document.get("postID").toString();

                                    db.collection("posts").document(ll).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            if(task.isSuccessful()&&task.getResult().exists()){

                                                PostInfo postInfo;
                                                postInfo = new PostInfo(task.getResult().toObject(PostInfo.class), task.getResult().getId());

                                                arrayList.add(postInfo);
                                                postAdapter.notifyDataSetChanged();
                                            } else {
                                                Log.d("실행실패", "Error getting documents: ", task.getException());
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });


                                }
                            }else {
                                tv_test.setText("찜한 상품이 없습니다.");
                            }


                        } else {
                            Log.d("실행실패", "Error getting documents: ", task.getException());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("gggg", "findByQuery => ");

            }
        });

        btn_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                //intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, GALLERY_CODE);
            }
        });

        btn_changeprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StorageReference storageRef = storage.getReference();
                if (imagePath != null) {
                    Uri file = Uri.fromFile(new File(imagePath));
                    final StorageReference riversRef = storageRef.child("profile/" + file.getLastPathSegment());
                    UploadTask uploadTask = riversRef.putFile(file);

                    Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw task.getException();
                            }

                            // Continue with the task to get the download URL
                            return riversRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                downloadURL = downloadUri.toString();

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                FirebaseUser uuser = FirebaseAuth.getInstance().getCurrentUser();


                                Map<String, Object> map = new HashMap<>();
                                map.put("imageURL", downloadURL);
                                myImgURL = downloadURL;

                                db.collection("users").document(userID).update(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(getApplicationContext(),"프로필이 변경되었습니다.",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });





        //올려놓은 물품
        db.collection("posts").whereEqualTo("postuserID", userID).whereEqualTo("yorn", "거래가능").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(!task.getResult().isEmpty()){
                                tv_test.setVisibility(View.GONE);
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("whynotnn", "findByQuery => "+document.getId() + " => " + document.getData());

                                            PostInfo postInfo2;
                                            postInfo2 = new PostInfo(document.toObject(PostInfo.class), document.getId());

                                            arrayList2.add(postInfo2);
                                            postAdapter2.notifyDataSetChanged();
                                        }
                                tv_test3.setVisibility(View.GONE);
                            }else {
                                //tv_test3.setVisibility(View.GONE);
                            }


                        } else {
                            Log.d("실행실패", "Error getting documents: ", task.getException());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("gggg", "findByQuery => ");

            }
        });


        btn_chatlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityChat.class);
                intent.putExtra("into", "myhome");
                startActivity(intent);
            }
        });


        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //MainActivity main_context = (MainActivity)MainActivity.mainActivity;
                //main_context.finish();
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(MyHomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        btn_changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                firebaseAuth.sendPasswordResetEmail(firebaseAuth.getCurrentUser().getEmail().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "사용자 이메일로 비밀번호 변경을 위한 메일이 발송되었습니다.", Toast.LENGTH_SHORT).show();
                        }else {

                        }
                    }
                });
            }
        });
    }



/*
    public String[] getBookedlist(){
        final int i=0;
        final String[] list = new String[20];
        db.collection("booked").whereEqualTo("uesrID", userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                for(final QueryDocumentSnapshot document : task.getResult()) {
                    Log.d("찜목록 테스트", "findByQuery => "+document.getId() + " => " + document.getData());
                    list[i] = document.get("postID").toString();
                    db.collection("posts").document(list[i]).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            PostInfo postInfo;
                            postInfo = new PostInfo(task.getResult().toObject(PostInfo.class), list[i]);
                            arrayList.add(postInfo);
                            postAdapter.notifyDataSetChanged();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
        return list;
    }
    */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_CODE&&data!=null) {
            Uri image = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), image);
                        profile.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    imagePath = getPath(data.getData());
                    //          File f = new File(getPath(data.getData()));
                    //        photo[0].setImageURI(Uri.fromFile(f));
        }
    }





    public String getPath (Uri uri){ //39. 갤러리에서 인텐트로 받은 이미지의 주소(uri)는 한번에 안받아지므로 따로 정의해주는 매쏘드
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this, uri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(index);
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
