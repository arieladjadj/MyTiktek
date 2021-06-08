package com.example.mytiktek;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.List;

public class SubjectAdapter  extends ArrayAdapter<Subject> {

    Context context;
    List<Subject> objects;


    public SubjectAdapter(Context context, int resource, int textViewResourceId, List<Subject> objects) {
        super(context, resource, textViewResourceId, objects);

        this.context=context;
        this.objects=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.subject_layout,parent,false);

        TextView tvSubName = (TextView)view.findViewById(R.id.tvSubjectName);
        ImageView ivSubImage=(ImageView)view.findViewById(R.id.ivSubjectImage);
        Subject temp = objects.get(position);
       /* if(!(temp.getSubjectName().equals("Math") || temp.getSubjectName().equals("Physics"))){
            //temp.setSubjectImageUrl("https://cdn.icon-icons.com/icons2/2518/PNG/512/math_icon_151208.png");
        }else {*/
            //  temp.setSubjectImageUrl("https://firebasestorage.googleapis.com/v0/b/my-tiktek.appspot.com/o/Subjects%2FMath%2FSubjectImage%2Fmath_icon.png?alt=media&token=a22e29cc-4a7f-44be-8947-5a89ce9b7d22");
            //set the subject image
            //Picasso.get().load(temp.getSubjectImageUrl()).into(ivSubImage);
            //ivSubImage.setImageBitmap(SubjectImages.subjectImages.get(0));
        //}
        ivSubImage.setImageBitmap(temp.getSubjectImageBitmap());
        tvSubName.setText(String.valueOf(temp.getSubjectName()));

        return view;
    }

}

