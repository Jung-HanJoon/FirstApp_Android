package com.example.rentaldream.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentaldream.R;
import com.example.rentaldream.model.RateInfo;
import com.example.rentaldream.ui.ActivityUserInfo;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

public class RateAdapter extends RecyclerView.Adapter<RateAdapter.CustomViewHoler> {

    private ArrayList<RateInfo> arrayList;
    private Context mContext;

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public RateAdapter(ArrayList<RateInfo> arrayList, Context mContext) {
        this.arrayList = arrayList;
        this.mContext = mContext;

        Fresco.initialize(mContext);
    }

    @NonNull
    @Override
    public RateAdapter.CustomViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_star, parent, false);
        RateAdapter.CustomViewHoler holder = new RateAdapter.CustomViewHoler(view);

        return holder;
    }

    Bitmap bitmap;



    @Override
    public void onBindViewHolder(@NonNull final RateAdapter.CustomViewHoler holder, final int position) {

        //holder.iv_profile.setImageResource(arrayList.get(position).getIv_profile());
        holder.tv_name.setText(arrayList.get(position).getPostername());
        holder.tv_content.setText(arrayList.get(position).getContent());
        holder.tv_date.setText(arrayList.get(position).getDate());
        holder.ratingbar.setRating(arrayList.get(position).getRate());



        if (arrayList.get(position).getPosterImg() != null) {
            //setImageSrc(holder.iv_profile, position);
            Uri uri = Uri.parse(arrayList.get(position).getPosterImg());
            holder.iv_profile.setImageURI(uri);
        }
        holder.iv_profile.setBackground(new ShapeDrawable(new OvalShape()));
        if (Build.VERSION.SDK_INT >= 21) {
            holder.iv_profile.setClipToOutline(true);
        }


        //holder.itemView.setTag(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ActivityUserInfo.class);
                intent.putExtra("target", arrayList.get(position).getPoster());
                mContext.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return (null != arrayList ? arrayList.size() : 0);
    }

/*
    public void remove(int position) {
        try {
            arrayList.remove(position);
            notifyItemRemoved(position);
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }
*/
/*
    public void setImageSrc(ImageView imageView, final int position) {
        //ImageView url 설정
        Thread mThread = new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(arrayList.get(position).getPosterImg());

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
    public class CustomViewHoler extends RecyclerView.ViewHolder {

        protected SimpleDraweeView iv_profile;
        protected TextView tv_name;
        private TextView tv_content;
        private RatingBar ratingbar;
        private TextView tv_date;

        public CustomViewHoler(@NonNull View itemView) {
            super(itemView);
            this.iv_profile = (SimpleDraweeView) itemView.findViewById(R.id.iv_profile);
            this.tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            this.tv_content = (TextView) itemView.findViewById(R.id.tv_content);
            this.tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            this.ratingbar = (RatingBar) itemView.findViewById(R.id.ratingbar);
        }
    }
}

