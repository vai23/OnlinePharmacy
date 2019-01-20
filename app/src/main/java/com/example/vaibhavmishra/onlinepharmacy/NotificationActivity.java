package com.example.vaibhavmishra.onlinepharmacy;

import android.drm.DrmStore;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import java.util.ArrayList;

public class NotificationActivity extends AppCompatActivity {

    private LinearLayout linearLayout;
    private ArrayList<Wrap> wrapList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        wrapList=new ArrayList<>(OnlinePharmacy.wrapList);

        Toolbar toolbar=findViewById(R.id.notification_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Notifications");
        actionBar.setDisplayHomeAsUpEnabled(true);

        linearLayout=findViewById(R.id.notification_linerarLayout);
        Space space=new Space(this);
        space.setMinimumHeight(16);
        linearLayout.addView(space);

        for(Wrap wrap : wrapList) {
            View view=LayoutInflater.from(this).inflate(R.layout.notification,null);
            TextView heading=view.findViewById(R.id.heading);
            TextView desc=view.findViewById(R.id.daysRem);

            heading.setText(wrap.heading);
            desc.setText(wrap.description);

            space=new Space(this);
            space.setMinimumHeight(16);

            linearLayout.addView(view);
            linearLayout.addView(space);
        }
    }
}
