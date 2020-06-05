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

import chawla.sanil.doctactproj.Homes.DoctorFull.DoctorHome;
import chawla.sanil.doctactproj.Homes.PatientFull.PatientHome;
import chawla.sanil.doctactproj.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorActivitySetup extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirebaseFirestore;
    private EditText name,age,address,speciality;
    private Boolean isChanged = false;
    private Uri mainImageUri = null;
    private Button SaveData;
    private Chip ch1,ch2;
    private CircleImageView ProfileImagePic;
    private TextView intro;
    private StorageReference mStorageReference;
    String UserId,gender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_setup);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseFirestore = FirebaseFirestore.getInstance();

        ProfileImagePic=(CircleImageView)findViewById(R.id.DocProfile);
        ch1=(Chip)findViewById(R.id.cd1);
        ch2=(Chip)findViewById(R.id.cd2);
        name=(EditText)findViewById(R.id.NameTextD);
        age=(EditText)findViewById(R.id.AgeDText);
        address=(EditText)findViewById(R.id.AddrDText);
        speciality=(EditText)findViewById(R.id.DocSpetext);
        SaveData=(Button)findViewById(R.id.StoreD);
        intro=(TextView)findViewById(R.id.setterpro);


        mStorageReference = FirebaseStorage.getInstance().getReference();
        UserId=mAuth.getCurrentUser().getUid();

        Typeface Bahaus = Typeface.createFromAsset(getAssets(), "fonts/mainfont.ttf");
        intro.setTypeface(Bahaus);
        intro.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f);

        ch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender="Male";
            }
        });
        ch2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender="Female";

            }
        });

        ProfileImagePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(DoctorActivitySetup.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(DoctorActivitySetup.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        BringImagePicker();
                    }
                } else {
                    BringImagePicker();
                }
            }
        });

        //Retrieving DATA from Firestore
        mFirebaseFirestore.collection("Doctors").document(UserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        Toast.makeText(DoctorActivitySetup.this, "Data Exists", Toast.LENGTH_LONG).show();
                        String tName = task.getResult().getString("name");
                        String image = task.getResult().getString("image");
                        String tSpe=task.getResult().getString("problems");
                        String tAge=task.getResult().getString("age");
                        String tAddr=task.getResult().getString("address");
                        name.setText(tName);
                        age.setText(tAge);
                        speciality.setText(tSpe);
                        address.setText(tAddr);
                        mainImageUri=Uri.parse(image);
                        RequestOptions placeholderrequest = new RequestOptions();
                        placeholderrequest.placeholder(R.drawable.ic_account_circle_black_24dp);
                        Glide.with(DoctorActivitySetup.this).setDefaultRequestOptions(placeholderrequest).load(image).into(ProfileImagePic);
                    }
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(DoctorActivitySetup.this, "Firestore error" + error, Toast.LENGTH_LONG).show();
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
                final String sProblems=speciality.getText().toString();
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

                                    Toast.makeText(DoctorActivitySetup.this, "Error" + errorMessage, Toast.LENGTH_SHORT).show();
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
        Intent patientHome=new Intent(DoctorActivitySetup.this, PatientHome.class);
        startActivity(patientHome);
        finish();
    }

    //Crop Image Lib - image selector
    private void BringImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(DoctorActivitySetup.this);
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
    private void storeFirestore(@NonNull Task<UploadTask.TaskSnapshot> task, String sName,String sAge,String sGender,String sSpecial,String sAddress) {
        Uri downloadUri=mainImageUri;
        //setting new image
        if (task != null) {
            downloadUri = task.getResult().getDownloadUrl();
        }
        Map<String, String> userMap = new HashMap<>();
        userMap.put("name", sName);
        userMap.put("image", downloadUri.toString());
        userMap.put("gender",sGender);
        userMap.put("speciality",sSpecial);
        userMap.put("address",sAddress);
        userMap.put("age",sAge);
        //storing data
        mFirebaseFirestore.collection("Doctors").document(UserId).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(DoctorActivitySetup.this, "Users settings have been saved", Toast.LENGTH_SHORT).show();
                    Intent docHome = new Intent(DoctorActivitySetup.this, DoctorHome.class);
                    startActivity(docHome);
                    finish();
                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(DoctorActivitySetup.this, "Firestore error" + error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
