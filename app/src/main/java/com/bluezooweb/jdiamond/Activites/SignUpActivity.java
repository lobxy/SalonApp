package com.bluezooweb.jdiamond.Activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bluezooweb.jdiamond.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "Sign Up Activity";
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private EditText et_password, et_confirmPassword, et_email;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setInverseBackgroundForced(true);
        dialog.setCancelable(false);

        et_confirmPassword = findViewById(R.id.signup_confirmPwd);
        et_password = findViewById(R.id.signup_password);
        et_email = findViewById(R.id.signup_email);

        Button submit = findViewById(R.id.signup_submit);
        Button toLogin = findViewById(R.id.signup_toLogin);
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
    }

    private void validate() {

        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        String cpwd = et_confirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {

            et_email.requestFocus();
            et_email.setError("Field is empty");

        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {

            et_email.requestFocus();
            et_email.setError("Invalid email address");

        } else if (TextUtils.isEmpty(password)) {

            et_password.requestFocus();
            et_password.setError("Field is empty");

        } else if (password.length() < 6) {

            et_password.requestFocus();
            et_password.setError("Password is less than 6 characters");

        } else if (TextUtils.isEmpty(cpwd)) {

            et_confirmPassword.requestFocus();
            et_confirmPassword.setError("Field is empty");
            et_password.setText("");

        } else if (!password.equals(cpwd)) {
            et_confirmPassword.requestFocus();
            et_confirmPassword.setError("Passwords don't match");
            et_confirmPassword.setText("");
            et_password.setText("");
        } else {
            if (connectivity()) {

                dialog.show();
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            Log.i(TAG, "onComplete: User created successfully!");
                            startActivity(new Intent(SignUpActivity.this, RegisterActivity.class));
                            finish();
                        } else {
                            dialog.dismiss();
                            Log.i(TAG, "onComplete: User creation error: " + task.getException().getLocalizedMessage());
                            et_confirmPassword.setText("");
                            et_password.setText("");
                        }
                    }
                });

            } else {
                Toast.makeText(this, "Can't reach to internet", Toast.LENGTH_LONG).show();
            }
        }

        //end of validate.
    }

    private boolean connectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }

//end of class
}
