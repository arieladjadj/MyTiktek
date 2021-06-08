package com.example.mytiktek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BooksActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private String currentSubjectName;

    private TextView subjectName;
    ListView lvBooks;
    ArrayList<Book> booksList;
    BookAdapter bookAdapter;
    private ImageView btnHome, subjectImage;
    private ProgressBar pbLoadingBooks;
    Book bookSelected;

    private boolean bookDownloaded; //true if the books and their images done to download
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference booksRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_books);

        bookDownloaded = false;
        btnHome = (ImageView)findViewById(R.id.btnHome);
        btnHome.setOnClickListener(this);
        subjectImage = (ImageView)findViewById(R.id.booksActivitySubjectImage);
        subjectName = (TextView)findViewById(R.id.subjectName);
        pbLoadingBooks = (ProgressBar)findViewById(R.id.pbLoadingBooks);

        Intent intent = getIntent();
        currentSubjectName = intent.getExtras().getString("SubjectName");
        subjectName.setText(currentSubjectName);
        for(Subject subject : SubjectsList.subjects){
            if(subject.getSubjectName().equals(currentSubjectName)){
                subjectImage.setImageBitmap(subject.getSubjectImageBitmap());
                break;
            }
        }
        booksRef = db.collection("subjects").document(currentSubjectName);
        lvBooks = (ListView)findViewById(R.id.lvSBooks);
        lvBooks.setOnItemClickListener(this);
        initBooks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(bookDownloaded) pbLoadingBooks.setVisibility(View.INVISIBLE);
    }

    private void initBooks() {
        pbLoadingBooks.setVisibility(View.VISIBLE);
        CurrentSubjectBooks.books = new ArrayList<Book>();

        booksRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                SubjectBooks subjectBooks = documentSnapshot.toObject(SubjectBooks.class);

                for(Book book : subjectBooks.getBooks().values() ){
                    CurrentSubjectBooks.books.add(book);
                }

                downloadBookCoverImages();
                //if(CurrentSubjectBooks.books.get(0).getBookCoverUrl().equals("https://firebasestorage.googleapis.com/v0/b/my-tiktek.appspot.com/o/Books%20Images%2Fmath_book1_cover.jpg?alt=media&token=6af03d34-60e5-4d31-9dc5-d59dd7eaf358")){
                //    CurrentSubjectBooks.books.get(0).setBookImage(BitmapFactory.decodeResource(getResources(), R.drawable.math_book1_cover));
                //} //to reduce time while waiting for server to sent book cover image
           //     bookAdapter.notifyDataSetChanged();
            }
        });
    }

    private void downloadBookCoverImages(){
        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance

        for (int i = 0; i < CurrentSubjectBooks.books.size(); i++) {
            int finalI = i;  //If there is no subject image bitmap - download it from firebase server
            if(CurrentSubjectBooks.books.get(i).getBookImage() == null) {
                imageLoader.loadImage(CurrentSubjectBooks.books.get(i).getBookCoverUrl(), new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        CurrentSubjectBooks.books.get(finalI).setBookImage(loadedImage);

                        if (SubjectsList.haveImages()) {
                            showBooks();
                        }
                    }
                });
            }
            if(CurrentSubjectBooks.haveImages()){
                showBooks();
            }
        }
    }

    private void showBooks(){
        pbLoadingBooks.setVisibility(View.INVISIBLE);
        bookAdapter = new BookAdapter(this,0,0, CurrentSubjectBooks.books);
        lvBooks.setAdapter(bookAdapter);
        bookDownloaded = true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //bookSelected = bookAdapter.getItem(position);
        Intent intent = new Intent(BooksActivity.this, ExerciseSelection.class);
        //intent.putExtra("BookName", bookSelected.getBookName());
        //intent.putExtra("SubjectPicture"...)
        intent.putExtra("BookPosition", position);
        startActivityForResult(intent, 0);
    }

    @Override
    public void onClick(View v) {
        if(v == btnHome){
            setResult(0);
            finish();
        }
    }
}
