package chawla.sanil.doctactproj.Homes.PatientFull;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import chawla.sanil.doctactproj.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class customAdapter extends RecyclerView.Adapter<customAdapter.ViewHolder> {

    List<String> prescritpions=new ArrayList<>();
    public Context context;
    private FirebaseFirestore mFirebaseFirestore;
    private FirebaseAuth mAuth;
    private String type;

    public customAdapter(List<String> prescritpions) {
        this.prescritpions = prescritpions;
    }

    @NonNull
    @Override
    public customAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_doc, viewGroup, false);
        context = viewGroup.getContext();
        mAuth = FirebaseAuth.getInstance();
        mFirebaseFirestore = FirebaseFirestore.getInstance();
        return new customAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull customAdapter.ViewHolder viewHolder, int i) {

        viewHolder.setData(prescritpions.get(i));

    }

    @Override
    public int getItemCount() {
        return prescritpions.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder
    {
        private View mView;
        private TextView pres;


        public ViewHolder(View view) {
            super(view);
            mView = view;
        }


        public void setData(String presStr) {
            pres = (TextView) mView.findViewById(R.id.docName);
            pres.setText(presStr);

        }



    }


}
