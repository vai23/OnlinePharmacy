package com.example.vaibhavmishra.onlinepharmacy;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;

public class EditStaffActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private LinearLayout linearLayout;
    private ArrayList<StaffMember> staffMemberList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_staff);

        staffMemberList=new ArrayList<>(OnlinePharmacy.staffMemberList);
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("staffmembers");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                StaffMember staffMember=dataSnapshot.getValue(StaffMember.class);
                if(!isAlreadyPresent(staffMember)) {
                    staffMemberList.add(staffMember);
                    addStaffMembers(staffMember);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        toolbar=findViewById(R.id.edit_staff_activity_toolbar_t);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Edit staff members");

        linearLayout=findViewById(R.id.edit_staff_list);
        addStaffMembers();
    }

    private boolean isAlreadyPresent(StaffMember value) {
        for(StaffMember staff:staffMemberList.toArray(new StaffMember[]{})) {
            if(staff.getUsername().equals(value.getUsername()))
                return true;
        }
        return false;
    }

    private void addStaffMembers() {

        String currentUserUID=getIntent().getStringExtra("UID");

        for(final StaffMember staff : staffMemberList.toArray(new StaffMember[]{})) {
            if(staff.getownerUID().equals(currentUserUID)) {
                View view=LayoutInflater.from(this).inflate(R.layout.staff_member_edit,null);
                Space space=new Space(this);
                space.setMinimumHeight(24);

                final LinearLayout ll=new LinearLayout(this);
                ll.setOrientation(LinearLayout.VERTICAL);

                final ImageButton delete=view.findViewById(R.id.staff_delete);
                final TextView username=view.findViewById(R.id.staff_username);
                TextView name=view.findViewById(R.id.staff_name);
                username.setText(username.getText().toString()+" "+staff.getUsername());
                name.setText(name.getText().toString()+" "+staff.getName());
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog=new Dialog(EditStaffActivity.this);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.setContentView(R.layout.dialog_layout);
                        dialog.setTitle("dialog");
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                        TextView cancel=dialog.findViewById(R.id.cancel_dialog);
                        TextView ok=dialog.findViewById(R.id.ok_dialog);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                delete(ll,view,staff);
                                dialog.dismiss();
                                Toast.makeText(EditStaffActivity.this,username.getText().toString()+
                                        " deleted successfuly",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                ll.addView(view);
                ll.addView(space);
                linearLayout.addView(ll);
            }
        }
    }

    private void delete(LinearLayout ll, View view,StaffMember staff) {
        String id=staff.getID();
        DatabaseReference reference=FirebaseDatabase.getInstance()
                .getReference("staffmembers");
        reference.child(id).setValue(null);
        linearLayout.removeView(ll);
    }

    private void addStaffMembers(final StaffMember staff) {

        String currentUserUID=getIntent().getStringExtra("UID");
            if(staff.getownerUID().equals(currentUserUID)) {
                View view=LayoutInflater.from(this).inflate(R.layout.staff_member_edit,null);
                Space space=new Space(this);
                space.setMinimumHeight(24);

                final LinearLayout ll=new LinearLayout(this);
                ll.setOrientation(LinearLayout.VERTICAL);

                final ImageButton delete=view.findViewById(R.id.staff_delete);
                final TextView username=view.findViewById(R.id.staff_username);
                TextView name=view.findViewById(R.id.staff_name);
                username.setText(username.getText().toString()+" "+staff.getUsername());
                name.setText(name.getText().toString()+" "+staff.getName());
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog=new Dialog(EditStaffActivity.this);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.setContentView(R.layout.dialog_layout);
                        dialog.setTitle("dialog");
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                        TextView cancel=dialog.findViewById(R.id.cancel_dialog);
                        TextView ok=dialog.findViewById(R.id.ok_dialog);
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                delete(ll,view,staff);
                                dialog.dismiss();
                                Toast.makeText(EditStaffActivity.this,username.getText().toString()+
                                        " deleted successfuly",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                ll.addView(view);
                ll.addView(space);
                linearLayout.addView(ll);
        }
    }

}
