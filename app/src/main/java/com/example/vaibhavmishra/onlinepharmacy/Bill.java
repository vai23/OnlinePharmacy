package com.example.vaibhavmishra.onlinepharmacy;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import static android.view.Gravity.CENTER;

public class Bill extends AppCompatActivity {

    private Toolbar toolbar;
    private Button proceedToPayment;
    private TableLayout tableLayout;
    public static ArrayList<Cart> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill);

        cartList=new ArrayList<>(OnlinePharmacy.cartList);

        toolbar=findViewById(R.id.toolbar_bill);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Bill");
        actionBar.setDisplayHomeAsUpEnabled(true);

        tableLayout=findViewById(R.id.tableLayout);
        addBill();

        proceedToPayment=findViewById(R.id.proceed_to_payment);
        proceedToPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proceedToPayment(view);
            }
        });
    }

    private void proceedToPayment(View view) {
        Intent intent=new Intent(this,Payment.class);
        startActivity(intent);
    }

    private void addBill() {

        Cart cart[]=cartList.toArray(new Cart[]{});

        for(int i=-1;i<cart.length;i++)
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
            if(i==-1)
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
                tv1.setText(cart[i].getName());
                tv2.setText(cart[i].getCost());
                tv3.setText(cart[i].getQuantity());
                tv4.setText(cart[i].getTotalCost());
            }
            tableRow.addView(tv1, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1.5f));
            tableRow.addView(tv2, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            tableRow.addView(tv3, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 0.8f));
            tableRow.addView(tv4, new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            tableLayout.addView(tableRow);
        }
    }
}
