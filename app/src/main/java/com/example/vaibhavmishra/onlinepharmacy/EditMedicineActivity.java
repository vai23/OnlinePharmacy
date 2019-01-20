package com.example.vaibhavmishra.onlinepharmacy;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
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

import java.util.ArrayList;

public class EditMedicineActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private LinearLayout linearLayout;
    private ArrayList<Medicine> medicineList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_medicine);

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("medicines");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Medicine medicine=dataSnapshot.getValue(Medicine.class);
                if(!isAlreadyPresent(medicine)) {
                    medicineList.add(medicine);
                    addMedicine(medicine);
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

        toolbar=findViewById(R.id.edit_medicine_activity_toolbar_t);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Edit Medicine");
        actionBar.setDisplayHomeAsUpEnabled(true);

        medicineList=new ArrayList<>(OnlinePharmacy.medicineList);

        linearLayout=findViewById(R.id.edit_medicine_list);
        addMedicine();
    }

    private void setRipple(View v) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Drawable drawable=this.getDrawable(R.drawable.medicine_information_styler);
            RippleDrawable rippleDrawable=new RippleDrawable(getPressedState(R.color.colorPrimaryLight),drawable,null);
            v.setBackground(rippleDrawable);
        }
    }
    public static ColorStateList getPressedState(int pressedColor)
    {
        return new ColorStateList(new int[][]{ new int[]{}},new int[]{pressedColor});
    }

    private boolean isAlreadyPresent(Medicine value) {
        for(Medicine medicine:medicineList) {
            if(medicine.getName().equals(value.getName()))
                return true;
        }
        return false;
    }

    private void addMedicine(final Medicine medicine) {
        String currentUserUID=getIntent().getStringExtra("UID");

            if(medicine.getOwnerUID().equals(currentUserUID)) {
                View view=LayoutInflater.from(this).inflate(R.layout.medicine_edit,null);
                Space space=new Space(this);
                space.setMinimumHeight(24);

                final LinearLayout ll=new LinearLayout(this);
                ll.setOrientation(LinearLayout.VERTICAL);
                final ImageButton delete=view.findViewById(R.id.medicine_delete_button);
                final TextView name=view.findViewById(R.id.medicine_edit_name);
                TextView code=view.findViewById(R.id.medicine_edit_code);
                name.setText(medicine.getName());
                code.setText("Code: "+medicine.getCode());

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog=new Dialog(EditMedicineActivity.this);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.setContentView(R.layout.dialog_layout);
                        dialog.setTitle("dialog");
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                        TextView heading=dialog.findViewById(R.id.dialog_heading);
                        TextView content=dialog.findViewById(R.id.textView2);
                        TextView cancel=dialog.findViewById(R.id.cancel_dialog);
                        TextView ok=dialog.findViewById(R.id.ok_dialog);
                        heading.setText("Delete this medicine?");
                        content.setText("Are you sure you wanr to delete this medicine.");
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                delete(ll,view,medicine);
                                dialog.dismiss();
                                Toast.makeText(EditMedicineActivity.this,name.getText().toString()+
                                        " medicine deleted successfuly",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                //setRipple(view);

                ll.addView(view);
                ll.addView(space);
                linearLayout.addView(ll);
            }

    }

    private void addMedicine() {

        String currentUserUID=getIntent().getStringExtra("UID");

        for(final Medicine medicine : medicineList) {
            if(medicine.getOwnerUID().equals(currentUserUID)) {
                View view=LayoutInflater.from(this).inflate(R.layout.medicine_edit,null);
                Space space=new Space(this);
                space.setMinimumHeight(24);

                final LinearLayout ll=new LinearLayout(this);
                ll.setOrientation(LinearLayout.VERTICAL);
                final ImageButton delete=view.findViewById(R.id.medicine_delete_button);

                final TextView name=view.findViewById(R.id.medicine_edit_name);
                TextView code=view.findViewById(R.id.medicine_edit_code);
                name.setText(medicine.getName());
                code.setText("Code: "+medicine.getCode());

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Dialog dialog=new Dialog(EditMedicineActivity.this);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.setContentView(R.layout.dialog_layout);
                        dialog.setTitle("dialog");
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                        TextView heading=dialog.findViewById(R.id.dialog_heading);
                        TextView content=dialog.findViewById(R.id.textView2);
                        TextView cancel=dialog.findViewById(R.id.cancel_dialog);
                        TextView ok=dialog.findViewById(R.id.ok_dialog);
                        heading.setText("Delete this medicine?");
                        content.setText("Are you sure you wanr to delete this medicine.");
                        cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                            }
                        });
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                delete(ll,view,medicine);
                                dialog.dismiss();
                                Toast.makeText(EditMedicineActivity.this,name.getText().toString()+
                                        " medicine deleted successfuly",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                //setRipple(view);


                ll.addView(view);
                ll.addView(space);
                linearLayout.addView(ll);
            }
        }
    }

    private void delete(LinearLayout ll, View view, Medicine medicine) {
        String id=medicine.getID();
        DatabaseReference reference=FirebaseDatabase.getInstance()
                .getReference("medicines");
        for(Medicine med:medicineList) {
            if(med.getName().equals(medicine.getName())) {
                medicineList.remove(med);
            }
        }
        reference.child(id).setValue(null);
        linearLayout.removeView(ll);
    }
}
