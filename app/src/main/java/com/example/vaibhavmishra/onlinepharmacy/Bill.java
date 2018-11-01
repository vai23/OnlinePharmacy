package com.example.vaibhavmishra.onlinepharmacy;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import static android.view.Gravity.CENTER;

public class Bill extends AppCompatActivity {

    private Toolbar toolbar;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        toolbar=findViewById(R.id.toolbar_bill);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Bill");
        actionBar.setDisplayHomeAsUpEnabled(true);

        tableLayout=findViewById(R.id.tableLayout);
        addBill();
    }

    private void addBill() {

        for(int i=1;i<31;i++)
        {
            TableRow tableRow=new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            tableRow.setLayoutParams(lp);
            TextView tv1=new TextView(this);
            TextView tv2=new TextView(this);
            TextView tv3=new TextView(this);
            TextView tv4=new TextView(this);
            Drawable drawable=getApplicationContext().getResources().getDrawable(R.drawable.cell_shape);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            {
                tv1.setBackground(drawable);
                tv2.setBackground(drawable);
                tv3.setBackground(drawable);
                tv4.setBackground(drawable);
            }
            tv1.setGravity(CENTER);
            tv2.setGravity(CENTER);
            tv3.setGravity(CENTER);
            tv4.setGravity(CENTER);
            tv1.setPadding(16,16,16,16);
            tv2.setPadding(16,16,16,16);
            tv3.setPadding(16,16,16,16);
            tv4.setPadding(16,16,16,16);
            tv1.setTextColor(Color.WHITE);
            tv2.setTextColor(Color.WHITE);
            tv3.setTextColor(Color.WHITE);
            tv4.setTextColor(Color.WHITE);
            if(i==1)
            {
                tv1.setText("Medicine Name");
                tv2.setText("Cost");
                tv3.setText("Quantity");
                tv4.setText("Total Cost");
                tv1.setTextSize(16);
                tv2.setTextSize(16);
                tv3.setTextSize(16);
                tv4.setTextSize(16);
            }
            else
            {
                tv1.setText("Keraglo AD 50ml");
                tv2.setText("229.90");
                tv3.setText("4");
                tv4.setText(Double.toString(4*229.90));
            }
            tableRow.addView(tv1, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f));
            tableRow.addView(tv2, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            tableRow.addView(tv3, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.8f));
            tableRow.addView(tv4, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            tableLayout.addView(tableRow);
        }
    }
}
