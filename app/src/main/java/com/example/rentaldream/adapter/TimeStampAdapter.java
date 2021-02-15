package com.example.rentaldream.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentaldream.R;
import com.example.rentaldream.model.TimeStampInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;


    public class TimeStampAdapter extends RecyclerView.Adapter<TimeStampAdapter.CustomViewHoler> {

        private ArrayList<TimeStampInfo> arrayList;
        private Context mContext;



        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }


        public TimeStampAdapter(Context mContext, ArrayList<TimeStampInfo> arrayList) {
            this.mContext = mContext;
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public TimeStampAdapter.CustomViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_timestamp, parent, false);
            TimeStampAdapter.CustomViewHoler holder = new TimeStampAdapter.CustomViewHoler(view);

            return holder;
        }


        @Override
        public void onBindViewHolder(@NonNull final TimeStampAdapter.CustomViewHoler holder, final int position) {
            DecimalFormat formatter = new DecimalFormat("###,###");
            //holder.iv_profile.setImageResource(arrayList.get(position).getImageURL());

            holder.tv_time.setText(arrayList.get(position).getTime());
            holder.tv_message.setText(arrayList.get(position).getMessage());




        }

        @Override
        public int getItemCount() {
            return (null != arrayList ? arrayList.size() : 0);
        }


        public class CustomViewHoler extends RecyclerView.ViewHolder {

            private TextView tv_time;
            private TextView tv_message;



            public CustomViewHoler(@NonNull View itemView) {
                super(itemView);
                this.tv_time=(TextView)itemView.findViewById(R.id.tv_time);
                this.tv_message = (TextView)itemView.findViewById(R.id.tv_message);
            }
        }
    }





