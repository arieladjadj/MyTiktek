package com.example.mytiktek;

import android.graphics.Bitmap;

import java.util.Map;

public class Book {
   // private Map<String, Map<String, String>> bookName1;

    private Bitmap bookImage;  //after download?
    private String bookName;
    private String coverImageUrl;
    private String description1;
    private String description2;
    private String authors;
    private String numberOfPages;

    private Map<String,Map<String,Map<String, String>>> pages;  /*  pages:
                                                            1:
                                                              1:
                                                                solutionUrl: www.aabbcc.com
                                                                rate: 4
                                                              2:
                                                                solutionUrl: www.xyz.com
                                                                rate: 3.6
                                                            2:
                                                              3:
                                                                solutionUrl: www.picture.com
                                                                rate: 4.3
                                                     */

    public String getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(String numberOfPages) {
        this.numberOfPages = numberOfPages;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public Map<String, Map<String, Map<String, String>>> getPages() {
        return pages;
    }

    public void setPages(Map<String, Map<String, Map<String, String>>> pages) {
        this.pages = pages;
    }

    public String getBookCoverUrl() {
        return coverImageUrl;
    }

    public void setBookCoverUrl(String bookImageUrl) {
        this.coverImageUrl = bookImageUrl;
    }

    public String getDescription1() {
        return description1;
    }

    public void setDescription1(String description1) {
        this.description1 = description1;
    }

    public String getDescription2() {
        return description2;
    }

    public void setDescription2(String description2) {
        this.description2 = description2;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public Bitmap getBookImage() {
        return bookImage;
    }

    public void setBookImage(Bitmap bookImage) {
        this.bookImage = bookImage;
    }

    public Book() {
    }

}
