package chawla.sanil.doctactproj.Homes.DoctorFull;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import chawla.sanil.doctactproj.PatientModel;
import chawla.sanil.doctactproj.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DocPresFrag extends Fragment {

    private FirebaseAuth mAuth;
    private RecyclerView req_list_viewP;
    private FirebaseFirestore mFirebaseFirestore;
    ArrayList<String> patientIds;
    List<PatientModel> mPatientModels;
    public PatientAdapter mPatientAdapter;



    public DocPresFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_doc_pres, container, false);
        req_list_viewP=(RecyclerView)view.findViewById(R.id.ReqRecViewPreV);
        mFirebaseFirestore= FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();
        mPatientModels=new ArrayList<>();
        patientIds = new ArrayList<>();



        mFirebaseFirestore.collection("Doctors").document(mAuth.getCurrentUser().getUid()).collection("Scehdule").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    PatientModel model = doc.getDocument().toObject(PatientModel.class);
                    mPatientModels.add(model);
                    mPatientAdapter=new PatientAdapter(mPatientModels,"prescription");
                    req_list_viewP.setLayoutManager(new LinearLayoutManager(getContext()));
                    req_list_viewP.setAdapter(mPatientAdapter);
                }
            }
        });
        return view;
    }

}
