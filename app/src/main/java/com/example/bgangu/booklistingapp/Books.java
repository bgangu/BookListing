package com.example.bgangu.booklistingapp;

import java.util.ArrayList;

public class Books extends ArrayList<Books> {
    private String mBookTitle;
    private String mBookAuthor;
    private String mBookPublishedDate;

    public Books(String bookTitle, String bookAuthor, String bookPublishedDate) {
        mBookTitle = bookTitle;
        mBookAuthor = bookAuthor;
        mBookPublishedDate = bookPublishedDate;
    }

    public String getBookTitle() {
        return mBookTitle;
    }

    public String getBookAuthor() {
        return mBookAuthor;
    }

    public String getBookPublishedDate() {
        return mBookPublishedDate;
    }
}
