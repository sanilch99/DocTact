package chawla.sanil.doctactproj.Auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import chawla.sanil.doctactproj.Homes.DoctorFull.DoctorHome;
import chawla.sanil.doctactproj.Homes.PatientFull.PatientHome;
import chawla.sanil.doctactproj.R;
import chawla.sanil.doctactproj.Setups.DoctorActivitySetup;
import chawla.sanil.doctactproj.Setups.LabSetupActivity;
import chawla.sanil.doctactproj.Setups.PatientSetupActivity;
import chawla.sanil.doctactproj.Setups.PharmSetupActivity;

public class LoginActivity extends AppCompatActivity  {

    private Button NewRegbtn;
    private EditText Email;
    private EditText Pass;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirebaseFirestore;
    private Button Login;
    private Boolean mLocationPermissionGranted=false;
    public static  final int PERMISSIONS_REQUEST_ENABLE_GPS=9002;
    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=9003;
    public static final int ERROR_DIALOG_REQUEST=9001;

    private static final String TAG = "LoginActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Email = (EditText) findViewById(R.id.EmailText2);
        Pass = (EditText) findViewById(R.id.PassText2);
        Login = (Button) findViewById(R.id.LoginBtn);
        
        //switch to Reg Activity
        NewRegbtn = (Button) findViewById(R.id.NewReg);
        NewRegbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent RegisterIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(RegisterIntent);
                finish();
            }
        });

        //login existing user
        mAuth = FirebaseAuth.getInstance();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Email.getText().toString();
                String pass = Pass.getText().toString();
                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String uid = mAuth.getCurrentUser().getUid();
                            mFirebaseFirestore.collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().exists()) {
                                            String UserType = task.getResult().getString("role");
                                            Toast.makeText(LoginActivity.this, "Usertype"+UserType, Toast.LENGTH_SHORT).show();
                                            if (UserType.equals("Doctor")) {
                                                //Intent DoctorSetup = new Intent(LoginActivity.this, DoctorActivitySetup.class);
                                                //startActivity(DoctorSetup);
                                                Intent DoctorHome = new Intent(LoginActivity.this, DoctorHome.class);
                                                startActivity(DoctorHome);
                                                finish();
                                            } else if (UserType.equals("Lab")) {
                                                Intent LabSetup = new Intent(LoginActivity.this, LabSetupActivity.class);
                                                startActivity(LabSetup);
                                                finish();
                                            } else if (UserType.equals("Patient")) {
                                                //Intent PatientSetup = new Intent(LoginActivity.this, PatientSetupActivity.class);
                                                //startActivity(PatientSetup);
                                                Intent apatHome=new Intent(LoginActivity.this, PatientHome.class);
                                                startActivity(apatHome);
                                                finish();
                                            } else if (UserType.equals("Pharmacist")) {
                                                Intent PharmSetup = new Intent(LoginActivity.this, PharmSetupActivity.class);
                                                startActivity(PharmSetup);
                                                finish();
                                            } else {
                                                Toast.makeText(LoginActivity.this, "kat gaya hogaya", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });

    }

}