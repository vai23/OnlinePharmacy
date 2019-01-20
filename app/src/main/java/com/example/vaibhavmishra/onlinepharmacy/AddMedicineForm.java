package com.example.vaibhavmishra.onlinepharmacy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddMedicineForm extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText name,code,cost,expirydate,shelf,quantity,company,credit,debit;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine_form);

        toolbar=findViewById(R.id.add_medicine_form_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Add Medicine");
        actionBar.setDisplayHomeAsUpEnabled(true);

        name=findViewById(R.id.name_of_medicine);
        code=findViewById(R.id.code_of_mecine);
        cost=findViewById(R.id.cost_of_medicine);
        expirydate=findViewById(R.id.expirydate_of_medicine);
        shelf=findViewById(R.id.shelfno_of_medicine);
        quantity=findViewById(R.id.quantity_available_in_stock);
        company=findViewById(R.id.company_name);
        credit=findViewById(R.id.credit);
        debit=findViewById(R.id.debit);
    }

    public void addMedicineToDatabase(View view) {
        showProgressDialog();
            if(!isValidInformation()) {
            hideProgressDialog();
            Toast.makeText(this,"Fill all fields",Toast.LENGTH_SHORT).show();
            return;
        }

        if(!isValidDate()) {
            hideProgressDialog();
            Toast.makeText(this,"Invalid date format",Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            DatabaseReference ref=FirebaseDatabase.getInstance().getReference("medicines");
            Medicine medicine=new Medicine(getIntent().getStringExtra("UID"),name.getText().toString(),
                    Integer.parseInt(code.getText().toString()),Float.parseFloat(cost.getText().toString()),
                    expirydate.getText().toString(),shelf.getText().toString(),
                    Integer.parseInt(quantity.getText().toString()));
            String id=ref.push().getKey();
            medicine.setID(id);
            ref.child(id).setValue(medicine);

            DatabaseReference dr=FirebaseDatabase.getInstance().getReference("company");
            Company comp=new Company(company.getText().toString(),credit.getText().toString(),
                    debit.getText().toString(),quantity.getText().toString());
            id=dr.push().getKey();
            comp.setId(id);
            comp.setOwnerID(getIntent().getStringExtra("UID"));
            dr.child(id).setValue(comp);

            hideProgressDialog();
        }
        catch (Exception e) {
            hideProgressDialog();
            Toast.makeText(this,"ERROR",Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this,"Medicine added to database",Toast.LENGTH_SHORT).show();
        Intent intent=new Intent(this,OnlinePharmacy.class);
        startActivity(intent);
    }

    private boolean isValidDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);

        try {
            Date date=sdf.parse(expirydate.getText().toString());
        }
        catch (ParseException pe) {
            return false;
        }
        return true;
    }

    private boolean isValidInformation() {

        return !(TextUtils.isEmpty(name.getText()) ||
                TextUtils.isEmpty(code.getText()) ||
                TextUtils.isEmpty(cost.getText()) ||
                TextUtils.isEmpty(expirydate.getText()) ||
                TextUtils.isEmpty(shelf.getText()) ||
                TextUtils.isEmpty(company.getText()) ||
                TextUtils.isEmpty(credit.getText()) ||
                TextUtils.isEmpty(debit.getText()));
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
}
