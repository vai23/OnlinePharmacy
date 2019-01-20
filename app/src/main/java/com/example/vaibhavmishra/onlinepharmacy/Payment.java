package com.example.vaibhavmishra.onlinepharmacy;

import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Payment extends AppCompatActivity {

    private Toolbar toolbar;
    private Button payCash;
    private Button payOnline;
    private EditText name;
    private EditText drName;
    private EditText emailText;
    private EditText mobileNo;
    private ArrayList<Cart> cartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        cartList=new ArrayList<>(Bill.cartList);

        toolbar=findViewById(R.id.toolbar_payment);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Payment");
        actionBar.setDisplayHomeAsUpEnabled(true);

        payCash=findViewById(R.id.payCash_button);
        payOnline=findViewById(R.id.payOnline_button);
        name=findViewById(R.id.customer_name);
        drName=findViewById(R.id.doctor_name);
        mobileNo=findViewById(R.id.mobile_number);

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

        emailText=findViewById(R.id.customer_email);
    }

    private String getBill() {
        String bill="Your Bill -- \n\n";
        int i=1;
        float total=0;
        for(Cart cart:cartList) {
            String st=Integer.toString(i)+"- "+cart.getName()+" ("+cart.getQuantity()+"x"+cart.getCost()+") ="+cart.getTotalCost();
            bill=bill+st+"\n";
            i++;
            total+=Float.parseFloat(cart.getTotalCost());
        }
        bill+="\n";
        bill+=("Total Amount="+Float.toString(total));
        return bill;
    }

    private void payOnline(View view) {
        if(!check()) {
            return;
        }
        save();
        Intent intent=new Intent(this,PaymentGateway.class);
        startActivity(intent);
    }

    private boolean check() {
        if(TextUtils.isEmpty(name.getText())||TextUtils.isEmpty(drName.getText())
                ||TextUtils.isEmpty(emailText.getText())||TextUtils.isEmpty(mobileNo.getText())) {
            Toast.makeText(this,"Fill all fields",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(mobileNo.getText().toString().length()<10) {
            Toast.makeText(this,"Invalid mobile number",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void payByCash(View view) {
        if(!check()) {
            return;
        }
        save();
        Toast toast= Toast.makeText(this,"Payment Completed",Toast.LENGTH_SHORT);
        toast.show();
        //smsSendMessage();
        sendEmail();
    }

    private void save() {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("customers");
        String id=ref.push().getKey();
        Customer customer=new Customer(name.getText().toString(),drName.getText().toString(),
                emailText.getText().toString(),mobileNo.getText().toString());
        customer.setId(id);
        customer.setOwnerUID(FirebaseAuth.getInstance().getUid());
        ref.child(id).setValue(customer);
    }

    protected void sendEmail() {
        if(emailText.getText().toString()==null || !emailText.getText().toString().endsWith("@gmail.com"))
            return;

        Log.i("Send email", "");
        String[] TO = {
                emailText.getText().toString()
        };
        String[] CC = {
                "vaibhavmishra740@gmail.com"
        };
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Thank you for shopping with us!");
        emailIntent.putExtra(Intent.EXTRA_TEXT, getBill());
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void smsSendMessage() {
        //EditText editText = (EditText) findViewById(R.id.editText_main);
        // Set the destination phone number to the string in editText.
        String destinationAddress = mobileNo.getText().toString();
        // Find the sms_message view.
        //EditText smsEditText = (EditText) findViewById(R.id.sms_message);
        // Get the text of the SMS message.
        //String smsMessage = smsEditText.getText().toString();
        // Set the service center address if needed, otherwise null.
        String scAddress = null;
        // Set pending intents to broadcast
        // when message sent and when delivered, or set to null.
        PendingIntent sentIntent = null, deliveryIntent = null;
        // Use SmsManager.
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage
                (destinationAddress, scAddress, "Thanks!",
                        sentIntent, deliveryIntent);
    }
}
