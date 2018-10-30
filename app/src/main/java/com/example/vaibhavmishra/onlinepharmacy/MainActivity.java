package com.example.vaibhavmishra.onlinepharmacy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class MainActivity extends AppCompatActivity {

    private CheckBox owner, staff;
    private boolean isOwnerChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        owner=findViewById(R.id.owner);
        staff=findViewById(R.id.staff);
        owner.setChecked(true);
        isOwnerChecked=true;
        owner.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                staff.setChecked(!owner.isChecked());
            }
        });
        staff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                owner.setChecked(!staff.isChecked());
            }
        });
    }

    public void signupPressed(View view) {
        Intent intent=new Intent(this,SignupPage.class);
        startActivity(intent);
    }

    public void login(View view) {
        Intent intent=new Intent(this,OnlinePharmacy.class);
        startActivity(intent);
    }
}
