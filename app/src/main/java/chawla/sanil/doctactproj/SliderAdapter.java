package chawla.sanil.doctactproj;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class SliderAdapter extends PagerAdapter  {

    Context context;
    LayoutInflater mLayoutInflater;

    public SliderAdapter(Context context)
    {
        this.context=context;
    }

    public int [] slideImages={
            R.drawable.patientslide,R.drawable.maindoctor,R.drawable.medicineslide,R.drawable.reportslide
    };

    public String[] slide_Headings={
            "Patients","Doctors","Pharmacists","Labs"
    };

    public String[] slide_desc={
            "Patients can view their profile, locate doctors and have complete health care facilities at the palm of their hand","Doctors can asdasdad d asd asd as dasd asd as das da asd as d","Pharmacists can asldjalsj ashf ashfka shk ahskfha skfh kashfk ahskf hakj ","Labs can send asljk dhakshd kahsfk h sfahkf askfhd slhf"
    };

    @Override
    public int getCount() {
        return slide_Headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view== (ConstraintLayout)o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        mLayoutInflater=(LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view=mLayoutInflater.inflate(R.layout.slide_layout,container,false);

        CircleImageView featurePic=(CircleImageView)view.findViewById(R.id.featureImg);
        TextView header_same=(TextView)view.findViewById(R.id.introText);
        TextView featurename=(TextView)view.findViewById(R.id.featureName);
        TextView featuredesc=(TextView)view.findViewById(R.id.descFeature);

        featurePic.setImageResource(slideImages[position]);
        featuredesc.setText(slide_desc[position]);
        featurename.setText(slide_Headings[position]);

        container.addView(view);

        return view;

    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
