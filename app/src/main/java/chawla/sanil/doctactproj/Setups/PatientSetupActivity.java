package chawla.sanil.doctactproj.Setups;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import chawla.sanil.doctactproj.Homes.PatientFull.PatientHome;
import chawla.sanil.doctactproj.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class PatientSetupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirebaseFirestore;
    private EditText name,age,address,problems;
    private Boolean isChanged = false;
    private Uri mainImageUri = null;
    private Button SaveData;
    private Chip c1,c2;
    private CircleImageView ProfileImagePic;
    private TextView intro;
    private StorageReference mStorageReference;
    String UserId,gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_setup);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseFirestore = FirebaseFirestore.getInstance();

        ProfileImagePic=(CircleImageView)findViewById(R.id.PatProfile);
        c1=(Chip)findViewById(R.id.chip1);
        c2=(Chip)findViewById(R.id.chip2);
        name=(EditText)findViewById(R.id.NameTextPt);
        age=(EditText)findViewById(R.id.AgePText);
        address=(EditText)findViewById(R.id.AddrPText);
        problems=(EditText)findViewById(R.id.PatProtext);
        SaveData=(Button)findViewById(R.id.StoreP);
        intro=(TextView)findViewById(R.id.setterpro);

        mStorageReference = FirebaseStorage.getInstance().getReference();
        UserId=mAuth.getCurrentUser().getUid();

        Typeface Bahaus = Typeface.createFromAsset(getAssets(), "fonts/mainfont.ttf");
        intro.setTypeface(Bahaus);
        intro.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f);

        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender="Male";
            }
        });
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender="Female";

            }
        });

        ProfileImagePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(PatientSetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(PatientSetupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        BringImagePicker();
                    }
                } else {
                    BringImagePicker();
                }
            }
        });

        //Retrieving DATA from Firestore
        mFirebaseFirestore.collection("Patients").document(UserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        Toast.makeText(PatientSetupActivity.this, "Data Exists", Toast.LENGTH_LONG).show();
                        String tName = task.getResult().getString("name");
                        String image = task.getResult().getString("image");
                        String tProb=task.getResult().getString("problems");
                        String tAge=task.getResult().getString("age");
                        String tAddr=task.getResult().getString("address");
                        name.setText(tName);
                        age.setText(tAge);
                        problems.setText(tProb);
                        address.setText(tAddr);
                        mainImageUri=Uri.parse(image);
                        RequestOptions placeholderrequest = new RequestOptions();
                        placeholderrequest.placeholder(R.drawable.ic_account_circle_black_24dp);
                        Glide.with(PatientSetupActivity.this).setDefaultRequestOptions(placeholderrequest).load(image).into(ProfileImagePic);
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(PatientSetupActivity.this, "Firestore error" + error, Toast.LENGTH_LONG).show();
                }
                SaveData.setEnabled(true);
            }
        });

        SaveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String sName = name.getText().toString();
                final String sAge=age.getText().toString();
                final String sAddress=address.getText().toString();
                final String sProblems=problems.getText().toString();
                final String sGender=gender;

                //Checking for empty fields
                if ((!TextUtils.isEmpty(sName)) && (!TextUtils.isEmpty(sAge)) && (!TextUtils.isEmpty(sAddress)) &&(!TextUtils.isEmpty(sGender)) &&(!TextUtils.isEmpty(sProblems)) &&(mainImageUri != null)) {
                    //if new profile pic
                    if (isChanged) {

                        UserId = mAuth.getCurrentUser().getUid();
                        StorageReference imagePath = mStorageReference.child("profile_images").child(UserId + ".jpeg");
                        imagePath.putFile(mainImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                                if (task.isSuccessful()) {
                                    storeFirestore(task, sName,sAge,sGender,sProblems,sAddress);
                                } else {
                                    String errorMessage = task.getException().getMessage();

                                    Toast.makeText(PatientSetupActivity.this, "Error" + errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        sendToHome();
                    }
                    //if no change in profile pic
                    else {
                        storeFirestore(null, sName,sAge,sGender,sProblems,sAddress);
                        sendToHome();
                    }
                }
            }
        });

    }

    private void sendToHome() {
        Intent patientHome=new Intent(PatientSetupActivity.this, PatientHome.class);
        startActivity(patientHome);
        finish();
    }

    //Crop Image Lib - image selector
    private void BringImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(PatientSetupActivity.this);
    }

    //Getting result from image picker
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageUri = result.getUri();
                ProfileImagePic.setImageURI(mainImageUri);
                isChanged = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    //storing data in Firestore
    private void storeFirestore(@NonNull Task<UploadTask.TaskSnapshot> task, String sName,String sAge,String sGender,String sProblems,String sAddress) {
        Uri downloadUri=mainImageUri;
        //setting new image
        if (task != null) {
            downloadUri = task.getResult().getDownloadUrl();
        }
        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", sName);
        userMap.put("image", downloadUri.toString());
        userMap.put("gender",sGender);
        userMap.put("problems",sProblems);
        userMap.put("address",sAddress);
        userMap.put("age",sAge);
        //storing data
        mFirebaseFirestore.collection("Patients").document(UserId).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(PatientSetupActivity.this, "Users settings have been saved", Toast.LENGTH_SHORT).show();
                    Intent patientHome = new Intent(PatientSetupActivity.this, PatientHome.class);
                    startActivity(patientHome);
                    finish();
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(PatientSetupActivity.this, "Firestore error" + error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}

