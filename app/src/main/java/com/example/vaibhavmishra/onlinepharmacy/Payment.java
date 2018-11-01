package com.example.vaibhavmishra.onlinepharmacy;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Payment extends AppCompatActivity {

    private Toolbar toolbar;
    private Button payCash;
    private Button payOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        toolbar=findViewById(R.id.toolbar_payment);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Payment");
        actionBar.setDisplayHomeAsUpEnabled(true);

        payCash=findViewById(R.id.payCash_button);
        payOnline=findViewById(R.id.payOnline_button);

        payCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payByCash(view);
            }
        });
        payOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payOnline(view);
            }
        });
    }

    private void payOnline(View view) {
        Intent intent=new Intent(this,PaymentGateway.class);
        startActivity(intent);
    }

    private void payByCash(View view) {
        Toast toast= Toast.makeText(this,"Payment Completed",Toast.LENGTH_SHORT);
        toast.show();
    }
}
