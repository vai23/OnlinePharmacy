package com.example.vaibhavmishra.onlinepharmacy;

import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SignupPage extends AppCompatActivity {

    private Button register;
    private FirebaseAuth mAuth;
    private EditText ownerName, password, email, confirmPassword, shopname, username;
    private ProgressDialog mProgressDialog;
    ArrayList<User> userArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_page);

        register=findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registerUser();
            }
        });

        mAuth=FirebaseAuth.getInstance();

        ownerName=findViewById(R.id.ownername);
        shopname=findViewById(R.id.shopname);
        password=findViewById(R.id.signup_password);
        confirmPassword=findViewById(R.id.signup_confirm_password);
        email=findViewById(R.id.emailid);
        username=findViewById(R.id.owner_username);

        userArrayList=new ArrayList<>(MainActivity.usersList);
    }

    private void registerUser() {
        showProgressDialog();
        if(!isValidInformation())
        {
            hideProgressDialog();
            Toast.makeText(this,"Fill all fields",Toast.LENGTH_SHORT).show();
            return;
        }

        if(!isValidPassword())
        {
            hideProgressDialog();
            Toast.makeText(this,"Passwords do not match",Toast.LENGTH_SHORT).show();
            return;
        }

        if(isUsernameAlreadyTaken(username.getText().toString())) {
            username.setText("");
            hideProgressDialog();
            Toast.makeText(SignupPage.this,"Username already taken",Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    Log.d("tag", "createUserWithEmail:success");
                    createUser();
                    hideProgressDialog();
                    Toast.makeText(SignupPage.this,"Registration successful",Toast.LENGTH_SHORT).show();
                    back(null);
                }
                else
                {
                    try
                    {
                        throw task.getException();
                    }
                    catch (FirebaseAuthWeakPasswordException weakPassword)
                    {
                        password.setText("");
                        confirmPassword.setText("");
                        hideProgressDialog();
                        Toast.makeText(SignupPage.this,"Choose strong password",Toast.LENGTH_SHORT).show();
                    }
                    catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                    {
                        password.setText("");
                        confirmPassword.setText("");
                        ownerName.setText("");
                        shopname.setText("");
                        email.setText("");
                        hideProgressDialog();
                        Toast.makeText(SignupPage.this,"Invalid credentials",Toast.LENGTH_SHORT).show();
                    }
                    catch (FirebaseAuthUserCollisionException existEmail)
                    {
                        password.setText("");
                        confirmPassword.setText("");
                        email.setText("");
                        hideProgressDialog();
                        Toast.makeText(SignupPage.this,"Email already registered",Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e)
                    {
                        password.setText("");
                        confirmPassword.setText("");
                        ownerName.setText("");
                        shopname.setText("");
                        email.setText("");
                        hideProgressDialog();
                        Toast.makeText(SignupPage.this,"Fatal Error",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean isUsernameAlreadyTaken(String u) {
        for(User user : MainActivity.usersList.toArray(new User[]{})) {
            Log.d("Username",user.getUsername()+" "+u);
            if(user.getUsername().equals(u)) {
                return true;
            }
        }
        return false;
    }

    private void createUser() {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("users");
        String id=myRef.push().getKey();
        User user=new User(id,username.getText().toString(),email.getText().toString()
                ,ownerName.getText().toString(),shopname.getText().toString());
        myRef.child(id).setValue(user);
    }

    private boolean isValidInformation() {
        return ownerName.getText().toString().length()>0 &&
                shopname.getText().toString().length()>0 &&
                password.getText().toString().length()>0 &&
                confirmPassword.getText().toString().length()>0 &&
                email.getText().toString().length()>0 &&
                username.getText().toString().length()>0;
    }

    private boolean isValidPassword() {
        return password.getText().toString().equals(confirmPassword.getText().toString());
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

    public void back(View view) {
        Intent intent=new Intent(this,MainActivity.class);
        finish();
    }
}
