package com.example.mytiktek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {

    EditText mFullName,mEmail,mPassword,mPhone;
    Button mSaveBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;
    private DocumentReference userRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edit_profile);

        mFullName   = findViewById(R.id.epFullName);
        mEmail      = findViewById(R.id.epEmail);
        mPhone      = findViewById(R.id.epPhone);
        mSaveBtn    = findViewById(R.id.epSaveBtn);
        mSaveBtn.setOnClickListener(this);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.epProgressBar);

        initData();
    }

    private void initData() {

        userRef = fStore.collection("users").document(fAuth.getCurrentUser().getUid());
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);

                mFullName.setText(user.getFullName());
                mEmail.setText(user.getEmail());
                mPhone.setText(user.getPhone());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v == mSaveBtn) {
            String newFullName = mFullName.getText().toString();
            String newEmail = mEmail.getText().toString();
            String newPhoneNumber = mPhone.getText().toString();
            if (isFieldValid(newFullName, newEmail, newPhoneNumber)) {
                progressBar.setVisibility(View.VISIBLE);
                fAuth.getCurrentUser().updateEmail(newEmail)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    Log.d("Ariel", "User email address updated.");

                                    //update data on fireStore
                                    String pathToSolutionRate = "books." + CurrentSolutionImage.bookPos + ".pages." +
                                            CurrentSolutionImage.pageNumber + "." + CurrentSolutionImage.questionNumber + ".rate";
                                    Map<String, Object> updatedUserData = new HashMap<String, Object>();
                                    updatedUserData.put("email", newEmail);
                                    updatedUserData.put("fullName", newFullName);
                                    updatedUserData.put("phone", newPhoneNumber);

                                    String sName = CurrentSubjectBooks.getSubjectName();
                                    fStore.collection("users").document(fAuth.getUid())
                                            .update(updatedUserData);

                                    mFullName.setText(newFullName);
                                    mEmail.setText(newEmail);
                                    mPhone.setText(newPhoneNumber);
                                    Toast.makeText(EditProfileActivity.this, "Changes Saved", Toast.LENGTH_SHORT).show();
                                }else {
                                    Toast.makeText(EditProfileActivity.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        });
            }
        }
    }

    private boolean isFieldValid(String newFullName, String newEmail, String newPhoneNumber) {
        if(TextUtils.isEmpty(newFullName)){
            mFullName.setError("Full Name is Required");
            return false;
        }
        if(TextUtils.isEmpty(newEmail)){
            mEmail.setError("Email is Required.");
            return false ;
        }
        if(TextUtils.isEmpty(newPhoneNumber)){
            mPhone.setError("Full Name is Required");
            return false;
        }
       return true;
    }
}