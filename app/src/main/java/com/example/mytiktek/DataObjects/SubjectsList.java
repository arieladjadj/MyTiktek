package com.example.mytiktek.DataObjects;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class SubjectsList {
    public static ArrayList<Subject> subjects = new ArrayList<Subject>();
    public static ArrayList<Bitmap> subjectImages = new ArrayList<Bitmap>();


    public ArrayList<Bitmap> getSubjectImages() {
        return subjectImages;
    }

    public void setSubjectImages(ArrayList<Bitmap> subjectImages) {
        this.subjectImages = subjectImages;
    }

    private SubjectsList(){}

    public static ArrayList<Subject> getSubjects() {
        return subjects;
    }

    public static void setSubjects(ArrayList<Subject> subjects) {
        SubjectsList.subjects = subjects;
    }

    public static boolean haveImages(){
        int  i =0;
        for(Subject subject: subjects){
            if(subject.getSubjectImageBitmap() == null) {
                return false;
            }
        }
        return true;
    }
}
