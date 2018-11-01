package com.example.vaibhavmishra.onlinepharmacy;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Build;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Space;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

public class OnlinePharmacy extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private LinearLayout linearLayout;
    private boolean isOwnerChecked, isStaffChecked;
    private Toolbar toolbar;
    private int countBack=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_pharmacy);

        Intent intent=getIntent();
        isOwnerChecked=intent.getBooleanExtra("OWNER",false);
        isStaffChecked=intent.getBooleanExtra("STAFF",false);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout=findViewById(R.id.drawer_layout);

        linearLayout=findViewById(R.id.list_holder);
        if(isStaffChecked)      // If Staff is Checked------------------------
        {
            for(int i=0;i<15;i++)
            {
                View v=LayoutInflater.from(this).inflate(R.layout.medicine_information,null);
                linearLayout.addView(v);
                ImageButton add=v.findViewById(R.id.add_item);
                ImageButton remove=v.findViewById(R.id.remove_item);
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
                Space space=new Space(this);
                space.setMinimumHeight(4);
                linearLayout.addView(space);
            }
        }
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
}
