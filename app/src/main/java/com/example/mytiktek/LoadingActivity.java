package com.example.mytiktek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

public class LoadingActivity extends AppCompatActivity {

    private boolean hasChanged;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference subjectsRef = db.collection("subjects");
    private List<Bitmap> bitmaps = new ArrayList<Bitmap>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_loading);

        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(LoadingActivity.this));
        getImagedUrls();
    }

    private void getImagedUrls(){
        subjectsRef.get()
            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Subject subject = documentSnapshot.toObject(Subject.class);
                        subject.setDocumentId(documentSnapshot.getId());
                        Log.d("Ariel", subject.getDocumentId() );
                        //To reduce download time from firebase server - using resources from drawable
                        switch (subject.getSubjectName()){
                            case "History":
                                subject.setSubjectImageBitmap(BitmapFactory.decodeResource(LoadingActivity.this.getResources(),
                                        R.drawable.history_icon));
                                        break;
                            case "Bible":
                                subject.setSubjectImageBitmap(BitmapFactory.decodeResource(LoadingActivity.this.getResources(),
                                        R.drawable.bible_icon));
                                break;
                            case "Computer Science":
                                subject.setSubjectImageBitmap(BitmapFactory.decodeResource(LoadingActivity.this.getResources(),
                                        R.drawable.computer_sciences_icon));
                                break;
                            case "English":
                                subject.setSubjectImageBitmap(BitmapFactory.decodeResource(LoadingActivity.this.getResources(),
                                        R.drawable.english_icon));
                                break;
                            case "Hebrew":
                                subject.setSubjectImageBitmap(BitmapFactory.decodeResource(LoadingActivity.this.getResources(),
                                        R.drawable.hebrew_icon));
                                break;
                            case "Chemistry":
                                subject.setSubjectImageBitmap(BitmapFactory.decodeResource(LoadingActivity.this.getResources(),
                                        R.drawable.chemi_icon));
                                break;
                            case "Math":
                                subject.setSubjectImageBitmap(BitmapFactory.decodeResource(LoadingActivity.this.getResources(),
                                        R.drawable.math_icon));
                                break;
                            case "Physics":
                                subject.setSubjectImageBitmap(BitmapFactory.decodeResource(LoadingActivity.this.getResources(),
                                        R.drawable.physics_icon));
                                break;
                            case "Citizenship":
                                subject.setSubjectImageBitmap(BitmapFactory.decodeResource(LoadingActivity.this.getResources(),
                                        R.drawable.citizenship_icon));
                                break;
                            case "Geography":
                                subject.setSubjectImageBitmap(BitmapFactory.decodeResource(LoadingActivity.this.getResources(),
                                        R.drawable.geo_icon));
                                break;
                            case "Hebrew Literature":
                                subject.setSubjectImageBitmap(BitmapFactory.decodeResource(LoadingActivity.this.getResources(),
                                        R.drawable.let));
                                break;
                            case "Industry and Management":
                                subject.setSubjectImageBitmap(BitmapFactory.decodeResource(LoadingActivity.this.getResources(),
                                        R.drawable.mang_icon));
                                break;
                            case "Biology":
                                subject.setSubjectImageBitmap(BitmapFactory.decodeResource(LoadingActivity.this.getResources(),
                                        R.drawable.biology_icon));
                                break;
                            case "Talmud":
                                subject.setSubjectImageBitmap(BitmapFactory.decodeResource(LoadingActivity.this.getResources(),
                                R.drawable.talmud_icon));
                                break;
                        }
                        SubjectsList.subjects.add(subject);
                    }
                    downloadAndStoreImage();
                }
            });
    }

    //Download subject images (part of them) and insert them to public class SubjectImages
    public void downloadAndStoreImage() {
        ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance

        for (int i = 0; i < SubjectsList.subjects.size(); i++) {
            int finalI = i;  //If there is no subject image bitmap - download it from firebase server
            if(SubjectsList.subjects.get(i).getSubjectImageBitmap() == null) {
                imageLoader.loadImage(SubjectsList.subjects.get(i).getSubjectImageUrl(), new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        SubjectsList.subjects.get(finalI).setSubjectImageBitmap(loadedImage);

                        if (SubjectsList.haveImages()) {//finalI ==   SubjectsList.subjects.size()-1 ){
                            Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    }
                });
            }
            if(SubjectsList.haveImages()){
                Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }

    }
    
    void reduceTimeFromServer(){

    }
}