
package chawla.sanil.doctactproj;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import chawla.sanil.doctactproj.Auth.LoginActivity;
import chawla.sanil.doctactproj.Auth.RegisterActivity;
import chawla.sanil.doctactproj.Homes.PatientFull.PatientHome;
import chawla.sanil.doctactproj.Setups.DoctorActivitySetup;
import chawla.sanil.doctactproj.Setups.LabSetupActivity;
import chawla.sanil.doctactproj.Setups.PatientSetupActivity;
import chawla.sanil.doctactproj.Setups.PharmSetupActivity;

public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private LinearLayout mDotslayout;
    private SliderAdapter mSliderAdapter;
    private TextView[] mDots;
    private Button nextBtn;
    private Button backBtn;
    private Button moveToLogin;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirebaseFirestore;
    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isFirstTime()) {

            mViewPager = (ViewPager) findViewById(R.id.mSlideViewPager);
            mDotslayout = (LinearLayout) findViewById(R.id.mDotsLayout);
            nextBtn=(Button)findViewById(R.id.next);
            backBtn=(Button)findViewById(R.id.back);
            moveToLogin=(Button)findViewById(R.id.changeToLogin);
            mSliderAdapter = new SliderAdapter(this);
            mViewPager.setAdapter(mSliderAdapter);
            addDotsIndicator(0);
            mViewPager.addOnPageChangeListener(viewListener);

            moveToLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent RegIntent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(RegIntent);
                    finish();

                }
            });

            nextBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(mCurrentPage + 1);
                }
            });

            backBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mViewPager.setCurrentItem(mCurrentPage - 1);
                }
            });

        }

        else{
            mAuth=FirebaseAuth.getInstance();
            mFirebaseFirestore=FirebaseFirestore.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            if (currentUser != null) {
                String uid=mAuth.getCurrentUser().getUid();
                mFirebaseFirestore.collection("Users").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            if(task.getResult().exists()){
                                String UserType=task.getResult().getString("role");
                                System.out.println(UserType);
                                if(UserType.equals("Doctor")){
                                    Intent DoctorSetup=new Intent(MainActivity.this, DoctorActivitySetup.class);
                                    startActivity(DoctorSetup);
                                }
                                else if(UserType.equals("Lab")){
                                    Intent LabSetup=new Intent(MainActivity.this, LabSetupActivity.class);
                                    startActivity(LabSetup);
                                }
                                else if(UserType.equals("Patient")){
                                    //Intent PatientSetup=new Intent(MainActivity.this, PatientSetupActivity.class);
                                    //startActivity(PatientSetup);
                                    Intent apatHome=new Intent(MainActivity.this, PatientHome.class);
                                    startActivity(apatHome);
                                    finish();
                                }
                                else if(UserType.equals("Pharmacist")){
                                    Intent PharmSetup=new Intent(MainActivity.this, PharmSetupActivity.class);
                                    startActivity(PharmSetup);
                                }
                                else{
                                    Toast.makeText(MainActivity.this,"fuck hogaya",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                        else{
                            Toast.makeText(MainActivity.this,"zyada bada fuck hogaya",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
            else{
            Intent LoginIntent=new Intent(MainActivity.this, LoginActivity.class);
            startActivity(LoginIntent);
            }
        }
    }

    public void addDotsIndicator(int position)
    {
        mDots=new TextView[4];
        mDotslayout.removeAllViews();

        for(int i=0;i<mDots.length; i++){
            mDots[i]=new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(35);
            mDots[i].setTextColor(Color.parseColor("#9f0070"));
            mDotslayout.addView(mDots[i]);
        }

        if(mDots.length>0)
        {
            mDots[position].setTextColor(Color.parseColor("#ffffff"));
        }

    }

    ViewPager.OnPageChangeListener viewListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i1) {

        }

        @Override
        public void onPageSelected(int i) {
            addDotsIndicator(i);
            mCurrentPage=i;
            if(i==0)
            {
                nextBtn.setEnabled(true);
                backBtn.setEnabled(false);
                backBtn.setVisibility(View.INVISIBLE);
                nextBtn.setText("Next");
                backBtn.setText("");
                moveToLogin.setEnabled(false);
                moveToLogin.setVisibility(View.INVISIBLE);
                moveToLogin.setText("");
            }
            else if(i==mDots.length-1)
            {
                nextBtn.setEnabled(true);
                backBtn.setEnabled(true);
                backBtn.setVisibility(View.VISIBLE);
                nextBtn.setVisibility(View.VISIBLE);
                nextBtn.setText("Finish");
                backBtn.setText("Back");
                moveToLogin.setEnabled(true);
                moveToLogin.setVisibility(View.VISIBLE);
                moveToLogin.setText("");
            }
            else{

                nextBtn.setEnabled(true);
                backBtn.setEnabled(true);
                backBtn.setVisibility(View.VISIBLE);
                nextBtn.setText("Next");
                backBtn.setText("Back");
                moveToLogin.setEnabled(false);
                moveToLogin.setVisibility(View.INVISIBLE);
                moveToLogin.setText("");
            }


        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };

    private boolean isFirstTime()
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {
            // first time
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
        }
        return !ranBefore;
    }

    @Override
    public void onBackPressed() {

    }
}
