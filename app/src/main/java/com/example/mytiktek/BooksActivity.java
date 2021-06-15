package com.example.mytiktek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytiktek.Adapters.BookAdapter;
import com.example.mytiktek.DataObjects.Book;
import com.example.mytiktek.DataObjects.Subject;
import com.example.mytiktek.DataObjects.SubjectBooks;
import com.example.mytiktek.DataObjects.SubjectsList;
import com.example.mytiktek.LocalData.CurrentSubjectBooks;
import com.example.mytiktek.service.NetworkChangeListener;
import com.example.mytiktek.service.NetworkHelper;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;

public class BooksActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    private String currentSubjectName;

    private TextView subjectName;
    ListView lvBooks;
    ArrayList<Book> booksList;
    BookAdapter bookAdapter;
    private ImageView  subjectImage;
    private ProgressBar pbLoadingBooks;
    Book bookSelected;
    private ImageView btnHome, btnUploadSolution;

    private boolean bookDownloaded; //true if the books and their images done to download
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference booksRef;
    private NetworkChangeListener networkChangeListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_books);

        bookDownloaded = false;
        subjectImage = (ImageView)findViewById(R.id.booksActivitySubjectImage);
        subjectName = (TextView)findViewById(R.id.subjectName);
        pbLoadingBooks = (ProgressBar)findViewById(R.id.pbLoadingBooks);
        btnUploadSolution = (ImageView)findViewById(R.id.btnUploadSolution);
        btnUploadSolution.setOnClickListener(this);
        btnHome = (ImageView)findViewById(R.id.btnHome);
        btnHome.setOnClickListener(this);

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
        checkNetworkConnection();
    }

    private void checkNetworkConnection() {
        if(!NetworkHelper.isConnectedToInternet(this)) {
            Toast.makeText(this, "Waiting for Internet connection", Toast.LENGTH_SHORT).show();
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkNetworkConnection();
                }
            }, 3000);
        }else{
            initBooks();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(bookDownloaded) pbLoadingBooks.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onStart() {
        networkChangeListener = new NetworkChangeListener();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, intentFilter);
        super.onStart();
    }
    @Override
    protected void onStop() {
        if(networkChangeListener != null){
            unregisterReceiver(networkChangeListener);
            networkChangeListener = null;
        }
        super.onStop();
    }
    private void initBooks() {
        pbLoadingBooks.setVisibility(View.VISIBLE);
        CurrentSubjectBooks.books = new ArrayList<Book>();
        CurrentSubjectBooks.setSubjectName(currentSubjectName);

        booksRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                SubjectBooks subjectBooks = documentSnapshot.toObject(SubjectBooks.class);

                for(Book book : subjectBooks.getBooks().values() ){
                    CurrentSubjectBooks.books.add(book);
                }

                downloadBookCoverImages();
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
        }else if(v == btnUploadSolution){
            Toast.makeText(this, "Upload solution will be available on v2.0", Toast.LENGTH_SHORT).show();
        }
    }

}
