package com.example.mytiktek;

import android.graphics.Bitmap;

import com.google.firebase.firestore.Exclude;

public class Subject {

    //private Bitmap subjectImage;
    private String subjectName;
    private String documentId; // = subjectName
    private String subjectImageUrl;

    public Bitmap getSubjectImageBitmap() {
        return SubjectImageBitmap;
    }

    public void setSubjectImageBitmap(Bitmap subjectImageBitmap) {
        SubjectImageBitmap = subjectImageBitmap;
    }

    private Bitmap SubjectImageBitmap;

    public Subject(){
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getSubjectImageUrl() {
        return subjectImageUrl;
    }

    public void setSubjectImageUrl(String subjectImageUrl) {
        this.subjectImageUrl = subjectImageUrl;
    }


    @Exclude
    public String getDocumentId() {
        return documentId;
    }
    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

}