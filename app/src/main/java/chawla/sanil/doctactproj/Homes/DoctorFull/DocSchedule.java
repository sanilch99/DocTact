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
public class DocSchedule extends Fragment {

    private FirebaseAuth mAuth;
    private RecyclerView req_list_viewf;
    private FirebaseFirestore mFirebaseFirestore;
    ArrayList<String> patientIds;
    List<PatientModel> mPatientModels;
    public PatientAdapter mPatientAdapter;


    public DocSchedule() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_doc_schedule, container, false);
        req_list_viewf=(RecyclerView)view.findViewById(R.id.ReqRecViewS);
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
                    mPatientAdapter=new PatientAdapter(mPatientModels,"schedule");
                    req_list_viewf.setLayoutManager(new LinearLayoutManager(getContext()));
                    req_list_viewf.setAdapter(mPatientAdapter);
                }
            }
        });
        return view;
    }

}
