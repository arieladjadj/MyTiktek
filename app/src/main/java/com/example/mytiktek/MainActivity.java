package com.example.mytiktek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.transition.Slide;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    ListView lvSubjects;
    ArrayList<Subject> subjectList;
    SubjectAdapter subjectAdapter;
    Bitmap home0;
    Subject subjectSelected;
    private Button btnMyProfile, btnSolutionsHistory;
    private ImageView btnHome, btnUploadSolution;

    private Dialog dialog;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference subjectsRef = db.collection("subjects");
    private CollectionReference usersRef = db.collection("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        btnUploadSolution = (ImageView)findViewById(R.id.btnUploadSolution);
        btnUploadSolution.setOnClickListener(this);
        lvSubjects = (ListView)findViewById(R.id.lvSubjects);
        lvSubjects.setOnItemClickListener(this);
        btnHome = (ImageView)findViewById(R.id.btnHome);
        btnMyProfile = (Button)findViewById(R.id.btnMyProfile);
        btnMyProfile.setOnClickListener(this);
        btnSolutionsHistory = (Button)findViewById(R.id.btnSolutionsHistory);
        btnSolutionsHistory.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        InitScreen();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    private void InitScreen() {
        home0 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_baseline_school_24);
        Bitmap home1 = BitmapFactory.decodeResource(getResources(), R.drawable.ic_subject);

        subjectAdapter = new SubjectAdapter(this,0,0, SubjectsList.subjects);

        lvSubjects.setAdapter(subjectAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        subjectSelected = subjectAdapter.getItem(position);
        Intent intent = new Intent(MainActivity.this, BooksActivity.class);
        intent.putExtra("SubjectName", subjectSelected.getDocumentId());
        //intent.putExtra("SubjectPicture"...)
        startActivityForResult(intent, 0);
    }

    public void createLoginOrRegisterDialog(Class nextActivity)
    {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            Intent intent;
            if(firebaseAuth.getCurrentUser().isEmailVerified()){
               intent  = new Intent(this, nextActivity);
            }else{
               // intent = new Intent(this, EmailVerification.class);
                intent  = new Intent(this, nextActivity);

            }
            startActivityForResult(intent, 0);
            return;
        }
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.register_or_login_dialog);
        dialog.setTitle("My Profile");
        dialog.setCancelable(true);
        TextView login = (TextView)dialog.findViewById(R.id.tvLogin);
        TextView register = (TextView)dialog.findViewById(R.id.tvRegister);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        dialog.show();

    }
    @Override
    public void onClick(View v) {
        if(v == btnMyProfile){
            createLoginOrRegisterDialog(ShowProfileActivity.class);
        }else if(v == btnSolutionsHistory) {
            createLoginOrRegisterDialog(SolutionsHistoryActivity.class);
        }else if(v == btnUploadSolution){
            Toast.makeText(this, "Upload solution will be available on v2.0", Toast.LENGTH_SHORT).show();
        }
    }
}