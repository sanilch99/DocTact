package chawla.sanil.doctactproj.Homes.DoctorFull;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import chawla.sanil.doctactproj.R;

public class DocProfFrag extends Fragment {


    public DocProfFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doc_prof, container, false);
    }

}
