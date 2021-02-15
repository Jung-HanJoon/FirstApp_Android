package com.example.rentaldream.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentaldream.model.ChatListInfo;
import com.example.rentaldream.R;
import com.example.rentaldream.ui.ActivityTalk;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.MetadataChanges;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

import static com.example.rentaldream.ui.FirstShowActivity.myUid;


public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.CustomViewHoler> {

    private ArrayList<ChatListInfo> arrayList;
    private Context mContext;
    private FirebaseFirestore db  = FirebaseFirestore.getInstance();
    Bitmap bitmap;


    public ChatListAdapter(Context mContext, ArrayList<ChatListInfo> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ChatListAdapter.CustomViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chatlist, parent, false);
        CustomViewHoler holder = new CustomViewHoler(view);

        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ChatListAdapter.CustomViewHoler holder, final int position) {

        holder.iv_profile.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT >= 21) {
            holder.iv_profile.setClipToOutline(true);
        }

        if(arrayList.get(position).getSeller().equals(myUid)){
            holder.tv_name.setText(arrayList.get(position).getBuyer_name());
            if(arrayList.get(position).getChecks()!=0){
                holder.notice.setText(String.valueOf(arrayList.get(position).getChecks()));
                holder.notice.setVisibility(View.VISIBLE);
            }else {
                holder.notice.setVisibility(View.INVISIBLE);
            }
        }else{
            holder.tv_name.setText(arrayList.get(position).getSeller_name());
            if(arrayList.get(position).getCheckb()!=0) {
                holder.notice.setText(String.valueOf(arrayList.get(position).getCheckb()));
                holder.notice.setVisibility(View.VISIBLE);
            }else {
                holder.notice.setVisibility(View.INVISIBLE);
            }
        }
        if(arrayList.get(position).getState().equals("deny")){
            holder.state.setText("대여신청 거절");
        }else if(arrayList.get(position).getState().equals("request")){
            holder.state.setText("대여수락 대기중");
            holder.state.setBackgroundResource(R.drawable.state4);
        }else if(arrayList.get(position).getState().equals("contract")){
            holder.state.setText("거래중");
            holder.state.setBackgroundResource(R.drawable.state5);
        }else if(arrayList.get(position).getState().equals("finish")){
            holder.state.setText("거래종료");
        }else {
            holder.state.setVisibility(View.INVISIBLE);
        }


        holder.tv_title.setText(arrayList.get(position).getTitle());

        final String postID = arrayList.get(position).getPostID();
        //setImageSrc(holder.iv_profile, position);
        if(myUid.equals(arrayList.get(position).getSeller())){
            Uri uri = Uri.parse(arrayList.get(position).getBuyerImg());
            holder.iv_profile.setImageURI(uri);
        }else {
            Uri uri = Uri.parse(arrayList.get(position).getSellerImg());
            holder.iv_profile.setImageURI(uri);
        }

        final DocumentReference erer = db.collection("chat").document(arrayList.get(position).getChatRoomId());


        erer.addSnapshotListener(MetadataChanges.INCLUDE, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {

                if(arrayList.get(position).getSeller().equals(myUid)){

                    if(snapshot.getLong("checks")!=0){
                        holder.notice.setText(String.valueOf(snapshot.get("checks")));
                        holder.notice.setVisibility(View.VISIBLE);
                    }else {
                        holder.notice.setVisibility(View.INVISIBLE);
                    }
                }else{

                    if(snapshot.getLong("checkb")!=0) {
                        holder.notice.setText(String.valueOf(snapshot.get("checkb")));
                        holder.notice.setVisibility(View.VISIBLE);
                    }else {
                        holder.notice.setVisibility(View.INVISIBLE);
                    }
                }
                if(snapshot.get("state").toString().equals("deny")){
                    holder.state.setText("대여신청 거절");
                }else if(snapshot.get("state").toString().equals("request")){
                    holder.state.setText("대여수락 대기중");
                }else if(snapshot.get("state").toString().equals("contract")){
                    holder.state.setText("거래중");
                }else if(snapshot.get("state").toString().equals("finish")){
                    holder.state.setText("거래종료");
                }else {
                    holder.state.setVisibility(View.INVISIBLE);
                }
            }
        });





        holder.itemView.setTag(position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.getAdapterPosition();

                if (arrayList.get(position).getSeller().equals(myUid)){
                    db.collection("chat").document(arrayList.get(position).getChatRoomId()).update("checks",0).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(mContext, ActivityTalk.class);
                            intent.putExtra("postID", postID);
                            intent.putExtra("cId", arrayList.get(position).getChatRoomId());
                            intent.putExtra("sellerID", arrayList.get(position).getSeller());
                            intent.putExtra("saln ", arrayList.get(position).getSeller_name()+"");
                            Log.i("sellername", arrayList.get(position).getSeller_name().toString()+"");
                            intent.putExtra("sImg", arrayList.get(position).getSellerImg());
                            intent.putExtra("buyer", arrayList.get(position).getBuyer());
                            intent.putExtra("bName", arrayList.get(position).getBuyer_name().toString());
                            intent.putExtra("bImg", arrayList.get(position).getBuyerImg());
                            intent.putExtra("title", arrayList.get(position).getTitle());
                            //intent.putExtra("aImg", bitmap);
                            intent.putExtra("test", arrayList.get(position));

                            mContext.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
                        }
                    });
                }else{
                    db.collection("chat").document(arrayList.get(position).getChatRoomId()).update("checkb",0).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(mContext, ActivityTalk.class);
                            intent.putExtra("postID", postID);
                            intent.putExtra("cId", arrayList.get(position).getChatRoomId());
                            intent.putExtra("sellerID", arrayList.get(position).getSeller());
                            intent.putExtra("saln ", arrayList.get(position).getSeller_name()+"");
                            Log.i("sellername", arrayList.get(position).getSeller_name().toString()+"");
                            intent.putExtra("sImg", arrayList.get(position).getSellerImg());
                            intent.putExtra("buyer", arrayList.get(position).getBuyer());
                            intent.putExtra("bName", arrayList.get(position).getBuyer_name().toString());
                            intent.putExtra("bImg", arrayList.get(position).getBuyerImg());
                            intent.putExtra("title", arrayList.get(position).getTitle());
                            //intent.putExtra("aImg", bitmap);
                            intent.putExtra("test", arrayList.get(position));

                            mContext.startActivity(intent.addFlags(FLAG_ACTIVITY_NEW_TASK));
                        }
                    });
                }

            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                remove(holder.getAdapterPosition());
                return true;
            }
        });
        holder.setIsRecyclable(false);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }


    public void remove(int position){
        try{
            arrayList.remove(position);
            notifyItemRemoved(position);
        }catch (IndexOutOfBoundsException ex){
            ex.printStackTrace();
        }
    }


        public Bitmap setImageSrc(ImageView imageView, final int position) {
            //ImageView url 설정
            Thread mThread = new Thread() {
                @Override
                public void run() {
                    try {
                        URL url;
                        if(myUid.equals(arrayList.get(position).getSeller())){
                            url = new URL(arrayList.get(position).getBuyerImg());
                        }else {
                            url = new URL(arrayList.get(position).getSellerImg());
                        }

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
            return bitmap;
        }


    public class CustomViewHoler extends RecyclerView.ViewHolder {

        private SimpleDraweeView iv_profile;
        private TextView tv_name;
        private TextView tv_title;
        private TextView notice;
        private TextView state;

        public CustomViewHoler(@NonNull View itemView) {
            super(itemView);
            this.iv_profile=(SimpleDraweeView)itemView.findViewById(R.id.iv_product);
            this.tv_name = (TextView)itemView.findViewById(R.id.tv_name);
            this.tv_title = (TextView)itemView.findViewById(R.id.tv_title);
            this.notice=(TextView)itemView.findViewById(R.id.notice);
            this.state=(TextView)itemView.findViewById(R.id.state);
        }
    }
}



