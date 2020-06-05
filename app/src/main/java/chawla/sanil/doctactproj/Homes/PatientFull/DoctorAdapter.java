package chawla.sanil.doctactproj.Homes.PatientFull;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chawla.sanil.doctactproj.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {

    private List<DoctorModel> mDoctorModels;
    public Context context;
    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseAuth mAuth;
    private String type;

    public DoctorAdapter(List<DoctorModel> doctorModels, String type) {
        mDoctorModels = doctorModels;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_doc, viewGroup, false);
        context = viewGroup.getContext();
        mAuth = FirebaseAuth.getInstance();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final String docId = mDoctorModels.get(i).DocId;
        final String addr_data = mDoctorModels.get(i).getAddress();
        final String name_data = mDoctorModels.get(i).getName();
        String gender_data = mDoctorModels.get(i).getGender();
        final String imageURL_data = mDoctorModels.get(i).getImage();
        String age_data = mDoctorModels.get(i).getAge();
        final String spec_data = mDoctorModels.get(i).getSpeciality();

        viewHolder.setData(name_data, imageURL_data, spec_data);

        if(type=="appointments")
        {
            viewHolder.full.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //do nothing
                }
            });
        }

        viewHolder.full.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.book_doc);
                TextView docNa = (TextView) dialog.findViewById(R.id.biDocName);
                TextView docAd = (TextView) dialog.findViewById(R.id.biDocAddr);
                TextView docSp = (TextView) dialog.findViewById(R.id.biDocSpec);
                ImageView docpi = (ImageView) dialog.findViewById(R.id.bidDocPic);
                docNa.setText(name_data);
                docAd.setText(addr_data);
                docSp.setText(spec_data);
                RequestOptions placeholderOptions = new RequestOptions();
                placeholderOptions.placeholder(R.drawable.ic_account_circle_black_24dp);
                Glide.with(context).applyDefaultRequestOptions(placeholderOptions).load(imageURL_data).into(docpi);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                Button dibg = (Button) dialog.findViewById(R.id.BookDoc);
                dibg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mFirebaseFirestore.collection("Patients").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                final String uid=mAuth.getCurrentUser().getUid();
                                final String cname=task.getResult().get("name").toString();
                                final String cpro=task.getResult().get("problems").toString();
                                final String cage=task.getResult().get("age").toString();
                                final String cgender=task.getResult().get("gender").toString();
                                final String cima=task.getResult().get("image").toString();
                                addAppointment(docId,cname,cpro,cage,cima,cgender,uid);
                            }
                        });
                        dialog.hide();
                    }
                });
                dialog.show();
            }
        });
    }


    public void addAppointment(String did,String sn,String sp,String sa,String si,String sg,String pid) {
        Map<String, String> reqMap = new HashMap<>();
        reqMap.put("id",pid);
        reqMap.put("name",sn);
        reqMap.put("problems",sp);
        reqMap.put("age",sa);
        reqMap.put("image",si);
        reqMap.put("gender",sg);

        //REQUEST IS SENT PATIENT TO DOCTOR
        mFirebaseFirestore.collection("Doctors").document(did).collection("Requests").document(mAuth.getCurrentUser().getUid()).set(reqMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                //ADD LOTTIE REQUEST SENT ANIMATION
                Toast.makeText(context,"Request Made",Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDoctorModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView dname, dspecial, daddress, dage, dgen;
        private CircleImageView dpic;
        private ConstraintLayout full;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            full = mView.findViewById(R.id.fullC);

        }


        public void setData(String name, String imageURI, String spec) {
            dname = (TextView) mView.findViewById(R.id.docName);
            dspecial = (TextView) mView.findViewById(R.id.docSpec);
            dpic = (CircleImageView) mView.findViewById(R.id.docPic);

            dname.setText(name);
            dspecial.setText(spec);
            RequestOptions placeholderOptions = new RequestOptions();
            placeholderOptions.placeholder(R.drawable.ic_account_circle_black_24dp);
            Glide.with(context).applyDefaultRequestOptions(placeholderOptions).load(imageURI).into(dpic);
        }

    }
}
