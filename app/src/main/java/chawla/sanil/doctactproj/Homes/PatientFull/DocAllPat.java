package chawla.sanil.doctactproj.Homes.PatientFull;


import android.os.Bundle;
import android.support.annotation.NonNull;
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

import chawla.sanil.doctactproj.R;


public class DocAllPat extends Fragment {

    private FirebaseAuth mAuth;
    private RecyclerView doc_list_view;
    private FirebaseFirestore mFirebaseFirestore;
    private DocumentSnapshot lastVisible;
    private boolean isFirstPageFirstLoad = true;
    private DoctorAdapter mDoctorAdapter;
    private List<DoctorModel> mDoctorModels;


    public DocAllPat() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_doc_all_pat, container, false);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        doc_list_view=(RecyclerView)view.findViewById(R.id.DocRecView);
        mDoctorModels=new ArrayList<>();

        mDoctorAdapter=new DoctorAdapter(mDoctorModels,"");
        doc_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        doc_list_view.setAdapter(mDoctorAdapter);

        //HELPS TO RETRIEVE REAL TIME DATA USING SNAPSHOT LISTENER
        if (mAuth.getCurrentUser() != null) {
            mFirebaseFirestore = FirebaseFirestore.getInstance();
            doc_list_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    Boolean isReachedBottom = !recyclerView.canScrollVertically(1);// +1 for top to bottom and -1 for reverse
                    if (isReachedBottom) {
                        String desc = lastVisible.getString("desc");
                        LoadMoreDocs();

                    }

                }
            });

            Query firstQuery = mFirebaseFirestore.collection("Doctors").limit(3);
            firstQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                    if (isFirstPageFirstLoad) {
                        lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                    }
                    if (documentSnapshots != null) {
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {
                                String DocId = doc.getDocument().getId();
                                DoctorModel doctorModel = doc.getDocument().toObject(DoctorModel.class).withId(DocId);
                                if (isFirstPageFirstLoad) {
                                    mDoctorModels.add(doctorModel);
                                } else {
                                    mDoctorModels.add(0, doctorModel);
                                }
                                mDoctorAdapter.notifyDataSetChanged();
                            }
                        }
                        isFirstPageFirstLoad = false;
                    }
                }
            });
        }


        // Inflate the layout for this fragment
        return view;


    }
    public void LoadMoreDocs() {
        Query nextQuery = mFirebaseFirestore.collection("Doctors")
                .startAfter(lastVisible)
                .limit(3);
        nextQuery.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                if (!documentSnapshots.isEmpty()) {
                    lastVisible = documentSnapshots.getDocuments().get(documentSnapshots.size() - 1);
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                        if (doc.getType() == DocumentChange.Type.ADDED) {
                            String DocId = doc.getDocument().getId();
                            DoctorModel mDoc = doc.getDocument().toObject(DoctorModel.class).withId(DocId);
                            mDoctorModels.add(mDoc);
                            mDoctorAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });

    }

}
