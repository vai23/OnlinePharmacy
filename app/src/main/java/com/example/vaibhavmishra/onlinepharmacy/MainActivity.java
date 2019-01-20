package com.example.vaibhavmishra.onlinepharmacy;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private CheckBox owner, staff;
    private boolean isOwnerChecked;
    private ProgressDialog mProgressDialog;
    private EditText username, password;
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReferenceStaff;
    private DatabaseReference databaseReferenceMed;
    public static ArrayList<User> usersList;
    public static ArrayList<StaffMember> staffMemberList;
    public static ArrayList<Medicine> medicineMemberList;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        owner=findViewById(R.id.owner);
        staff=findViewById(R.id.staff);
        staff.setChecked(true);
        isOwnerChecked=false;
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

        databaseReference=FirebaseDatabase.getInstance().getReference("users");
        usersList=new ArrayList<>();
        databaseReferenceStaff=FirebaseDatabase.getInstance().getReference().child("staffmembers");
        staffMemberList=new ArrayList<>();
        databaseReferenceMed=FirebaseDatabase.getInstance().getReference().child("medicines");
        medicineMemberList=new ArrayList<>();
        auth=FirebaseAuth.getInstance();
    }

    private void addNotification() {
        Toast.makeText(this,"jhghj",Toast.LENGTH_SHORT).show();
        NotificationManager mNotificationManager;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, "notify_001");
        Intent ii = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        bigText.bigText("hfdefheeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeello");
        bigText.setBigContentTitle("Today's Bible Verse");
        bigText.setSummaryText("Text in detail");

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.ic_notifications_active_black_24dp);
        mBuilder.setContentTitle("Your Title");
        mBuilder.setContentText("Your text");
        mBuilder.setPriority(Notification.PRIORITY_MAX);
        mBuilder.setStyle(bigText);

        mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notify_001",
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            mNotificationManager.createNotificationChannel(channel);
        }

        mNotificationManager.notify(0, mBuilder.build());
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                usersList.clear();
                for(DataSnapshot snap:dataSnapshot.getChildren()) {
                    User user=snap.getValue(User.class);
                    usersList.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReferenceStaff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                staffMemberList.clear();
                for(DataSnapshot snap:dataSnapshot.getChildren()) {
                    StaffMember user=snap.getValue(StaffMember.class);
                    staffMemberList.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        databaseReferenceMed.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                medicineMemberList.clear();
                for(DataSnapshot snap:dataSnapshot.getChildren()) {
                    Medicine user=snap.getValue(Medicine.class);
                    medicineMemberList.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void signupPressed(View view) {
        Intent intent=new Intent(this,SignupPage.class);
        startActivity(intent);
    }

    public void login(View view) {
        if(owner.isChecked()) {
            showProgressDialog();

            String Email=getEmailOf(username.getText().toString());

            if(Email==null) {
                username.setText("");
                password.setText("");
                hideProgressDialog();
                Toast.makeText(MainActivity.this,"Try again",Toast.LENGTH_SHORT).show();
                return;
            }

            auth.signInWithEmailAndPassword(Email,password.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Intent intent=new Intent(MainActivity.this,OnlinePharmacy.class);
                                intent.putExtra("OWNER",owner.isChecked());
                                intent.putExtra("STAFF",staff.isChecked());
                                intent.putExtra("USERNAME",username.getText().toString());
                                intent.putExtra("PASSWORD",password.getText().toString());
                                startActivity(intent);
                            }
                            else {
                                username.setText("");
                                password.setText("");
                                hideProgressDialog();
                                Toast.makeText(MainActivity.this,"Invalid credentials",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
        else {
            showProgressDialog();

            String ownerUID="";
            boolean isValid=false;

            for(StaffMember s:staffMemberList) {
                if(s.getUsername().equals(username.getText().toString())
                        && s.getPassword().equals(password.getText().toString())) {
                    isValid=true;
                    ownerUID=s.getownerUID();
                    break;
                }
            }

            if(!isValid) {
                hideProgressDialog();
                Toast.makeText(this,"Invalid credentials",Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent=new Intent(this,OnlinePharmacy.class);
            intent.putExtra("OWNER",owner.isChecked());
            intent.putExtra("STAFF",staff.isChecked());
            intent.putExtra("USERNAME",username.getText().toString());
            intent.putExtra("PASSWORD",password.getText().toString());
            intent.putExtra("UID",ownerUID);
            startActivity(intent);
        }
    }

    private String getEmailOf(String s) {
        for(User user:usersList.toArray(new User[]{}))
        {
            if(user.getUsername().equals(s))
                return user.getEmail();
        }
        return null;
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Please wait...");
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    /*
        pd=new ProgressDialog(this);
        pd.setMessage("Please Wait...");
        pd.show();

        Intent intent=new Intent(this,OnlinePharmacy.class);
        intent.putExtra("OWNER",owner.isChecked());
        intent.putExtra("STAFF",staff.isChecked());
        startActivity(intent);
     */

}
