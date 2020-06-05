package chawla.sanil.doctactproj.Homes.DoctorFull;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import chawla.sanil.doctactproj.R;

public class DoctorHome extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home);

        //loading the default fragment
        loadFragment(new DocProfFrag());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.DocBotNav);
        navigation.setOnNavigationItemSelectedListener(this);

    }


    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.DocFramCont, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;

        switch (item.getItemId()) {
            case R.id.DocItemProf:
                fragment = new DocProfFrag();
                break;

            case R.id.DocItemAppr:
                fragment = new DocReqFrag();
                break;

            case R.id.DocItemSched:
                fragment = new DocSchedule();
                break;

            case R.id.DocItemPres:
                fragment = new DocPresFrag();
                break;
        }

        return loadFragment(fragment);
    }
}
