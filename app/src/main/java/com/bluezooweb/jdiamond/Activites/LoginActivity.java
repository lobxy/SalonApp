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

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Login Activity";
    private FirebaseAuth auth = FirebaseAuth.getInstance();

    private EditText et_password, et_email;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait...");
        dialog.setInverseBackgroundForced(true);
        dialog.setCancelable(false);

        et_password = findViewById(R.id.login_password);
        et_email = findViewById(R.id.login_email);

        Button login = findViewById(R.id.login_submit);
        Button toSignUp = findViewById(R.id.login_goToRegister);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

        toSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

    }

    private void validate() {

        String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();

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

        } else {

            if (connectivity()) {
                dialog.show();
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();
                            Log.i(TAG, "onComplete: Login successfull");
                           /*
                            if (checkForData()) {



                            } else {

                                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                                finish();

                            }*/

                            startActivity(new Intent(LoginActivity.this, MainScreenActivity.class));
                            finish();
                        } else {
                            dialog.dismiss();
                            Log.i(TAG, "onComplete: Login Unsuccessfull \n Error: " + task.getException().getLocalizedMessage());
                            Toast.makeText(LoginActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            } else {
                Toast.makeText(this, "Can't reach to internet", Toast.LENGTH_LONG).show();
            }
        }

    }


    private boolean connectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
