package com.example.rentaldream.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentaldream.R;

public class ActivityRentalList extends AppCompatActivity {


    RecyclerView rv_rentallist;
    Button btn_goback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rentallist);

        rv_rentallist = (RecyclerView)findViewById(R.id.rv_rentallist);
        btn_goback = (Button)findViewById(R.id.btn_goback);

        btn_goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }
}
