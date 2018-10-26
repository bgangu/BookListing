package com.example.bgangu.booklistingapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.ArrayList;

public class BooksLoader extends AsyncTaskLoader<ArrayList<Books>> {

    private String mURL;


    public BooksLoader(Context context, String URL) {
        super(context);
        mURL = URL;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }


    @Override
    public ArrayList<Books> loadInBackground() {
        if (mURL == null) {
            return null;
        }
        ArrayList<Books> booksList = QueryUtils.fetchBooks(mURL);
        return booksList;
    }
}
