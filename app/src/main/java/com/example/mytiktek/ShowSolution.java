package com.example.mytiktek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firestore.v1.FirestoreGrpc;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import static com.google.android.gms.common.util.CollectionUtils.mapOf;

public class ShowSolution extends AppCompatActivity implements View.OnClickListener {

    private PhotoView solutionImage2;
    private ImageView btnHome;
    private TextView tvBookName;
    private ImageView ivBookCover;
    private RatingBar ratingBar;
    private TextView tvSolutionRate;
    private Button btnSubmitRate;
    private boolean rated; //if user rate the solution already
    private FirebaseFirestore fStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_show_solution);

        rated = false;
        fStore = FirebaseFirestore.getInstance();
        btnHome = (ImageView)findViewById(R.id.btnHome);
        tvBookName = (TextView)findViewById(R.id.showBookName);
        tvSolutionRate = (TextView)findViewById(R.id.tvSolutionRate);
        ivBookCover = (ImageView)findViewById(R.id.showBookImage);
        solutionImage2 = (PhotoView)findViewById(R.id.solutionImage2);
        btnSubmitRate = (Button)findViewById(R.id.btnSubmitRate);

        btnHome.setOnClickListener(this);
        btnSubmitRate.setOnClickListener(this);
        tvBookName.setText(CurrentSolutionImage.bookName + "\n Page " + CurrentSolutionImage.pageNumber +
                " question " +CurrentSolutionImage.questionNumber +"\nBy " + CurrentSolutionImage.publisher);
        tvSolutionRate.setText("Rate: "+ CurrentSolutionImage.solutionRate);
        ivBookCover.setImageBitmap(CurrentSolutionImage.bookCoverImage);
        solutionImage2.setImageBitmap(CurrentSolutionImage.solutionImage);
        ratingBar = (RatingBar)findViewById(R.id.rbRate);

        saveHistory();
    }

    private void saveHistory() {
        if(FirebaseAuth.getInstance().getCurrentUser() != null) {
            HistoryDB db = new HistoryDB(ShowSolution.this);
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            Bitmap solutionBitmap = CurrentSolutionImage.bookCoverImage;
            solutionBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
            byte[] solutionImg = byteArray.toByteArray();
            String encodedImage = Base64.encodeToString(solutionImg, Base64.DEFAULT);


            String solutionData = CurrentSolutionImage.bookName + "\n Page " + CurrentSolutionImage.pageNumber +
                    " question " +CurrentSolutionImage.questionNumber +"\nBy " + CurrentSolutionImage.publisher;

            boolean insert = db.addData(String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getUid()),
                    solutionData, encodedImage); // solutionImg);
        }
    }

    @Override
    public void onClick(View v) {
        if(v == btnHome){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else if(v == btnSubmitRate){
            if(rated){
                Toast.makeText(this, "You already rated this solution",Toast.LENGTH_SHORT).show();
            }else {
                double rate = ratingBar.getRating();
                rate = (rate + Double.valueOf(CurrentSolutionImage.solutionRate)) / 2;
                String pathToSolutionRate = "books." + CurrentSolutionImage.bookPos + ".pages." +
                        CurrentSolutionImage.pageNumber + "." + CurrentSolutionImage.questionNumber + ".rate";
                Map<String, Object> updatedERate = new HashMap<String, Object>();
                updatedERate.put(pathToSolutionRate, String.valueOf(rate));
                String sName = CurrentSubjectBooks.getSubjectName();
                fStore.collection("subjects").document(CurrentSubjectBooks.getSubjectName()) //update firebase
                        .update(updatedERate);
                tvSolutionRate.setText("Rate: " + String.valueOf(rate)); //update screen
                CurrentSubjectBooks.books.get(Integer.valueOf(CurrentSolutionImage.bookPos)).getPages()  //update local data
                        .get(CurrentSolutionImage.pageNumber).get(CurrentSolutionImage.questionNumber).
                        put("rate", String.valueOf(rate));
                rated = true;
            }
        }
    }
}