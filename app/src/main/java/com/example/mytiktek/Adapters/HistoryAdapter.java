package com.example.mytiktek.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mytiktek.DataObjects.Book;
import com.example.mytiktek.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HistoryAdapter extends ArrayAdapter<Book> {
        Context context;
        List<Book> objects;

    public HistoryAdapter(Context context, int resource, int textViewResourceId, List<Book> objects) {
            super(context, resource, textViewResourceId, objects);

            this.context=context;
            this.objects=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.solution_layout,parent,false);

            TextView tvBookData = (TextView)view.findViewById(R.id.tvBookData);
            ImageView ivBookImage=(ImageView)view.findViewById(R.id.ivBookImage2);;
            Book book = objects.get(position);

            if(book.getBookImage() != null){  //to reduce time while waiting for server to sent book cover image
                ivBookImage.setImageBitmap(book.getBookImage());
            }
            else Picasso.get().load(book.getBookCoverUrl()).into(ivBookImage);
            //ivSubImage.setImageBitmap(temp.getBookImage());
            tvBookData.setText(book.getBookName());

            return view;
            }
}