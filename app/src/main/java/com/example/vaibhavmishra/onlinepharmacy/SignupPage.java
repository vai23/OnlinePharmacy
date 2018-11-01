package com.example.vaibhavmishra.onlinepharmacy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class SignupPage extends AppCompatActivity implements View.OnClickListener {

    private Button signup;
    private EditText email;
    private EditText password;

    private ProgressDialog pdg;
    private FirebaseAuth fauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);
        signup=(Button) findViewById(R.id.register_button);
        email=(EditText) findViewById(R.id.emailid);
        password=(EditText) findViewById(R.id.signup_password);
        fauth=FirebaseAuth.getInstance();
        pdg =new ProgressDialog(this);
        signup.setOnClickListener(this);
        email.setOnClickListener(this);
        password.setOnClickListener(this);

    }

    private void registeruser()
    {
        String e=email.getText().toString().trim();
        String p=password.getText().toString().trim();

        if(TextUtils.isEmpty(e))
        {
            Toast.makeText(this,"Please Enter Email",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(p))
        {
            Toast.makeText(this,"Please Enter Password",Toast.LENGTH_SHORT).show();
            return;
        }

        pdg.setMessage("Registering User ...");
        pdg.show();
        fauth.createUserWithEmailAndPassword(e,p).addOnCompleteListener(this, new OnCompleteListener<com.google.firebase.auth.AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<com.google.firebase.auth.AuthResult> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(SignupPage.this,"Registered Succesffuly",Toast.LENGTH_SHORT).show();
                    pdg.dismiss();
                    return;
                }
                else
                {
                    Toast.makeText(SignupPage.this,"Couldnot Register Try again !!",Toast.LENGTH_SHORT).show();
                    pdg.dismiss();
                    return;

                }

            }
        });

    }
    public void back(View view) {
        Intent intent=new Intent(this,MainActivity.class);
        finish();
    }

    @Override
    public void onClick(View v) {
        if(v == signup)
        {
            registeruser();
        }

    }
}
