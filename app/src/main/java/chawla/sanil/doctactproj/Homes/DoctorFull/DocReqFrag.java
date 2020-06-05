package chawla.sanil.doctactproj.Homes.DoctorFull;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import chawla.sanil.doctactproj.Homes.PatientFull.DoctorModel;
import chawla.sanil.doctactproj.PatientModel;
import chawla.sanil.doctactproj.R;


public class DocReqFrag extends Fragment {
    private FirebaseAuth mAuth;
    private RecyclerView req_list_view;
    private FirebaseFirestore mFirebaseFirestore;
    ArrayList<String> patientIds;
    List<PatientModel> mPatientModels;
    public PatientAdapter mPatientAdapter;
    public DocReqFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_doc_req, container, false);
        req_list_view=(RecyclerView)view.findViewById(R.id.ReqRecView);
        mFirebaseFirestore=FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        mPatientModels=new ArrayList<>();
        patientIds = new ArrayList<>();

            mFirebaseFirestore.collection("Doctors").document(mAuth.getCurrentUser().getUid()).collection("Requests").addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                        PatientModel model = doc.getDocument().toObject(PatientModel.class);
                        mPatientModels.add(model);
                        mPatientAdapter=new PatientAdapter(mPatientModels,"requests");
                        req_list_view.setLayoutManager(new LinearLayoutManager(getContext()));
                        req_list_view.setAdapter(mPatientAdapter);
                    }
                }
            });
        return view;
    }

}
