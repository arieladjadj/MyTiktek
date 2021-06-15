package com.example.mytiktek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ExerciseSelection extends AppCompatActivity implements View.OnClickListener {

    private NumberPicker npQuestionNumber, npPageNumber;
    private Button btnSearch;
    private TextView tvBookName;
    private ImageView ivBookCoverImage;
    private ImageView btnHome;
    private ProgressBar progressBar;

    private Book currentBook;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference booksRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_exercise_selection);

        currentBook = CurrentSubjectBooks.books.get(getIntent().getExtras().getInt("BookPosition"));

        tvBookName = (TextView)findViewById(R.id.bookName);
        tvBookName.setText(currentBook.getBookName());
        ivBookCoverImage = (ImageView)findViewById(R.id.bookImage);
        btnHome = (ImageView)findViewById(R.id.btnHome);
        btnHome.setOnClickListener(this);
        progressBar = (ProgressBar)findViewById(R.id.pbLoadingImage);

        if(currentBook.getBookImage() != null) {
            ivBookCoverImage.setImageBitmap(currentBook.getBookImage()); //to reduce time while waiting for server to sent book cover image
            CurrentSolutionImage.bookCoverImage = currentBook.getBookImage();
        } else{
                Picasso.get().load(currentBook.getBookCoverUrl()).into(ivBookCoverImage);
            }

        npQuestionNumber = (NumberPicker) findViewById(R.id.npQuestionNumber);
        npQuestionNumber.setMinValue(1);
        npQuestionNumber.setMaxValue(160);
        npPageNumber = (NumberPicker) findViewById(R.id.npPageNumber);
        npPageNumber.setMinValue(1);
        npPageNumber.setMaxValue(Integer.valueOf(currentBook.getNumberOfPages()));
        btnSearch = (Button) findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if ( v == btnSearch) {
            searchForSolution();
        }else if(v == btnHome){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    //ask for the server for a picture of the solution - if exists, send if to ShowSolutionActivity
    // if not, show a toast.
    private void searchForSolution() {
        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance
        String pageNumber = String.valueOf(npPageNumber.getValue());
        String questionNumber = String.valueOf(npQuestionNumber.getValue());

        if(!isQuestionExists(pageNumber, questionNumber)){
            Toast.makeText(this, "Solution not found", Toast.LENGTH_SHORT).show();
        }else{
            progressBar.setVisibility(View.VISIBLE);
            //download solution image to static var
            imageLoader.loadImage(currentBook.getPages().get(pageNumber).get(questionNumber).get("solutionUrl"), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    CurrentSolutionImage.solutionImage = loadedImage;
                   // CurrentSolutionImage.bookCoverImage = currentBook.getBookImage();
                    CurrentSolutionImage.bookName = currentBook.getBookName();
                    CurrentSolutionImage.pageNumber = pageNumber;
                    CurrentSolutionImage.questionNumber = questionNumber;
                    CurrentSolutionImage.solutionRate = currentBook.getPages().get(pageNumber).get(questionNumber).get("rate");
                    CurrentSolutionImage.publisher = currentBook.getPages().get(pageNumber).get(questionNumber).get("publisher");
                    CurrentSolutionImage.bookPos = String.valueOf(getIntent().getExtras().getInt("BookPosition"));

                    Intent intent = new Intent(ExerciseSelection.this, ShowSolution.class);
                    startActivity(intent);
                }
            });
        }
    }

    private boolean isQuestionExists(String pageNumber, String questionNumber) {
        return currentBook.getPages().containsKey(pageNumber) &&
                currentBook.getPages().get(pageNumber).containsKey(questionNumber);

    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.INVISIBLE);
    }
}