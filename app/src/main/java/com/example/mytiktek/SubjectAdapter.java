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
        ivSubImage.setImageBitmap(temp.getSubjectImageBitmap());
        tvSubName.setText(String.valueOf(temp.getSubjectName()));

        return view;
    }

}

