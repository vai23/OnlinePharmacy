package com.example.vaibhavmishra.onlinepharmacy;

import android.app.IntentService;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddStaffMemberActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button addStaffMember;
    private EditText name,address,age,username,password;
    private ProgressDialog mProgressDialog;
    private ArrayList<StaffMember> staffMemberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff_member);

        toolbar=findViewById(R.id.add_staff_activity_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Add Staff Member");
        actionBar.setDisplayHomeAsUpEnabled(true);

        addStaffMember=findViewById(R.id.addstaff_to_database);
        name=findViewById(R.id.name_of_staff);
        address=findViewById(R.id.address_of_staff);
        age=findViewById(R.id.age_of_staff);
        username=findViewById(R.id.username_staff);
        password=findViewById(R.id.password_staff);

        addStaffMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStaffMember(view);
            }
        });
    }

    private void addStaffMember(View view) {
        showProgressDialog();
        if(!isValidInformation()) {
            hideProgressDialog();
            Toast.makeText(this,"Fill all fields",Toast.LENGTH_SHORT).show();
            return;
        }

        if(isUsernameAlreadyExist()) {
            hideProgressDialog();
            Toast.makeText(this,"Username already existed",Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Intent intent=getIntent();
            String uid=intent.getStringExtra("UID");
            StaffMember staffMember=new StaffMember(uid,name.getText().toString(),
                    address.getText().toString(),Integer.parseInt(age.getText().toString()),
                    username.getText().toString(),password.getText().toString());

            DatabaseReference ref=FirebaseDatabase.getInstance().getReference("staffmembers");
            String id=ref.push().getKey();
            staffMember.setID(id);
            ref.child(id).setValue(staffMember);
            hideProgressDialog();
        }
        catch (Exception e) {
            hideProgressDialog();
            Toast.makeText(this,"ERROR",Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this,"Staff member added successfuly",Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,OnlinePharmacy.class);
        startActivity(intent);
    }

    private boolean isUsernameAlreadyExist() {
        staffMemberList=new ArrayList<>(OnlinePharmacy.staffMemberList);
        for(StaffMember staff : staffMemberList.toArray(new StaffMember[]{})) {
            if(staff.getownerUID().equals(getIntent().getStringExtra("UID"))
               && staff.getUsername().equals(username.getText().toString())) {
                return true;
            }
        }
        return false;
    }

    private boolean isValidInformation() {
        return !(TextUtils.isEmpty(name.getText()) ||
                TextUtils.isEmpty(address.getText()) ||
                TextUtils.isEmpty(age.getText()) ||
                TextUtils.isEmpty(username.getText()) ||
                TextUtils.isEmpty(password.getText()));
    }

    private void showProgressDialog() {
        if(mProgressDialog == null) {
            mProgressDialog=new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Please wait...");
        }
    }

    private void hideProgressDialog() {
        if(mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
