package com.example.mytiktek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.github.chrisbanes.photoview.PhotoView;

public class ShowSolution extends AppCompatActivity implements View.OnClickListener {

    private PhotoView solutionImage2;
    private ImageView btnHome;
    private TextView tvBookName;
    private ImageView ivBookCover;
    private RatingBar ratingBar;
    private Button btnSubmitRanting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_show_solution);

        btnHome = (ImageView)findViewById(R.id.btnHome);
        btnHome.setOnClickListener(this);

        tvBookName = (TextView)findViewById(R.id.showBookName);
        ivBookCover = (ImageView)findViewById(R.id.showBookImage);
     //   tvPageAndQuestion = (TextView)findViewById(R.id.tvPageAndQuestion);
        solutionImage2 = (PhotoView)findViewById(R.id.solutionImage2);
        tvBookName.setText(CurrentSolutionImage.bookName + "\n Page " + CurrentSolutionImage.pageNumber +
                " question " +CurrentSolutionImage.questionNumber);
        ivBookCover.setImageBitmap(CurrentSolutionImage.bookCoverImage);
        solutionImage2.setImageBitmap(CurrentSolutionImage.solutionImage);
        ratingBar = (RatingBar)findViewById(R.id.rbRate);
    }

    @Override
    public void onClick(View v) {
        if(v == btnHome){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}