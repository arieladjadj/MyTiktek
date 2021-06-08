package com.example.mytiktek;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CurrentSubjectBooks {
    public static List<Book> books;  // {book id, Book.class} - ID is the place on the list
    public static String subjectName;  //currentSubjectName

    public static List<Book> getBooks() {
        return books;
    }

    public static void setBooks(List<Book> books) {
        CurrentSubjectBooks.books = books;
    }

    public static String getSubjectName() {
        return subjectName;
    }

    public static void setSubjectName(String subjectName) {
        CurrentSubjectBooks.subjectName = subjectName;
    }

    public CurrentSubjectBooks() {
    }

    public static boolean haveImages() {
        int  i =0;
        for(Book book: books){
            if(book.getBookImage() == null) {
                return false;
            }
        }
        return true;
    }
}
