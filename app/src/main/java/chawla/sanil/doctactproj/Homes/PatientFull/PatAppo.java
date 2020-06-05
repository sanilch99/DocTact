package chawla.sanil.doctactproj.Homes.PatientFull;


import android.os.Bundle;
import android.app.Fragment;
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

import chawla.sanil.doctactproj.Homes.DoctorFull.PatientAdapter;
import chawla.sanil.doctactproj.PatientModel;
import chawla.sanil.doctactproj.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatAppo extends android.support.v4.app.Fragment {
    private FirebaseAuth mAuth;
    private RecyclerView req_list_view;
    private FirebaseFirestore mFirebaseFirestore;
    ArrayList<String> patientIds;
    List<DoctorModel> mDoctorModels;
    public DoctorAdapter mDoctorAdapter;


    public PatAppo() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_pat_appo, container, false);
        req_list_view=(RecyclerView)view.findViewById(R.id.recAppView);
        mFirebaseFirestore= FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();
        mDoctorModels=new ArrayList<>();
        patientIds = new ArrayList<>();

        mFirebaseFirestore.collection("Patients").document(mAuth.getCurrentUser().getUid()).collection("Appointments").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        String DocId = doc.getDocument().getId();
                        DoctorModel mDoc = doc.getDocument().toObject(DoctorModel.class).withId(DocId);
                        mDoctorModels.add(mDoc);
                        mDoctorAdapter=new DoctorAdapter(mDoctorModels,"appointments");
                        req_list_view.setLayoutManager(new LinearLayoutManager(getContext()));
                        req_list_view.setAdapter(mDoctorAdapter);
                        mDoctorAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
        return view;
    }

}
