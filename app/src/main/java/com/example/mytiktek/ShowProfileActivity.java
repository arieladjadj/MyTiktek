package com.example.mytiktek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mytiktek.DataObjects.User;
import com.example.mytiktek.service.NetworkChangeListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ShowProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvSignOut;
    private FirebaseAuth firebaseAuth;
    private TextView tvFullName, tvUploads, tvAverageRate, tvAddress, tvEmail, tvPhoneNumber;
    private FirebaseFirestore firebaseFirestore;
    private String currentUid;
    private DocumentReference userRef;
    private User currentUser;
    private ImageView ivSetting;
    private NetworkChangeListener networkChangeListener;
    private ImageView btnHome, btnUploadSolution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_show_profile);

        networkChangeListener = new NetworkChangeListener();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, intentFilter);

        tvSignOut = (TextView)findViewById(R.id.showProfileSignOut);
        tvSignOut.setOnClickListener(this);
        tvFullName = (TextView)findViewById(R.id.tvFullName);
        tvAddress = (TextView)findViewById(R.id.tvAddress);
        tvUploads = (TextView)findViewById(R.id.tvUploads);
        tvAverageRate = (TextView)findViewById(R.id.tvAverageRate);
        tvEmail = (TextView)findViewById(R.id.showProfileEmail);
        tvPhoneNumber = (TextView)findViewById(R.id.showProfilePhoneNumber);
        ivSetting = (ImageView)findViewById(R.id.settings);
        ivSetting.setOnClickListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        currentUid = firebaseAuth.getCurrentUser().getUid();

        btnUploadSolution = (ImageView)findViewById(R.id.btnUploadSolution);
        btnUploadSolution.setOnClickListener(this);
        btnHome = (ImageView)findViewById(R.id.btnHome);
        btnHome.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initUserFields();
    }

    private void initUserFields() {
        userRef = firebaseFirestore.collection("users").document(currentUid);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentUser = documentSnapshot.toObject(User.class);

                tvFullName.setText(currentUser.getFullName());
                tvEmail.setText(currentUser.getEmail());
                tvPhoneNumber.setText(currentUser.getPhone());
                tvUploads.setText(currentUser.getNumberOfUploads());
                tvAverageRate.setText(currentUser.getAverageRate());


            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == tvSignOut){
            firebaseAuth.signOut();
            setResult(0);
            finish();
        }else if(v == ivSetting){
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivity(intent);
        }else if(v == btnHome){
            setResult(0);
            finish();
        }else if(v == btnUploadSolution){
            Toast.makeText(this, "Upload solution will be available on v2.0", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStop() {
        if(networkChangeListener != null){
            unregisterReceiver(networkChangeListener);
            networkChangeListener = null;
        }
        super.onStop();
    }
}