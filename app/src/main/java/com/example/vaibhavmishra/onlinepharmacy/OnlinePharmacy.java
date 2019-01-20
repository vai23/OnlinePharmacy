package com.example.vaibhavmishra.onlinepharmacy;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.*;

public class OnlinePharmacy extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private LinearLayout linearLayout;
    private Button checkout;
    private boolean isOwnerChecked, isStaffChecked;
    private Toolbar toolbar;
    private int countBack=0;
    private FirebaseUser currentUser;
    private NavigationView navView;
    private EditText searchBar;
    private MenuItem notificationIcon;
    public static ArrayList<Medicine> medicineList;
    public static ArrayList<StaffMember> staffMemberList;
    private ArrayList<View> staffMedicineList;
    private ArrayList<View> staffRemovedMedicineList;
    private HashMap<View,Boolean> staffMedicineMap;
    public static ArrayList<Cart> cartList;
    public static ArrayList<Wrap> wrapList;
    private boolean changeNotificationIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_pharmacy);

        searchBar=findViewById(R.id.search_edittext);

        wrapList=new ArrayList<>();
        cartList=new ArrayList<>();
        staffMedicineMap=new HashMap<>();
        staffMedicineList=new ArrayList<>();
        staffRemovedMedicineList=new ArrayList<>();
        medicineList=new ArrayList<>(MainActivity.medicineMemberList);
        staffMemberList=new ArrayList<>(MainActivity.staffMemberList);

        changeNotificationIcon=false;

        final Intent intent=getIntent();
        isOwnerChecked=intent.getBooleanExtra("OWNER",false);
        isStaffChecked=intent.getBooleanExtra("STAFF",false);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout=findViewById(R.id.drawer_layout);
        checkout=findViewById(R.id.checkout_button);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkout_med(view);
            }
        });

        currentUser=FirebaseAuth.getInstance().getCurrentUser();

        navView=findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.account_item:
                        Toast.makeText(OnlinePharmacy.this,"Account Selected",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.logout_item:
                        FirebaseAuth.getInstance().signOut();
                        Intent intent1=new Intent(OnlinePharmacy.this,MainActivity.class);
                        intent1.putExtra("RESTART",true);
                        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        Toast.makeText(OnlinePharmacy.this,"Logged out successfuly",Toast.LENGTH_SHORT).show();
                        startActivity(intent1);
                        break;
                }

                drawerLayout.closeDrawer(Gravity.START);
                return true;
            }
        });

        linearLayout=findViewById(R.id.list_holder);
        if(isStaffChecked)      // If Staff is Checked------------------------
        {
            STAFF();
        }
        else {
            try {
                checkForExpiry();
            }catch (Exception e) {}
            OWNER();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(wrapList.size()>0 && isOwnerChecked) {
            invalidateOptionsMenu();
            menu.findItem(R.id.notification).setIcon(R.drawable.ic_notifications_active_red_24dp);
        }
        if(changeNotificationIcon) {
            menu.findItem(R.id.notification).setIcon(R.drawable.ic_notifications);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    private void addNotification(String bigTextSt,long drem,int i) {
        NotificationManager mNotificationManager;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, "notify_001");
        Intent ii = new Intent(this, OnlinePharmacy.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, ii, 0);

        NotificationCompat.BigTextStyle bigText = new NotificationCompat.BigTextStyle();
        //bigText.bigText(bigTextSt);
        bigText.setBigContentTitle(bigTextSt);
        //bigText.setSummaryText();

        mBuilder.setContentIntent(pendingIntent);
        mBuilder.setSmallIcon(R.drawable.ic_notifications_active_black_24dp);
        mBuilder.setContentTitle("Your Title");
        mBuilder.setContentText(""+drem+" days remaining before expiring.");
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

        mNotificationManager.notify(i, mBuilder.build());
    }


    private void checkForExpiry() throws Exception {
        int i=0;
        for(Medicine med : medicineList) {
            SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
            String curDate=format.format(new Date());
            String expDate=med.getExpiryDate();

            long diff=Math.abs(format.parse(curDate).getTime()-(format.parse(expDate).getTime()));
            long days=diff/(1000*60*60*24);

            if(days <= 10) {
                String bigText=med.getName()+" is about to expire";
                Wrap wrap=new Wrap();
                wrap.heading=bigText;
                wrap.description=""+days+" days remaining before expiring";
                wrapList.add(wrap);
                addNotification(bigText,days,i);
                i++;
            }
        }
    }

    private void removeStaffMember(View view) {
        Intent intent=new Intent(this,EditStaffActivity.class);
        intent.putExtra("UID",currentUser.getUid());
        //Toast.makeText(this,""+staffMemberList.size(),Toast.LENGTH_SHORT).show();
        startActivity(intent);
    }

    private void editMedicine(View view) {
        //showNotification();
        Intent intent=new Intent(this,EditMedicineActivity.class);
        intent.putExtra("UID",currentUser.getUid());
        startActivity(intent);
    }

    private void addStaffMember(View view) {
        Intent intent=new Intent(this,AddStaffMemberActivity.class);
        intent.putExtra("UID",currentUser.getUid());
        startActivity(intent);
    }

    private void addMedicine(View view) {
        Intent intent=new Intent(this,AddMedicineForm.class);
        intent.putExtra("UID",currentUser.getUid());
        startActivity(intent);
    }

    private void checkout_med(View view) {
        if(cartList.isEmpty()) {
            Toast.makeText(this,"There are no items in the cart",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent=new Intent(this,Bill.class);
        startActivity(intent);
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

    @Override
    public boolean onCreateOptionsMenu(Menu m)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.pharmacy_menu,m);
        notificationIcon=m.findItem(R.id.notification);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.notification:
                if(wrapList.size()>0) {
                    Intent intent=new Intent(this,NotificationActivity.class);
                    changeNotificationIcon=true;
                    startActivity(intent);
                }
                else {
                    Toast.makeText(this,"There are no notifications",Toast.LENGTH_SHORT).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
            return;
        }
        if(countBack<1)
        {
            Toast toast=Toast.makeText(this,"Press again to exit",Toast.LENGTH_SHORT);
            toast.show();
            countBack++;
            return;
        }
        countBack=0;
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }

    /*
     public void onBackPressed() {
        finish();
     }

     <activity
     android:name="com.example.shoppingapp.AddNewItems"
     android:label=""
     android:noHistory="true">
     </activity>

    */


    private void STAFF() {
        for(final Medicine medicine:medicineList)
        {
            if(!getIntent().getStringExtra("UID").equals(medicine.getOwnerUID()))
                continue;
            //Toast.makeText(this,medicine.getOwnerUID(),Toast.LENGTH_SHORT).show();

            View v=LayoutInflater.from(this).inflate(R.layout.medicine_information,null);
            LinearLayout ll=new LinearLayout(this);
            ll.setOrientation(LinearLayout.VERTICAL);

            ImageButton add=v.findViewById(R.id.add_item);
            ImageButton remove=v.findViewById(R.id.remove_item);
            Button addToCart=v.findViewById(R.id.button2);

            TextView medName=v.findViewById(R.id.textView);
            TextView medCost=v.findViewById(R.id.textView4);
            TextView medCode=v.findViewById(R.id.code_Textview);
            TextView medExp=v.findViewById(R.id.expiry_date);
            EditText shelfNo=v.findViewById(R.id.shelfNumber);

            medName.setText(medicine.getName());
            medCode.setText("Code: "+Integer.toString(medicine.getCode()));
            medCost.setText("Cost: "+Float.toString(medicine.getCost()));
            medExp.setText("Expiry date: "+medicine.getExpiryDate());
            shelfNo.setText(medicine.getShelfNumber());

            final EditText editText=v.findViewById(R.id.total_number_of_medicines);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    editText.setText(Integer.toString(Integer.parseInt(editText.getText().toString())+1));
                }
            });
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!editText.getText().toString().equals("0"))
                        editText.setText(Integer.toString(Integer.parseInt(editText.getText().toString())-1));
                }
            });
            setRipple(v);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("CLICKED","You clicked it!");
                }
            });

            addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String q="1";
                    if(editText.getText().toString().equals("0")) {
                        Toast.makeText(OnlinePharmacy.this,"Added 1 item to the cart",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        q=editText.getText().toString();
                        Toast.makeText(OnlinePharmacy.this,"Added "+q+" items to the cart",Toast.LENGTH_SHORT).show();
                    }

                    if(medicine.getQuantity()<Integer.parseInt(q)) {
                        Toast.makeText(OnlinePharmacy.this,"There are not enough medicine in the stock",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Cart cart=new Cart(medicine.getName(),Float.toString(medicine.getCost()),q);
                    cartList.add(cart);

                    DatabaseReference ref=FirebaseDatabase.getInstance().getReference("medicines");
                    String id=medicine.getID();
                    ref.child(id).child("quantity").setValue(medicine.getQuantity()-Integer.parseInt(q));
                }
            });

            Space space=new Space(this);
            space.setMinimumHeight(16);

            ll.addView(v);
            ll.addView(space);
            linearLayout.addView(ll);

            staffMedicineList.add(v);
            staffMedicineMap.put(ll,true);
        }

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //Toast.makeText(OnlinePharmacy.this,charSequence,Toast.LENGTH_SHORT).show();

                Iterator<Map.Entry<View,Boolean>>iterator=staffMedicineMap.entrySet().iterator();
                while (iterator.hasNext()) {
                    HashMap.Entry<View,Boolean> map=iterator.next();
                    View view=map.getKey().findViewById(R.id.medicine_information_layout);
                    TextView textView=view.findViewById(R.id.textView);
                    if(textView.getText().toString()
                            .toLowerCase().startsWith(charSequence.toString().toLowerCase())) {
                        if(!map.getValue()) {
                            linearLayout.addView(map.getKey());
                            staffMedicineMap.put(map.getKey(),!map.getValue());
                        }
                    }
                    else {
                        linearLayout.removeView(map.getKey());
                        staffMedicineMap.put(map.getKey(),!map.getValue());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void OWNER() {
        searchBar.setFocusable(false);
        searchBar.setFocusableInTouchMode(false);
        searchBar.setClickable(false);
        View button=findViewById(R.id.checkout_button);
        ((ViewGroup)button.getParent()).removeView(button);
        View relativeLayout=LayoutInflater.from(this).inflate(R.layout.owner_side,null);
        linearLayout.addView(relativeLayout);

        TextView showUsername=findViewById(R.id.show_username);

        final Button addMedicine=relativeLayout.findViewById(R.id.add_medicine);
        final Button addStaffMember=relativeLayout.findViewById(R.id.add_staff_member);
        final Button editMedicine=findViewById(R.id.edit_medicine);
        final Button removeStaffMember=findViewById(R.id.remove_staff_member);

        addMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMedicine(view);
            }
        });
        addStaffMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStaffMember(view);
            }
        });
        editMedicine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editMedicine(view);
            }
        });
        removeStaffMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeStaffMember(view);
            }
        });
    }

}
