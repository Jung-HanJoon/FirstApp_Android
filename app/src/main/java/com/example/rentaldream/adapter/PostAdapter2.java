package com.example.rentaldream.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentaldream.model.PostInfo;
import com.example.rentaldream.R;
import com.example.rentaldream.ui.PostViewActivity;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PostAdapter2 extends RecyclerView.Adapter<PostAdapter2.CustomViewHoler> {

    private ArrayList<PostInfo> arrayList;
    private Context mContext;

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public PostAdapter2(Context mContext, ArrayList<PostInfo> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;

        Fresco.initialize(mContext);
    }

    @NonNull
    @Override
    public PostAdapter2.CustomViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_post_horizen, parent, false);
        CustomViewHoler holder = new CustomViewHoler(view);

        return holder;
    }

    Bitmap bitmap;
    @Override
    public void onBindViewHolder(@NonNull final PostAdapter2.CustomViewHoler holder, final int position) {

        //holder.iv_profile.setImageResource(arrayList.get(position).getImageURL());
        holder.tv_title.setText(arrayList.get(position).getProduct());
        final String postID = arrayList.get(position).getPostId();

        if(arrayList.get(position).getImageURL() != null){
            //setImageSrc(holder.iv_profile, position);
            Uri uri = Uri.parse(arrayList.get(position).getImageURL());
            holder.iv_profile.setImageURI(uri);
        }else {
            holder.iv_profile.setImageResource(R.drawable.ic_rental);
        }

        //holder.itemView.setTag(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.getAdapterPosition();
                //Toast.makeText(mContext, postID,Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext, PostViewActivity.class);

                intent.putExtra("postID", postID);
                mContext.startActivity(intent);
            }
        });

        try {
            if(arrayList.get(position).getReturndate()>0){
                long returndate = arrayList.get(position).getReturndate();
                //long now = new Date(returndate).getTime();
                long now = System.currentTimeMillis();
                SimpleDateFormat ddays = new SimpleDateFormat("D");

                Date dates = new Date(returndate-now);
                String dday = ddays.format(dates);
                holder.tv_dayleft.setText("D-"+dday);
            }else {
                Log.i("반납일 = ", String.valueOf(arrayList.get(position).getReturndate()));
                holder.tv_dayleft.setVisibility(View.GONE);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
/*
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                remove(holder.getAdapterPosition());
                return true;
            }
        });

 */
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


    public void setImageSrc(ImageView imageView, final int position) {
        //ImageView url 설정
        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(arrayList.get(position).getImageURL());

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


    public class CustomViewHoler extends RecyclerView.ViewHolder {

        private SimpleDraweeView iv_profile;
        private TextView tv_title;
        private TextView tv_dayleft;

        public CustomViewHoler(@NonNull View itemView) {
            super(itemView);
            this.iv_profile=(SimpleDraweeView)itemView.findViewById(R.id.iv_product);
            this.tv_title = (TextView)itemView.findViewById(R.id.tv_title);
            this.tv_dayleft = (TextView)itemView.findViewById(R.id.tv_dayleft);
        }
    }
}

