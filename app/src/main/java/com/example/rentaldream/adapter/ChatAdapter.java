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
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentaldream.model.ChatInfo;
import com.example.rentaldream.R;
import com.example.rentaldream.ui.ActivityUserInfo;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

import java.text.DecimalFormat;
import java.util.ArrayList;


import static com.example.rentaldream.ui.FirstShowActivity.myImgURL;
import static com.example.rentaldream.ui.FirstShowActivity.myUid;
import static com.example.rentaldream.ui.FirstShowActivity.myprofile;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.CustomViewHoler> {

        private ArrayList<ChatInfo> arrayList;
        private Context mContext;


    public ChatAdapter(Context mContext, ArrayList<ChatInfo> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
            Fresco.initialize(mContext);
        }

        @NonNull
        @Override
        public ChatAdapter.CustomViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_chat2, parent, false);
            ChatAdapter.CustomViewHoler holder = new ChatAdapter.CustomViewHoler(view);


            return holder;
        }

        Bitmap bitmap;
        @Override
        public void onBindViewHolder(@NonNull final ChatAdapter.CustomViewHoler holder, int position) {
            DecimalFormat formatter = new DecimalFormat("###,###");
            //holder.iv_profile.setImageResource(arrayList.get(position).getImageURL());



            holder.iv_profile.setBackground(new ShapeDrawable(new OvalShape()));
            holder.iv_profile2.setBackground(new ShapeDrawable(new OvalShape()));

            if(Build.VERSION.SDK_INT >= 21) {
                holder.iv_profile.setClipToOutline(true);
                holder.iv_profile2.setClipToOutline(true);
            }

            if(arrayList.get(position).getSendUid().equals(myUid)){
                holder.tv_name.setText(arrayList.get(position).getSendName());
                holder.tv_content.setText(arrayList.get(position).getMsg());
                if(myprofile!=null){
                    Uri uri = Uri.parse(myImgURL);
                    holder.iv_profile.setImageURI(uri);
                }
                holder.ll2.setVisibility(View.GONE);
            }else{
                if(arrayList.get(position).getTargetImg()!=null){
                    Uri uri = Uri.parse(arrayList.get(position).getSendImg());
                    holder.iv_profile2.setImageURI(uri);
                }
                holder.tv_name2.setText(arrayList.get(position).getSendName());
                holder.tv_content2.setText(arrayList.get(position).getMsg());
                /*
                if(myprofile!=null) {
                    holder.iv_profile.setImageBitmap(myprofile);
                }

                 */
                holder.ll1.setVisibility(View.GONE);
            }


            final String postID = arrayList.get(position).getPostId();
/*
            if(arrayList.get(position).getImageURL() != null){
                setImageSrc(holder.iv_profile, position);
            }else {
                holder.iv_profile.setImageResource(R.drawable.ic_rental);
            }
            */




            //holder.itemView.setTag(position);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.getAdapterPosition();
                    //Toast.makeText(mContext, postID,Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(mContext, ActivityUserInfo.class);
                    intent.putExtra("target", arrayList.get(holder.getAdapterPosition()).getSendUid());
                    mContext.startActivity(intent);
                }
            });
/*
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    remove(holder.getAdapterPosition());
                    return true;
                }
            });

 */
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

        public class CustomViewHoler extends RecyclerView.ViewHolder {

            private SimpleDraweeView iv_profile;
            private SimpleDraweeView iv_profile2;
            private TextView tv_name;
            private TextView tv_name2;
            private TextView tv_content;
            private TextView tv_content2;
            private LinearLayout ll1;
            private LinearLayout ll2;



            public CustomViewHoler(@NonNull View itemView) {
                super(itemView);
                this.iv_profile=(SimpleDraweeView)itemView.findViewById(R.id.iv_product);
                this.tv_name = (TextView)itemView.findViewById(R.id.tv_name);
                this.tv_content = (TextView)itemView.findViewById(R.id.tv_content);
                this.iv_profile2=(SimpleDraweeView)itemView.findViewById(R.id.iv_profile);
                this.tv_name2 = (TextView)itemView.findViewById(R.id.tv_name2);
                this.tv_content2 = (TextView)itemView.findViewById(R.id.tv_content2);
                this.ll1 = (LinearLayout)itemView.findViewById(R.id.ll1);
                this.ll2 = (LinearLayout)itemView.findViewById(R.id.ll2);
            }
        }
    }



