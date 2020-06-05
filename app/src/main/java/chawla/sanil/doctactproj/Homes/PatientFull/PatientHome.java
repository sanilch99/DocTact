package chawla.sanil.doctactproj.Homes.PatientFull;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import chawla.sanil.doctactproj.Homes.DoctorFull.DocPresFrag;
import chawla.sanil.doctactproj.Homes.DoctorFull.DocProfFrag;
import chawla.sanil.doctactproj.Homes.DoctorFull.DocReqFrag;
import chawla.sanil.doctactproj.Homes.DoctorFull.DocSchedule;
import chawla.sanil.doctactproj.R;

public class PatientHome extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_home);

        //loading the default fragment
        loadFragment(new DocAllPat());

        //getting bottom navigation view and attaching the listener
        BottomNavigationView navigation = findViewById(R.id.PatBotNav);
        navigation.setOnNavigationItemSelectedListener(this);
    }


    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.PatFramCont, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;

        switch (menuItem.getItemId()) {
            case R.id.PatItemProf:
                fragment = new DocAllPat();
                break;

            case R.id.ViewDocItem:
                fragment = new DocAllPat();
                break;

            case R.id.AppoItem:
                fragment = new PatAppo();
                break;

            case R.id.PatItemPres:
                fragment = new PatPres();
                break;
        }

        return loadFragment(fragment);
    }
}
