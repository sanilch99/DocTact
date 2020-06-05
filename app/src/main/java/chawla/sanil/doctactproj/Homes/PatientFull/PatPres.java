package chawla.sanil.doctactproj.Homes.PatientFull;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import chawla.sanil.doctactproj.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PatPres extends android.support.v4.app.Fragment {

    private FirebaseAuth mAuth;
    private RecyclerView req_list_view;
    private FirebaseFirestore mFirebaseFirestore;

    ArrayList<String> patientIds;
    List<String> presList;

    public customAdapter mDoctorAdapter;



    public PatPres() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_pat_pres, container, false);
        req_list_view=(RecyclerView)view.findViewById(R.id.recP);
        mFirebaseFirestore= FirebaseFirestore.getInstance();
        mAuth= FirebaseAuth.getInstance();
        presList=new ArrayList<>();
        patientIds = new ArrayList<>();

        mFirebaseFirestore.collection("Patients").document(mAuth.getCurrentUser().getUid()).collection("Prescriptions").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        String DocId = doc.getDocument().getId();
                        presList.add(doc.getDocument().get("prescription").toString());
                        mDoctorAdapter = new customAdapter(presList);
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
