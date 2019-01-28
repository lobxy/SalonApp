package com.bluezooweb.jdiamond.Activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.bluezooweb.jdiamond.Model.UserRegister;
import com.bluezooweb.jdiamond.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "Register Activity";
    FirebaseAuth auth;
    DatabaseReference reference;

    String gender = "No data", uid, email, device_token, name, contact;

    EditText et_name, et_contact;
    Spinner genderSpinner;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...\nDon't close the application!");
        dialog.setInverseBackgroundForced(true);
        dialog.setCancelable(false);

        et_contact = findViewById(R.id.reg_contact);
        et_name = findViewById(R.id.reg_name);

        reference = FirebaseDatabase.getInstance().getReference("Users");
        auth = FirebaseAuth.getInstance();

        Button submit = findViewById(R.id.reg_submit);


        //set spinner
        genderSpinner = findViewById(R.id.reg_gender);
        String[] values = getResources().getStringArray(R.array.gender);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, values);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                gender = adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Shit it
            }
        });
        genderSpinner.setAdapter(adapter);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });

    }

    private void validate() {
        name = et_name.getText().toString().trim();
        contact = et_contact.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            et_name.requestFocus();
            et_name.setError("Field is empty");
        } else if (TextUtils.isEmpty(contact)) {
            et_contact.requestFocus();
            et_contact.setError("Field is empty");
        } else if (contact.length() < 10) {
            et_contact.requestFocus();
            et_contact.setError("Invalid contact number");
        } else {

            submitData(name, contact);

        }
    }

    private void submitData(final String name, final String contact) {
        Log.i(TAG, "submitData: Gender: " + gender);
        email = auth.getCurrentUser().getEmail();

        if (gender.equals("No Data")) {
            gender = genderSpinner.getSelectedItem().toString();
        }

        dialog.show();
        auth.getCurrentUser().getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
            @Override
            public void onSuccess(GetTokenResult getTokenResult) {
                device_token = getTokenResult.getToken();
                uid = auth.getCurrentUser().getUid();

                //String device_token, String name, String uid, String gender, String contact
                UserRegister userRegister = new UserRegister(device_token, name, uid, gender, contact);

                reference.child(uid).setValue(userRegister).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            dialog.dismiss();

                            Log.i(TAG, "onComplete: Data submitted ");
                            startActivity(new Intent(RegisterActivity.this, MainScreenActivity.class));
                            finish();
                        } else {
                            dialog.dismiss();

                            Log.i(TAG, "onFailure:Token Error: " + task.getException().getLocalizedMessage());
                            Toast.makeText(RegisterActivity.this, "Error: " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i(TAG, "onFailure:Token Error: " + e.getLocalizedMessage());
                Toast.makeText(RegisterActivity.this, "Error: " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }


}
