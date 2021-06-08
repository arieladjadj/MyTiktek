package com.example.mytiktek;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Map;

public class BookAdapter extends ArrayAdapter<Book> {
    Context context;
    List<Book> objects;

    public BookAdapter(Context context, int resource, int textViewResourceId, List<Book> objects) {
        super(context, resource, textViewResourceId, objects);

        this.context=context;
        this.objects=objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.book_layout,parent,false);

        TextView tvBookName = (TextView)view.findViewById(R.id.tvBookName);
        TextView tbAuthors = (TextView)view.findViewById(R.id.tvAuthors);
        ImageView ivBookImage=(ImageView)view.findViewById(R.id.ivBookImage);
        TextView tvDescription1 = (TextView)view.findViewById(R.id.tvDescription1);
        TextView tvDescription2 = (TextView)view.findViewById(R.id.tvDescription2);
        Book book = objects.get(position);

        if(book.getBookImage() != null){  //to reduce time while waiting for server to sent book cover image
            ivBookImage.setImageBitmap(book.getBookImage());
        }
        else Picasso.get().load(book.getBookCoverUrl()).into(ivBookImage);
    //    ivSubImage.setImageBitmap(temp.getBookImage());
        tvBookName.setText(book.getBookName());
        tbAuthors.setText(book.getAuthors());
        tvDescription1.setText(book.getDescription1());
        tvDescription2.setText(book.getDescription2());

        return view;
    }
}
