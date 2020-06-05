package chawla.sanil.doctactproj.Auth;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import chawla.sanil.doctactproj.Homes.DoctorFull.DoctorHome;
import chawla.sanil.doctactproj.R;
import chawla.sanil.doctactproj.Setups.DoctorActivitySetup;
import chawla.sanil.doctactproj.Setups.LabSetupActivity;
import chawla.sanil.doctactproj.Setups.PatientSetupActivity;
import chawla.sanil.doctactproj.Setups.PharmSetupActivity;

public class RegisterActivity extends AppCompatActivity {
    private TextView mainTitle;
    private  Spinner spinner;
    private static final String[] paths = {"Doctor", "Patient", "Pharmacist", "Lab"};
    private String UserType;
    private Button backtoLogin;
    private FirebaseAuth mAuth;
    private Button RegBtn;
    private EditText EmailId;
    private EditText Password;
    private FirebaseFirestore mFirebaseFirestore;
    private EditText CnfPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mainTitle=(TextView)findViewById(R.id.Intro);
        Typeface Bahaus = Typeface.createFromAsset(getAssets(), "fonts/mainfont.ttf");
        mainTitle.setTypeface(Bahaus);
        mainTitle.setText("DocTact");
        mainTitle.setTextColor(Color.parseColor("#9f0070"));

        backtoLogin=(Button)findViewById(R.id.BackToLog);
        backtoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent LoginIntent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(LoginIntent);
                finish();
            }
        });

        // Drop Down Complete Implementation
        spinner = (Spinner)findViewById(R.id.RoleSelector);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(RegisterActivity.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        UserType=adapterView.getItemAtPosition(i).toString();
                        break;
                    case 1:
                        UserType=adapterView.getItemAtPosition(i).toString();
                        break;
                    case 2:
                        UserType=adapterView.getItemAtPosition(i).toString();
                        break;
                    case 3:
                        UserType=adapterView.getItemAtPosition(i).toString();
                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                UserType="";
            }
        });

        RegBtn=(Button)findViewById(R.id.CompleteRegBtn);
        EmailId=(EditText)findViewById(R.id.EmailText2);
        Password=(EditText)findViewById(R.id.PassText);
        CnfPassword=(EditText)findViewById(R.id.CnfPassText);

        RegBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFirebaseFirestore=FirebaseFirestore.getInstance();
                mAuth=FirebaseAuth.getInstance();
                final String Email=EmailId.getText().toString();
                final String Pass=Password.getText().toString();
                String Cnfpass=CnfPassword.getText().toString();
                final String UT=UserType;

                if((!TextUtils.isEmpty(Email)) &&(!TextUtils.isEmpty(Cnfpass)) &&(!TextUtils.isEmpty(Pass))) {
                    if(Pass.equals(Cnfpass)) {
                        mAuth.createUserWithEmailAndPassword(Email, Pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Bangaya bc ", Toast.LENGTH_LONG).show();
                                    Map<String, String> LoginDeets = new HashMap<>();
                                    final String uid = mAuth.getCurrentUser().getUid();

                                    LoginDeets.put("uid", uid);
                                    LoginDeets.put("role", UT);
                                    mFirebaseFirestore.collection("Users").document(uid).set(LoginDeets).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(RegisterActivity.this,"Bhai data pohoch gaya hai bc",Toast.LENGTH_LONG).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(RegisterActivity.this,"Error ",Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    if (UT == "Doctor") {
                                        Intent DoctorSetup = new Intent(RegisterActivity.this, DoctorActivitySetup.class);
                                        startActivity(DoctorSetup);
                                        //Intent DoctorHome = new Intent(RegisterActivity.this, DoctorHome.class);
                                        //startActivity(DoctorHome);
                                        finish();
                                        } else if (UT == "Lab") {
                                        Intent LabSetup = new Intent(RegisterActivity.this, LabSetupActivity.class);
                                        startActivity(LabSetup);
                                        finish();
                                        } else if (UT == "Patient") {
                                        Intent PatientSetup = new Intent(RegisterActivity.this, PatientSetupActivity.class);
                                        startActivity(PatientSetup);
                                        finish();
                                        } else if (UT == "Pharmacist") {
                                        Intent PharmSetup = new Intent(RegisterActivity.this, PharmSetupActivity.class);
                                        startActivity(PharmSetup);
                                        finish();
                                        } else {
                                        Toast.makeText(RegisterActivity.this, "fuck hogaya", Toast.LENGTH_SHORT).show();
                                        }
                                }
                                else {
                                    String Err = task.getException().getMessage();
                                    Toast.makeText(RegisterActivity.this, "Error while registeration" + Err, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(RegisterActivity.this,"Passwords do not match",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}

