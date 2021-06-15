package com.example.mytiktek.DataObjects;

import com.example.mytiktek.DataObjects.Book;

import java.util.Map;

public class SubjectBooks {

    public Map<String, Book> books;  // {book id, Book.class}
    public  String subjectName;  //currentSubjectName

    public Map<String, Book> getBooks() {
        return books;
    }

    public void setBooks(Map<String, Book> books) {
        this.books = books;
    }

    public  String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public SubjectBooks() {
    }
}
