package chawla.sanil.doctactproj;

import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;

import gr.net.maroulis.library.EasySplashScreen;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EasySplashScreen config = new EasySplashScreen(SplashScreen.this)
                .withFullScreen()
                .withTargetActivity(MainActivity.class)
                .withSplashTimeOut(3000)
                .withBackgroundResource(R.color.MainBackground)
                .withLogo(R.drawable.maindoctor)
                .withAfterLogoText("DocTact");




        config.getAfterLogoTextView().setTextColor(Color.parseColor("#9f0070"));
        config.getAfterLogoTextView().setTextSize(TypedValue.COMPLEX_UNIT_SP, 44f);

        Typeface Bahaus = Typeface.createFromAsset(getAssets(), "fonts/mainfont.ttf");
        config.getAfterLogoTextView().setTypeface(Bahaus);

        View easySplashScreenView = config.create();
        setContentView(easySplashScreenView);

    }
}
