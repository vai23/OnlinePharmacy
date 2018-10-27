package com.example.vaibhavmishra.onlinepharmacy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

public class OnlinePharmacy extends AppCompatActivity {

    private int countBack=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_pharmacy);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu m)
    {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.pharmacy_menu,m);
        return true;
    }

    public void onBackPressed(){
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
