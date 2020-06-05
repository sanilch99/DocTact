package chawla.sanil.doctactproj.Homes.DoctorFull;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chawla.sanil.doctactproj.PatientModel;
import chawla.sanil.doctactproj.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.ViewHolder> {

    private Context mContext;
    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseAuth mAuth;
    private List<PatientModel> fulllist;
    private String type;
    DocPresFrag doc;

    public PatientAdapter(List<PatientModel> fulllist, String type) {
        this.fulllist = fulllist;
        this.type = type;
    }

    @NonNull
    @Override
    public PatientAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.appointment_card, viewGroup, false);
        mContext=viewGroup.getContext();
        mAuth=FirebaseAuth.getInstance();
        mFirebaseFirestore=FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientAdapter.ViewHolder viewHolder, int i) {
        final String addr_data = fulllist.get(i).getAddress();
        final String name_data = fulllist.get(i).getName();
        String gender_data = fulllist.get(i).getGender();
        final String imageURL_data = fulllist.get(i).getImage();
        String age_data = fulllist.get(i).getAge();
        final String spec_data = fulllist.get(i).getProblems();
        String pid=fulllist.get(i).getId();
        System.out.println(name_data);

        viewHolder.setData(name_data,imageURL_data,spec_data,i,pid);
    }


    @Override
    public int getItemCount() {
        return fulllist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private TextView pname, ppro, daddress, dage, dgen;
        private CircleImageView ppic;
        private ConstraintLayout full;
        private Button accept,reject,addpre;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            return;
        }


        public void setData(String name, final String imageURI, String spec, final int p, final String pid) {
            pname = (TextView) mView.findViewById(R.id.patName);
            ppro = (TextView) mView.findViewById(R.id.patPROB);
            ppic = (CircleImageView) mView.findViewById(R.id.patPic);
            accept = (Button) mView.findViewById(R.id.button3);
            reject = (Button) mView.findViewById(R.id.button4);
            addpre=(Button)mView.findViewById(R.id.button8);

            full=(ConstraintLayout)mView.findViewById(R.id.card);
            pname.setText(name);
            ppro.setText(spec);
            RequestOptions placeholderOptions = new RequestOptions();
            placeholderOptions.placeholder(R.drawable.ic_account_circle_black_24dp);
            Glide.with(mContext).applyDefaultRequestOptions(placeholderOptions).load(imageURI).into(ppic);

            if(type=="schedule" )
            {
                accept.setVisibility(View.GONE);
                reject.setVisibility(View.GONE);

            }
            if(type=="prescription")
            {
                accept.setVisibility(View.GONE);
                reject.setVisibility(View.GONE);
                addpre.setVisibility(View.VISIBLE);

                addpre.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Dialog dialog=new Dialog(mContext);
                        dialog.setContentView(R.layout.choose);
                        Button cImage=(Button)dialog.findViewById(R.id.button2);
                        Button cText=(Button)dialog.findViewById(R.id.button5);

                        cImage.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.setContentView(R.layout.customimage);
                                TextView ptId=(TextView)dialog.findViewById(R.id.textView3);
                                ptId.setText(pid);

                                CircleImageView presImage=(CircleImageView)dialog.findViewById(R.id.presImage);
                                presImage.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                         //Image Selected
                                    }
                                });




                            }
                        });

                        cText.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.setContentView(R.layout.customtext);

                                TextView ptId=(TextView)dialog.findViewById(R.id.tId);
                                ptId.setText(pid);

                                final EditText pres=(EditText)dialog.findViewById(R.id.editPres);


                                Button subPres=(Button)dialog.findViewById(R.id.button7);
                                subPres.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Map<String, String> presMap=new HashMap<>();
                                        presMap.put("prescription",pres.getText().toString());
                                        presMap.put("id",pid);
                                        mFirebaseFirestore.collection("Patients").document(pid).collection("Prescriptions").document(mAuth.getCurrentUser().getUid()).set(presMap);
                                        addpre.setText("Edit Prescritpion");
                                        dialog.dismiss();
                                    }
                                });
                            }
                        });

                        dialog.show();


                    }
                });
            }


                accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String, String> reqMap = new HashMap<>();
                        reqMap.put("id", pid);
                        reqMap.put("name", pname.getText().toString());
                        reqMap.put("problems", ppro.getText().toString());
                        reqMap.put("image", imageURI);
                        mView.setVisibility(View.GONE);
                        deleteItem(p);
                        mFirebaseFirestore.collection("Doctors").document(mAuth.getCurrentUser().getUid()).collection("Requests").document(pid).delete();
                        mFirebaseFirestore.collection("Doctors").document(mAuth.getCurrentUser().getUid()).collection("Scehdule").document(pid).set(reqMap);

                        mFirebaseFirestore.collection("Doctors").document(mAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                Map<String, String> userMap = new HashMap<>();
                                userMap.put("name", task.getResult().getString("name"));
                                userMap.put("image", task.getResult().getString("image"));
                                userMap.put("speciality", task.getResult().getString("speciality"));
                                mFirebaseFirestore.collection("Patients").document(pid).collection("Appointments").document(mAuth.getCurrentUser().getUid()).set(userMap);
                            }
                        });
                    }
                });

                reject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mView.setVisibility(View.GONE);
                        deleteItem(p);
                        mFirebaseFirestore.collection("Doctors").document(mAuth.getCurrentUser().getUid()).collection("Requests").document(pid).delete();
                    }
                });

            }
                private void deleteItem(int position){
                    fulllist.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, fulllist.size());
                    mView.setVisibility(View.GONE);
                }

        }
    }
